package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.entity.bomb.BombEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.BoomerangItem;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

public class BoomerangEntity extends AbstractArrow implements ItemSupplier {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Float> DATA_RENDER_ROTATION = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Optional<UUID>> TARGET_UUID = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private final float power;
    private boolean dealtDamage;

    public BoomerangEntity(EntityType<BoomerangEntity> boomerangEntity, Level level) {
        super(boomerangEntity, level);
        this.power = 1F;
    }

    public BoomerangEntity(Level level, LivingEntity livingEntity, ItemStack boomerangStack, float power, @Nullable Entity target, boolean gravity) {
        super(BrassArmoryEntityTypes.BOOMERANG.get(), livingEntity, level);
        this.entityData.set(ID_LOYALTY, (byte)EnchantmentHelper.getLoyalty(boomerangStack));
        this.entityData.set(TARGET_UUID, target != null ? Optional.of(target.getUUID()) : Optional.of(livingEntity.getUUID()));
        this.setItem(boomerangStack);
        float xRot = livingEntity.getXRot();
        float yRot = livingEntity.getYRot();
        this.power = power;
        this.shootFromRotation(livingEntity, xRot, yRot, 0.0F, power, 1.0F);
        this.setXRot(xRot);
        this.setYRot(yRot);
        this.setNoGravity(gravity);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte)0);
        this.entityData.define(DATA_RENDER_ROTATION, 0F);
        this.entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
        this.entityData.define(TARGET_UUID, Optional.empty());
    }

    @Override
    public void tick() {
        Vec3 deltaMovement = this.getDeltaMovement();
        Optional<UUID> targetUUID = this.entityData.get(TARGET_UUID);
        if (!this.inGround) {
            if (!this.level.isClientSide) {
                Entity owner = this.getOwner();
                if (targetUUID.isPresent() && ((ServerLevel)this.level).getEntity(targetUUID.get()) instanceof LivingEntity livingTarget && this.isNoGravity()) {
                    boolean targetingOwner = livingTarget == owner;
                    //boolean neverHadTarget = !this.dealtDamage && targetingOwner;

                    Vec3 thisPos = this.position();
                    Vec3 futurePos = thisPos.add(deltaMovement);
                    Vec3 targetPos = livingTarget.position().add(0D, (double) livingTarget.getEyeHeight() * 0.75D, 0D);

                    double thisToTarget = thisPos.distanceTo(targetPos);
                    double futureToTarget = futurePos.distanceTo(targetPos);
                    double maxLength = deltaMovement.length();
                    double length = thisToTarget - futureToTarget;

                    double adjustRange = 12D;
                    double min = /*neverHadTarget ? 0.01D :*/ 0.04D;

                    double enchScale = targetingOwner ? ((double)this.entityData.get(ID_LOYALTY) * 0.15D) + 1D : 1D;
                    min *= enchScale;
                    double scale = length / maxLength >= 0.5D && futureToTarget <= adjustRange ? Math.max((adjustRange - futureToTarget) / adjustRange, min) : min;
                    scale *= enchScale * 0.5D;
                    deltaMovement = deltaMovement.scale(1D - scale).add(targetPos.subtract(thisPos).normalize().scale(scale));
                }
                ItemStack boomerangStack = this.getItem();
                Tier tier = ((BoomerangItem)boomerangStack.getItem()).getTier();
                if (this.isInLava() && tier != Tiers.NETHERITE && owner instanceof LivingEntity livingOwner) {
                    boomerangStack.hurtAndBreak(tier != Tiers.WOOD ? 1 : 2, livingOwner, (living) -> living.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
            }
            this.entityData.set(DATA_RENDER_ROTATION, this.entityData.get(DATA_RENDER_ROTATION) + (float)deltaMovement.length() * 24F + 8F);
        }

        double radToDeg = 180D / Math.PI;
        float xRot = Mth.wrapDegrees((float)(-(Mth.atan2(deltaMovement.y, Math.hypot(deltaMovement.x, deltaMovement.z)) * radToDeg)));
        this.xRotO = xRot;
        float yRot = Mth.wrapDegrees((float)(Mth.atan2(deltaMovement.z, deltaMovement.x) * radToDeg) - 90.0F);
        this.yRotO = yRot;

        this.setDeltaMovement(deltaMovement);
        super.tick();
        this.setXRot(xRot);
        this.setYRot(yRot);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity hitResultEntity = entityHitResult.getEntity();
        LivingEntity owner = (LivingEntity)this.getOwner();
        ItemStack boomerangStack = this.getItem();

        if (hitResultEntity == owner && !this.level.isClientSide()) {
            boolean flag = true;
            if (owner.getMainHandItem().isEmpty()) owner.setItemInHand(InteractionHand.MAIN_HAND, boomerangStack);
            else if (owner.getOffhandItem().isEmpty()) owner.setItemInHand(InteractionHand.OFF_HAND, boomerangStack);
            else flag = owner instanceof Player player && this.tryPickup(player);
            if (flag) {
                owner.take(this, 1);
                this.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
                this.discard();
            }
            return;
        }

        this.playSound(BrassArmorySounds.BOMB_HIT.get(), 1.0F, 1.0F);

        float damage = ((BoomerangItem)boomerangStack.getItem()).getAttackDamage();
        if (hitResultEntity instanceof LivingEntity livingentity) {
            damage += EnchantmentHelper.getDamageBonus(boomerangStack, livingentity.getMobType());
        }

        DamageSource damagesource = DamageSource.trident(this, (owner == null ? this : owner));

        if (hitResultEntity.hurt(damagesource, damage)) {
            if (hitResultEntity.getType() == EntityType.ENDERMAN) return;

            if (hitResultEntity instanceof LivingEntity livingEntity) {
                if (owner != null) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, owner);
                    EnchantmentHelper.doPostDamageEffects(owner, livingEntity);
                }

                if (this.entityData.get(TARGET_UUID).orElse(null) == livingEntity.getUUID()) {
                    this.returnToSender();
                }

                if ((double)this.entityData.get(ID_LOYALTY) > 0D) this.setNoPhysics(true);

                this.dealtDamage = true;
                this.doPostHurtEffects(livingEntity);
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHitBlock(BlockHitResult blockHitResult) {
        double loyalty = (double)this.entityData.get(ID_LOYALTY);
        if (loyalty > 0D) {
            this.setNoPhysics(true);
            this.returnToSender();
            this.setDeltaMovement(BombEntity.bounce(this.getDeltaMovement(), blockHitResult.getDirection().getAxis(), loyalty / 10D));
            return;
        }
        super.onHitBlock(blockHitResult);
    }

    private void returnToSender() {
        Entity owner = this.getOwner();
        if (owner != null) this.entityData.set(TARGET_UUID, Optional.of(owner.getUUID()));
        else this.entityData.set(TARGET_UUID, Optional.empty());
    }

    @Override
    @Nonnull
    protected ItemStack getPickupItem() {
        return this.getItem();
    }

    protected Item getDefaultItem() {
        return BrassArmoryItems.STONE_BOOMERANG.get();
    }

    protected ItemStack getItemRaw() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    @ParametersAreNonnullByDefault
    public void setItem(ItemStack boomerangStack) {
        if (!boomerangStack.is(this.getDefaultItem()) || boomerangStack.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, Util.make(boomerangStack.copy(), (itemStack) -> itemStack.setCount(1)));
        }
    }

    @Override
    @Nonnull
    public ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.dealtDamage && player.getItemInHand(hand).isEmpty()) {
            ItemStack boomerangStack = this.getItem();
            float critChance = BoomerangItem.getCrit(boomerangStack) + (20F * this.power);
            if (critChance > 100F) critChance = 100F;
            BoomerangItem.setCrit(boomerangStack, critChance);
            player.setItemInHand(hand, boomerangStack);
            player.startUsingItem(hand);
            this.discard();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public float getRotation() {
        return this.entityData.get(DATA_RENDER_ROTATION);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        ItemStack itemstack = this.getItemRaw();
        if (!itemstack.isEmpty()) compoundTag.put("Item", itemstack.save(new CompoundTag()));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        ItemStack itemstack = ItemStack.of(compoundTag.getCompound("Item"));
        this.setItem(itemstack);
    }

    @Override
    @NotNull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}