package com.milamber_brass.brass_armory.capabilities;

import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.item.QuiverItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class QuiverCapabilityHandler implements IQuiverCapability {
    private Player player;
    private ItemStack ammoStack;
    private ItemStack quiverStack;

    @Override
    public void setEntity(Player player) {
        this.player = player;
    }

    @Override
    public void tick() {
        if (!this.getQuiverStack().isEmpty()) {
            if (this.player.getInventory().items.contains(this.quiverStack)) {
                Optional<ItemStack> inQuiverAmmoStack = QuiverItem.getContents(this.quiverStack).findFirst();
                if (inQuiverAmmoStack.isPresent() && (!ItemStack.isSameItemSameTags(inQuiverAmmoStack.get(), this.getAmmoStack()) || inQuiverAmmoStack.get().getCount() != this.getAmmoStack().getCount())) {
                    QuiverItem.removeOne(this.quiverStack);
                    if (!this.getAmmoStack().isEmpty()) QuiverItem.add(this.quiverStack, this.ammoStack);
                    this.setQuiverStack(ItemStack.EMPTY);
                    this.setAmmoStack(ItemStack.EMPTY);
                }
            } else this.setQuiverStack(ItemStack.EMPTY);
        }
    }

    @Override
    public @NotNull ItemStack getAmmoStack() {
        return this.ammoStack != null ? this.ammoStack : ItemStack.EMPTY;
    }

    @Override
    public void setAmmoStack(ItemStack stack) {
        this.ammoStack = stack;
    }

    @Override
    public @NotNull ItemStack getQuiverStack() {
        return this.quiverStack != null ? this.quiverStack : ItemStack.EMPTY;
    }

    @Override
    public void setQuiverStack(ItemStack stack) {
        this.quiverStack = stack;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ArmoryUtil.addStack(tag, this.getAmmoStack(), "ammo");
        ArmoryUtil.addStack(tag, this.getQuiverStack(), "quiver");
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.setAmmoStack(ArmoryUtil.loadStack(tag, "ammo", ItemStack.EMPTY));
        this.setQuiverStack(ArmoryUtil.loadStack(tag, "quiver", ItemStack.EMPTY));
    }
}
