package com.milamber_brass.brass_armory.util;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
public final class TierUtil {
    public static boolean isCorrectTierForDrops(Tier tier, BlockState state) {
        return !state.is(tier.getIncorrectBlocksForDrops());
    }
}
