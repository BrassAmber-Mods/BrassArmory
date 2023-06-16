package com.milamber_brass.brass_armory.entity.projectile.arrow;

import com.milamber_brass.brass_armory.client.render.SpecialArrowRenderer;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractSpecialArrowEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FrostArrowEntity extends AbstractSpecialArrowEntity {
    public FrostArrowEntity(EntityType<? extends FrostArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FrostArrowEntity(Level level, LivingEntity shooter) {
        super(BrassArmoryEntityTypes.FROST_ARROW.get(), level, shooter);
        this.setBaseDamage(3D);
    }

    public FrostArrowEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.FROST_ARROW.get(), level, x, y, z);
        this.setBaseDamage(3D);
    }

    @Override
    public void tick() {
        if (!this.dealtDamage) {
            Vec3 thisPos = this.position();
            Vec3 nextPos = thisPos.add(this.getDeltaMovement().scale(1.1D));
            BlockHitResult blockHitResult = this.level().clip(new ClipContext(thisPos, nextPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, this));

            if (blockHitResult.getType().equals(HitResult.Type.BLOCK)) {
                BlockState state = this.level().getBlockState(blockHitResult.getBlockPos());
                if (state.is(Blocks.WATER) && ((!this.isUnderWater() && this.tickCount > 2) || !this.level().getBlockState(this.blockPosition()).is(Blocks.WATER))) {
                    freezeAOE(this.level(), blockHitResult.getBlockPos(), this);
                    this.dealtDamage = true;
                }
            }
        }
        super.tick();
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 70, 3));
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.dealtDamage) {
            freezeAOE(this.level(), result.getBlockPos(), this);
        }
    }

    public static void freezeAOE(Level level, BlockPos pos, @Nullable Entity entity) {
        BlockState blockstate = Blocks.ICE.defaultBlockState();

        for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-3, 0, -3), pos.offset(3, 0, 3))) {
            if (blockpos.closerThan(pos, 2.5F)) {
                FluidState flu = level.getFluidState(blockpos);

                if (flu.is(FluidTags.WATER) && flu.isSource() && level.getBlockState(blockpos).canBeReplaced() && blockstate.canSurvive(level, blockpos) && !ForgeEventFactory.onBlockPlace(entity, BlockSnapshot.create(level.dimension(), level, blockpos), Direction.UP)) {
                    level.setBlockAndUpdate(blockpos, blockstate);
                    level.scheduleTick(blockpos, Blocks.FROSTED_ICE, Mth.nextInt(level.random, 60, 120));
                }
            }
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return BrassArmoryItems.FROST_ARROW.get().getDefaultInstance();
    }

    @Override
    protected void spawnArrowParticles(int particleCount) {
        for (int j = 0; j < particleCount; ++j) {
            this.level().addParticle(ParticleTypes.FALLING_WATER, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 40, 75, 40);
        }
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return SpecialArrowRenderer.FROST_ARROW_TEXTURE;
    }
}