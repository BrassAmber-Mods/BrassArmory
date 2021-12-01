package com.milamber_brass.brass_armory.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

import java.util.Properties;

public class FlailItem extends SwordItem {

    public FlailItem(Tiers tier, int attackDamageIn, Properties builderIn) {
        super(tier, attackDamageIn, -2.6F, builderIn);
    }

}
