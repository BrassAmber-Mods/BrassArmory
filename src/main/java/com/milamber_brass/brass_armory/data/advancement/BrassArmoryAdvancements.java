package com.milamber_brass.brass_armory.data.advancement;

import net.minecraft.advancements.CriteriaTriggers;

public class BrassArmoryAdvancements {
    public static final WitherKatanaTrigger WITHER_KATANA_TRIGGER = CriteriaTriggers.register(new WitherKatanaTrigger());
    public static final MaceSmashTrigger MACE_SMASH_TRIGGER = CriteriaTriggers.register(new MaceSmashTrigger());
    public static final BombLightTrigger THROW_BOMB = CriteriaTriggers.register(new BombLightTrigger());
    public static final BombCatchTrigger CATCH_BOMB = CriteriaTriggers.register(new BombCatchTrigger());
    public static final LongDistanceWarpArrowTrigger LONG_DISTANCE_WARP_ARROW_TRIGGER = CriteriaTriggers.register(new LongDistanceWarpArrowTrigger());
    public static final LongbowTrigger LONGBOW = CriteriaTriggers.register(new LongbowTrigger());

    public static void init() {}
}
