package com.milamber_brass.brass_armory.entity.projectile.cannon_balls;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractBulletEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryAdvancements;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.CarcassRoundItem;
import com.milamber_brass.brass_armory.util.Impact;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class CarcassRoundEntity extends AbstractBulletEntity {
    public static final int LEVEL_EVENT_CONSTANT = 801682224;

    public CarcassRoundEntity(EntityType<? extends AbstractBulletEntity> entityType, Level level) {
        super(entityType, level);
    }

    public CarcassRoundEntity(Level level, LivingEntity livingEntity) {
        super(BrassArmoryEntityTypes.CARCASS_ROUND.get(), livingEntity, level);
    }

    public CarcassRoundEntity(double x, double y, double z, Level level) {
        super(BrassArmoryEntityTypes.CARCASS_ROUND.get(), x, y, z, level);
    }

    @Override
    public void shootFromRotation(Entity shooter, float xRot, float yRot, float v, float speed, float accuracy) {
        super.shootFromRotation(shooter, xRot, yRot, v, speed * 0.5F, accuracy);
    }

    @Override
    public @NotNull ItemStack getItem() {
        if (!this.level.isClientSide()) return super.getItem();
        else return Util.make(super.getItem(), stack -> PotionUtils.setPotion(stack, Potions.SLOW_FALLING));
    }

    @Override
    public void tick() {
        super.tick();
        if (CarcassRoundItem.isADragonRound(this.getItem())) {
            this.level.addParticle(ParticleTypes.DRAGON_BREATH, this.getX(), this.getEyeY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHit(HitResult onHit) {
        Vec3 pos = onHit.getLocation();
        Impact impact = new Impact(this.level, this, pos, 1.0F, this.isOnFire(), Explosion.BlockInteraction.DESTROY);

        if (!this.level.isClientSide) {
            impact.explode();
            impact.finalizeExplosion(true);
            ItemStack stack = this.getItem();
            Potion potion = PotionUtils.getPotion(stack);
            List<MobEffectInstance> list = PotionUtils.getMobEffects(stack);

            Vec3 diff = pos.subtract(this.position());
            if (potion.equals(Potions.WATER) && list.isEmpty()) this.applyWater(diff);
            else if (!list.isEmpty()) {
                this.applySplash(list, onHit instanceof EntityHitResult entityHitResult ? entityHitResult.getEntity() : null, diff);
                this.makeAreaOfEffectCloud(stack, potion, pos);
            }

            if (CarcassRoundItem.isADragonRound(stack)) this.level.levelEvent(2006, this.blockPosition(), -1);
            else this.level.levelEvent(LEVEL_EVENT_CONSTANT, this.blockPosition(), PotionUtils.getColor(stack));

            if (this.getOwner() instanceof ServerPlayer player) BrassArmoryAdvancements.DRAGON_ROUND.trigger(player);
        }
        this.gameEvent(GameEvent.PROJECTILE_LAND, this.getOwner());
        super.onHit(onHit);
        this.discard();
    }

    protected void applyWater(Vec3 diff) {
        AABB boundingBox = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D).move(diff);
        List<LivingEntity> livingEntities = this.level.getEntitiesOfClass(LivingEntity.class, boundingBox, LivingEntity::isSensitiveToWater);
        if (!livingEntities.isEmpty()) {
            for(LivingEntity livingentity : livingEntities) {
                if (this.distanceToSqr(livingentity) < 16.0D && livingentity.isSensitiveToWater()) {
                    livingentity.hurt(DamageSource.indirectMagic(this, this.getOwner()), 1.0F);
                }
            }
        }

        for(Axolotl axolotl : this.level.getEntitiesOfClass(Axolotl.class, boundingBox)) {
            axolotl.rehydrate();
        }
    }

    protected void applySplash(List<MobEffectInstance> mobEffectInstances, @Nullable Entity victim, Vec3 diff) {
        List<LivingEntity> livingEntities = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D).move(diff));
        if (!livingEntities.isEmpty()) {
            Entity effectSource = this.getEffectSource();
            for(LivingEntity livingentity : livingEntities) {
                if (livingentity.isAffectedByPotions()) {
                    double distanceToSqr = this.distanceToSqr(livingentity);
                    if (distanceToSqr < 16.0D) {
                        double durationMultiplier = 1.0D - Math.sqrt(distanceToSqr) / 4.0D;
                        if (livingentity.equals(victim)) durationMultiplier = 1.0D;

                        for(MobEffectInstance mobeffectinstance : mobEffectInstances) {
                            MobEffect mobeffect = mobeffectinstance.getEffect();
                            if (mobeffect.isInstantenous()) {
                                mobeffect.applyInstantenousEffect(this, this.getOwner(), livingentity, mobeffectinstance.getAmplifier(), durationMultiplier);
                            } else {
                                int duration = (int)(durationMultiplier * (double)mobeffectinstance.getDuration() + 0.5D);
                                if (duration > 20) {
                                    livingentity.addEffect(new MobEffectInstance(mobeffect, duration, mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()), effectSource);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    protected void makeAreaOfEffectCloud(ItemStack itemStack, Potion potion, Vec3 pos) {
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, pos.x, pos.y, pos.z);
        if (this.getOwner() instanceof LivingEntity living) areaeffectcloud.setOwner(living);

        areaeffectcloud.setRadius(3.0F);

        if (CarcassRoundItem.isADragonRound(itemStack)) {
            areaeffectcloud.setParticle(ParticleTypes.DRAGON_BREATH);
            areaeffectcloud.setDuration(600);
            areaeffectcloud.setRadiusPerTick((7.0F - areaeffectcloud.getRadius()) / (float)areaeffectcloud.getDuration());
            areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
        } else {
            areaeffectcloud.setRadiusOnUse(-0.5F);
            areaeffectcloud.setWaitTime(10);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());
            areaeffectcloud.setPotion(potion);

            for(MobEffectInstance mobeffectinstance : PotionUtils.getCustomEffects(itemStack)) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }

            CompoundTag compoundtag = itemStack.getTag();
            if (compoundtag != null && compoundtag.contains("CustomPotionColor", 99)) {
                areaeffectcloud.setFixedColor(compoundtag.getInt("CustomPotionColor"));
            }
        }

        this.level.addFreshEntity(areaeffectcloud);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return BrassArmoryItems.CARCASS_ROUND.get();
    }
}
