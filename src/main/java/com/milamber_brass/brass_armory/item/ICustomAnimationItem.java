package com.milamber_brass.brass_armory.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ICustomAnimationItem {
    default int getCustomUseDuration(ItemStack stack, Player player) {
        return 0;
    }

    default int getChargeDuration(ItemStack itemStack) {
        return 0;
    }
}
