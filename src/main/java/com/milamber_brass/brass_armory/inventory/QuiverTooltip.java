package com.milamber_brass.brass_armory.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record QuiverTooltip(NonNullList<ItemStack> items, int weight) implements TooltipComponent {

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public int getWeight() {
        return this.weight;
    }
}