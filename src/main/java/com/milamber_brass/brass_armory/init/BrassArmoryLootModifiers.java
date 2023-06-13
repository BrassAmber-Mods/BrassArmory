package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.loot.WarpCrystalLootModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrassArmoryLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BrassArmory.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> WARP_CRYSTAL_LOOT_MODIFIER = REGISTRY.register("warp_crystal_loot_modifier", WarpCrystalLootModifier.CODEC);

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
