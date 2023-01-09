package com.milamber_brass.brass_armory.util;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class Impact extends Explosion {
    public Impact(Level level, @Nullable Entity entity, Vec3 pos, float range, boolean fire, BlockInteraction blockInteraction) {
        super(level, entity, pos.x, pos.y, pos.z, range, fire, blockInteraction);
    }

    @Override
    public @NotNull ExplosionDamageCalculator makeDamageCalculator(@Nullable Entity entity) {
        return entity == null ? new ImpactDamageCalculator() : new EntityBasedImpactDamageCalculator(entity);
    }

    @Override
    public void finalizeExplosion(boolean emitter) {
        if (!this.level.isClientSide) {
            Vec3 vec3 = this.getPosition();
            this.level.playSound(null, vec3.x, vec3.y, vec3.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 3.7F);

            if (this.blockInteraction != BlockInteraction.NONE) {
                ObjectArrayList<Pair<ItemStack, BlockPos>> list = new ObjectArrayList<>();
                Collections.shuffle(this.toBlow);

                for (BlockPos blockpos : this.toBlow) {
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    if (!blockstate.isAir()) {
                        BlockPos blockpos1 = blockpos.immutable();
                        this.level.getProfiler().push("explosion_blocks");
                        if (blockstate.canDropFromExplosion(this.level, blockpos, this) && this.level instanceof ServerLevel) {
                            BlockEntity blockentity = blockstate.hasBlockEntity() ? this.level.getBlockEntity(blockpos) : null;
                            LootContext.Builder builder = (new LootContext.Builder((ServerLevel) this.level)).withRandom(this.level.random).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).withOptionalParameter(LootContextParams.THIS_ENTITY, this.source);
                            if (this.blockInteraction == BlockInteraction.DESTROY) {
                                builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.radius);
                            }

                            blockstate.getDrops(builder).forEach((itemStack) -> addBlockDrops(list, itemStack, blockpos1));
                        }
                        if (emitter) this.level.levelEvent(2001, blockpos, Block.getId(blockstate));
                        blockstate.onBlockExploded(this.level, blockpos, this);
                        this.level.getProfiler().pop();
                    }
                }

                for (Pair<ItemStack, BlockPos> pair : list) {
                    Block.popResource(this.level, pair.getSecond(), pair.getFirst());
                }
            }

            if (this.fire) {
                for (BlockPos blockpos2 : this.toBlow) {
                    if (this.random.nextInt(3) == 0 && this.level.getBlockState(blockpos2).isAir() && this.level.getBlockState(blockpos2.below()).isSolidRender(this.level, blockpos2.below())) {
                        this.level.setBlockAndUpdate(blockpos2, BaseFireBlock.getState(this.level, blockpos2));
                    }
                }
            }
        }
    }


    public static class ImpactDamageCalculator extends ExplosionDamageCalculator {
        public ImpactDamageCalculator() {
            super();
        }

        @Override
        public @NotNull Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
            return super.getBlockExplosionResistance(explosion, blockGetter, blockPos, blockState, fluidState).map(f -> f * 0.2F);
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
}
