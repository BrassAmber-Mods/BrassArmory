package com.milamber_brass.brass_armory.entity.projectile.abstracts;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class AbstractSpecialArrowEntity extends AbstractArrow {
    protected boolean dealtDamage = false;

    public AbstractSpecialArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public AbstractSpecialArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level, LivingEntity shooter) {
        super(entityType, shooter, level);
        this.pickup = Pickup.CREATIVE_ONLY;
    }

    public AbstractSpecialArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level, double x, double y, double z) {
        super(entityType, x, y, z, level);
        this.pickup = Pickup.CREATIVE_ONLY;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!result.getType().equals(HitResult.Type.MISS)) this.dealtDamage = true;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() == this.getOwner() && this.tickCount < 2) return;
        super.onHitEntity(entityHitResult);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (!this.inGround) this.spawnArrowParticles(2);
            else if (this.inGroundTime % 20 == 0) this.spawnArrowParticles(1);
        } else if (this.inGround && this.inGroundTime != 0 && this.inGroundTime >= 600) {
            this.level.broadcastEntityEvent(this, (byte)0);
        }
    }

    protected abstract void spawnArrowParticles(int particleCount);

    public abstract ResourceLocation getTextureLocation();

    @Override
    public @NotNull Component getName() {
        return this.getPickupItem().getItem().getName(this.getPickupItem());
    }

    @Override
    @Nonnull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}