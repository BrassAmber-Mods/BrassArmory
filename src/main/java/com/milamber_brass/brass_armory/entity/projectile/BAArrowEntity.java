package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.ArmoryUtil;
import com.milamber_brass.brass_armory.block.RopeBlock;
import com.milamber_brass.brass_armory.data.advancement.BrassArmoryAdvancements;
import com.milamber_brass.brass_armory.init.BrassArmoryBlocks;
import com.milamber_brass.brass_armory.init.BrassArmoryDispenseBehaviors;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BAArrowEntity extends AbstractArrow {

    private static final EntityDataAccessor<String> ARROW_TYPE = SynchedEntityData.defineId(BAArrowEntity.class, EntityDataSerializers.STRING);
    private static final String ARROW_TYPE_STRING = "ArrowType";
    private boolean hitEntity = false;
    private boolean placeRope = false;
    private BlockPos currentRopePos;
    private double baDamage;
    private int totalRope = 0;
    private Direction hitBlockfaceDirection;
    private int ticksSinceRope;

    /**
     * Used to initialize the EntityType.
     */
    public BAArrowEntity(EntityType<? extends AbstractArrow> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    /**
     * Used when fired by a Player.
     */
    public BAArrowEntity(Level worldIn, LivingEntity shooter, ArrowType typeIn) {
        super(BrassArmoryEntityTypes.BA_ARROW.get(), shooter, worldIn);;
        this.setArrowType(typeIn.getSerializedName());
        if (this.isArrowType(ArrowType.LASER)) {
            this.setPierceLevel((byte) 5);
            this.setNoGravity(true);
        }
        this.setBaseDamage(this.getArrowType().getDamage());
    }

    /**
     * Used for Dispensers in: {@link BrassArmoryDispenseBehaviors}
     */
    public BAArrowEntity(Level worldIn, double x, double y, double z, ArrowType typeIn) {
        super(BrassArmoryEntityTypes.BA_ARROW.get(), x, y, z, worldIn);
        this.setArrowType(typeIn.getSerializedName());
    }

    /*********************************************************** Data ********************************************************/

    public ArrowType getArrowType() {
        return ArrowType.byName(this.getEntityData().get(ARROW_TYPE));
    }

    public void setArrowType(String arrowTypeName) {
        this.getEntityData().set(ARROW_TYPE, arrowTypeName);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(ARROW_TYPE, ArrowType.EMPTY.getSerializedName());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getArrowType() != null) {
            compound.putString(ARROW_TYPE_STRING, this.getArrowType().getSerializedName());
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(ARROW_TYPE_STRING)) {
            this.setArrowType(compound.getString(ARROW_TYPE_STRING));
        }
    }

    /*********************************************************** Arrow Hits ********************************************************/

    /**
     * Called when this projectile hits a LivingEntity.
     */
    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        this.hitEntity = true;
        switch (ArrowType.byName(this.getEntityData().get(ARROW_TYPE))) {
            case DIRT -> this.level.setBlock(new BlockPos(this.getX(), this.getY(), this.getZ()), Blocks.DIRT.defaultBlockState(), 2);
            case EXPLOSION -> this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Explosion.BlockInteraction.BREAK);
            case FROST -> living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 70, 3));
            case SLIME -> living.knockback(this.getDeltaMovement().length() * 0.75D, this.getX() - living.getX(), this.getZ() - living.getZ());
            case FIRE -> living.setSecondsOnFire(16);
            case CONCUSSION -> living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, Mth.clamp(this.tickCount * 2, 80, 240), Mth.clamp(this.tickCount / 80, 0, 2)));
            case WARP, LASER, ROPE, GRASS -> { }
        }
    }

    /**
     * Called when this arrow hits a block.
     */
    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        switch (ArrowType.byName(this.getEntityData().get(ARROW_TYPE))) {
            case DIRT:
                BlockPos relative = result.getBlockPos().relative(result.getDirection());
                if (this.level.getBlockState(relative).getMaterial().isReplaceable()) this.level.setBlock(relative, Blocks.DIRT.defaultBlockState(), 2);
                this.discard();
                break;
            case EXPLOSION:
                this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Explosion.BlockInteraction.BREAK);
                this.discard();
                break;
            case FROST:
                BlockState blockstate = Blocks.ICE.defaultBlockState();
                BlockPos pos = result.getBlockPos();

                for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-3F, 0.0D, -3F), pos.offset(3F, 0.0D, 3F))) {
                    if (blockpos.closerThan(pos, 2.5F)) {
                        BlockState state = this.level.getBlockState(blockpos);
                        boolean isFull = state.getBlock().equals(Blocks.WATER) && state.getValue(LiquidBlock.LEVEL) == 0;
                        if (state.getMaterial() == Material.WATER && isFull && blockstate.canSurvive(this.level, blockpos) && this.level.isUnobstructed(blockstate, blockpos, CollisionContext.empty()) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(this, net.minecraftforge.common.util.BlockSnapshot.create(this.level.dimension(), this.level, blockpos), Direction.UP.UP)) {
                            this.level.setBlockAndUpdate(blockpos, blockstate);
                            this.level.scheduleTick(blockpos, Blocks.FROSTED_ICE, Mth.nextInt(this.random, 60, 120));
                        }
                    }

                }
                this.discard();
                break;
            case GRASS:
                BlockPos resultPos = result.getBlockPos();
                if (this.level.getBlockState(resultPos).is(Blocks.DIRT)) {
                    this.level.setBlock(resultPos, Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                }
                if (this.level instanceof ServerLevel serverLevel && serverLevel.getBlockState(resultPos).getMaterial().equals(Material.DIRT)) {
                    BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), this.level, resultPos, FakePlayerFactory.getMinecraft(serverLevel));
                }
                this.discard();
                break;
            case ROPE:
                this.placeRopes(result);
                this.setInvisible(true);
                break;
            case SLIME:
                this.spawnSmallSlime();
                this.discard();
                break;
            case FIRE:
                ArmoryUtil.blockHitSetOnFire(result, this.level, this.getOwner());
                this.discard();
                break;
            case LASER:
                if (this.hitEntity) this.discard();
                break;
            case WARP:
                if (this.level.getBlockState(this.blockPosition()).getFluidState().isEmpty()) this.teleportShooter();
                break;
            case CONCUSSION:
            default:
                break;
        }
    }

    /**
     * Called very frequently when the arrow exists.
     */
    public void tick() {
        if (this.isArrowType(ArrowType.FROST)) this.freezeWaterInPath();
        super.tick();
        // check that the arrow is not in the ground and has been flying for 1/5 a second.
        if (this.isArrowType(ArrowType.LASER)) {
            if (!this.inGround) {
                if (this.tickCount > 4) {
                    // Set deltaMovement after calling tick()s, so that it remains unchanged by the method itself
                    Vec3 deltaMovement = this.getDeltaMovement();
                    super.tick();
                    if (this.isNoGravity()) {
                        super.tick();
                        this.setDeltaMovement(deltaMovement);
                    }

                    if (!this.level.hasChunk(this.chunkPosition().x, this.chunkPosition().z)) {
                        this.remove(RemovalReason.UNLOADED_TO_CHUNK);
                    }
                }
            } else this.setNoGravity(false);
        }
        // Check that we have entered place rope mode and that the ticks since last placing a rope are at least equal to 10 ticks
        else if (this.isArrowType(ArrowType.ROPE) && this.placeRope) {
            this.ticksSinceRope++; // Increase the counter for number of ticks since last placing a rope
            if (this.ticksSinceRope > 6) {
                BlockPos newPos = currentRopePos.relative(Direction.DOWN, 1);
                int maxRopeLength = 24;
                if (this.level.getBlockState(newPos).isAir() && this.totalRope < maxRopeLength) {
                    this.level.setBlock(newPos, BrassArmoryBlocks.EXPLORERS_ROPE_BLOCK.get().defaultBlockState().setValue(RopeBlock.FACING, this.hitBlockfaceDirection).setValue(RopeBlock.HAS_ARROW, this.totalRope == 0), 2);
                    this.currentRopePos = newPos;
                    this.totalRope++;
                    this.ticksSinceRope = 0;
                } else {
                    // No space to place more Ropes or Rope Limit reached.
                    this.placeRope = false;
                    this.discard();
                }
            }
        }
        if (this.level.isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) this.spawnArrowParticles(1);
            } else this.spawnArrowParticles(2);
        } else if (this.inGround && this.inGroundTime != 0 && this.inGroundTime >= 600) {
            this.level.broadcastEntityEvent(this, (byte) 0);
        }
    }

    /*********************************************************** Arrow Functionalities ********************************************************/

    private void placeRopes(BlockHitResult result) {
        hitBlockfaceDirection = result.getDirection();
        if (!hitBlockfaceDirection.equals(Direction.DOWN) && !hitBlockfaceDirection.equals(Direction.UP)) {
            BlockPos hitPos = result.getBlockPos();
            currentRopePos = hitPos.relative(hitBlockfaceDirection);
            BlockState hitBlockState = this.level.getBlockState(hitPos);
            // Check if the block that the arrow hit can hold the Rope.
            if (hitBlockState.isFaceSturdy(this.level, currentRopePos, hitBlockfaceDirection)) {
                // Check if there's space to place a Rope.
                if (this.level.getBlockState(currentRopePos).isAir()) {
                    this.level.setBlock(currentRopePos, BrassArmoryBlocks.EXPLORERS_ROPE_BLOCK.get().defaultBlockState().setValue(RopeBlock.FACING, hitBlockfaceDirection).setValue(RopeBlock.HAS_ARROW, totalRope == 0), 2);
                    this.totalRope++;
                    this.placeRope = true;
                    // Prevent the arrow from being picked up while the ropes are being placed.
                    this.pickup = Pickup.DISALLOWED;
                }
            }
        }
    }

    /**
     * Referenced from {@link net.minecraftforge.event.entity.EntityTeleportEvent.EnderPearl}
     */
    private void teleportShooter() {
        for (int i = 0; i < 32; ++i) {
            this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * .5D, this.getZ(), 0.0D, 0.0D, 0.0D);
        }

        Entity entity = this.getOwner();
        if (!this.level.isClientSide && this.isAlive()) {
            // Check if the shooter is a Player.
            if (entity instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.connection.getConnection().isConnected() && serverPlayer.level == this.level && !serverPlayer.isSleeping()) {
                    net.minecraftforge.event.entity.EntityTeleportEvent.EnderPearl event = net.minecraftforge.event.ForgeEventFactory.onEnderPearlLand(serverPlayer, this.getX(), this.getY(), this.getZ(), EntityType.ENDER_PEARL.create(this.level), 5.0F);
                    if (!event.isCanceled()) {
                        // Small chance to spawn an Endermite.
                        if (this.random.nextFloat() < 0.05F && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                            Endermite endermite = EntityType.ENDERMITE.create(this.level);

                            if (endermite != null) {
                                endermite.moveTo(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
                                this.level.addFreshEntity(endermite);
                            }
                        }

                        if (serverPlayer.distanceTo(this) >= 100D) BrassArmoryAdvancements.LONG_DISTANCE_WARP_ARROW_TRIGGER.trigger(serverPlayer);

                        if (serverPlayer.isPassenger()) {
                            serverPlayer.dismountTo(this.getX(), this.getY(), this.getZ());
                        } else {
                            entity.teleportTo(this.getX(), this.getY(), this.getZ());
                        }

                        entity.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                        entity.resetFallDistance();
                    }
                }
            } else if (entity != null) {
                entity.teleportTo(this.getX(), this.getY(), this.getZ());
                entity.resetFallDistance();
            }

            this.discard();
        }
    }

    /**
     * Spawn a small Slime and remove the arrow.
     */
    private void spawnSmallSlime() {
        if (!this.level.isClientSide && this.isAlive()) {
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                // Slime default size is small so we don't need to set a size.
                Slime slimeEntity = EntityType.SLIME.create(this.level);
                // slime was spawning with wrong bounding box and too much health, though visually small
                slimeEntity.refreshDimensions();
                slimeEntity.setHealth(1);
                // Set the Slime's position to the center of the block.
                slimeEntity.moveTo(this.blockPosition().getX() + .5f, this.blockPosition().getY() + 0.05f, this.blockPosition().getZ() + .5f, this.getYRot(), this.getXRot());
                this.level.addFreshEntity(slimeEntity);

                // Remove this arrow only if a Slime spawns
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    public void freezeWaterInPath() {
        Vec3 thisPos = this.position();
        Vec3 nextPos = thisPos.add(this.getDeltaMovement());
        BlockHitResult blockHitResult = this.level.clip(new ClipContext(thisPos, nextPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, this));

        if (blockHitResult.getType() != HitResult.Type.MISS) {
            this.onHitBlock(blockHitResult);
        }
    }

    /*********************************************************** Particles ********************************************************/

    private void spawnArrowParticles(int particleCount) {
        if (particleCount > 0) {
            double d0 = 40.0;
            double d1 = 75.0;
            double d2 = 40.0;
            switch (ArrowType.byName(this.getEntityData().get(ARROW_TYPE))) {

                case DIRT:
                case GRASS:
                    for (int j = 0; j < particleCount; ++j) {
                        this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DIRT.defaultBlockState()), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
                    }
                    break;
                case EXPLOSION:
                    if (this.level.isClientSide && level.getRandom().nextInt(particleCount) == 1) {
                        Vec3 smokeVec = this.position().add(this.getDeltaMovement().multiply(-1.5D, -1.5D, -1.5D)).add(0, 0.125D, 0);
                        this.level.addParticle(ParticleTypes.SMOKE, smokeVec.x, smokeVec.y, smokeVec.z, 0.0D, 0.0D, 0.0D);
                    }
                    break;
                case FIRE:
                    for (int j = 0; j < particleCount; ++j) {
                        this.level.addParticle(ParticleTypes.FLAME, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0D, 0D, 0D);
                    }
                    break;
                case FROST:
                    for (int j = 0; j < particleCount; ++j) {
                        this.level.addParticle(ParticleTypes.FALLING_WATER, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
                    }
                    break;
                case LASER:
                    for (int j = 0; j < particleCount; ++j) {
                        this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
                    }
                    break;
                case SLIME:
                    for (int j = 0; j < particleCount; ++j) {
                        this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState()), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
                    }
                    break;
                case WARP:
                    for (int j = 0; j < particleCount; ++j) {
                        this.level.addParticle(ParticleTypes.REVERSE_PORTAL, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0D, 0D, 0D);
                    }
                    break;
                case ROPE:
                case CONCUSSION:
                default:
                    break;
            }
        }
    }

    /*********************************************************** Util ********************************************************/

    /**
     * Returns whether or not the ArrowTypes match.
     */
    private boolean isArrowType(ArrowType arrowType) {
        return ArrowType.byName(this.getEntityData().get(ARROW_TYPE)) == arrowType;
    }

    @Override
    public double getBaseDamage() {
        return this.baDamage;
    }

    @Override
    public void setBaseDamage(double damageIn) {
        this.baDamage = damageIn;
    }

    /**
     * Returns the correct Item when picking up an arrow.
     */
    @Override
    @Nonnull
    protected ItemStack getPickupItem() {
        return new ItemStack(ArrowType.getModItemFor(this.getArrowType()));
    }

    @Override
    public @NotNull Component getName() {
        return this.getPickupItem().getItem().getName(this.getPickupItem());
    }

    /*********************************************************** Networking
     * @return********************************************************/

    @Override
    @Nonnull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
