package com.milamber_brass.brass_armory.entity.projectile.bomb;

import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.core.Direction.Axis;
import static net.minecraft.core.Direction.UP;
import static net.minecraft.world.phys.HitResult.Type.MISS;

public class BombEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Integer> DATA_FUSE = SynchedEntityData.defineId(BombEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_RENDER_ROTATION = SynchedEntityData.defineId(BombEntity.class, EntityDataSerializers.FLOAT);
    private float rotDirection;
    private int hitPerTick;
    private boolean defused;

    public BombEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
        this.setFuse(60);
        this.setOnGround(false);
        this.rotDirection = -10F;
        this.defused = false;
    }

    public BombEntity(Level level, LivingEntity livingEntity, @Nullable HumanoidArm arm) {
        super(BrassArmoryEntityTypes.BOMB.get(), livingEntity, level);
        this.setFuse(60);
        this.setOnGround(false);
        this.rotDirection = arm == HumanoidArm.LEFT ? 10F : -10F;
        this.defused = false;
    }

    public BombEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.BOMB.get(), x, y, z, level);
        this.setFuse(60);
        this.setOnGround(false);
        this.rotDirection = -10F;
        this.defused = false;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FUSE, 80);
        this.entityData.define(DATA_RENDER_ROTATION, 0F);
    }

    protected BombType getBombType() {
        return BombType.NORMAL;
    }

    @Override
    @NotNull
    protected Item getDefaultItem() {
        return BombType.getBombItem(this.getBombType());
    }

    @Override
    public void tick() {
        if (!this.isNoGravity()) this.setRotation(this.getRotation() + this.rotDirection * (float)this.getDeltaMovement().length());
        this.hitPerTick = 0;
        super.tick();
        int newFuse = this.getFuse() - (this.isOnFire() ? 2 : 1);
        this.setFuse(newFuse);
        if (newFuse <= 0) {
            this.discard();
            if (!this.defused && !this.level.isClientSide) explode(this.level, this);
        }
        if (this.isOnGround()) this.onGroundTick();
        if (!this.defused) {
            if (this.level.isClientSide && level.getRandom().nextInt(2) == 1) {
                Vec3 smokeVec = this.position().add(this.getDeltaMovement().multiply(-1.5D, -1.5D, -1.5D)).add(0, 0.125D, 0);
                this.level.addParticle(ParticleTypes.SMOKE, smokeVec.x, smokeVec.y, smokeVec.z, 0.0D, 0.0D, 0.0D);
            }
            this.playSound(BrassArmorySounds.BOMB_FUSE.get(), 0.03F, level.getRandom().nextFloat() * 0.6F + 1F);
            if (this.isInWaterOrBubble()) {
                this.setFuse(40);
                this.defused = true;
            }
        }
    }

    protected void onGroundTick() {  //Code for default bomb's rolling
        this.setRotation(this.getRotation() + this.rotDirection * (float)this.getDeltaMovement().length() * (this.isInWaterOrBubble() ? 4F : 16F));
        Vec3 vec = this.getDeltaMovement();
        boolean flag = isFree(vec.x, vec.y, vec.z);
        this.setDeltaMovement(vec.multiply(0.96D, flag ? 1D : 0D, 0.96D));
        this.setOnGround(!flag);
    }

    @Override
    public void lavaHurt() {
        this.explode(this.level);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void thunderHit(ServerLevel serverLevel, LightningBolt lightningBolt) {
        this.explode(this.level);
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull //Pick up bomb if hand is empty
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).isEmpty() && !this.defused) {
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
    @ParametersAreNonnullByDefault //Called when bomb hits a block or entity, hitResult is never "miss"
    protected void onHit(HitResult hitResult) {
        if (hitResult instanceof BlockHitResult blockHitResult) {
            BlockPos pos = blockHitResult.getBlockPos();
            BlockState blockState = this.level.getBlockState(pos);
            if (blockState.getBlock() instanceof IronBarsBlock && blockState.getMaterial().equals(Material.GLASS)) {
                this.level.destroyBlock(pos, true, this.getOwner());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.85D));
                return;
            }
        }
        if (this.hitPerTick == 0) onHitEffects();
        if (++this.hitPerTick > 8) {
            this.explode(this.level);
            return;
        }
        this.bombOnHit(hitResult);
        this.rotDirection -= (float)this.getDeltaMovement().length() * 2F;
    }

    protected void bombOnHit(HitResult hitResult) { //Bounce logic for default and bouncy bombs
        if (hitResult instanceof BlockHitResult blockHitResult) {
            double bounceMultiplier = this.getBombType().getBounceMultiplier();
            Axis axis = blockHitResult.getDirection().getAxis();
            Vec3 movement = bounce(this.getDeltaMovement(), axis, bounceMultiplier);
            this.setDeltaMovement(movement);
            HitResult newHitResult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (newHitResult.getType() != MISS) {
                this.onHit(newHitResult);
            } else if (movement.y < 0.05D && blockHitResult.getDirection() == UP) {
                this.setOnGround(true);
                this.setDeltaMovement(movement.x, 0, movement.z);
            }
        }
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static Vec3 bounce(Vec3 deltaMovement, Axis axis, double multiplier) {
        double newX = deltaMovement.x * (axis.equals(Axis.X) ? -1D : 1D) * multiplier;
        double newY = deltaMovement.y * (axis.equals(Axis.Y) ? -1D : 1D) * multiplier;
        double newZ = deltaMovement.z * (axis.equals(Axis.Z) ? -1D : 1D) * multiplier;
        return new Vec3(newX, newY, newZ);
    }

    protected void onHitEffects() {
        float volume = (float)this.getDeltaMovement().length() / this.getBombType().getVolumeMultiplier();
        this.playSound(getSoundEvent(), volume, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
    }

    protected SoundEvent getSoundEvent() {
        return BrassArmorySounds.BOMB_HIT.get();
    }

    @Override
    public double getMyRidingOffset() {
        return 0.25D;
    }

    @ParametersAreNonnullByDefault
    private void explode(Level level) {
        this.discard();
        if (!level.isClientSide) explode(level, this);
    }

    public static void explode(Level level, Entity victim) {
        Entity bomb = victim instanceof BombEntity ? victim : null;
        if (bomb != null && bomb.getVehicle() != null) victim = bomb.getVehicle();
        level.explode(bomb, victim.getX(), victim.getY() + (double)(victim.getEyeHeight() * 0.5F), victim.getZ(), 2.0F, Explosion.BlockInteraction.BREAK);
    }

    @ParametersAreNonnullByDefault
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putShort("Fuse", (short)this.getFuse());
    }

    @ParametersAreNonnullByDefault
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        this.setFuse(compoundTag.getShort("Fuse"));
    }

    public void setFuse(int newFuse) {
        this.entityData.set(DATA_FUSE, newFuse);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE);
    }

    public void setRotation(float newRotation) {
        this.entityData.set(DATA_RENDER_ROTATION, newRotation);
    }

    public float getRotation() {
        return this.entityData.get(DATA_RENDER_ROTATION);
    }

    public boolean getDefused() {
        return this.defused;
    }

    @Override
    @NotNull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}