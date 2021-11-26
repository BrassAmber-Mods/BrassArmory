package com.milamber_brass.brass_armory.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;

public class DaggerItem extends SwordItem {

    public DaggerItem(IItemTier tier, int attackDamageIn, Properties builderIn) {
        super(tier, attackDamageIn, -2.0F, builderIn);
    }

}
