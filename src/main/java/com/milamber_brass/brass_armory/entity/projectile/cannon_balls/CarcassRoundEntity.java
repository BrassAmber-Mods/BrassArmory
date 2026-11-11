package com.milamber_brass.brass_armory.entity.projectile.cannon_balls;

import org.jetbrains.annotations.Nullable;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractBulletEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryAdvancements;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.CarcassRoundItem;
import com.milamber_brass.brass_armory.util.Impact;
import com.milamber_brass.brass_armory.util.ItemData;
import com.milamber_brass.brass_armory.util.PotionData;
import java.util.List;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

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
        if (!this.level().isClientSide()) return super.getItem();
        else return Util.make(super.getItem(), stack -> PotionData.setPotion(stack, Potions.SLOW_FALLING));
    }

    @Override
    public void tick() {
        super.tick();
        if (CarcassRoundItem.isADragonRound(this.getItem())) {
            this.level().addParticle(net.minecraft.core.particles.PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1F), this.getX(), this.getEyeY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHit(HitResult onHit) {
        Vec3 pos = onHit.getLocation();

        if (this.level() instanceof ServerLevel serverLevel) {
            Impact impact = new Impact(serverLevel, this, pos, 1.0F, this.isOnFire(), Explosion.BlockInteraction.DESTROY);
            impact.explode();
            impact.finalizeExplosion(true);
            ItemStack stack = this.getItem();
            Holder<Potion> potion = PotionData.getPotion(stack);
            List<MobEffectInstance> list = PotionData.getMobEffects(stack);

            Vec3 diff = pos.subtract(this.position());
            if (potion.equals(Potions.WATER) && list.isEmpty()) this.applyWater(diff);
            else if (!list.isEmpty()) {
                this.applySplash(list, onHit instanceof EntityHitResult entityHitResult ? entityHitResult.getEntity() : null, diff);
                this.makeAreaOfEffectCloud(stack, potion, pos);
            }

            if (CarcassRoundItem.isADragonRound(stack)) this.level().levelEvent(2006, this.blockPosition(), -1);
            else this.level().levelEvent(LEVEL_EVENT_CONSTANT, this.blockPosition(), PotionData.getColor(stack));

            if (this.getOwner() instanceof ServerPlayer player) BrassArmoryAdvancements.DRAGON_ROUND.trigger(player);
        }
        this.gameEvent(GameEvent.PROJECTILE_LAND, this.getOwner());
        super.onHit(onHit);
        this.discard();
    }

    protected void applyWater(Vec3 diff) {
        AABB boundingBox = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D).move(diff);
        List<LivingEntity> livingEntities = this.level().getEntitiesOfClass(LivingEntity.class, boundingBox, LivingEntity::isSensitiveToWater);
        if (!livingEntities.isEmpty()) {
            for(LivingEntity livingentity : livingEntities) {
                if (this.distanceToSqr(livingentity) < 16.0D && livingentity.isSensitiveToWater()) {
                    livingentity.hurtOrSimulate(this.damageSources().indirectMagic(this, this.getOwner()), 1.0F);
                }
            }
        }

        for(Axolotl axolotl : this.level().getEntitiesOfClass(Axolotl.class, boundingBox)) {
            axolotl.rehydrate();
        }
    }

    protected void applySplash(List<MobEffectInstance> mobEffectInstances, @Nullable Entity victim, Vec3 diff) {
        List<LivingEntity> livingEntities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D).move(diff));
        if (!livingEntities.isEmpty()) {
            Entity effectSource = this.getEffectSource();
            for(LivingEntity livingentity : livingEntities) {
                if (livingentity.isAffectedByPotions()) {
                    double distanceToSqr = this.distanceToSqr(livingentity);
                    if (distanceToSqr < 16.0D) {
                        double durationMultiplier = 1.0D - Math.sqrt(distanceToSqr) / 4.0D;
                        if (livingentity.equals(victim)) durationMultiplier = 1.0D;

                        for(MobEffectInstance mobeffectinstance : mobEffectInstances) {
                            var mobeffect = mobeffectinstance.getEffect();
                            if (mobeffect.value().isInstantaneous()) {
                                mobeffect.value().applyInstantaneousEffect((net.minecraft.server.level.ServerLevel)this.level(), this, this.getOwner(), livingentity, mobeffectinstance.getAmplifier(), durationMultiplier);
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

    protected void makeAreaOfEffectCloud(ItemStack itemStack, Holder<Potion> potion, Vec3 pos) {
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), pos.x, pos.y, pos.z);
        if (this.getOwner() instanceof LivingEntity living) areaeffectcloud.setOwner(living);

        areaeffectcloud.setRadius(3.0F);

        if (CarcassRoundItem.isADragonRound(itemStack)) {
            areaeffectcloud.setCustomParticle(net.minecraft.core.particles.PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1F));
            areaeffectcloud.setDuration(600);
            areaeffectcloud.setRadiusPerTick((7.0F - areaeffectcloud.getRadius()) / (float)areaeffectcloud.getDuration());
            areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 1));
        } else {
            areaeffectcloud.setRadiusOnUse(-0.5F);
            areaeffectcloud.setWaitTime(10);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());
            areaeffectcloud.setPotionContents(itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));

            for(MobEffectInstance mobeffectinstance : PotionData.getCustomEffects(itemStack)) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }

            CompoundTag compoundtag = ItemData.getTag(itemStack);
            if (compoundtag != null && compoundtag.contains("CustomPotionColor")) {
                areaeffectcloud.setPotionContents(new PotionContents(Optional.of(potion), Optional.of(compoundtag.getIntOr("CustomPotionColor", 0)), PotionData.getCustomEffects(itemStack), Optional.empty()));
            }
        }

        this.level().addFreshEntity(areaeffectcloud);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return BrassArmoryItems.CARCASS_ROUND.get();
    }
}
