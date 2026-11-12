package com.milamber_brass.brass_armory.entity.projectile.abstracts;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.milamber_brass.brass_armory.item.abstracts.ArmoryTieredItem;
import org.jetbrains.annotations.Nullable;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.util.ItemData;
import net.minecraft.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

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
        super(entityType, x, y, z, level, weaponStack, null);
        this.setItem(weaponStack);
        double damage = 0;
        for (AttributeModifier attributeModifier : weaponStack.getAttributeModifiers().modifiers().stream().filter(entry -> entry.attribute().equals(Attributes.ATTACK_DAMAGE) && entry.slot().test(EquipmentSlot.MAINHAND)).map(ItemAttributeModifiers.Entry::modifier).toList()) {
            damage += attributeModifier.amount();
        }
        this.entityData.set(DATA_DAMAGE_VALUE, (float)damage);
    }

    public AbstractThrownWeaponEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity living, Level level, ItemStack weaponStack) {
        super(entityType, living, level, weaponStack, null);
        this.setItem(weaponStack);

        double damage = living.getAttributeValue(Attributes.ATTACK_DAMAGE);
        ItemStack mainHandItem = living.getMainHandItem();
        if (mainHandItem != weaponStack) {
            if (!mainHandItem.isEmpty()) {
                for (AttributeModifier attributeModifier : mainHandItem.getAttributeModifiers().modifiers().stream().filter(entry -> entry.attribute().equals(Attributes.ATTACK_DAMAGE) && entry.slot().test(EquipmentSlot.MAINHAND)).map(ItemAttributeModifiers.Entry::modifier).toList()) {
                    damage -= attributeModifier.amount();
                }
            }
            for (AttributeModifier attributeModifier : weaponStack.getAttributeModifiers().modifiers().stream().filter(entry -> entry.attribute().equals(Attributes.ATTACK_DAMAGE) && entry.slot().test(EquipmentSlot.MAINHAND)).map(ItemAttributeModifiers.Entry::modifier).toList()) {
                damage += attributeModifier.amount();
            }
        }
        this.entityData.set(DATA_DAMAGE_VALUE, (float)damage);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ITEM_STACK, ItemStack.EMPTY);
        builder.define(DATA_LOYALTY_LEVEL, 0);
        builder.define(DATA_DAMAGE_VALUE, 1F);
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
                if (!this.level().isClientSide() && this.pickup == Pickup.ALLOWED) this.spawnAtLocation((net.minecraft.server.level.ServerLevel)this.level(), this.getPickupItem(), 0.1F);
                this.discard();
            }
        }
    }

    @Override
    public void lavaHurt() {
        ItemStack stack = this.getItem();
        if (stack.getItem() instanceof ArmoryTieredItem tieredItem) {
            ToolMaterial tier = tieredItem.getTier();
            if (tier != ToolMaterial.NETHERITE && this.getOwner() instanceof LivingEntity livingOwner) {
                stack.hurtAndBreak(tier != ToolMaterial.WOOD ? 1 : 2, livingOwner, EquipmentSlot.MAINHAND);
            }
            if (stack.getDamageValue() == stack.getMaxDamage()) if (this.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) this.kill(serverLevel);
        }
        super.lavaHurt();
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 pointOne, Vec3 pointTwo) {
        EntityHitResult result = super.findHitEntity(pointOne, pointTwo);
        if (result != null && result.getEntity() instanceof LivingEntity living) {
            return living.hurtTime > 0 ? null : super.findHitEntity(pointOne, pointTwo);
        }
        return super.findHitEntity(pointOne, pointTwo);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (this.isNoPhysics()) return;
        Entity victimEntity = entityHitResult.getEntity();
        Entity ownerEntity = this.getOwner();

        float damage = this.entityData.get(DATA_DAMAGE_VALUE);
        if (this.level() instanceof ServerLevel serverLevel) damage = EnchantmentHelper.modifyDamage(serverLevel, this.getItem(), victimEntity, ArmoryUtil.getIndirectEntityDamageSource(this.level(), this.onHitDamageType(), this, this.getOwner()), damage);
        if (this.isCritArrow()) damage *= 1.5F;
        if (this.getItem().getItem() instanceof AbstractThrownWeaponItem item) damage *= this.getDeltaMovement().length() / item.getThrowMultiplier();

        if (victimEntity.hurtOrSimulate(ArmoryUtil.getIndirectEntityDamageSource(this.level(), this.onHitDamageType(), this, this.getOwner()), damage)) {
            if (victimEntity.getType() == net.minecraft.world.entity.EntityTypes.ENDERMAN) return;
            if (this.isOnFire()) victimEntity.igniteForSeconds(40);

            if (victimEntity instanceof LivingEntity livingVictim) {
                if (ownerEntity instanceof LivingEntity livingOwner) {
                    if (this.level() instanceof ServerLevel serverLevel) EnchantmentHelper.doPostAttackEffects(serverLevel, livingVictim, ArmoryUtil.getIndirectEntityDamageSource(this.level(), this.onHitDamageType(), this, livingOwner));
                }
                this.doPostHurtEffects(livingVictim);
            }
        }

        this.dealtDamage = true;

        this.setDeltaMovement(this.onHitDeltaMovement());
        this.playSound(this.onHitSoundEvent(), 1.0F, 1.0F);
    }

    protected abstract ResourceKey<DamageType> onHitDamageType();

    protected Vec3 onHitDeltaMovement() {
        return this.getDeltaMovement().multiply(0.95D, 0.95D, 0.95D);
    }

    protected abstract SoundEvent onHitSoundEvent();

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

        @Override
    protected abstract SoundEvent getDefaultHitGroundSoundEvent();

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) super.playerTouch(player);
    }

    protected abstract Item getDefaultItem();

    protected ItemStack getItemRaw() {
        return this.entityData.get(DATA_ITEM_STACK);
    }

        @Override
    protected ItemStack getPickupItem() {
        return this.getItem();
    }

    public void setItem(ItemStack weaponStack) {
        this.entityData.set(DATA_LOYALTY_LEVEL, weaponStack.getEnchantmentLevel(this.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOYALTY)));
        // Components such as damage and enchantments are not stored in CUSTOM_DATA.
        this.entityData.set(DATA_ITEM_STACK, weaponStack.copyWithCount(1));
    }

    @Override
        public ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        
        return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
    }

    @Override
    public @NotNull Component getName() {
        return this.getPickupItem().getItem().getName(this.getPickupItem());
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getPower() {
        return this.power;
    }

    @Override
    public boolean isInGround() {
        return super.isInGround();
    }

    @Override
    public void addAdditionalSaveData(ValueOutput compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        ItemStack weaponStack = this.getItemRaw();
        if (!weaponStack.isEmpty()) compoundTag.store("WeaponStack", ItemStack.CODEC, weaponStack);
        compoundTag.putBoolean("DealtDamage", this.dealtDamage);
        compoundTag.putFloat("ThrownWeaponDamage", this.entityData.get(DATA_DAMAGE_VALUE));
        compoundTag.putInt("LoyaltyLevel", this.entityData.get(DATA_LOYALTY_LEVEL));
        compoundTag.putFloat("ThrownWeaponPower", this.getPower());
    }

    @Override
    public void readAdditionalSaveData(ValueInput compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setItem(compoundTag.read("WeaponStack", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        this.dealtDamage = compoundTag.getBooleanOr("DealtDamage", false);
        this.entityData.set(DATA_DAMAGE_VALUE, compoundTag.getFloatOr("ThrownWeaponDamage", 0F));
        this.entityData.set(DATA_LOYALTY_LEVEL, compoundTag.getIntOr("LoyaltyLevel", 0));
        this.setPower(compoundTag.getFloatOr("ThrownWeaponPower", 0F));
    }


    @Override
    protected ItemStack getDefaultPickupItem() { return this.getPickupItem(); }
}
