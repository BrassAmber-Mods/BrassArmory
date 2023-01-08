package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.effect.BleedEffect;
import com.milamber_brass.brass_armory.effect.ConfusionEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrassArmoryEffects {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BrassArmory.MOD_ID);

    public static final RegistryObject<MobEffect> BLEEDING = REGISTRY.register("bleed", BleedEffect::new);
    public static final RegistryObject<MobEffect> CONFUSION = REGISTRY.register("confusion", ConfusionEffect::new);

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
