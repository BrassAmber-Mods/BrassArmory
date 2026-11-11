package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.entity.projectile.SpikyBallEntity;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import com.milamber_brass.brass_armory.item.abstracts.ArmoryTieredItem;
import net.minecraft.world.level.Level;

public class SpikyBallItem extends ArmoryTieredItem {

    public SpikyBallItem(ToolMaterial tier, Properties properties) {
        super(tier, properties);
    }

        @Override
        public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), BrassArmorySounds.SPIKY_BALL_THROW.get(), SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide()) {
            SpikyBallEntity spikyBall = new SpikyBallEntity(level, player, interactionHand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite());
            spikyBall.setItem(stack);
            spikyBall.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.75F, 0.25F);
            level.addFreshEntity(spikyBall);
        }
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
            player.getCooldowns().addCooldown(stack, 10);
        }
        return InteractionResult.SUCCESS;
    }
}
