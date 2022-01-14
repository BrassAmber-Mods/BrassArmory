package com.milamber_brass.brass_armory.item;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public interface ICustomAnimationItem {
    default int getCustomUseDuration(ItemStack stack, LocalPlayer player) {
        return 0;
    }

    default int getChargeDuration(ItemStack itemStack) {
        return 0;
    }
}
