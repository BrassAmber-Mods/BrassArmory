package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.SpearEntity;
import com.milamber_brass.brass_armory.entity.ThrownBattleAxeEntity;
import com.milamber_brass.brass_armory.entity.ThrownDaggerEntity;
import com.milamber_brass.brass_armory.entity.projectile.BAArrowEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BrassArmoryEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, BrassArmory.MOD_ID);

    public static final RegistryObject<EntityType<SpearEntity>> SPEAR =
            ENTITY_TYPES.register("thrown_spear", () -> EntityType.Builder
                    .<SpearEntity>of(SpearEntity::new, EntityClassification.MISC).sized(0.5f, 0.5f)
                    .clientTrackingRange(64).updateInterval(20)
                    .build(new ResourceLocation(BrassArmory.MOD_ID, "thrown_spear").toString()));
    
    public static final RegistryObject<EntityType<ThrownDaggerEntity>> DAGGER =
            ENTITY_TYPES.register("thrown_dagger", () -> EntityType.Builder
                    .<ThrownDaggerEntity>of(ThrownDaggerEntity::new, EntityClassification.MISC).sized(0.5f, 0.5f)
                    .clientTrackingRange(64).updateInterval(20)
                    .build(new ResourceLocation(BrassArmory.MOD_ID, "thrown_dagger").toString()));
    
    public static final RegistryObject<EntityType<ThrownBattleAxeEntity>> BATTLEAXE =
            ENTITY_TYPES.register("thrown_battleaxe", () -> EntityType.Builder
                    .<ThrownBattleAxeEntity>of(ThrownBattleAxeEntity::new, EntityClassification.MISC).sized(0.5f, 0.5f)
                    .clientTrackingRange(64).updateInterval(20)
                    .build(new ResourceLocation(BrassArmory.MOD_ID, "thrown_battleaxe").toString()));

    public static final RegistryObject<EntityType<BAArrowEntity>> BA_ARROW = registerEntityType("ba_arrow",
            EntityType.Builder.<BAArrowEntity>of(BAArrowEntity::new, EntityClassification.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20));

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

