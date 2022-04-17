package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static net.minecraft.world.phys.HitResult.Type.BLOCK;

public class BulletEntity extends AbstractArrow {
    private int hitPerTick;

    public BulletEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public BulletEntity(LivingEntity livingEntity, Level level) {
        super(BrassArmoryEntityTypes.BULLET.get(), livingEntity, level);
    }

    @Override
    public void tick() {
        this.tickDespawn();
        this.hitPerTick = 0;
        boolean wasInGround = this.inGround;
        super.tick();
        if (!this.isNoGravity() && !this.inGround && !this.isNoPhysics()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.025D, 0));
        }
        if (wasInGround && !this.inGround) this.discard();
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHit(HitResult hitResult) {
        if (++this.hitPerTick > 8) {
            this.discard();
            return;
        }

        HitResult.Type resultType = hitResult.getType();
        if (resultType == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)hitResult);
            if (((EntityHitResult)hitResult).getEntity() instanceof LivingEntity) {
                this.gameEvent(GameEvent.PROJECTILE_LAND, this.getOwner());
            }
        } else if (resultType == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult)hitResult);
            this.gameEvent(GameEvent.PROJECTILE_LAND, this.getOwner());
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHitBlock(BlockHitResult blockHitResult) {
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState blockState = this.level.getBlockState(pos);

        if (blockState.getBlock() instanceof IronBarsBlock && blockState.getMaterial().equals(Material.GLASS)) {
            this.level.destroyBlock(pos, true, this.getOwner());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
        } else {
            double hardness;
            if (blockState.is(BlockTags.MINEABLE_WITH_PICKAXE)) hardness = 0.85D;
            else if (blockState.is(BlockTags.MINEABLE_WITH_AXE)) hardness = 0.5D;
            else hardness = 0D;


            if (hardness > 0D && this.getDeltaMovement().length() > 0.55D) {
                if (blockState.is(Tags.Blocks.STORAGE_BLOCKS)) hardness += 0.1D;
                this.setDeltaMovement(ricochet(this.getDeltaMovement(), blockHitResult.getDirection().getAxis(), this.random, hardness));
                HitResult newHitResult = ProjectileUtil.getHitResult(this, this::canHitEntity);
                if (newHitResult.getType() == BLOCK) this.onHit(newHitResult);
            } else super.onHitBlock(blockHitResult);
        }
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static Vec3 ricochet(Vec3 deltaMovement, Direction.Axis axis, Random random, double hardness) {
        double newX = deltaMovement.x * (axis.equals(Direction.Axis.X) ? -hardness : hardness);
        double newY = deltaMovement.y * (axis.equals(Direction.Axis.Y) ? -hardness : hardness);
        double newZ = deltaMovement.z * (axis.equals(Direction.Axis.Z) ? -hardness : hardness);
        return new Vec3(newX, newY, newZ).add(random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity target = entityHitResult.getEntity();
        int invulnerableTime = target.invulnerableTime;
        target.invulnerableTime = 0;
        super.onHitEntity(entityHitResult);
        target.invulnerableTime = invulnerableTime;
        this.discard();
    }

    @Override
    @ParametersAreNonnullByDefault
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Nonnull
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
