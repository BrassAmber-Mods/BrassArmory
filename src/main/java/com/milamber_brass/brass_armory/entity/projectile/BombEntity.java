package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.BombItem;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
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
    private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(BombEntity.class, EntityDataSerializers.INT);
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
        this.entityData.define(DATA_TYPE_ID, 0);
        this.entityData.define(DATA_RENDER_ROTATION, 0F);
    }

    @Override
    @NotNull
    protected Item getDefaultItem() {
        return BrassArmoryItems.BOMB.get();
    }

    @Override
    @NotNull
    public ItemStack getItem() {
        //Doesn't use super getter due to it failing if the entity is unloaded
        //Is needed for the renderer and bomb entity stack getter
        ItemStack bombStack = BombItem.getBomb(this.getTypeId()).getDefaultInstance();
        bombStack.setCount(1);
        BombItem.setFuseLength(bombStack, this.getFuse());
        BombItem.setFuseLit(bombStack, true);
        bombStack.setEntityRepresentation(this);
        return bombStack;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setItem(ItemStack bombStack) {
        this.setTypeId(((BombItem)bombStack.getItem()).getBombType());
    }

    @Override
    public void tick() {
        if (!this.isNoGravity()) this.setRotation(this.getRotation() + this.rotDirection * (float)this.getDeltaMovement().length());
        hitPerTick = 0;
        super.tick();
        int newFuse = this.getFuse() - (this.isOnFire() ? 2 : 1);
        this.setFuse(newFuse);
        if (newFuse <= 0) {
            this.discard();
            if (!this.defused && !this.level.isClientSide) explode(this.level, this);
        }
        if (this.getTypeId() == 0 && this.isOnGround()) {
            this.setRotation(this.getRotation() + this.rotDirection * (float)this.getDeltaMovement().length() * (this.isInWaterOrBubble() ? 4F : 16F));
            Vec3 vec = this.getDeltaMovement();
            boolean flag = isFree(vec.x, vec.y, vec.z);
            this.setDeltaMovement(vec.multiply(0.96D, flag ? 1D : 0D, 0.96D));
            this.setOnGround(!flag);
        } else if (this.getTypeId() == 2 && this.isNoGravity()) isStuck(null);
        if (this.level.isClientSide && level.getRandom().nextInt(2) == 1 && !this.defused) {
            Vec3 smokeVec = this.position().add(this.getDeltaMovement().multiply(-1.5D, -1.5D, -1.5D)).add(0, 0.125D, 0);
            this.level.addParticle(ParticleTypes.SMOKE, smokeVec.x, smokeVec.y, smokeVec.z, 0.0D, 0.0D, 0.0D);
        }
        if (this.isInWaterOrBubble()) {
            if (!this.defused) this.setFuse(40);
            this.defused = true;
        }
        if (!this.defused) this.playSound(BrassArmorySounds.BOMB_FUSE.get(), 0.03F, level.getRandom().nextFloat() * 0.6F + 1F);
    }

    @Override
    public void lavaHurt() {
        this.discard();
        if (!this.level.isClientSide) explode(this.level, this);
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
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
    @ParametersAreNonnullByDefault
    protected void onHit(HitResult hitResult) {
        if (hitPerTick == 0) onHitEffects();
        if (++hitPerTick > 8) {
            this.discard();
            explode(this.level, this);
            return;
        }
        this.rotDirection -= (float)this.getDeltaMovement().length() * 2F;
        if (this.getTypeId() != 2) {
            double typeMul = this.getTypeId() == 0 ? 0.3D : 1D;
            if (hitResult instanceof BlockHitResult blockHitResult) {
                Axis axis = blockHitResult.getDirection().getAxis();
                Vec3 movement = this.getDeltaMovement();
                double newX = movement.x * (axis.equals(Axis.X) ? -1D : 1D) * typeMul;
                double newY = movement.y * (axis.equals(Axis.Y) ? -1D : 1D) * typeMul;
                double newZ = movement.z * (axis.equals(Axis.Z) ? -1D : 1D) * typeMul;
                this.setDeltaMovement(newX, newY, newZ);
                HitResult newHitResult = ProjectileUtil.getHitResult(this, this::canHitEntity);
                if (newHitResult.getType() != MISS) {
                    this.onHit(newHitResult);
                } else if (this.getTypeId() == 0 && newY < 0.05D && blockHitResult.getDirection() == UP) {
                    this.setOnGround(true);
                    this.setDeltaMovement(movement.x, 0, movement.z);
                }
            }
        } else {
            if (hitResult instanceof EntityHitResult entityHit && !this.level.isClientSide) {
                Entity mount = entityHit.getEntity();
                if (this.getOwner() != mount && mount instanceof LivingEntity) this.startRiding(entityHit.getEntity());
            }
            if (hitResult instanceof BlockHitResult) {
                Vec3 movement = this.getDeltaMovement();
                for (double d = 0D; true; d += 0.025D) {
                    Vec3 newMovement = movement.multiply(d, d, d);
                    if (this.isStuck(newMovement)) {
                        this.moveTo(this.position().add(newMovement));
                        break;
                    }
                }
            }
            this.setDeltaMovement(0, 0, 0);
        }
    }

    private boolean isStuck(@Nullable Vec3 vec) {
        AABB aabb = this.getBoundingBox().inflate(0.1D);
        if (vec != null) aabb = aabb.move(vec);
        boolean flag = !this.level.noCollision(this, aabb) || this.getVehicle() instanceof LivingEntity;
        this.setNoGravity(flag);
        if (flag) this.setDeltaMovement(0, 0, 0);
        return flag;
    }

    private void onHitEffects() {
        int type = this.getTypeId();
        float volume = (float)this.getDeltaMovement().length() / (type == 0 ? 3F : 1F);
        if (type != 0) {
            int i = 3 + this.level.getRandom().nextInt(2);
            for(int j = 0; j < i; ++j) {
                this.level.addParticle(this.getParticleType(), this.getRandomX(0.2D), this.getY(), this.getRandomZ(0.1D), 0.0D, 0.0D, 0.0D);
            }
        }
        this.playSound(getSoundEvent(), volume, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
    }

    private ParticleOptions getParticleType() {
        return switch (this.getTypeId()) {
            case 1 -> new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState());
            case 2 -> new BlockParticleOption(ParticleTypes.BLOCK, Blocks.HONEY_BLOCK.defaultBlockState());
            default -> null;
        };
    }

    private SoundEvent getSoundEvent() {
        return switch (this.getTypeId()) {
            case 0 -> BrassArmorySounds.BOMB_HIT.get();
            case 1 -> BrassArmorySounds.BOUNCY_BOMB_HIT.get();
            default -> BrassArmorySounds.STICKY_BOMB_HIT.get();
        };
    }

    @Override
    public double getMyRidingOffset() {
        return 0.25D;
    }

    public static void explode(Level level, Entity victim) {
        Entity bomb = victim instanceof BombEntity ? victim : null;
        if (bomb != null && bomb.getVehicle() != null) victim = bomb.getVehicle();
        level.explode(bomb, victim.getX(), victim.getY() + (double)(victim.getEyeHeight() * 0.5F), victim.getZ(), 2.0F, Explosion.BlockInteraction.BREAK);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putShort("Fuse", (short)this.getFuse());
        compoundTag.putShort("BombType", (short)this.getTypeId());
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        this.setFuse(compoundTag.getShort("Fuse"));
        this.setTypeId(compoundTag.getShort("BombType"));
    }

    public void setFuse(int newFuse) {
        this.entityData.set(DATA_FUSE, newFuse);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE);
    }

    public void setTypeId(int newType) {
        this.entityData.set(DATA_TYPE_ID, newType);
    }

    public int getTypeId() {
        return this.entityData.get(DATA_TYPE_ID);
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