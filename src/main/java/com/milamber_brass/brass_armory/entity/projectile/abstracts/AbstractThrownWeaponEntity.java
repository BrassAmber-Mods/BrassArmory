package com.milamber_brass.brass_armory.entity.projectile.abstracts;

import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class AbstractThrownWeaponEntity extends AbstractArrow implements ItemSupplier {
    protected static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(AbstractThrownWeaponEntity.class, EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<Integer> DATA_LOYALTY_LEVEL = SynchedEntityData.defineId(AbstractThrownWeaponEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> DATA_DAMAGE_VALUE = SynchedEntityData.defineId(AbstractThrownWeaponEntity.class, EntityDataSerializers.FLOAT);
    protected float power;
    protected boolean dealtDamage;

    public AbstractThrownWeaponEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public AbstractThrownWeaponEntity(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level level, ItemStack weaponStack) {
        super(entityType, x, y, z, level);
        this.setItem(weaponStack);
        double damage = 0;
        for (AttributeModifier attributeModifier : weaponStack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE)) {
            damage += attributeModifier.getAmount();
        }
        this.entityData.set(DATA_DAMAGE_VALUE, (float)damage);
    }

    public AbstractThrownWeaponEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity living, Level level, ItemStack weaponStack) {
        super(entityType, living, level);
        this.setItem(weaponStack);

        double damage = living.getAttributeValue(Attributes.ATTACK_DAMAGE);
        ItemStack mainHandItem = living.getMainHandItem();
        if (mainHandItem != weaponStack) {
            if (!mainHandItem.isEmpty()) {
                for (AttributeModifier attributeModifier : mainHandItem.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE)) {
                    damage -= attributeModifier.getAmount();
                }
            }
            for (AttributeModifier attributeModifier : weaponStack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE)) {
                damage += attributeModifier.getAmount();
            }
        }
        this.entityData.set(DATA_DAMAGE_VALUE, (float)damage);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
        this.entityData.define(DATA_LOYALTY_LEVEL, 0);
        this.entityData.define(DATA_DAMAGE_VALUE, 1F);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) this.dealtDamage = true;
        this.loyaltyTick();
        super.tick();
    }

    protected void loyaltyTick() {
        Entity entity = this.getOwner();
        double loyaltyLevel = (double)this.entityData.get(DATA_LOYALTY_LEVEL);
        if (loyaltyLevel > 0D && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (entity.isAlive() && (!(entity instanceof ServerPlayer) || !entity.isSpectator())) {
                this.setNoPhysics(true);
                this.xo = this.getX();
                this.yo = this.getY();
                this.zo = this.getZ();
                Vec3 deltaDifference = entity.getEyePosition().subtract(this.position());
                this.setDeltaMovement(this.getDeltaMovement().scale(Math.max(0D, 1D - loyaltyLevel / 10D)).add(deltaDifference.normalize().scale(loyaltyLevel / 10D)));
            } else {
                if (!this.level.isClientSide && this.pickup == Pickup.ALLOWED) this.spawnAtLocation(this.getPickupItem(), 0.1F);
                this.discard();
            }
        }
    }

    @Override
    public void lavaHurt() {
        ItemStack stack = this.getItem();
        if (stack.getItem() instanceof TieredItem tieredItem) {
            Tier tier = tieredItem.getTier();
            if (tier != Tiers.NETHERITE && this.getOwner() instanceof LivingEntity livingOwner) {
                stack.hurtAndBreak(tier != Tiers.WOOD ? 1 : 2, livingOwner, (living) -> living.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
            if (stack.getDamageValue() == stack.getMaxDamage()) this.kill();
        }
        super.lavaHurt();
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    protected EntityHitResult findHitEntity(Vec3 pointOne, Vec3 pointTwo) {
        EntityHitResult result = super.findHitEntity(pointOne, pointTwo);
        if (result != null && result.getEntity() instanceof LivingEntity living) {
            return living.hurtTime > 0 ? null : super.findHitEntity(pointOne, pointTwo);
        }
        return super.findHitEntity(pointOne, pointTwo);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity victimEntity = entityHitResult.getEntity();
        Entity ownerEntity = this.getOwner();

        float damage = this.entityData.get(DATA_DAMAGE_VALUE);
        if (victimEntity instanceof LivingEntity livingVictim) damage += EnchantmentHelper.getDamageBonus(this.getItem(), livingVictim.getMobType());
        else damage += EnchantmentHelper.getDamageBonus(this.getItem(), MobType.UNDEFINED);
        if (this.isCritArrow()) damage *= 1.5F;
        if (this.getItem().getItem() instanceof AbstractThrownWeaponItem item) damage *= this.getDeltaMovement().length() / item.getThrowMultiplier();

        if (victimEntity.hurt((new IndirectEntityDamageSource(this.onHitDamageSource(), this, ownerEntity == null ? this : ownerEntity)).setProjectile(), damage)) {
            if (victimEntity.getType() == EntityType.ENDERMAN) return;
            if (this.isOnFire()) victimEntity.setSecondsOnFire(40);

            if (victimEntity instanceof LivingEntity livingVictim) {
                if (ownerEntity instanceof LivingEntity livingOwner) {
                    EnchantmentHelper.doPostHurtEffects(livingVictim, livingOwner);
                    EnchantmentHelper.doPostDamageEffects(livingOwner, livingVictim);
                }
                this.doPostHurtEffects(livingVictim);
            }
        }

        this.dealtDamage = true;

        this.setDeltaMovement(this.onHitDeltaMovement());
        this.playSound(this.onHitSoundEvent(), 1.0F, 1.0F);
    }

    protected abstract String onHitDamageSource();

    protected Vec3 onHitDeltaMovement() {
        return this.getDeltaMovement().multiply(0.95D, 0.95D, 0.95D);
    }

    protected abstract SoundEvent onHitSoundEvent();

    @Override
    @ParametersAreNonnullByDefault
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    @Nonnull
    @Override
    protected abstract SoundEvent getDefaultHitGroundSoundEvent();

    @Override
    @ParametersAreNonnullByDefault
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) super.playerTouch(player);
    }

    protected abstract Item getDefaultItem();

    protected ItemStack getItemRaw() {
        return this.entityData.get(DATA_ITEM_STACK);
    }

    @Nonnull
    @Override
    protected ItemStack getPickupItem() {
        return this.getItem();
    }

    @ParametersAreNonnullByDefault
    public void setItem(ItemStack weaponStack) {
        weaponStack.setEntityRepresentation(this);
        this.entityData.set(DATA_LOYALTY_LEVEL, EnchantmentHelper.getLoyalty(weaponStack));
        if (!weaponStack.is(this.getDefaultItem()) || weaponStack.hasTag()) {
            this.entityData.set(DATA_ITEM_STACK, Util.make(weaponStack.copy(), (itemStack) -> itemStack.setCount(1)));
        }
    }

    @Override
    @Nonnull
    public ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        itemstack.setEntityRepresentation(this);
        return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getPower() {
        return this.power;
    }

    public boolean isInGround() {
        return this.inGround;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        ItemStack weaponStack = this.getItemRaw();
        if (!weaponStack.isEmpty()) compoundTag.put("WeaponStack", weaponStack.save(new CompoundTag()));
        compoundTag.putBoolean("DealtDamage", this.dealtDamage);
        compoundTag.putFloat("ThrownWeaponDamage", this.entityData.get(DATA_DAMAGE_VALUE));
        compoundTag.putInt("LoyaltyLevel", this.entityData.get(DATA_LOYALTY_LEVEL));
        compoundTag.putFloat("ThrownWeaponPower", this.getPower());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setItem(ItemStack.of(compoundTag.getCompound("WeaponStack")));
        this.dealtDamage = compoundTag.getBoolean("DealtDamage");
        this.entityData.set(DATA_DAMAGE_VALUE, compoundTag.getFloat("ThrownWeaponDamage"));
        this.entityData.set(DATA_LOYALTY_LEVEL, compoundTag.getInt("LoyaltyLevel"));
        this.setPower(compoundTag.getFloat("ThrownWeaponPower"));
    }

    @Override
    @NotNull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
