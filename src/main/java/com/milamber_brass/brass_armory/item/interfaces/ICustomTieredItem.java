package com.milamber_brass.brass_armory.item.interfaces;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

public interface ICustomTieredItem {
    default ToolMaterial getTier() {
        return null;
    }

    default int getEnchantmentValue() {
        return this.getTier().enchantmentValue();
    }

    default boolean isValidRepairItem(ItemStack itemStack) {
        return itemStack.is(this.getTier().repairItems());
    }
}
