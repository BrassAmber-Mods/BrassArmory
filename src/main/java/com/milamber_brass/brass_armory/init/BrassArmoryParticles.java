package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.client.particle.BloodParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrassArmoryParticles {
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, BrassArmory.MOD_ID);

    public static final RegistryObject<SimpleParticleType> BLOOD_FALL_PARTICLE = REGISTRY.register("blood_fall_particle", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BLOOD_LAND_PARTICLE = REGISTRY.register("blood_land_particle", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BrassArmoryParticles.BLOOD_FALL_PARTICLE.get(), BloodParticle.BloodFallProvider::new);
        event.registerSpriteSet(BrassArmoryParticles.BLOOD_LAND_PARTICLE.get(), BloodParticle.BloodLandProvider::new);
    }
}
