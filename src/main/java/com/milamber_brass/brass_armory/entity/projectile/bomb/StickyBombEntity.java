package com.milamber_brass.brass_armory.entity.projectile.bomb;

import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class StickyBombEntity extends BombEntity {

    public StickyBombEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public StickyBombEntity(Level level, LivingEntity livingEntity, @Nullable HumanoidArm arm) {
        super(level, livingEntity, arm);
    }

    public StickyBombEntity(Level level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Override
    public BombType getBombType() {
        return BombType.STICKY;
    }

    @Override
    public boolean isOnGround() {
        return this.isNoGravity();
    }

    @Override
    protected void onGroundTick() {
        this.isStuck(null);
    }

    private boolean isStuck(@Nullable Vec3 vec) { //Checks if the boundingBox is in a block or not, if it is, it stays put
        AABB aabb = this.getBoundingBox().inflate(0.1D);
        if (vec != null) aabb = aabb.move(vec);
        boolean flag = !this.level.noCollision(this, aabb) || this.getVehicle() instanceof LivingEntity;
        this.setNoGravity(flag);
        if (flag) this.setDeltaMovement(0, 0, 0);
        return flag;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        this.setDeltaMovement(0, 0, 0);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!this.level.isClientSide) {
            Entity mount = entityHitResult.getEntity();
            if (this.getOwner() != mount && mount instanceof LivingEntity) this.startRiding(entityHitResult.getEntity());
        }
        super.onHitEntity(entityHitResult);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHitBlock(BlockHitResult blockHitResult) {
        Vec3 movement = this.getDeltaMovement();
        for (double d = 0D; true; d += 0.025D) {
            Vec3 newMovement = movement.multiply(d, d, d);
            if (this.isStuck(newMovement)) {
                this.moveTo(this.position().add(newMovement));
                break;
            }
        }
    }

    @Override
    protected SoundEvent getSoundEvent() {
        return BrassArmorySounds.STICKY_BOMB_HIT.get();
    }
}
