package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.BoomerangItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class BoomerangEntity extends AbstractThrownWeaponEntity {
    protected static final EntityDataAccessor<Float> DATA_PLAYER_X = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> DATA_PLAYER_Y = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> DATA_PLAYER_Z = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.FLOAT);

    private Vec3 deltaFirstMovement;
    public int spin = 0;

    public BoomerangEntity(EntityType<BoomerangEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BoomerangEntity(Level level, LivingEntity livingEntity, ItemStack boomerangStack) {
        super(BrassArmoryEntityTypes.BOOMERANG.get(), livingEntity, level, boomerangStack);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, boomerangStack) > 0) this.setSecondsOnFire(100);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PLAYER_X, 0F);
        this.entityData.define(DATA_PLAYER_Y, 0F);
        this.entityData.define(DATA_PLAYER_Z, 0F);
    }

    @Override
    public void setPower(float power) {
        this.setNoGravity(power > 0.25F);
        super.setPower(power);
    }

    @Override
    protected void loyaltyTick() {
        BlockPos pos = this.blockPosition();
        if (this.level.getBlockState(pos).getBlock() == Blocks.COBWEB) {
            this.level.destroyBlock(pos, true, this.getOwner());
        }

        if (!this.isNoGravity()) return;
        double loyaltyLevel = (double)this.entityData.get(DATA_LOYALTY_LEVEL);
        if (!this.inGround || loyaltyLevel > 0D) {
            if (this.deltaFirstMovement == null) this.deltaFirstMovement = this.getDeltaMovement();
            Vec3 newMovement = this.deltaFirstMovement.scale(Math.max(1D - ((double)this.tickCount / 30D), -1D));

            if (this.inGroundTime > 4) this.setNoPhysics(true);
            if (loyaltyLevel > 0D && this.tickCount >= (this.level.isClientSide ? 31 : 30)) {
                Entity entity = this.getOwner();
                if (entity != null && entity.isAlive() && (!(entity instanceof ServerPlayer) || !entity.isSpectator())) {
                    Vec3 deltaDifference = entity.getEyePosition().subtract(this.position());
                    this.entityData.set(DATA_PLAYER_X, (float)deltaDifference.x);
                    this.entityData.set(DATA_PLAYER_Y, (float)deltaDifference.y);
                    this.entityData.set(DATA_PLAYER_Z, (float)deltaDifference.z);
                }

                Vec3 deltaDifference = new Vec3(this.entityData.get(DATA_PLAYER_X), this.entityData.get(DATA_PLAYER_Y), this.entityData.get(DATA_PLAYER_Z));
                double dif = newMovement.length() / deltaDifference.length();
                newMovement = deltaDifference.scale(dif * (loyaltyLevel * 2));
            } else if (this.tickCount >= 70) this.deltaFirstMovement = this.deltaFirstMovement.add(0D, 0.025D, 0D);
            this.setDeltaMovement(newMovement);
            this.spin++;
        } else this.setNoGravity(false);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity hitResultEntity = entityHitResult.getEntity();
        LivingEntity owner = (LivingEntity)this.getOwner();

        if (!this.level.isClientSide() && hitResultEntity == owner ) {
            ItemStack boomerangStack = this.getItem();
            float crit;
            if (!this.isNoGravity() || !this.dealtDamage || this.isNoPhysics()) crit = 0;
            else crit = BoomerangItem.getCrit(boomerangStack) + 20F;

            BoomerangItem.setCrit(boomerangStack, crit, this.level.getGameTime());
            this.setItem(boomerangStack);

            boolean flag = true;
            if (owner.getMainHandItem().isEmpty() && this.pickup == Pickup.ALLOWED) owner.setItemInHand(InteractionHand.MAIN_HAND, boomerangStack);
            else if (owner.getOffhandItem().isEmpty() && this.pickup == Pickup.ALLOWED) owner.setItemInHand(InteractionHand.OFF_HAND, boomerangStack);
            else flag = owner instanceof Player player && this.tryPickup(player);
            if (flag) {
                owner.take(this, 1);
                this.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
                this.discard();
            }
            return;
        }
        super.onHitEntity(entityHitResult);
    }

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            boolean flag = this.tickCount > 40 && (int)this.entityData.get(DATA_LOYALTY_LEVEL) > 0;
            if (!this.level.isClientSide && ((this.inGround && this.shakeTime <= 0) || flag)) {
                if (flag) {
                    List<BoomerangEntity> entitiesInRange = level.getEntitiesOfClass(BoomerangEntity.class, player.getBoundingBox().inflate(0.2D), Entity -> true);
                    if (!entitiesInRange.contains(this)) return;
                    this.onHitEntity(new EntityHitResult(player));
                } else if (this.tryPickup(player)) {
                    player.take(this, 1);
                    this.discard();
                }
            }
        }
    }

    @Override
    protected String onHitDamageSource() {
        return "boomerang";
    }

    @Override
    protected SoundEvent onHitSoundEvent() {
        return BrassArmorySounds.BOOMERANG_HIT.get();
    }

    @NotNull
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return BrassArmorySounds.BOOMERANG_HIT_GROUND.get();
    }

    @Override
    protected Item getDefaultItem() {
        return BrassArmoryItems.WOODEN_BOOMERANG.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putDouble("BAX", this.deltaFirstMovement.x);
        compoundTag.putDouble("BAY", this.deltaFirstMovement.y);
        compoundTag.putDouble("BAZ", this.deltaFirstMovement.z);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        double x = compoundTag.getDouble("BAX");
        double y = compoundTag.getDouble("BAY");
        double z = compoundTag.getDouble("BAZ");
        this.deltaFirstMovement = new Vec3(x, y, z);
    }
}