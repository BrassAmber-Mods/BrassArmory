package com.milamber_brass.brass_armory.entity.projectile.bomb;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractRollableItemProjectileEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryAdvancements;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.BombItem;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BombEntity extends AbstractRollableItemProjectileEntity {
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

    public BombEntity(Level level, LivingEntity livingEntity) {
        this(level, livingEntity, null);
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

    @NotNull
    @Override
    protected Item getDefaultItem() {
        return BrassArmoryItems.BOMB.get();
    }

    @Override
    public void tick() {
        super.tick();
        int newFuse = this.getFuse() - (this.isOnFire() ? 2 : 1);
        this.setFuse(newFuse);
        if (newFuse <= 0) {
            this.discard();
            if (!this.defused && !this.level().isClientSide) this.explode(this.level());
        }
        if (!this.defused) {
            if (this.level().isClientSide && level().getRandom().nextInt(2) == 1) {
                Vec3 smokeVec = this.position().add(this.getDeltaMovement().multiply(-1.5D, -1.5D, -1.5D)).add(0, 0.125D, 0);
                this.level().addParticle(ParticleTypes.SMOKE, smokeVec.x, smokeVec.y, smokeVec.z, 0.0D, 0.0D, 0.0D);
            }
            this.playSound(BrassArmorySounds.BOMB_FUSE.get(), 0.03F, this.level().getRandom().nextFloat() * 0.6F + 1F);
            if (this.isInWaterOrBubble()) {
                this.setFuse(40);
                this.defused = true;
            }
        }
    }

    @Override
    public void lavaHurt() {
        this.explode(this.level());
    }

    @Override
    public void thunderHit(ServerLevel serverLevel, LightningBolt lightningBolt) {
        this.explode(this.level());
    }

    @Nonnull
    @Override //Pick up bomb if hand is empty
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.defused && !this.onGround() && player instanceof ServerPlayer serverPlayer) BrassArmoryAdvancements.CATCH_BOMB.trigger(serverPlayer);
        return this.defused ? InteractionResult.PASS : super.interact(player, hand);
    }

    @Nonnull
    @Override
    public ItemStack getItem() {
        return Util.make(super.getItem(), stack -> {
            BombItem.setFuseLit(stack, true);
            BombItem.setFuseLength(stack, this.getFuse());
            stack.setEntityRepresentation(this);
        });
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if (this.hitPerTick == 7) {
            this.explode(this.level());
            return;
        }
        super.onHitBlock(blockHitResult);
    }

    @Override
    protected double getBounceMultiplier() {
        return 0.3D;
    }

    @Override
    protected SoundEvent getSoundEvent() {
        return BrassArmorySounds.BOMB_HIT.get();
    }

    @Override
    protected float getVolumeMultiplier() {
        return 0.5F;
    }

    @Override
    public double getMyRidingOffset() {
        return 0.25D;
    }

    protected void explode(Level level) {
        if (!this.isRemoved()) this.discard();
        if (!level.isClientSide) explode(level, this);
    }

    public static void explode(Level level, Entity victim) {
        BombEntity bomb = victim instanceof BombEntity bombEntity ? bombEntity : null;
        if (bomb != null && bomb.getVehicle() != null) victim = bomb.getVehicle();

        ArmoryUtil.explode(level, bomb, victim.position().add(0.0D, victim.getBbHeight() * 0.5F, 0.0D), 2.0F, bomb != null && bomb.isOnFire(), Level.ExplosionInteraction.TNT);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putShort("Fuse", (short)this.getFuse());
    }

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