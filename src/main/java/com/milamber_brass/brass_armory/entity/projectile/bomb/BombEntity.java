package com.milamber_brass.brass_armory.entity.projectile.bomb;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractRollableItemProjectile;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.BombItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BombEntity extends AbstractRollableItemProjectile {
    private static final EntityDataAccessor<Integer> DATA_FUSE = SynchedEntityData.defineId(BombEntity.class, EntityDataSerializers.INT);
    private boolean defused;

    public BombEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
        this.setFuse(60);
        this.setOnGround(false);
        this.defused = false;
    }

    public BombEntity(Level level, LivingEntity livingEntity, @Nullable HumanoidArm arm) {
        super(BrassArmoryEntityTypes.BOMB.get(), livingEntity, level, arm);
        this.setFuse(60);
        this.setOnGround(false);
        this.defused = false;
    }

    public BombEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.BOMB.get(), x, y, z, level);
        this.setFuse(60);
        this.setOnGround(false);
        this.defused = false;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FUSE, 80);
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
        super.tick();
        int newFuse = this.getFuse() - (this.isOnFire() ? 2 : 1);
        this.setFuse(newFuse);
        if (newFuse <= 0) {
            this.discard();
            if (!this.defused && !this.level.isClientSide) explode(this.level, this);
        }
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

    @Override
    public void lavaHurt() {
        this.explode(this.level);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void thunderHit(ServerLevel serverLevel, LightningBolt lightningBolt) {
        this.explode(this.level);
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault //Pick up bomb if hand is empty
    public InteractionResult interact(Player player, InteractionHand hand) {
        return this.defused ? InteractionResult.PASS : super.interact(player, hand);
    }

    @Nonnull
    @Override
    public ItemStack getItem() {
        ItemStack bombStack = super.getItem();
        BombItem.setFuseLit(bombStack, true);
        BombItem.setFuseLength(bombStack, this.getFuse());
        bombStack.setEntityRepresentation(this);
        return bombStack;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if (this.hitPerTick == 7) {
            this.explode(this.level);
            return;
        }
        super.onHitBlock(blockHitResult);
    }

    @Override
    protected double getBounceMultiplier() {
        return this.getBombType().getBounceMultiplier();
    }

    @Override
    protected SoundEvent getSoundEvent() {
        return BrassArmorySounds.BOMB_HIT.get();
    }

    @Override
    protected float getVolumeMultiplier() {
        return this.getBombType().getVolumeMultiplier();
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

    public boolean getDefused() {
        return this.defused;
    }
}