package com.milamber_brass.brass_armory.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ConfusionEffect extends MobEffect {
    public ConfusionEffect() {
        super(MobEffectCategory.HARMFUL, 15912622);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }
}
