package com.milamberBrass.brass_armory.items;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup BRASS_ARMORY = new ItemGroup("brass_armory")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModItems.DIAMOND_SPEAR.get());
        }
    };
}

