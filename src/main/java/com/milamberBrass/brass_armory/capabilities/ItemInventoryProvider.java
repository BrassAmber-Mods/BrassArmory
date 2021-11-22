package com.milamberBrass.brass_armory.capabilities;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemInventoryProvider implements ICapabilitySerializable<ListNBT> {

    private LazyOptional<IItemHandler> instance;

    public ItemInventoryProvider(IInventory inventory) {
        instance = LazyOptional.of(() -> new InvWrapper(inventory));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public ListNBT serializeNBT() {
        // I'm not convinced this is the most optimized code, but it's what I managed to get to work.
        InvWrapper inv = (InvWrapper) instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
        ListNBT tag = new ListNBT();
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                CompoundNBT invslotnbt = stack.serializeNBT();
                invslotnbt.putByte("Slot", (byte) i);
                tag.add(i, invslotnbt);
            }
        }
        return tag;
    }

    @Override
    public void deserializeNBT(ListNBT tag) {
        // I'm not convinced this is the most optimized code, but it's what I managed to get to work.
        InvWrapper inv = (InvWrapper) instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
        for (int i = 0; i < tag.size(); i++) {
            CompoundNBT invslotnbt = tag.getCompound(i);
            byte slot = invslotnbt.getByte("Slot");
            invslotnbt.remove("Slot");
            inv.setStackInSlot(slot, ItemStack.read(invslotnbt));
        }
    }
}
