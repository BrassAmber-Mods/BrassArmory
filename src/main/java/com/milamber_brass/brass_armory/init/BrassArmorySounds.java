package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrassArmorySounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BrassArmory.MOD_ID);

    //BOMBS
    public static final RegistryObject<SoundEvent> BOMB_LIT = REGISTRY.register("item.bomb.lit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.bomb.lit")));
    public static final RegistryObject<SoundEvent> BOMB_THROW = REGISTRY.register("item.bomb.throw", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.bomb.throw")));
    public static final RegistryObject<SoundEvent> BOMB_FUSE = REGISTRY.register("item.bomb.fuse", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.bomb.fuse")));
    public static final RegistryObject<SoundEvent> BOMB_HIT = REGISTRY.register("item.bomb.hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.bomb.hit")));
    public static final RegistryObject<SoundEvent> BOUNCY_BOMB_HIT = REGISTRY.register("item.bomb.bouncy_hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.bomb.bouncy_hit")));
    public static final RegistryObject<SoundEvent> STICKY_BOMB_HIT = REGISTRY.register("item.bomb.sticky_hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.bomb.sticky_hit")));

    //SPIKY BALLS
    public static final RegistryObject<SoundEvent> SPIKY_BALL_THROW = REGISTRY.register("item.spiky_ball.throw", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.spiky_ball.throw")));
    public static final RegistryObject<SoundEvent> SPIKY_BALL_HIT = REGISTRY.register("item.spiky_ball.hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.spiky_ball.hit")));

    //FLAILS
    public static final RegistryObject<SoundEvent> FLAIL_THROW = REGISTRY.register("item.flail.throw", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.flail.throw")));
    public static final RegistryObject<SoundEvent> FLAIL_HIT = REGISTRY.register("item.flail.hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.flail.hit")));

    //SPEARS
    public static final RegistryObject<SoundEvent> SPEAR_THROW = REGISTRY.register("item.spear.throw", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.spear.throw")));
    public static final RegistryObject<SoundEvent> SPEAR_HIT = REGISTRY.register("item.spear.hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.spear.hit")));
    public static final RegistryObject<SoundEvent> SPEAR_HIT_GROUND = REGISTRY.register("item.spear.hit_ground", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.spear.hit_ground")));

    //DAGGERS
    public static final RegistryObject<SoundEvent> DAGGER_THROW = REGISTRY.register("item.dagger.throw", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.dagger.throw")));
    public static final RegistryObject<SoundEvent> DAGGER_HIT = REGISTRY.register("item.dagger.hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.dagger.hit")));
    public static final RegistryObject<SoundEvent> DAGGER_HIT_GROUND = REGISTRY.register("item.dagger.hit_ground", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.dagger.hit_ground")));

    //BATTLEAXES
    public static final RegistryObject<SoundEvent> BATTLEAXE_THROW = REGISTRY.register("item.battleaxe.throw", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.battleaxe.throw")));
    public static final RegistryObject<SoundEvent> BATTLEAXE_HIT = REGISTRY.register("item.battleaxe.hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.battleaxe.hit")));
    public static final RegistryObject<SoundEvent> BATTLEAXE_HIT_GROUND = REGISTRY.register("item.battleaxe.hit_ground", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.battleaxe.hit_ground")));

    //BOOMERANGS
    public static final RegistryObject<SoundEvent> BOOMERANG_THROW = REGISTRY.register("item.boomerang.throw", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.boomerang.throw")));
    public static final RegistryObject<SoundEvent> BOOMERANG_HIT = REGISTRY.register("item.boomerang.hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.boomerang.hit")));
    public static final RegistryObject<SoundEvent> BOOMERANG_HIT_GROUND = REGISTRY.register("item.boomerang.hit_ground", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.boomerang.hit_ground")));

    //FIRE_ROD
    public static final RegistryObject<SoundEvent> FIRE_ROD_THROW = REGISTRY.register("item.fire_rod.throw", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.fire_rod.throw")));
    public static final RegistryObject<SoundEvent> FIRE_ROD_HIT = REGISTRY.register("item.fire_rod.hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.fire_rod.hit")));
    public static final RegistryObject<SoundEvent> FIRE_ROD_HIT_GROUND = REGISTRY.register("item.fire_rod.hit_ground", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.fire_rod.hit_ground")));

    //MACES
    public static final RegistryObject<SoundEvent> MACE_SMASH = REGISTRY.register("item.mace.smash", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.mace.smash")));

    //GUNS
    public static final RegistryObject<SoundEvent> GUN_LOAD = REGISTRY.register("item.gun.load", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.gun.load")));
    public static final RegistryObject<SoundEvent> GUN_SHOOT = REGISTRY.register("item.gun.shoot", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.gun.shoot")));

    //KATANAS
    public static final RegistryObject<SoundEvent> KATANA_SMALL_UPGRADE = REGISTRY.register("item.katana.small_upgrade", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.katana.small_upgrade")));
    public static final RegistryObject<SoundEvent> KATANA_LARGE_UPGRADE = REGISTRY.register("item.katana.large_upgrade", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "item.katana.large_upgrade")));

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
