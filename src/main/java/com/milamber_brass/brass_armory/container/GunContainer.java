package com.milamber_brass.brass_armory.container;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryMenus;
import com.milamber_brass.brass_armory.item.abstracts.AbstractGunItem;
import com.milamber_brass.brass_armory.item.ammo_behaviour.AbstractAmmoBehaviour;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

public class GunContainer extends AbstractContainerMenu {
    public static final ResourceLocation EMPTY_POWDER_SLOT = BrassArmory.locate("gui/empty_powder_slot");
    public static final ResourceLocation EMPTY_AMMO_SLOT = BrassArmory.locate("gui/empty_ammo_slot");

    public final ItemStack gunStack;
    private final Slot ammoSlot;
    private final Slot powderSlot;

    public GunContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new ItemStackHandler(2), ItemStack.EMPTY);
    }

    @ParametersAreNonnullByDefault
    public GunContainer(int id, Inventory playerInventory, IItemHandler slot, ItemStack stack) {
        super(BrassArmoryMenus.GUN_MENU.get(), id);
        this.gunStack = stack;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new DefaultSlot(playerInventory, 9 + row * 9 + column, 8 + column * 18, 84 + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new DefaultSlot(playerInventory, column, 8 + column * 18, 142));
        }

        this.addSlot(new DefaultSlot(playerInventory, 40, 8, 50) {
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });

        int maxAmmo;
        Predicate<ItemStack> inputPredicate;
        if (this.gunStack.getItem() instanceof AbstractGunItem gunItem) {
            inputPredicate = gunItem.getAllSupportedProjectiles();
            maxAmmo = gunItem.getMaxAmmoItems();
        } else {
            inputPredicate = inputStack -> true;
            maxAmmo = 1;
        }

        this.ammoSlot = this.addSlot(new GunSlot(slot, 0, 61, 24, inputPredicate, maxAmmo) {
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_AMMO_SLOT);
            }
        });

        this.powderSlot = this.addSlot(new GunSlot(slot, 1, 99, 24, inputStack -> inputStack.getItem().equals(Items.GUNPOWDER) || inputStack.getItem().equals(Items.FIRE_CHARGE), 1) {
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_POWDER_SLOT);
            }
        });
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack retStack = ItemStack.EMPTY;
        Slot slot = getSlot(index);
        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            retStack = item.copy();
            if (index < 27) {
                if (!this.moveItemStackTo(item, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(item, 0, 27, false)) {
                return ItemStack.EMPTY;
            }

            if (item.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return retStack;
    }

    @Nonnull
    public static MenuConstructor getServerContainer(ItemStack stack) {
        return (id, playerInv, player) -> new GunContainer(id, playerInv, new ItemStackHandler(2), stack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void removed(Player player) {
        super.removed(player);
        ItemStack ammoStack = this.ammoSlot.getItem();
        ItemStack powderStack = this.powderSlot.getItem();

        if (!powderStack.isEmpty() && !ammoStack.isEmpty()) {
            AbstractAmmoBehaviour.getStackBehaviour(ammoStack).ifPresent(behaviour -> behaviour.onLoad(player.level, player, this.gunStack, ammoStack));
            AbstractGunItem.addStack(this.gunStack, ammoStack, "BAAmmo");
            AbstractGunItem.addStack(this.gunStack, powderStack, "BAPowder");
            AbstractGunItem.setLoad(this.gunStack, 1);
        } else {
            player.drop(ammoStack, true);
            player.drop(powderStack, true);
        }
    }

    static class GunSlot extends SlotItemHandler {
        private final Predicate<ItemStack> mayPlace;
        private final int maxStack;

        public GunSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> mayPlace, int maxStack) {
            super(itemHandler, index, xPosition, yPosition);
            this.mayPlace = mayPlace;
            this.maxStack = maxStack;
        }

        @Nullable
        @Override
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            return super.getNoItemIcon();
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
        @ParametersAreNonnullByDefault
        public boolean mayPickup(Player player) {
            return !(this.getItem().getItem() instanceof AbstractGunItem);
        }
    }
}
