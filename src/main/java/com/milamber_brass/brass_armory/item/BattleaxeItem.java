package com.milamber_brass.brass_armory.item;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class BattleaxeItem extends SwordItem {

    public BattleaxeItem(Tiers tier, int attackDamageIn, Properties builderIn) {
        super(tier, attackDamageIn, -3.1F, builderIn);
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level.isClientSide) {
            player.displayClientMessage(new TextComponent("Not actually throwable yet! Join our discord for updates!"), true);
        }
        return super.use(level, player, interactionHand);
    }

}


