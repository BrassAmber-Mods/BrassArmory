package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.CannonEntity;
import com.milamber_brass.brass_armory.entity.projectile.*;
import com.milamber_brass.brass_armory.entity.projectile.arrow.*;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BouncyBombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.StickyBombEntity;
import com.milamber_brass.brass_armory.entity.projectile.cannon_balls.CannonBallEntity;
import com.milamber_brass.brass_armory.entity.projectile.cannon_balls.CarcassRoundEntity;
import com.milamber_brass.brass_armory.entity.projectile.cannon_balls.SiegeRoundEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BrassArmoryEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, BrassArmory.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<DirtArrowEntity>> DIRT_ARROW = ENTITY_TYPES.register("dirt_arrow", () ->
            EntityType.Builder.<DirtArrowEntity>of(DirtArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "dirt_arrow"))));

    public static final DeferredHolder<EntityType<?>, EntityType<ExplosionArrowEntity>> EXPLOSION_ARROW = ENTITY_TYPES.register("explosion_arrow", () ->
            EntityType.Builder.<ExplosionArrowEntity>of(ExplosionArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "explosion_arrow"))));

    public static final DeferredHolder<EntityType<?>, EntityType<FrostArrowEntity>> FROST_ARROW = ENTITY_TYPES.register("frost_arrow", () ->
            EntityType.Builder.<FrostArrowEntity>of(FrostArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "frost_arrow"))));

    public static final DeferredHolder<EntityType<?>, EntityType<GrassArrowEntity>> GRASS_ARROW = ENTITY_TYPES.register("grass_arrow", () ->
            EntityType.Builder.<GrassArrowEntity>of(GrassArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "grass_arrow"))));

    public static final DeferredHolder<EntityType<?>, EntityType<LaserArrowEntity>> LASER_ARROW = ENTITY_TYPES.register("laser_arrow", () ->
            EntityType.Builder.<LaserArrowEntity>of(LaserArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "laser_arrow"))));

    public static final DeferredHolder<EntityType<?>, EntityType<RopeArrowEntity>> ROPE_ARROW = ENTITY_TYPES.register("rope_arrow", () ->
            EntityType.Builder.<RopeArrowEntity>of(RopeArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "rope_arrow"))));

    public static final DeferredHolder<EntityType<?>, EntityType<SlimeArrowEntity>> SLIME_ARROW = ENTITY_TYPES.register("slime_arrow", () ->
            EntityType.Builder.<SlimeArrowEntity>of(SlimeArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "slime_arrow"))));

    public static final DeferredHolder<EntityType<?>, EntityType<WarpArrowEntity>> WARP_ARROW = ENTITY_TYPES.register("warp_arrow", () ->
            EntityType.Builder.<WarpArrowEntity>of(WarpArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "warp_arrow"))));

    public static final DeferredHolder<EntityType<?>, EntityType<FireArrowEntity>> FIRE_ARROW = ENTITY_TYPES.register("fire_arrow", () ->
            EntityType.Builder.<FireArrowEntity>of(FireArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "fire_arrow"))));

    public static final DeferredHolder<EntityType<?>, EntityType<ConfusionArrowEntity>> CONFUSION_ARROW = ENTITY_TYPES.register("confusion_arrow", () ->
            EntityType.Builder.<ConfusionArrowEntity>of(ConfusionArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "confusion_arrow"))));

    public static final DeferredHolder<EntityType<?>, EntityType<TorchArrowEntity>> TORCH_ARROW = ENTITY_TYPES.register("torch_arrow", () ->
            EntityType.Builder.<TorchArrowEntity>of(TorchArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "torch_arrow"))));

    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> BULLET = ENTITY_TYPES.register("bullet", () ->
            EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "bullet"))));

    //------------------------------------BOMB-----------------------------------------------------------

    public static final DeferredHolder<EntityType<?>, EntityType<BombEntity>> BOMB = ENTITY_TYPES.register("bomb", () ->
            EntityType.Builder.<BombEntity>of(BombEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "bomb"))));

    public static final DeferredHolder<EntityType<?>, EntityType<BombEntity>> BOUNCY_BOMB = ENTITY_TYPES.register("bouncy_bomb", () ->
            EntityType.Builder.<BombEntity>of(BouncyBombEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "bouncy_bomb"))));

    public static final DeferredHolder<EntityType<?>, EntityType<BombEntity>> STICKY_BOMB = ENTITY_TYPES.register("sticky_bomb", () ->
            EntityType.Builder.<BombEntity>of(StickyBombEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "sticky_bomb"))));

    //------------------------------------THROWN WEAPONS-------------------------------------------------------

    public static final DeferredHolder<EntityType<?>, EntityType<BoomerangEntity>> BOOMERANG = ENTITY_TYPES.register("boomerang", () ->
            EntityType.Builder.<BoomerangEntity>of(BoomerangEntity::new, MobCategory.MISC)
                    .sized(0.75F, 0.2F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "boomerang"))));

    public static final DeferredHolder<EntityType<?>, EntityType<SpearEntity>> SPEAR = ENTITY_TYPES.register("spear", () ->
            EntityType.Builder.<SpearEntity>of(SpearEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "spear"))));

    public static final DeferredHolder<EntityType<?>, EntityType<FireRodEntity>> FIRE_ROD = ENTITY_TYPES.register("fire_rod", () ->
            EntityType.Builder.<FireRodEntity>of(FireRodEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "fire_rod"))));

    public static final DeferredHolder<EntityType<?>, EntityType<DaggerEntity>> DAGGER = ENTITY_TYPES.register("dagger", () ->
            EntityType.Builder.<DaggerEntity>of(DaggerEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "dagger"))));

    public static final DeferredHolder<EntityType<?>, EntityType<BattleaxeEntity>> BATTLEAXE = ENTITY_TYPES.register("battleaxe", () ->
            EntityType.Builder.<BattleaxeEntity>of(BattleaxeEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "battleaxe"))));

    public static final DeferredHolder<EntityType<?>, EntityType<SpikyBallEntity>> SPIKY_BALL = ENTITY_TYPES.register("spiky_ball", () ->
            EntityType.Builder.<SpikyBallEntity>of(SpikyBallEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "spiky_ball"))));

    //------------------------------------------------------

    public static final DeferredHolder<EntityType<?>, EntityType<FlailHeadEntity>> FLAIL_HEAD = ENTITY_TYPES.register("flail_head", () ->
            EntityType.Builder.<FlailHeadEntity>of(FlailHeadEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "flail_head"))));

    //------------------------------------------------------

    public static final DeferredHolder<EntityType<?>, EntityType<CannonEntity>> CANNON = ENTITY_TYPES.register("cannon", () ->
            EntityType.Builder.<CannonEntity>of(CannonEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(4)
                    .fireImmune()
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "cannon"))));

    public static final DeferredHolder<EntityType<?>, EntityType<CannonBallEntity>> CANNON_BALL = ENTITY_TYPES.register("cannon_ball", () ->
            EntityType.Builder.<CannonBallEntity>of(CannonBallEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "cannon_ball"))));

    public static final DeferredHolder<EntityType<?>, EntityType<CarcassRoundEntity>> CARCASS_ROUND = ENTITY_TYPES.register("carcass_round", () ->
            EntityType.Builder.<CarcassRoundEntity>of(CarcassRoundEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "carcass_round"))));

    public static final DeferredHolder<EntityType<?>, EntityType<SiegeRoundEntity>> SIEGE_ROUND = ENTITY_TYPES.register("siege_round", () ->
            EntityType.Builder.<SiegeRoundEntity>of(SiegeRoundEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, "siege_round"))));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}

