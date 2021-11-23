package com.milamberBrass.brass_armory.items.custom;

import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;

public class Dagger extends SwordItem {

    public Dagger(IItemTier tier, int attackDamageIn, Properties builderIn) {
        super(tier, attackDamageIn, -2.0F, builderIn);
    }

}
