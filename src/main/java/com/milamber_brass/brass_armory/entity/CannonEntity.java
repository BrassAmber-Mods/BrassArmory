package com.milamber_brass.brass_armory.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import com.milamber_brass.brass_armory.behaviour.GunBehaviours;
import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.capabilities.EffectCapabilityHandler;
import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.init.BrassArmoryAdvancements;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.inventory.GunContainer;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import java.util.function.Predicate;
import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;

public class CannonEntity extends Entity implements iGun {
    protected static final EntityDataAccessor<CompoundTag> DATA_TAG = SynchedEntityData.defineId(CannonEntity.class, com.milamber_brass.brass_armory.init.BrassArmoryDataSerializers.CANNON_DATA.get());
    protected static final EntityDataAccessor<Integer> DATA_FUSE = SynchedEntityData.defineId(CannonEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> DATA_DAMAGE = SynchedEntityData.defineId(CannonEntity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Integer> DATA_HURTDIR = SynchedEntityData.defineId(CannonEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> DATA_HURT = SynchedEntityData.defineId(CannonEntity.class, EntityDataSerializers.INT);

    protected static final EntityDataAccessor<Integer> DATA_MOVE_TICKS = SynchedEntityData.defineId(CannonEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> DATA_SOUND_TICKS = SynchedEntityData.defineId(CannonEntity.class, EntityDataSerializers.INT);
    @Nullable private ServerPlayer inventoryViewer;
    public static final String uuid = "BAPlayerUUID";

    public CannonEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public CannonEntity(Level level, double x, double y, double z) {
        this(BrassArmoryEntityTypes.CANNON.get(), level);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_TAG, new CompoundTag());
        builder.define(DATA_FUSE, 0);
        builder.define(DATA_DAMAGE, 0.0F);
        builder.define(DATA_HURTDIR, 1);
        builder.define(DATA_HURT, 0);
        builder.define(DATA_MOVE_TICKS, 0);
        builder.define(DATA_SOUND_TICKS, 0);
    }

    public ItemStack getAmmo() {
        return ArmoryUtil.loadStack(this.entityData.get(DATA_TAG), GunContainer.gunAmmo, ItemStack.EMPTY);
    }

    @Override
    public void onOpen(Player owner, CompoundTag tag) {
        owner.level().playSound(null, owner, BrassArmorySounds.CANNON_OPEN.get(), SoundSource.PLAYERS, 1.35F, this.level().getRandom().nextFloat() * 0.35F + 0.235F);
    }

    @Override
    public void onLoad(Player owner, CompoundTag tag) {
        owner.level().playSound(null, owner, BrassArmorySounds.CANNON_CLOSE.get(), SoundSource.PLAYERS, 1.5F, this.level().getRandom().nextFloat() * 0.5F + 0.25F);
        this.reLoad(tag);
        this.inventoryViewer = null;
    }

    public CompoundTag getTag() {
        return this.entityData.get(DATA_TAG);
    }

    public void reLoad(CompoundTag tag) {
        this.entityData.set(DATA_TAG, tag.copy());
    }

    @Override
    public Predicate<ItemStack> ammoPredicate() {
        return itemStack -> itemStack.is(BrassArmoryTags.Items.CANNON_AMMO);
    }

    public ItemStack getPowder() {
        return ArmoryUtil.loadStack(this.entityData.get(DATA_TAG), GunContainer.gunPowder, ItemStack.EMPTY);
    }

    @Override
    public Predicate<ItemStack> powderPredicate() {
        return itemStack -> itemStack.is(BrassArmoryTags.Items.FLINTLOCK_POWDER);
    }

    public void setFuse(int fuse) {
        this.entityData.set(DATA_FUSE, fuse);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE);
    }

    public void setDamage(float damage) {
        this.entityData.set(DATA_DAMAGE, damage);
    }

    public float getDamage() {
        return this.entityData.get(DATA_DAMAGE);
    }

    public void setHurtDir(int hurtDir) {
        this.entityData.set(DATA_HURTDIR, hurtDir);
    }

    public int getHurtDir() {
        return this.entityData.get(DATA_HURTDIR);
    }

    public void setHurtTime(int hurtTime) {
        this.entityData.set(DATA_HURT, hurtTime);
    }

    public int getHurtTime() {
        return this.entityData.get(DATA_HURT);
    }

    public void setMoveTicks(int moveTicks) {
        this.entityData.set(DATA_MOVE_TICKS, moveTicks);
    }

    public int getMoveTicks() {
        return this.entityData.get(DATA_MOVE_TICKS);
    }

    public void setSoundTicks(int soundTicks) {
        this.entityData.set(DATA_SOUND_TICKS, soundTicks);
    }

    public int getSoundTicks() {
        return this.entityData.get(DATA_SOUND_TICKS);
    }

    @Override
    public double damage() {
        return 50D;
    }

    @Override
    public float accuracy() {
        return 4F;
    }

    @Override
    public float speed() {
        return 4F;
    }

    @Override
    public float particleMultiplier() {
        return 1.75F;
    }

    @Override
    public double particleOffset() {
        return 1.4D;
    }

    @Override
    public void onShoot(Level level, LivingEntity owner, Entity shooter, ItemStack ammoStack, @Nullable Projectile projectile) {
        if (projectile != null) {
            Vec3 vec3 = shooter.getEyePosition().add(shooter.getViewVector(ArmoryUtil.frameTime(level)).scale(this.particleOffset()));
            BlockHitResult blockHitResult = level.clip(new ClipContext(shooter.getEyePosition(), vec3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, shooter));
            if (blockHitResult.getType().equals(HitResult.Type.MISS)) projectile.setPos(vec3);
        }
    }


    @Override
    public boolean canBeCollidedWith(Entity other) {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide() && !this.level().getBlockState(this.blockPosition().below()).isFaceSturdy(this.level(), this.blockPosition().below(), Direction.UP)) {
            if (this.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)) {
                this.dropItems();
            }
            this.discard();
            return;
        }

        if (this.getDamage() > 0.0F) this.setDamage(this.getDamage() - 2.0F);
        if (this.getHurtTime() > 0) this.setHurtTime(this.getHurtTime() - 1);

        LivingEntity living = this.getControllingPassenger();
        if (living != null) {
            float yRot = living.getYRot();

            this.setYRot(yRot);
            if (yRot != this.yRotO) {
                this.setMoveTicks(40);
                this.setSoundTicks(this.getSoundTicks() + 1);
            }
            this.yRotO = this.getYRot();


            float xRot = Mth.clamp(living.getXRot(), -45F, 15F) ;

            this.setXRot(xRot);
            if (xRot != this.xRotO) {
                this.setMoveTicks(40);
                this.setSoundTicks(this.getSoundTicks() + 1);
            }
            this.xRotO = this.getXRot();

            this.setRot(this.getYRot(), this.getXRot());
        }

        int moveTicks = this.getMoveTicks();
        if (moveTicks > 0) {
            this.setMoveTicks(moveTicks - 1);
            if (moveTicks >= 35) this.level().playSound(null, this, BrassArmorySounds.CANNON_MOVE.get(), SoundSource.NEUTRAL, Float.MIN_VALUE, Float.MIN_VALUE);
        }

        int fuse = this.getEntityData().get(DATA_FUSE);
        if (fuse > 0 && !this.level().isClientSide()) {
            CompoundTag compoundTag = this.entityData.get(DATA_TAG);
            if (compoundTag.contains(uuid)) {
                Player player = this.level().getPlayerByUUID(compoundTag.read(uuid, net.minecraft.core.UUIDUtil.CODEC).orElse(new java.util.UUID(0L, 0L)));
                if (player == null) {
                    this.entityData.set(DATA_FUSE, 0);
                    return;
                }

                if (fuse >= 30) {
                    if (fuse == 30) {
                        GunBehaviours.getPowderBehaviour(this.getPowder()).ifPresent(powderBehaviour -> {
                            ItemStack ammo = this.getAmmo();
                            GunBehaviours.getAmmoBehavior(ammo).ifPresent(ammoBehaviour -> {
                                if (ammoBehaviour.onShoot(this.level(), player, this, ammo, powderBehaviour, this) && !this.level().isClientSide()) {
                                    this.reLoad(Util.make(this.entityData.get(DATA_TAG).copy(), tag -> {
                                        ArmoryUtil.clearStack(tag, GunContainer.gunAmmo);
                                        ArmoryUtil.clearStack(tag, GunContainer.gunPowder);
                                    }));
                                    if (player instanceof ServerPlayer serverPlayer) BrassArmoryAdvancements.FIRE_CANNON.trigger(serverPlayer);
                                }
                                this.playSound(BrassArmorySounds.CANNON_SHOOT.get(), 1.5F, this.level().getRandom().nextFloat() * 0.5F + 1.25F);
                            });
                            if (this.getControllingPassenger() instanceof Player plyr)
                                EffectCapabilityHandler.setShakePower(plyr, 1.75D);
                        });
                    } else if (fuse >= 40) {
                        this.entityData.set(DATA_FUSE, 0);
                        return;
                    }
                } else
                    this.playSound(BrassArmorySounds.CANNON_FUSE.get(), 0.03F, this.level().getRandom().nextFloat() * 0.6F + 1F);
                this.getEntityData().set(DATA_FUSE, fuse + 1);
            } else this.setFuse(0);
        }

        super.tick();

        if (this.getSoundTicks() > 0) this.setSoundTicks(this.getSoundTicks() - 1);
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }


    @Override
    public void animateHurt(float v) {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() * 11.0F);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof LivingEntity living ? living : null;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        var dimensions = super.getDimensions(pose);
        return dimensions.withEyeHeight(dimensions.height() * 0.7F);
    }

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (player.isSecondaryUseActive()) {
            if (this.entityData.get(DATA_FUSE) == 0 && player instanceof ServerPlayer serverPlayer) this.openInventory(serverPlayer);
            return InteractionResult.SUCCESS;
        } else if (ArmoryUtil.isFuseLighter(player.getItemInHand(hand))) {
            if (this.entityData.get(DATA_FUSE) == 0 && !this.isInventoryOpen()) {
                if (!this.level().isClientSide()) {
                    this.reLoad(Util.make(this.entityData.get(DATA_TAG).copy(), tag -> tag.store(uuid, net.minecraft.core.UUIDUtil.CODEC, player.getUUID())));
                    this.getEntityData().set(DATA_FUSE, 1);

                    player.getItemInHand(hand).hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }
                return InteractionResult.SUCCESS;
            } else return InteractionResult.CONSUME;
        } else {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            if (!this.level().isClientSide()) player.startRiding(this);
            return InteractionResult.SUCCESS;
        }
    }

    public void openInventory(ServerPlayer player) {
        if (this.isRemoved() || this.getFuse() != 0 || this.isInventoryOpen() || player.distanceToSqr(this) > 64D) return;
        CompoundTag tag = this.getTag().copy();
        ArmoryUtil.addStack(tag, BrassArmoryItems.CANNON.get().getDefaultInstance(), GunContainer.gunIcon);
        player.openMenu(new SimpleMenuProvider(GunContainer.getServerContainer(this, tag), Component.empty()));
        if (player.containerMenu instanceof GunContainer<?> menu && menu.gun == this) {
            this.inventoryViewer = player;
            // The open menu owns these items until it closes, including if the cannon is destroyed.
            CompoundTag unloaded = this.getTag().copy();
            ArmoryUtil.clearStack(unloaded, GunContainer.gunAmmo);
            ArmoryUtil.clearStack(unloaded, GunContainer.gunPowder);
            this.reLoad(unloaded);
        }
    }

    public boolean isInventoryOpen() {
        return this.inventoryViewer != null && this.inventoryViewer.containerMenu instanceof GunContainer<?> menu && menu.gun == this;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float damage) {
        if (source.is(DamageTypeTags.IS_FIRE) || this.isInvulnerableToBase(source)) {
            return false;
        } else if (!this.level().isClientSide() && !this.isRemoved()) {

            if (source.getDirectEntity() instanceof Player p && ArmoryUtil.isFuseLighter(p.getMainHandItem()) && this.getFuse() == 0 && !this.isInventoryOpen()) {
                this.reLoad(Util.make(this.entityData.get(DATA_TAG).copy(), tag -> tag.store(uuid, net.minecraft.core.UUIDUtil.CODEC, p.getUUID())));
                this.getEntityData().set(DATA_FUSE, 1);

                return true;
            }

            this.setHurtDir(-this.getHurtDir());
            this.setHurtTime(10);
            this.markHurt();

            Entity culprit = source.getDirectEntity();
            this.gameEvent(GameEvent.ENTITY_DAMAGE, culprit);

            damage = damage * 10.0F * (culprit instanceof LivingEntity ? 3.0F : 1.0F);
            damage *= (source.is(DamageTypeTags.IS_EXPLOSION) ? 0.25F : 1.0F);

            boolean flag = culprit instanceof Player player && player.getAbilities().instabuild && !source.is(DamageTypeTags.IS_EXPLOSION) && !player.isSecondaryUseActive();
            if (flag || this.getDamage() + damage > this.getMaxHealth()) {
                if (!flag && serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)) {
                    this.dropItems();
                }
                this.discard();
            } else if (culprit instanceof Player player && player.isSecondaryUseActive()) {
                BlockPos relativePos = this.blockPosition().relative(player.getDirection());
                if (canSurvive(this.level(), relativePos.below())) {
                    this.setPos(Vec3.atBottomCenterOf(relativePos));
                    this.level().removeBlock(relativePos, false);
                    if (this.getDamage() < this.getMaxHealth() * 0.5F) this.setDamage(this.getMaxHealth() * 0.5F);
                } else this.setDamage(this.getDamage() + damage);
            } else this.setDamage(this.getDamage() + damage);
            return true;
        } else return true;
    }

    public static boolean canSurvive(Level level, BlockPos blockPos) {
        BlockState blockstate = level.getBlockState(blockPos);
        if (!blockstate.isFaceSturdy(level, blockPos, Direction.UP)) return false;
        else {
            BlockPos blockpos1 = blockPos.above();
            BlockState blockstate1 = level.getBlockState(blockpos1);

            if (!blockstate1.canBeReplaced()) return false;
            else return level.isUnobstructed(null, Shapes.block().move(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ()));
        }
    }


    protected float getMaxHealth() {
        return 200.0F;
    }

    protected void dropItems() {
        this.spawnAtLocation((ServerLevel)this.level(), this.getDropItem());
        if (!this.getAmmo().isEmpty()) this.spawnAtLocation((ServerLevel)this.level(), this.getAmmo());
        if (!this.getPowder().isEmpty()) this.spawnAtLocation((ServerLevel)this.level(), this.getPowder());
    }

    public @NotNull Item getDropItem() {
        return BrassArmoryItems.CANNON.get();
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(this.getDropItem());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput tag) {
        tag.read("DataTag", CompoundTag.CODEC).ifPresent(data -> this.getEntityData().set(DATA_TAG, data));
        this.getEntityData().set(DATA_FUSE, tag.getIntOr("DataFuse", 0));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput tag) {
        if (!this.getEntityData().get(DATA_TAG).isEmpty()) tag.store("DataTag", CompoundTag.CODEC, this.getEntityData().get(DATA_TAG));
        tag.putInt("DataFuse", this.entityData.get(DATA_FUSE));
    }



    @Override
    protected @NotNull Component getTypeName() {
        return this.getDropItem().getDefaultInstance().getHoverName();
    }
}