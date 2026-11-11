package com.milamber_brass.brass_armory.data;

import com.milamber_brass.brass_armory.BrassArmory;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;


public class BrassArmoryDamageTypes  {
    public static final ResourceKey<DamageType> BATTLE_AXE = ResourceKey.create(Registries.DAMAGE_TYPE, BrassArmory.locate("battleaxe"));
    public static final ResourceKey<DamageType> BOOMERANG = ResourceKey.create(Registries.DAMAGE_TYPE, BrassArmory.locate("boomerang"));
    public static final ResourceKey<DamageType> DAGGER = ResourceKey.create(Registries.DAMAGE_TYPE, BrassArmory.locate("dagger"));
    public static final ResourceKey<DamageType> FIRE_ROD = ResourceKey.create(Registries.DAMAGE_TYPE, BrassArmory.locate("fire_rod"));
    public static final ResourceKey<DamageType> FLAIL = ResourceKey.create(Registries.DAMAGE_TYPE, BrassArmory.locate("flail"));
    public static final ResourceKey<DamageType> SPEAR = ResourceKey.create(Registries.DAMAGE_TYPE, BrassArmory.locate("spear"));
    public static final ResourceKey<DamageType> IMPALE = ResourceKey.create(Registries.DAMAGE_TYPE, BrassArmory.locate("impale"));
    public static final ResourceKey<DamageType> BLEED = ResourceKey.create(Registries.DAMAGE_TYPE, BrassArmory.locate("bleed"));
    public static final ResourceKey<DamageType> SPIKY_BALL = ResourceKey.create(Registries.DAMAGE_TYPE, BrassArmory.locate("spiky_ball"));
    public static final ResourceKey<DamageType> MACE_SMASH = ResourceKey.create(Registries.DAMAGE_TYPE, BrassArmory.locate("mace_smash"));

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, BrassArmoryDamageTypes::bootstrap);

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(BATTLE_AXE, new DamageType("brass_armory.battleaxe", 0.0F));
        context.register(BOOMERANG, new DamageType("brass_armory.boomerang", 0.0F));
        context.register(DAGGER, new DamageType("brass_armory.dagger", 0.0F));
        context.register(FIRE_ROD, new DamageType("brass_armory.fire_rod", 0.0F));
        context.register(FLAIL, new DamageType("brass_armory.flail", 0.0F));
        context.register(SPEAR, new DamageType("brass_armory.spear", 0.0F));
        context.register(IMPALE, new DamageType("brass_armory.impale", 0.0F));
        context.register(BLEED, new DamageType("brass_armory.bleed", 0.0F));
        context.register(SPIKY_BALL, new DamageType("brass_armory.spiky_ball", 0.0F));
        context.register(MACE_SMASH, new DamageType("brass_armory.mace_smash", 0.0F));
    }
}
