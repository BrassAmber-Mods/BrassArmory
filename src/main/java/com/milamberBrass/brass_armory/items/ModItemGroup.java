package com.milamberBrass.brass_armory.items;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ModItemGroup {

    public static final ItemGroup BRASS_ARMORY = new ItemGroup("brass_armory") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.DIAMOND_SPEAR.get());
        }
    };

}

