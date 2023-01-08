package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.data.advancement.*;
import net.minecraft.advancements.CriteriaTriggers;

public class BrassArmoryAdvancements {
    public static final WitherKatanaTrigger WITHER_KATANA_TRIGGER = CriteriaTriggers.register(new WitherKatanaTrigger());
    public static final MaceSmashTrigger MACE_SMASH_TRIGGER = CriteriaTriggers.register(new MaceSmashTrigger());
    public static final BombLightTrigger THROW_BOMB = CriteriaTriggers.register(new BombLightTrigger());
    public static final BombCatchTrigger CATCH_BOMB = CriteriaTriggers.register(new BombCatchTrigger());
    public static final LongDistanceWarpArrowTrigger LONG_DISTANCE_WARP_ARROW_TRIGGER = CriteriaTriggers.register(new LongDistanceWarpArrowTrigger());
    public static final LongbowTrigger LONGBOW = CriteriaTriggers.register(new LongbowTrigger());
    public static final GunTrigger LOAD_GUN = CriteriaTriggers.register(new GunTrigger());
    public static final CannonTrigger FIRE_CANNON = CriteriaTriggers.register(new CannonTrigger());
    public static final SiegeTrigger SIEGE = CriteriaTriggers.register(new SiegeTrigger());
    public static final DragonRoundTrigger DRAGON_ROUND = CriteriaTriggers.register(new DragonRoundTrigger());

    public static void init() {}
}
