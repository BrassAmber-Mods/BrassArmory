package com.milamber_brass.brass_armory.entity.projectile.arrow;

import com.milamber_brass.brass_armory.block.RopeBlock;
import com.milamber_brass.brass_armory.client.render.SpecialArrowRenderer;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractSpecialArrowEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryBlocks;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RopeArrowEntity extends AbstractSpecialArrowEntity {
    private boolean isPlacingRope = false;
    private BlockPos currentRopePos;
    private int totalRope = 0;
    private Direction hitBlockFaceDirection;
    private int ticksSinceRope;

    public RopeArrowEntity(EntityType<? extends RopeArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public RopeArrowEntity(Level level, LivingEntity shooter) {
        super(BrassArmoryEntityTypes.ROPE_ARROW.get(), level, shooter);
        this.setBaseDamage(0.25D);
    }

    public RopeArrowEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.ROPE_ARROW.get(), level, x, y, z);
        this.setBaseDamage(0.25D);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isPlacingRope) {
            this.ticksSinceRope++; // Increase the counter for number of ticks since last placing a rope
            if (this.ticksSinceRope > 6) {
                BlockPos newPos = currentRopePos.relative(Direction.DOWN, 1);
                int maxRopeLength = 24;
                if (this.level().getBlockState(newPos).isAir() && this.totalRope < maxRopeLength) {
                    this.level().setBlock(newPos, BrassArmoryBlocks.EXPLORERS_ROPE_BLOCK.get().defaultBlockState().setValue(RopeBlock.FACING, this.hitBlockFaceDirection).setValue(RopeBlock.HAS_ARROW, this.totalRope == 0), 2);
                    this.currentRopePos = newPos;
                    this.totalRope++;
                    this.ticksSinceRope = 0;
                } else {
                    // No space to place more Ropes or Rope Limit reached.
                    this.isPlacingRope = false;
                    this.discard();
                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        Direction direction = result.getDirection();
        if (direction.equals(Direction.DOWN)) {
            this.setDeltaMovement(BombEntity.bounce(this.getDeltaMovement(), direction.getAxis(), 0.5D).scale(0.5D));
        } else {
            super.onHitBlock(result);
            if (!direction.equals(Direction.UP) && RopeBlock.canAttachTo(this.level(), result.getBlockPos(), direction)) {
                this.placeRopes(result);
                this.setInvisible(true);
            }
        }
    }

    private void placeRopes(BlockHitResult result) {
        this.hitBlockFaceDirection = result.getDirection();
        if (!this.hitBlockFaceDirection.equals(Direction.DOWN) && !this.hitBlockFaceDirection.equals(Direction.UP)) {
            BlockPos hitPos = result.getBlockPos();
            this.currentRopePos = hitPos.relative(this.hitBlockFaceDirection);
            BlockState hitBlockState = this.level().getBlockState(hitPos);
            // Check if the block that the arrow hit can hold the Rope.
            if (hitBlockState.isFaceSturdy(this.level(), this.currentRopePos, this.hitBlockFaceDirection)) {
                // Check if there's space to place a Rope.
                if (this.level().getBlockState(this.currentRopePos).isAir()) {
                    this.level().setBlock(this.currentRopePos, BrassArmoryBlocks.EXPLORERS_ROPE_BLOCK.get().defaultBlockState().setValue(RopeBlock.FACING, this.hitBlockFaceDirection).setValue(RopeBlock.HAS_ARROW, this.totalRope == 0), 2);
                    this.totalRope++;
                    this.isPlacingRope = true;
                    // Prevent the arrow from being picked up while the ropes are being placed.
                    this.pickup = Pickup.DISALLOWED;
                }
            }
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return BrassArmoryItems.ROPE_ARROW.get().getDefaultInstance();
    }

    @Override
    protected void spawnArrowParticles(int particleCount) {

    }

    @Override
    public ResourceLocation getTextureLocation() {
        return SpecialArrowRenderer.ROPE_ARROW_TEXTURE;
    }
}