package com.milamber_brass.brass_armory.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class BrassArmoryItemGroups {

    public static final CreativeModeTab BRASS_ARMORY = new CreativeModeTab("brass_armory") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BrassArmoryItems.DIAMOND_SPEAR.get());
        }
    };

}
