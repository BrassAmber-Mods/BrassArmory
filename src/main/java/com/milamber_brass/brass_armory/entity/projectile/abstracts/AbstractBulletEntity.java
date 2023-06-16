package com.milamber_brass.brass_armory.entity.projectile.abstracts;

import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class AbstractBulletEntity extends AbstractArrow implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(AbstractBulletEntity.class, EntityDataSerializers.ITEM_STACK);

    protected AbstractBulletEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    protected AbstractBulletEntity(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level level) {
        super(entityType, x, y, z, level);
    }

    protected AbstractBulletEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity living, Level level) {
        super(entityType, living, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    @Override
    protected void tickDespawn() {
        ++this.life;
        if (this.life >= 600) this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        this.lastState = this.level().getBlockState(blockHitResult.getBlockPos());
        BlockState blockstate = this.level().getBlockState(blockHitResult.getBlockPos());
        blockstate.onProjectileHit(this.level(), blockstate, blockHitResult, this);
        Vec3 vec3 = blockHitResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale(0.05D);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.shakeTime = 40;
        this.setCritArrow(false);
        this.setPierceLevel((byte)0);
        this.setSoundEvent(this.getDefaultHitGroundSoundEvent());
        this.setShotFromCrossbow(false);
        this.resetPiercedEntities();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity living) {
            int arrowCount = living.getArrowCount();
            super.onHitEntity(entityHitResult);
            living.setArrowCount(arrowCount);
        } else super.onHitEntity(entityHitResult);
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return BrassArmorySounds.BULLET_HIT.get();
    }

    @Override
    public void playerTouch(Player player) {

    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull Component getName() {
        return this.getItem().getItem().getName(this.getItem());
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
    }

    public void setItem(ItemStack itemStack) {
        this.getEntityData().set(DATA_ITEM_STACK, Util.make(itemStack.copy(), (stack) -> stack.setCount(1)));
    }

    protected ItemStack getItemRaw() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    protected abstract Item getDefaultItem();

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ItemStack stack = this.getItemRaw();
        if (!stack.isEmpty()) tag.put("BulletStack", stack.save(new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setItem(ItemStack.of(tag.getCompound("BulletStack")));
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
