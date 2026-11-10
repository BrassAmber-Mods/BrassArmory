package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@ParametersAreNonnullByDefault
public class BrassArmoryParticles {
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, BrassArmory.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOD_FALL_PARTICLE = REGISTRY.register("blood_fall_particle", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOD_LAND_PARTICLE = REGISTRY.register("blood_land_particle", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

}
