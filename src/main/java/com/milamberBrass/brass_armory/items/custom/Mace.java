package com.milamberBrass.brass_armory.items.custom;

import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;

public class Mace extends SwordItem {
    public Mace(IItemTier tier, int attackDamageIn, Properties builderIn) {
        super(tier, attackDamageIn, -3.2F, builderIn);
    }
}
