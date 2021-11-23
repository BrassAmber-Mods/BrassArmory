package com.milamberBrass.brass_armory.entities.custom;

import com.milamberBrass.brass_armory.BrassArmory;
import com.milamberBrass.brass_armory.entities.ModEntityTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class Spear_Entity extends AbstractArrowEntity {

    public int returningTicks;
    public ItemTier finalTier;
    private ItemStack thrownStack;
    private boolean dealtDamage;

    public Spear_Entity(EntityType<? extends Spear_Entity> type, World worldIn) {
        super(type, worldIn);
    }

    public Spear_Entity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn, ItemTier tier) {
        super(ModEntityTypes.SPEAR.get(), thrower, worldIn);
        this.thrownStack = thrownStackIn.copy();
        finalTier = tier;

    }

    public Spear_Entity(World worldIn, double x, double y, double z) {
        super(ModEntityTypes.SPEAR.get(), x, y, z, worldIn);
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
            if (!this.level.isClientSide && this.pickup == AbstractArrowEntity.PickupStatus.ALLOWED) {
                this.spawnAtLocation(this.getPickupItem(), 0.1F);
            }
            this.remove();
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
    protected EntityRayTraceResult findHitEntity(Vector3d startVec, Vector3d endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityRayTraceResult result) {
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
    public void playerTouch(PlayerEntity entityIn) {
        Entity entity = this.getOwner();
        if (entity == null || entity.getUUID() == entityIn.getUUID()) {
            super.playerTouch(entityIn);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @ParametersAreNonnullByDefault
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Spear", 10)) {
            this.thrownStack = ItemStack.of(compound.getCompound("Spear"));
        }

        this.dealtDamage = compound.getBoolean("DealtDamage");
    }

    @ParametersAreNonnullByDefault
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Spear", this.thrownStack.save(new CompoundNBT()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void tickDespawn() {
        if (this.pickup != AbstractArrowEntity.PickupStatus.ALLOWED) {
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


