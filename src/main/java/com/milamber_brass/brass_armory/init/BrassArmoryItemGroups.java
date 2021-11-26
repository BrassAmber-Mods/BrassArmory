package com.milamber_brass.brass_armory.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class BrassArmoryItemGroups {

    public static final ItemGroup BRASS_ARMORY = new ItemGroup("brass_armory") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BrassArmoryItems.DIAMOND_SPEAR.get());
        }
    };

}
