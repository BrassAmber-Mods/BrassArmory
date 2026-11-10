package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.effect.BleedEffect;
import com.milamber_brass.brass_armory.effect.ConfusionEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BrassArmoryEffects {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, BrassArmory.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> BLEEDING = REGISTRY.register("bleed", BleedEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> CONFUSION = REGISTRY.register("confusion", ConfusionEffect::new);

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
