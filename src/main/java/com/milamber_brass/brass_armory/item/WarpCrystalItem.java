package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.init.BrassArmoryCapabilities;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.util.ArmoryCooldownCache;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class WarpCrystalItem extends Item {
    public static final int WARP_TICKS = 40;
    public final int maxDistance;

    public WarpCrystalItem(Properties properties, int maxDistance) {
        super(properties);
        this.maxDistance = maxDistance;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int useDurationLeft) {
        if (level.isClientSide) {
            level.addParticle(ParticleTypes.PORTAL, living.getRandomX(0.5D), living.getRandomY() - 0.25D, living.getRandomZ(0.5D), (level.random.nextDouble() - 0.5D) * 2.0D, -level.random.nextDouble(), (level.random.nextDouble() - 0.5D) * 2.0D);
        }

        if (living instanceof Player player) {
            player.getCapability(BrassArmoryCapabilities.EFFECT_CAPABILITY).ifPresent(iEffectCapability -> {
                int usedTicks = this.getUseDuration(stack) - useDurationLeft;
                float slow = iEffectCapability.getSlow();

                if (level.isClientSide) {
                    Minecraft mc = Minecraft.getInstance();

                    if (mc.getCameraEntity() == living) {
                        int beatTick = useDurationLeft % 34;
                        double x = living.getX(), y = living.getY(), z = living.getZ();

                        if (beatTick == 10) {
                            level.playLocalSound(x, y, z, BrassArmorySounds.HEART_BEAT_1.get(), SoundSource.PLAYERS, 0.05F + slow, 0.7F / 3.0F * slow - slow, false);
                        } else if (beatTick == 15) {
                            level.playLocalSound(x, y, z, BrassArmorySounds.HEART_BEAT_2.get(), SoundSource.PLAYERS, 0.05F + slow, 0.8F / 3.0F * slow - slow, false);
                        }

                        if (usedTicks >= WARP_TICKS && useDurationLeft % 20 == 0 && level.random.nextInt(2) == 0) {
                            level.playLocalSound(x, y, z, BrassArmorySounds.CRYSTAL_WHISPER.get(), SoundSource.PLAYERS, 0.2F, living.getVoicePitch() * 0.02F, false);
                        }
                    }
                } else {
                    iEffectCapability.setSlow(Mth.clamp(((float) usedTicks / (float) WARP_TICKS) * 0.25F, slow, 0.25F));
                }
            });
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int useDurationLeft) {
        int usedTicks = this.getUseDuration(stack) - useDurationLeft;

        Vec3 farPoint = living.getLookAngle().scale(this.maxDistance).add(living.position());
        double farY = farPoint.y;

        if (usedTicks >= WARP_TICKS - 2) {
            for (int i = this.maxDistance; i > 0; i -= Mth.clamp(i / 25, 1, 3)) {
                farPoint = living.getLookAngle().scale(i).add(living.position());
                if ((farPoint.y < (double)level.getMaxBuildHeight() || farPoint.y < Math.sqrt(farY - (double)level.getMaxBuildHeight()) + (double)level.getMaxBuildHeight()) && farPoint.y > (double)level.getMinBuildHeight()) {
                    for (BlockPos blockpos : BlockPos.betweenClosed(BlockPos.containing(farPoint).offset(-1, 0, -1), BlockPos.containing(farPoint).offset(1, 0, 1))) {
                        if (!level.getBlockState(blockpos).blocksMotion() && !level.getBlockState(blockpos.above()).blocksMotion()) {
                            Vec3 vec = living.getEyePosition().add(living.getLookAngle());
                            for (int l2 = 0; l2 < 8; ++l2) {
                                level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), vec.x, vec.y, vec.z, level.random.nextGaussian() * 0.15D, level.random.nextDouble() * 0.2D, level.random.nextGaussian() * 0.15D);
                            }

                            farPoint = Vec3.atBottomCenterOf(blockpos);
                            living.teleportToWithTicket(farPoint.x, farPoint.y, farPoint.z);

                            level.playSound(null, living, BrassArmorySounds.CRYSTAL_WARP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                            if (living instanceof Player player) {
                                if (!player.getAbilities().instabuild && player.getRandom().nextInt(5) > 0) stack.shrink(1);
                                ArmoryUtil.addCooldownToList(player, ArmoryCooldownCache.BrassArmoryWarpCrystals, 1200);
                                player.awardStat(Stats.ITEM_USED.get(this));
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    public int getMaxDistance() {
        return this.maxDistance;
    }
}
