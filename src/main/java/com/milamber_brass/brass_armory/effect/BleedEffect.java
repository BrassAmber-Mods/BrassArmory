package com.milamber_brass.brass_armory.effect;

import com.milamber_brass.brass_armory.data.BrassArmoryDamageTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryEffects;
import com.milamber_brass.brass_armory.init.BrassArmoryParticles;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class BleedEffect extends MobEffect {

    public BleedEffect() {
        super(MobEffectCategory.HARMFUL, 0x56CBFD);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        int duration = Objects.requireNonNull(living.getEffect(BrassArmoryEffects.BLEEDING.get())).getDuration();

        if (living.level().isClientSide) {
            int iMax = amplifier > 100 ? 100 : (living.level().random.nextInt(100) <= duration ? 1 : 0) + amplifier;
            double y = living.isDeadOrDying() ? living.getY() + 0.15D : living.getRandomY() - 0.25D;
            for (int i = 0; i < iMax; i++) {
                living.level().addParticle(BrassArmoryParticles.BLOOD_FALL_PARTICLE.get(), living.getRandomX(0.5D), y, living.getRandomZ(0.5D), 0D, 0D, 0D);
            }
        } else if (duration == 1 || amplifier >= 20 || duration % (20 - amplifier) == 0) {
            if (amplifier > 0 && duration <= 1) {
                living.addEffect(new MobEffectInstance(BrassArmoryEffects.BLEEDING.get(), 20, amplifier - 1, false, false, true));
                return;
            }
            living.hurt(ArmoryUtil.getDamageSource(living.level(), BrassArmoryDamageTypes.BLEED), 1F + (float) (amplifier / 2));
        }

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    public static void bleedHarder(LivingEntity living, int duration, int bonus) {
        MobEffectInstance instance = living.getEffect(BrassArmoryEffects.BLEEDING.get());
        int newDuration = Math.max(instance != null ? instance.getDuration() : 0, duration);
        int newAmplifier = (instance != null ? instance.getAmplifier() + 1 : 0) + bonus;

        living.addEffect(new MobEffectInstance(BrassArmoryEffects.BLEEDING.get(), newDuration, newAmplifier, false, false, true));
    }

    public static void witherHarder(LivingEntity living, int duration) {
        MobEffectInstance instance = living.getEffect(BrassArmoryEffects.BLEEDING.get());
        living.addEffect(new MobEffectInstance(MobEffects.WITHER, instance != null ? instance.getDuration() + instance.getAmplifier() * 20 : duration, 0));
    }
}
