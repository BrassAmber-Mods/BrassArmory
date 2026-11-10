package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.data.advancement.*;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BrassArmoryAdvancements {
    public static final WitherKatanaTrigger WITHER_KATANA_TRIGGER = new WitherKatanaTrigger();
    public static final MaceSmashTrigger MACE_SMASH_TRIGGER = new MaceSmashTrigger();
    public static final BombLightTrigger THROW_BOMB = new BombLightTrigger();
    public static final BombCatchTrigger CATCH_BOMB = new BombCatchTrigger();
    public static final LongDistanceWarpArrowTrigger LONG_DISTANCE_WARP_ARROW_TRIGGER = new LongDistanceWarpArrowTrigger();
    public static final LongbowTrigger LONGBOW = new LongbowTrigger();
    public static final GunTrigger LOAD_GUN = new GunTrigger();
    public static final CannonTrigger FIRE_CANNON = new CannonTrigger();
    public static final SiegeTrigger SIEGE = new SiegeTrigger();
    public static final DragonRoundTrigger DRAGON_ROUND = new DragonRoundTrigger();

    public static final DeferredRegister<CriterionTrigger<?>> REGISTRY = DeferredRegister.create(Registries.TRIGGER_TYPE, BrassArmory.MOD_ID);
    static {
        REGISTRY.register(WitherKatanaTrigger.ID.getPath(), () -> WITHER_KATANA_TRIGGER);
        REGISTRY.register(MaceSmashTrigger.ID.getPath(), () -> MACE_SMASH_TRIGGER);
        REGISTRY.register(BombLightTrigger.ID.getPath(), () -> THROW_BOMB);
        REGISTRY.register(BombCatchTrigger.ID.getPath(), () -> CATCH_BOMB);
        REGISTRY.register(LongDistanceWarpArrowTrigger.ID.getPath(), () -> LONG_DISTANCE_WARP_ARROW_TRIGGER);
        REGISTRY.register(LongbowTrigger.ID.getPath(), () -> LONGBOW);
        REGISTRY.register(GunTrigger.ID.getPath(), () -> LOAD_GUN);
        REGISTRY.register(CannonTrigger.ID.getPath(), () -> FIRE_CANNON);
        REGISTRY.register(SiegeTrigger.ID.getPath(), () -> SIEGE);
        REGISTRY.register(DragonRoundTrigger.ID.getPath(), () -> DRAGON_ROUND);
    }
    public static void init() {}

}
