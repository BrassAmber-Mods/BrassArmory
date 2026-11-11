package com.milamber_brass.brass_armory.effect;

import com.milamber_brass.brass_armory.data.BrassArmoryDamageTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryEffects;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import java.util.Objects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class BleedEffect extends MobEffect {

    public BleedEffect() {
        super(MobEffectCategory.HARMFUL, 0x56CBFD);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity living, int amplifier) {
        int duration = Objects.requireNonNull(living.getEffect(BrassArmoryEffects.BLEEDING)).getDuration();

        if (duration == 1 || amplifier >= 20 || duration % (20 - amplifier) == 0) {
            if (amplifier > 0 && duration <= 1) {
                living.addEffect(new MobEffectInstance(BrassArmoryEffects.BLEEDING, 20, amplifier - 1, false, false, true));
                return true;
            }
            living.hurtServer(level, ArmoryUtil.getDamageSource(living.level(), BrassArmoryDamageTypes.BLEED), 1F + (float) (amplifier / 2));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    public static void bleedHarder(LivingEntity living, int duration, int bonus) {
        MobEffectInstance instance = living.getEffect(BrassArmoryEffects.BLEEDING);
        int newDuration = Math.max(instance != null ? instance.getDuration() : 0, duration);
        int newAmplifier = (instance != null ? instance.getAmplifier() + 1 : 0) + bonus;

        living.addEffect(new MobEffectInstance(BrassArmoryEffects.BLEEDING, newDuration, newAmplifier, false, false, true));
    }

    public static void witherHarder(LivingEntity living, int duration) {
        MobEffectInstance instance = living.getEffect(BrassArmoryEffects.BLEEDING);
        living.addEffect(new MobEffectInstance(MobEffects.WITHER, instance != null ? instance.getDuration() + instance.getAmplifier() * 20 : duration, 0));
    }
}
