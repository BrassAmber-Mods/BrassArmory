package com.milamberBrass.brass_armory;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class BrassArmoryItemGroup {

    public static final ItemGroup BRASS_ARMORY = new ItemGroup("brass_armory") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BrassArmoryItems.DIAMOND_SPEAR.get());
        }
    };

}
