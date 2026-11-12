package com.milamber_brass.brass_armory.inventory;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.CannonEntity;
import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.init.BrassArmoryMenus;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.util.ItemData;
import com.mojang.datafixers.util.Pair;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;



import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GunContainer<T extends iGun> extends AbstractContainerMenu {
    public static final Identifier EMPTY_POWDER_SLOT = BrassArmory.locate("gui/empty_powder_slot");
    public static final Identifier EMPTY_AMMO_SLOT = BrassArmory.locate("gui/empty_ammo_slot");
    public static final String gunIcon = "BAGunIcon";
    public static final String gunAmmo = "BAAmmo";
    public static final String gunPowder = "BAPowder";

    public final iGun gun;
    public final CompoundTag tag;
    private final Slot ammoSlot;
    private final Slot powderSlot;
    public final Slot iconSlot;

    public GunContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(3), null, new CompoundTag());
    }

    public GunContainer(int id, Inventory playerInventory, Container slot, @Nullable T gun, CompoundTag tag) {
        super(BrassArmoryMenus.GUN_MENU.get(), id);
        this.gun = gun;
        this.tag = tag;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new DefaultSlot(playerInventory, 9 + row * 9 + column, 8 + column * 18, 84 + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new DefaultSlot(playerInventory, column, 8 + column * 18, 142));
        }

        this.addSlot(new DefaultSlot(playerInventory, 40, 8, 50) {
            @Override
            public Identifier getNoItemIcon() {
                return InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD;
            }
        });

        this.ammoSlot = this.addSlot(new GunSlot(slot, 0, 61, 24, gun != null ? gun.ammoPredicate() : inputStack -> true, 1) {
            @Override
            public Identifier getNoItemIcon() {
                return EMPTY_AMMO_SLOT;
            }
        });

        this.ammoSlot.set(ArmoryUtil.loadStack(tag, gunAmmo, ItemStack.EMPTY));

        this.powderSlot = this.addSlot(new GunSlot(slot, 1, 99, 24, gun != null ? gun.powderPredicate() : inputStack -> true, 1) {
            @Override
            public Identifier getNoItemIcon() {
                return EMPTY_POWDER_SLOT;
            }
        });

        this.powderSlot.set(ArmoryUtil.loadStack(tag, gunPowder, ItemStack.EMPTY));

        this.iconSlot = this.addSlot(new GunSlot(slot, 2, 80, 24) {
            @Override
            public boolean allowModification(Player player) {
                return false;
            }

            @Override
            public boolean isActive() {
                return false;
            }
        });

        this.iconSlot.set(ArmoryUtil.loadStack(tag, gunIcon, ItemStack.EMPTY).copy());

        if (gun != null) gun.onOpen(playerInventory.player, tag); //Gun is null on client
    }

        @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack retStack = ItemStack.EMPTY;
        if (index < 0 || index >= this.slots.size()) return ItemStack.EMPTY;
        Slot slot = getSlot(index);
        if (!slot.mayPickup(player) || !slot.allowModification(player)) return ItemStack.EMPTY;
        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            retStack = item.copy();
            if (index < 27) {
                if (!this.moveItemStackTo(item, 27, 36, false)) return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(item, 0, 27, false)) return ItemStack.EMPTY;

            if (item.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return retStack;
    }

        public static MenuConstructor getServerContainer(iGun gun, CompoundTag tag) {
        return (id, playerInv, player) -> new GunContainer<>(id, playerInv, new SimpleContainer(3), gun, tag);
    }

    public static MenuConstructor getServerContainer(iGun gun, ItemStack stack) {
        return (id, playerInv, player) -> new GunContainer<>(id, playerInv, new SimpleContainer(3), gun, ItemData.getTag(stack)) {
            @Override
            public void removed(Player player) {
                super.removed(player);
                ItemData.setTag(stack, this.tag);
            }
        };
    }

    @Override
    public boolean stillValid(Player player) {
        return !(this.gun instanceof CannonEntity cannon) || (!cannon.isRemoved() && cannon.level() == player.level() && cannon.distanceToSqr(player) <= 64D && cannon.getFuse() == 0);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (player.level().isClientSide()) return;
        if (this.gun instanceof CannonEntity cannon && cannon.isRemoved()) {
            player.getInventory().placeItemBackInInventory(this.ammoSlot.getItem(), net.minecraft.util.Prediction.SERVER_ONLY);
            player.getInventory().placeItemBackInInventory(this.powderSlot.getItem(), net.minecraft.util.Prediction.SERVER_ONLY);
            this.ammoSlot.set(ItemStack.EMPTY);
            this.powderSlot.set(ItemStack.EMPTY);
            return;
        }

        ArmoryUtil.clearStack(this.tag, gunAmmo);
        ItemStack ammoStack = this.ammoSlot.getItem();
        if (!ammoStack.isEmpty()) ArmoryUtil.addStack(this.tag, ammoStack, gunAmmo);

        ArmoryUtil.clearStack(this.tag, gunPowder);
        ItemStack powderStack = this.powderSlot.getItem();
        if (!powderStack.isEmpty()) ArmoryUtil.addStack(this.tag, powderStack, gunPowder);

        if (this.gun != null) this.gun.onLoad(player, this.tag); //Null on client side, not null on server side
    }

    static class GunSlot extends Slot {
        private final Predicate<ItemStack> mayPlace;
        private final int maxStack;

        public GunSlot(Container itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> mayPlace, int maxStack) {
            super(itemHandler, index, xPosition, yPosition);
            this.mayPlace = mayPlace;
            this.maxStack = maxStack;
        }

        public GunSlot(Container itemHandler, int index, int xPosition, int yPosition) {
            this(itemHandler, index, xPosition, yPosition, itemStack -> true, 64);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return this.mayPlace.test(stack);
        }

        @Override
        public int getMaxStackSize() {
            return this.maxStack;
        }

        @Override
        public int getMaxStackSize(@NotNull ItemStack stack) {
            return this.getMaxStackSize();
        }
    }


    static class DefaultSlot extends Slot {
        public DefaultSlot(Container container, int index, int xPosition, int yPosition) {
            super(container, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPickup(Player player) {
            return !(this.getItem().getItem() instanceof iGun);
        }
    }
}
