package com.milamber_brass.brass_armory.entity.projectile.cannon_balls;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractBulletEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryCapabilities;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.util.Impact;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CannonBallEntity extends AbstractBulletEntity {
    private static final EntityDataAccessor<Integer> DATA_HITS = SynchedEntityData.defineId(CannonBallEntity.class, EntityDataSerializers.INT);
    protected double peakSpeed = 0.0D;

    public CannonBallEntity(EntityType<? extends AbstractBulletEntity> entityType, Level level) {
        super(entityType, level);
    }

    public CannonBallEntity(Level level, LivingEntity livingEntity) {
        super(BrassArmoryEntityTypes.CANNON_BALL.get(), livingEntity, level);
    }

    public CannonBallEntity(double x, double y, double z, Level level) {
        super(BrassArmoryEntityTypes.CANNON_BALL.get(), x, y, z, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HITS, 0);
    }

    @Override
    public void shootFromRotation(Entity shooter, float xRot, float yRot, float v, float speed, float accuracy) {
        super.shootFromRotation(shooter, xRot, yRot, v, speed * 0.5F, accuracy);
    }

    protected void speedCheck(float speed) {
        if (this.peakSpeed < speed) this.peakSpeed = speed;
    }

    @Override
    public void tick() {
        this.speedCheck((float)this.getDeltaMovement().length());
        super.tick();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        double currentSpeed = this.getDeltaMovement().length();
        int hits = this.getEntityData().get(DATA_HITS);
        double peak = this.peakSpeed * ((double)hits * 0.1D + 0.2D);
        if (currentSpeed > peak) {
            float radius = (float)((currentSpeed / this.peakSpeed) * currentSpeed) * 0.95F;

            BlockPos pos = blockHitResult.getBlockPos();
            Optional<Float> resistance = Optional.empty();
            if (this.level() instanceof ServerLevel serverLevel) {
                Impact probe = new Impact(serverLevel, this, blockHitResult.getLocation(), radius, this.isOnFire(), Explosion.BlockInteraction.DESTROY);
                resistance = new Impact.EntityBasedImpactDamageCalculator(this).getBlockExplosionResistance(probe, serverLevel, pos, serverLevel.getBlockState(pos), serverLevel.getFluidState(pos));
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(Math.max(0.8F - (resistance.orElse(1.0F) * 0.2F), 0.1F)));

            if (this.level() instanceof ServerLevel serverLevel) {
                Impact impact = new Impact(serverLevel, this, blockHitResult.getLocation(), radius, this.isOnFire(), Explosion.BlockInteraction.DESTROY);
                impact.explode();
                impact.finalizeExplosion(true);
                this.getEntityData().set(DATA_HITS, this.getEntityData().get(DATA_HITS) + 1);
                Optional.ofNullable(this.getData(BrassArmoryCapabilities.POWDER_CAPABILITY).getPowderBehaviour()).ifPresent(powderBehaviour -> {
                    for (BlockPos blockpos : impact.toBlow) {
                        BlockState blockstate = serverLevel.getBlockState(blockpos);
                        if (!blockstate.isAir()) powderBehaviour.sendParticles(serverLevel, Vec3.atCenterOf(blockpos));
                    }
                });
            }

            if (!this.level().getBlockState(pos).isAir()) super.onHitBlock(blockHitResult);
        } else super.onHitBlock(blockHitResult);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (this.isNoPhysics()) return;
        Entity victimEntity = entityHitResult.getEntity();
        Entity ownerEntity = this.getOwner();

        float damage = (float)Mth.clamp(this.getDeltaMovement().length() * this.baseDamage, 0.0D, 2.147483647E9D);
        if (this.isCritArrow()) damage *= 1.5F;

        if (victimEntity.hurtOrSimulate(this.damageSources().thrown(this, ownerEntity), damage)) {
            if (victimEntity.getType() == net.minecraft.world.entity.EntityTypes.ENDERMAN) return;
            if (this.isOnFire()) victimEntity.igniteForSeconds(40);

            if (victimEntity instanceof LivingEntity livingVictim) {
                if (ownerEntity instanceof LivingEntity livingOwner) {
                    if (this.level() instanceof ServerLevel serverLevel) EnchantmentHelper.doPostAttackEffects(serverLevel, livingVictim, this.damageSources().thrown(this, livingOwner));
                }
                this.doPostHurtEffects(livingVictim);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().scale(0.95D));
        this.playSound(this.getDefaultHitGroundSoundEvent(), 1.0F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putDouble("peakSpeed", this.peakSpeed);
        tag.putInt("hitCount", this.getEntityData().get(DATA_HITS));
    }

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        this.peakSpeed = tag.getDoubleOr("peakSpeed", 0D);
        this.getEntityData().set(DATA_HITS, tag.getIntOr("hitCount", 0));
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return BrassArmoryItems.CANNON_BALL.get();
    }
}
