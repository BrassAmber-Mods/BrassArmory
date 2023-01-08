package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractRollableItemProjectileEntity;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.FlailItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.core.Direction.UP;
import static net.minecraft.world.phys.HitResult.Type.BLOCK;

@ParametersAreNonnullByDefault
public class FlailHeadEntity extends AbstractThrownWeaponEntity {
    protected static final EntityDataAccessor<Integer> DATA_OWNER = SynchedEntityData.defineId(FlailHeadEntity.class, EntityDataSerializers.INT);
    protected int hitPerTick;
    protected long hitTick;

    public FlailHeadEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public FlailHeadEntity(LivingEntity living, Level level, ItemStack weaponStack) {
        super(BrassArmoryEntityTypes.FLAIL_HEAD.get(), living, level, weaponStack);
        this.hitTick = Long.MAX_VALUE;
        double damage = living.getAttributeValue(Attributes.ATTACK_DAMAGE);
        ItemStack mainHandItem = living.getMainHandItem();
        if (mainHandItem != weaponStack && !mainHandItem.isEmpty()) {
            for (AttributeModifier attributeModifier : mainHandItem.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE)) {
                damage -= attributeModifier.getAmount();
            }
        }
        if (weaponStack.getItem() instanceof FlailItem flailItem) damage += flailItem.getAttackDamage();
        this.entityData.set(DATA_DAMAGE_VALUE, (float)damage);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_OWNER, Integer.MAX_VALUE);
    }

    @Override
    public void tick() {
        if (this.tickCount > 200) {
            this.kill();
            return;
        }
        this.hitPerTick = 0;
        if (this.entityData.get(DATA_OWNER) == Integer.MAX_VALUE && this.getOwner() != null) this.entityData.set(DATA_OWNER, this.getOwner().getId());
        super.tick();
        if (this.isOnGround()) this.onGroundTick();
        if (!this.level.isClientSide && ((this.hitTick != Long.MAX_VALUE && this.level.getGameTime() - this.hitTick > 5L) || (this.getOwner() instanceof LivingEntity living && living.distanceTo(this) > 8D))) {
            this.entityData.set(DATA_LOYALTY_LEVEL, 21);
            this.setNoPhysics(true);
        }
    }

    protected void onGroundTick() {
        Vec3 vec = this.getDeltaMovement();
        boolean flag = this.isFree(vec.x, vec.y, vec.z );
        this.setDeltaMovement(vec.multiply(0.9D, flag ? 1D : 0D, 0.9D));
        this.setOnGround(!flag);
    }


    @Override
    protected void onHit(HitResult hitResult) {
        switch (hitResult.getType()) {
            case ENTITY -> this.onHitEntity((EntityHitResult) hitResult);
            case BLOCK -> this.onHitBlock((BlockHitResult) hitResult);
            default -> {
                return;
            }
        }
        if (this.hitTick == Long.MAX_VALUE) this.hitTick = this.level.getGameTime();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!this.onGround && entityHitResult.getEntity() instanceof LivingEntity living && this.getOwner() != null && this.getOwner().getUUID() != living.getUUID()) {
            super.onHitEntity(entityHitResult);
            living.knockback(this.getDeltaMovement().length() * 0.25D, this.getX() - living.getX(), this.getZ() - living.getZ());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if (this.tickCount > 200) {
            this.discard();
            return;
        }

        if (this.hitPerTick == 0) this.onHitEffects();
        if (++this.hitPerTick > 8) {
            this.discard();
            return;
        }
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState blockState = this.level.getBlockState(pos);
        if (blockState.getBlock() instanceof IronBarsBlock && blockState.getMaterial().equals(Material.GLASS)) {
            this.level.destroyBlock(pos, true, this.getOwner());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.85D));
            return;
        }
        BlockState blockstate = this.level.getBlockState(pos);
        blockstate.onProjectileHit(this.level, blockstate, blockHitResult, this);
        Direction.Axis axis = blockHitResult.getDirection().getAxis();
        Vec3 movement = AbstractRollableItemProjectileEntity.bounce(this.getDeltaMovement(), axis, 0.2D);
        this.setDeltaMovement(movement);
        HitResult newHitResult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if (newHitResult.getType() == BLOCK) {
            this.onHit(newHitResult);
        } else if (movement.y < 0.05D && blockHitResult.getDirection() == UP) {
            this.setOnGround(true);
            this.setDeltaMovement(movement.x, 0, movement.z);
        }
    }

    protected void onHitEffects() {
        float volume = (float) this.getDeltaMovement().length() * 0.5F;
        this.playSound(this.onHitSoundEvent(), volume, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return this.entityData.get(DATA_OWNER) != Integer.MAX_VALUE ? this.level.getEntity(this.entityData.get(DATA_OWNER)) : super.getOwner();
    }

    @NotNull
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull Component getName() {
        return this.getTypeName();
    }

    @Override
    protected boolean tryPickup(Player player) {
        if (this.isNoPhysics() && this.ownedBy(player)) {
            player.getCooldowns().addCooldown(this.getItem().getItem(), 5);
            return true;
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putLong("BAHitTick", this.hitTick);
        super.addAdditionalSaveData(compoundTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.hitTick  = compoundTag.getLong("BAHitTick");
    }

    @Override
    protected String onHitDamageSource() {
        return "flail";
    }

    @Override
    protected SoundEvent onHitSoundEvent() {
        return BrassArmorySounds.FLAIL_HIT.get();
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return BrassArmorySounds.FLAIL_HIT.get();
    }

    @Override
    protected Item getDefaultItem() {
        return BrassArmoryItems.WOODEN_SPIKY_BALL.get();
    }
}
