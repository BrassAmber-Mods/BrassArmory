package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractBulletEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.TierSortingRegistry;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static net.minecraft.world.phys.HitResult.Type.BLOCK;

@ParametersAreNonnullByDefault
public class BulletEntity extends AbstractBulletEntity {
    private int hitPerTick;

    public BulletEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public BulletEntity(Level level, LivingEntity livingEntity) {
        super(BrassArmoryEntityTypes.BULLET.get(), livingEntity, level);
    }

    @Override
    public void tick() {
        this.hitPerTick = 0;
        super.tick();
        if (!this.isNoGravity() && !this.inGround && !this.isNoPhysics()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.025D, 0));
        }
    }

    @Override
    public void startFalling() {
        this.discard();
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (++this.hitPerTick > 8) {
            this.discard();
            return;
        }
        super.onHit(hitResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState blockState = this.level.getBlockState(pos);

        if (blockState.getBlock() instanceof IronBarsBlock && blockState.getMaterial().equals(Material.GLASS) && TierSortingRegistry.isCorrectTierForDrops(Tiers.IRON, blockState)) {
            this.level.destroyBlock(pos, true, this.getOwner());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
        } else {
            double hardness = Mth.clamp(blockState.getDestroySpeed(this.level, pos) * 0.15D, 0.0D, 1.0D) * 0.95D;

            if (this.getDeltaMovement().length() > (0.95D - hardness)) {
                this.setDeltaMovement(ricochet(this.getDeltaMovement(), blockHitResult.getDirection().getAxis(), this.random, hardness));
                HitResult newHitResult = ProjectileUtil.getHitResult(this, this::canHitEntity);
                if (newHitResult.getType() == BLOCK) this.onHit(newHitResult);
            } else super.onHitBlock(blockHitResult);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return BrassArmoryItems.MUSKET_BALL.get();
    }

    @Nonnull
    public static Vec3 ricochet(Vec3 deltaMovement, Direction.Axis axis, Random random, double hardness) {
        double newX = deltaMovement.x * (axis.equals(Direction.Axis.X) ? -hardness : hardness);
        double newY = deltaMovement.y * (axis.equals(Direction.Axis.Y) ? -hardness : hardness);
        double newZ = deltaMovement.z * (axis.equals(Direction.Axis.Z) ? -hardness : hardness);
        return new Vec3(newX, newY, newZ).add(random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity target = entityHitResult.getEntity();
        int invulnerableTime = target.invulnerableTime;
        target.invulnerableTime = 0;
        super.onHitEntity(entityHitResult);
        target.invulnerableTime = invulnerableTime;
        this.discard();
    }
}
