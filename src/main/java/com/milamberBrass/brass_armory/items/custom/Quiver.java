package com.milamberBrass.brass_armory.items.custom;

import com.milamberBrass.brass_armory.capabilities.ItemInventoryProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class Quiver extends Item {

    public Quiver(Properties builderIn) {
        super(builderIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        // Setup return results
        ItemStack stack = player.getHeldItem(hand);
        ActionResult<ItemStack> result = new ActionResult<>(ActionResultType.PASS, stack);

        // Ensure server-side only
        if (world.isRemote) {
            return result;
        }

        // Ensure item is a quiver
        if (!(stack.getItem() instanceof Quiver)) {
            return result;
        }

        System.out.println("YEET");

        // Get capability
        IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
        inv.insertItem(0, new ItemStack(Items.DIRT, 1), false);
        System.out.println(inv.getStackInSlot(0).getCount());

//        System.out.println("BEFORE: " + inv.getStackInSlot(0));
//        ItemStack clone = inv.getStackInSlot(0).copy();
//        if (clone.getItem() == Items.AIR) {
//            clone = new ItemStack(Items.DIRT, 1);
//        }
//        clone.setCount(clone.getCount() + 1);
//        inv.setStackInSlot(0, clone);


//        IQuiverInventory cap = stack.getCapability(QuiverInventoryProvider.CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
//        ArrayList<ItemStack> items = cap.getItems();
//        items.add(new ItemStack(Items.DIRT, 20));
//        cap.setItems(items);

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemInventoryProvider(new Inventory(4));
    }

}
