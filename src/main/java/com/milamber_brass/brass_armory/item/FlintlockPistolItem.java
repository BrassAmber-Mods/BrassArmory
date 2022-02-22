package com.milamber_brass.brass_armory.item;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class FlintlockPistolItem extends Item {
    public FlintlockPistolItem(Item.Properties builderIn) {
        super(builderIn);
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level.isClientSide) {
            player.displayClientMessage(new TextComponent("Whoops, seems like you don't have any ammo for that! Join our discord for updates!"), true);
        }
        level.playSound(player, player, SoundEvents.DISPENSER_DISPENSE, SoundSource.BLOCKS, 1.0F, 1.0F);
        return super.use(level, player, interactionHand);
    }
}
