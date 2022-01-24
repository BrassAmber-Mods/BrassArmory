package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.SpearEntity;
import com.milamber_brass.brass_armory.entity.projectile.BAArrowEntity;
import com.milamber_brass.brass_armory.entity.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.bomb.BouncyBombEntity;
import com.milamber_brass.brass_armory.entity.bomb.StickyBombEntity;
import com.milamber_brass.brass_armory.entity.projectile.BoomerangEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrassArmoryEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, BrassArmory.MOD_ID);

    public static final RegistryObject<EntityType<SpearEntity>> SPEAR = registerEntityType("spear",
            EntityType.Builder.<SpearEntity>of(SpearEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20));

    public static final RegistryObject<EntityType<BAArrowEntity>> BA_ARROW = registerEntityType("ba_arrow",
            EntityType.Builder.<BAArrowEntity>of(BAArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20));

    //------------------------------------BOMB-----------------------------------------------------------

    public static final RegistryObject<EntityType<BombEntity>> BOMB = registerEntityType("bomb",
            EntityType.Builder.<BombEntity>of(BombEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10));

    public static final RegistryObject<EntityType<BombEntity>> BOUNCY_BOMB = registerEntityType("bouncy_bomb",
            EntityType.Builder.<BombEntity>of(BouncyBombEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10));

    public static final RegistryObject<EntityType<BombEntity>> STICKY_BOMB = registerEntityType("sticky_bomb",
            EntityType.Builder.<BombEntity>of(StickyBombEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10));

    //------------------------------------BOOMERANG-----------------------------------------------------------

    public static final RegistryObject<EntityType<BoomerangEntity>> BOOMERANG = registerEntityType("boomerang",
            EntityType.Builder.<BoomerangEntity>of(BoomerangEntity::new, MobCategory.MISC)
                    .sized(0.75F, 0.2F)
                    .clientTrackingRange(4)
                    .updateInterval(10));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    /**
     * Helper method for registering all EntityTypes
     */
    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntityType(String registryName, EntityType.Builder<T> builder) {
        return ENTITY_TYPES.register(registryName, () -> builder.build(registryName));
    }

}

