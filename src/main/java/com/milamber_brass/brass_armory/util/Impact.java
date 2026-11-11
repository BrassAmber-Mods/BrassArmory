package com.milamber_brass.brass_armory.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Impact extends ServerExplosion {
    private final Map<BlockPos, BlockState> affectedBlocks;
    public final Set<BlockPos> toBlow;

    public Impact(ServerLevel level, @Nullable Entity entity, Vec3 pos, float range, boolean fire, BlockInteraction interaction) {
        this(level, entity, pos, range, fire, interaction, new LinkedHashMap<>());
    }

    private Impact(ServerLevel level, @Nullable Entity entity, Vec3 pos, float range, boolean fire, BlockInteraction interaction, Map<BlockPos, BlockState> blocks) {
        super(level, entity, null, new ImpactDamageCalculator(entity, blocks), pos, range, fire, interaction);
        this.affectedBlocks = blocks;
        this.toBlow = blocks.keySet();
    }

    public void finalizeExplosion(boolean emitter) {
        Vec3 pos = center();
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (level().getRandom().nextFloat() - level().getRandom().nextFloat()) * 0.2F) * 3.7F);
        if (emitter && getBlockInteraction() != BlockInteraction.KEEP) {
            affectedBlocks.forEach((blockPos, state) -> {
                if (!state.isAir()) level().levelEvent(2001, blockPos, Block.getId(state));
            });
        }
    }

    public static class ImpactDamageCalculator extends ExplosionDamageCalculator {
        private final Entity source;
        private final Map<BlockPos, BlockState> blocks;

        public ImpactDamageCalculator() {
            this(null, new LinkedHashMap<>());
        }

        private ImpactDamageCalculator(Entity source, Map<BlockPos, BlockState> blocks) {
            this.source = source;
            this.blocks = blocks;
        }

        @Override
        public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
            boolean explode = source == null || source.shouldBlockExplode(explosion, level, pos, state, power);
            if (explode) blocks.put(pos.immutable(), state);
            return explode;
        }

        @Override
        public @NotNull Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
            return super.getBlockExplosionResistance(explosion, blockGetter, blockPos, blockState, fluidState).map(f -> (source == null ? f : source.getBlockExplosionResistance(explosion, blockGetter, blockPos, blockState, fluidState, f)) * 0.2F);
        }
    }


    public static class EntityBasedImpactDamageCalculator extends EntityBasedExplosionDamageCalculator {
        public EntityBasedImpactDamageCalculator(Entity source) {
            super(source);
        }

        @Override
        public @NotNull Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
            return super.getBlockExplosionResistance(explosion, blockGetter, blockPos, blockState, fluidState).map((f) ->
                    this.source.getBlockExplosionResistance(explosion, blockGetter, blockPos, blockState, fluidState, f) * 0.2F);
        }
    }

    public static float projectileImpact(Entity entity, BlockPos pos) {
        return  entity.level().getBlockState(pos).getBlock().defaultDestroyTime() / (float) Math.min(entity.getDeltaMovement().length(), 2.0D);
    }
}
