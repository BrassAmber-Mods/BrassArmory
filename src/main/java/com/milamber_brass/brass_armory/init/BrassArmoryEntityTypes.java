package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.*;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BouncyBombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.StickyBombEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrassArmoryEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, BrassArmory.MOD_ID);

    public static final RegistryObject<EntityType<BAArrowEntity>> BA_ARROW = ENTITY_TYPES.register("ba_arrow", () ->
            EntityType.Builder.<BAArrowEntity>of(BAArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build("ba_arrow"));

    public static final RegistryObject<EntityType<BulletEntity>> BULLET = ENTITY_TYPES.register("bullet", () ->
            EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build("bullet"));

    //------------------------------------BOMB-----------------------------------------------------------

    public static final RegistryObject<EntityType<BombEntity>> BOMB = ENTITY_TYPES.register("bomb", () ->
            EntityType.Builder.<BombEntity>of(BombEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build("bomb"));

    public static final RegistryObject<EntityType<BombEntity>> BOUNCY_BOMB = ENTITY_TYPES.register("bouncy_bomb", () ->
            EntityType.Builder.<BombEntity>of(BouncyBombEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build("bouncy_bomb"));

    public static final RegistryObject<EntityType<BombEntity>> STICKY_BOMB = ENTITY_TYPES.register("sticky_bomb", () ->
            EntityType.Builder.<BombEntity>of(StickyBombEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build("sticky_bomb"));

    //------------------------------------THROWN WEAPONS-------------------------------------------------------

    public static final RegistryObject<EntityType<BoomerangEntity>> BOOMERANG = ENTITY_TYPES.register("boomerang", () ->
            EntityType.Builder.<BoomerangEntity>of(BoomerangEntity::new, MobCategory.MISC)
                    .sized(0.75F, 0.2F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build("boomerang"));

    public static final RegistryObject<EntityType<SpearEntity>> SPEAR = ENTITY_TYPES.register("spear", () ->
            EntityType.Builder.<SpearEntity>of(SpearEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build("spear"));

    public static final RegistryObject<EntityType<FireRodEntity>> FIRE_ROD = ENTITY_TYPES.register("fire_rod", () ->
            EntityType.Builder.<FireRodEntity>of(FireRodEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build("fire_rod"));

    public static final RegistryObject<EntityType<DaggerEntity>> DAGGER = ENTITY_TYPES.register("dagger", () ->
            EntityType.Builder.<DaggerEntity>of(DaggerEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build("dagger"));

    public static final RegistryObject<EntityType<BattleaxeEntity>> BATTLEAXE = ENTITY_TYPES.register("battleaxe", () ->
            EntityType.Builder.<BattleaxeEntity>of(BattleaxeEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build("battleaxe"));

    public static final RegistryObject<EntityType<SpikyBallEntity>> SPIKY_BALL = ENTITY_TYPES.register("spiky_ball", () ->
            EntityType.Builder.<SpikyBallEntity>of(SpikyBallEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build("spiky_ball"));

    //------------------------------------------------------

    public static final RegistryObject<EntityType<FlailHeadEntity>> FLAIL_HEAD = ENTITY_TYPES.register("flail_head", () ->
            EntityType.Builder.<FlailHeadEntity>of(FlailHeadEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10).build("flail_head"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}

