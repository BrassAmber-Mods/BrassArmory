package com.milamber_brass.brass_armory.entity.projectile.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.core.Direction.UP;

@ParametersAreNonnullByDefault
public abstract class AbstractRollableItemProjectileEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Float> DATA_RENDER_ROTATION = SynchedEntityData.defineId(AbstractRollableItemProjectileEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_RENDER_ROTATION0 = SynchedEntityData.defineId(AbstractRollableItemProjectileEntity.class, EntityDataSerializers.FLOAT);
    private float rotDirection;
    protected int hitPerTick;

    public AbstractRollableItemProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
        this.rotDirection = -10F;
    }

    public AbstractRollableItemProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, double x, double y, double z, Level level) {
        super(entityType, x, y, z, level);
        this.rotDirection = -10F;
    }

    public AbstractRollableItemProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, LivingEntity living, Level level, @Nullable HumanoidArm arm) {
        super(entityType, living, level);
        this.rotDirection = arm == HumanoidArm.LEFT ? 10F : -10F;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_RENDER_ROTATION, 0F);
        this.entityData.define(DATA_RENDER_ROTATION0, 0F);
    }

    @Override
    public void tick() {
        this.hitPerTick = 0;
        super.tick();
        if (this.onGround()) this.onGroundTick();
        else if (!this.isNoGravity()) {
            this.setRotation0(this.getRotation());
            this.setRotation(this.getRotation() + this.rotDirection * (float)this.getDeltaMovement().length());
        }
    }

    protected void onGroundTick() {
        Vec3 vec = this.getDeltaMovement();
        boolean flag = this.isFree(vec.x, vec.y, vec.z );
        vec = vec.multiply(0.9D, flag ? 1D : 0D, 0.9D);
        float length = (float)vec.length();
        this.setRotation0(this.getRotation());
        if (length > 0.01D) {
            this.setRotation(this.getRotation() + this.rotDirection * length * (this.isInWaterOrBubble() ? 2F : 8F));
        } else vec.scale(0.0D);
        this.setDeltaMovement(vec);
        this.setOnGround(!flag);
    }

    @Nonnull
    @Override //Pick up if hand is empty
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).isEmpty()) {
            player.setItemInHand(hand, this.getItem());
            this.discard();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        switch (hitResult.getType()) {
            case ENTITY:
                this.onHitEntity((EntityHitResult) hitResult);
                break;
            case BLOCK:
                this.onHitBlock((BlockHitResult) hitResult);
                break;
            default:
                return;
        }
        this.gameEvent(GameEvent.PROJECTILE_LAND, this.getOwner());
        this.rotDirection -= (float) this.getDeltaMovement().length() * 2F;
    }

    protected void onHitEffects() {
        float volume = (float)this.getDeltaMovement().length() * this.getVolumeMultiplier();
        this.playSound(getSoundEvent(), volume, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
    }

    protected abstract SoundEvent getSoundEvent();

    protected abstract float getVolumeMultiplier();

    @Override
    public @NotNull Component getName() {
        return this.getItem().getItem().getName(this.getItem());
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if (this.hitPerTick == 0) onHitEffects();
        if (++this.hitPerTick > 8) {
            this.kill();
            return;
        }
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState blockState = this.level().getBlockState(pos);
        if (blockState.getBlock() instanceof IronBarsBlock && blockState.is(Tags.Blocks.GLASS_PANES) && TierSortingRegistry.isCorrectTierForDrops(this.getItem().getItem() instanceof TieredItem tieredItem ? tieredItem.getTier() : Tiers.IRON, blockState)) {
            this.level().destroyBlock(pos, true, this.getOwner());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.85D));
            return;
        }
        super.onHitBlock(blockHitResult);
        Direction.Axis axis = blockHitResult.getDirection().getAxis();
        Vec3 movement = bounce(this.getDeltaMovement(), axis, this.getBounceMultiplier());
        this.setDeltaMovement(movement);
        HitResult newHitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (newHitResult.getType() == HitResult.Type.BLOCK) {
            this.onHit(newHitResult);
        } else if (movement.y < 0.05D && blockHitResult.getDirection() == UP) {
            this.setOnGround(true);
            this.setDeltaMovement(movement.x, 0, movement.z);
        }
    }

    protected abstract double getBounceMultiplier();

    @Nonnull
    public static Vec3 bounce(Vec3 deltaMovement, Direction.Axis axis, double multiplier) {
        if (multiplier == 1F) return deltaMovement.multiply(axis.equals(Direction.Axis.X) ? -1 : 1, axis.equals(Direction.Axis.Y) ? -1 : 1, axis.equals(Direction.Axis.Z) ? -1 : 1);
        double newX = deltaMovement.x * (axis.equals(Direction.Axis.X) ? -multiplier : 0.75D);
        double newY = deltaMovement.y * (axis.equals(Direction.Axis.Y) ? -multiplier : 0.75D);
        double newZ = deltaMovement.z * (axis.equals(Direction.Axis.Z) ? -multiplier : 0.75D);
        return new Vec3(newX, newY, newZ);
    }

    public void setRotation(float newRotation) {
        this.entityData.set(DATA_RENDER_ROTATION, newRotation);
    }

    public float getRotation() {
        return this.entityData.get(DATA_RENDER_ROTATION);
    }

    public void setRotation0(float newRotation) {
        this.entityData.set(DATA_RENDER_ROTATION0, newRotation);
    }

    public float getRotation0() {
        return this.entityData.get(DATA_RENDER_ROTATION0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putFloat("BARollableRotation", this.getRotation());
        super.addAdditionalSaveData(compoundTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setRotation(compoundTag.getFloat("BARollableRotation"));
        this.setRotation0(this.getRotation());
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
