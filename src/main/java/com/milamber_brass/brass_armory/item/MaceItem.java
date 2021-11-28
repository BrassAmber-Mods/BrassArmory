package com.milamber_brass.brass_armory.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;

public class MaceItem extends SwordItem {

    public MaceItem(IItemTier tier, int attackDamageIn, Properties builderIn) {
        super(tier, attackDamageIn, -3.2F, builderIn);
    }

}
