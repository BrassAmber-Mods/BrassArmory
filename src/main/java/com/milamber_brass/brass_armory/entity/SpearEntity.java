package com.milamber_brass.brass_armory.entity;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class SpearEntity extends AbstractArrow {

    public int returningTicks;
    public Tiers finalTier;
    private ItemStack thrownStack;
    private boolean dealtDamage;

    public SpearEntity(EntityType<? extends SpearEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public SpearEntity(Level worldIn, LivingEntity thrower, ItemStack thrownStackIn, Tiers tier) {
        super(BrassArmoryEntityTypes.SPEAR.get(), thrower, worldIn);
        this.thrownStack = thrownStackIn.copy();
        finalTier = tier;

    }

    public SpearEntity(Level worldIn, double x, double y, double z) {
        super(BrassArmoryEntityTypes.SPEAR.get(), x, y, z, worldIn);
    }


    public ResourceLocation getTierResourceLocation() {
        String tier2 = finalTier.toString();
        switch (tier2) {
            case "GOLD":
                return new ResourceLocation(BrassArmory.MOD_ID, "textures/item/gold_spear.png");

            case "STONE":
                return new ResourceLocation(BrassArmory.MOD_ID, "textures/item/stone_spear.png");

            case "IRON":
                return new ResourceLocation(BrassArmory.MOD_ID, "textures/item/iron_spear.png");

            case "DIAMOND":
                return new ResourceLocation(BrassArmory.MOD_ID, "textures/item/diamond_spear.png");

            case "NETHERITE":
                return new ResourceLocation(BrassArmory.MOD_ID, "textures/item/netherite_spear.png");

            default:
                return new ResourceLocation(BrassArmory.MOD_ID, "textures/item/wood_spear.png");
        }
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.level.isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                this.spawnAtLocation(this.getPickupItem(), 0.1F);
            }
            this.remove(RemovalReason.DISCARDED);
        }
        super.tick();
    }

    @Nonnull
    protected ItemStack getPickupItem() {
        return this.thrownStack.copy();
    }

    /**
     * Gets the EntityRayTraceResult representing the entity hit
     */
    @Nullable
    @ParametersAreNonnullByDefault
    protected EntityHitResult EntityHitResult(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        float f = 8.0F;
        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) entity;
            f += EnchantmentHelper.getDamageBonus(this.thrownStack, livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = DamageSource.trident(this, entity1 == null ? this : entity1);
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity) entity;
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity1);
                }

                this.doPostHurtEffects(livingentity1);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        this.playSound(soundevent, f1, 1.0F);
    }

    /**
     * The sound made when an entity is hit by this projectile
     */
    @Nonnull
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @ParametersAreNonnullByDefault
    public void playerTouch(Player entityIn) {
        Entity entity = this.getOwner();
        if (entity == null || entity.getUUID() == entityIn.getUUID()) {
            super.playerTouch(entityIn);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @ParametersAreNonnullByDefault
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Spear", 10)) {
            this.thrownStack = ItemStack.of(compound.getCompound("Spear"));
        }

        this.dealtDamage = compound.getBoolean("DealtDamage");
    }

    @ParametersAreNonnullByDefault
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Spear", this.thrownStack.save(new CompoundTag()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void tickDespawn() {
        if (this.pickup != AbstractArrow.Pickup.ALLOWED) {
            super.tickDespawn();
        }

    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

}


