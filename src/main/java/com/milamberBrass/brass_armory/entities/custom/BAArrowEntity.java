package com.milamberBrass.brass_armory.entities.custom;

import com.milamberBrass.brass_armory.blocks.ModBlocks;
import com.milamberBrass.brass_armory.blocks.custom.RopeBlock;
import com.milamberBrass.brass_armory.entities.ModEntityTypes;
import com.milamberBrass.brass_armory.entities.dispenser.CustomDispenserBehavior;
import com.milamberBrass.brass_armory.util.ArrowType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.fml.network.NetworkHooks;

public class BAArrowEntity extends AbstractArrowEntity {
	private static final DataParameter<String> ARROW_TYPE = EntityDataManager.defineId(BAArrowEntity.class, DataSerializers.STRING);
	private static final String ARROW_TYPE_STRING = "ArrowType";
	private boolean hitEntity = false;
	private int flightTime = 0;
	private boolean placeRope = false;
	private BlockPos currentRopePos;
	private double baDamage;
	private int maxRopeLength = 24;
	private int totalRope = 0;
	private Direction hitBlockfaceDirection;
	private int ticksSinceRope;

	/**
	 * Used to initialize the EntityType.
	 */
	public BAArrowEntity(EntityType<? extends AbstractArrowEntity> entityType, World worldIn) {
		super(entityType, worldIn);
	}

	/**
	 * Used when fired by a Player.
	 */
	public BAArrowEntity(EntityType<? extends AbstractArrowEntity> type, World worldIn, LivingEntity shooter, ArrowType typeIn) {
		super(type, shooter, worldIn);
		this.setArrowType(typeIn.getSerializedName());
		if (this.isArrowType(ArrowType.LASER)) {
			this.setPierceLevel((byte) 5);
		}
		this.setBaseDamage(this.getArrowType().getDamage());
	}

	/**
	 * Used when fired by a Player.
	 */
	public BAArrowEntity(World worldIn, LivingEntity shooter, ArrowType typeIn) {
		// Calls the constructor above.
		this(ModEntityTypes.BA_ARROW.get(), worldIn, shooter, typeIn);
	}

	/**
	 * Used for Dispensers in: {@link CustomDispenserBehavior}
	 */
	public BAArrowEntity(World worldIn, double x, double y, double z, ArrowType typeIn) {
		super(ModEntityTypes.BA_ARROW.get(), x, y, z, worldIn);
		this.setArrowType(typeIn.getSerializedName());
	}

	/*********************************************************** Data ********************************************************/

	public ArrowType getArrowType() {
		return ArrowType.byName(this.getEntityData().get(ARROW_TYPE));
	}

	public void setArrowType(String arrowTypeName) {
		if (arrowTypeName != null) {
			this.getEntityData().set(ARROW_TYPE, arrowTypeName);
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(ARROW_TYPE, ArrowType.EMPTY.getSerializedName());
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		if (this.getArrowType() != null) {
			compound.putString(ARROW_TYPE_STRING, this.getArrowType().getSerializedName());
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains(ARROW_TYPE_STRING)) {
			this.setArrowType(compound.getString(ARROW_TYPE_STRING));
		}
	}

	/*********************************************************** Arrow Hits ********************************************************/

	/**
	 * Called when this arrow hits a block or entity.
	 */
	protected void onHit(RayTraceResult result) {
		super.onHit(result);
		// Check if this is a Warp Arrow.
		if (this.isArrowType(ArrowType.WARP) && this.level.getBlockState(this.blockPosition()).getFluidState().isEmpty()) {
			this.teleportShooter();
		}
		// Check if this is a Slime Arrow.
		else if (this.isArrowType(ArrowType.SLIME)) {
			this.spawnSmallSlime();
		}
	}

	/**
	 * Called when this projectile hits a LivingEntity.
	 */
	@Override
	protected void doPostHurtEffects(LivingEntity living) {
		super.doPostHurtEffects(living);
		this.hitEntity = true;
		switch (ArrowType.byName(this.getEntityData().get(ARROW_TYPE))) {

		case DIRT:
			this.level.setBlock(new BlockPos(this.getX(), this.getY(), this.getZ()), Blocks.DIRT.defaultBlockState(), BlockFlags.DEFAULT);
			break;
		case EXPLOSION:
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Explosion.Mode.BREAK);
			break;
		case FROST:
			living.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 70, 3));
			break;
		case SLIME:
			living.knockback(2F, 1,1);
			break;
		case FIRE:
			living.setSecondsOnFire(16);
			break;
		case CONCUSSION:
			// add nausea with a duration of 2x the current flight time with a amplification of the flight time /80
			living.addEffect(new EffectInstance(Effects.CONFUSION, MathHelper.clamp(this.flightTime*2,80,240), MathHelper.clamp(this.flightTime/80, 0, 2)));
			break;
		case LASER:
		case WARP:
		case ROPE:
			// Do nothing; Only place ropes when the arrow hits a block.
		case GRASS:
		default:
			break;
		}
	}

	/**
	 * Called when this arrow hits a block.
	 */
	@Override
	protected void onHitBlock(BlockRayTraceResult result) {
		super.onHitBlock(result);
		switch (ArrowType.byName(this.getEntityData().get(ARROW_TYPE))) {

		case DIRT:
			this.setBlockAtArrowFace(Blocks.DIRT.defaultBlockState(), result);
			this.remove();
			break;
		case EXPLOSION:
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Explosion.Mode.BREAK);
			this.remove();
			break;
		case FROST:
			break;
		case GRASS:
			if (this.level.getBlockState(result.getBlockPos()) == Blocks.DIRT.defaultBlockState()) {
				this.setBlockAtArrow(Blocks.GRASS_BLOCK.defaultBlockState(), result);
				this.remove();
			} else if (this.level.getBlockState(result.getBlockPos()) == Blocks.GRASS_BLOCK.defaultBlockState()) {
				this.level.setBlock(result.getBlockPos().above(), Blocks.GRASS.defaultBlockState(), BlockFlags.DEFAULT);
				this.remove();
			}
			break;
		case ROPE:
			this.placeRopes(result);
			break;
		case SLIME:
			break;
		case FIRE:
			// TODO Firing at the side of Blocks doesn't work correctly.
			this.setBlockAtArrowFace(Blocks.FIRE.defaultBlockState(), result);
			break;
		case CONCUSSION:
			break;
		case LASER:
			if (this.hitEntity) {
				this.remove();
			}
		case WARP:
		default:
			break;
		}
	}

	/**
	 * Called very frequently when the arrow exists.
	 */
	public void tick() {
		super.tick();
		this.flightTime++; // Increase the counter for number of ticks spent flying
		if (this.isArrowType(ArrowType.FROST)) {
			freezeNearby(this.level, this.blockPosition());
			if (this.inGround && this.inGroundTime != 0) {
				this.remove();
			}
		}
		// check that the arrow is not in the ground and has been flying for half a second.
		else if (this.isArrowType(ArrowType.LASER) && !this.inGround && this.flightTime > 10) {
			// make the arrows Y position only decrease by 0.05 every tick (1 block per second).
			this.moveTo(this.getX(), this.yo-.0005, this.getZ());
			// if the arrow enters an unloaded chunk, remove it.
			if (!this.level.hasChunk(this.xChunk, this.zChunk)) {
				this.remove();
			}
		}
		// Check that we have entered place rope mode and that the ticks since last placing a rope are at least equal to 10 ticks
		else if (this.isArrowType(ArrowType.ROPE) && this.placeRope) {
			this.ticksSinceRope++; // Increase the counter for number of ticks since last placing a rope
			if (this.ticksSinceRope > 6) {
				BlockPos newPos = currentRopePos.relative(Direction.DOWN, 1);
				if (this.level.getBlockState(newPos).isAir() && this.totalRope < this.maxRopeLength) {
					this.level.setBlock(newPos, ModBlocks.ROPE.get().defaultBlockState().setValue(RopeBlock.FACING, this.hitBlockfaceDirection).setValue(RopeBlock.HAS_ARROW, this.totalRope == 0 ? true : false), BlockFlags.DEFAULT);
					this.currentRopePos = newPos;
					this.totalRope++;
					this.ticksSinceRope = 0;
				}
				else {
					// No space to place more Ropes or Rope Limit reached.
					this.placeRope = false;
					this.remove();
				}
			}
		}
		if (this.level.isClientSide) {
			if (this.inGround) {
				if (this.inGroundTime % 5 == 0) {
					this.spawnArrowParticles(1);
				}
			} else {
				this.spawnArrowParticles(2);

			}
		} else if (this.inGround && this.inGroundTime != 0 && this.inGroundTime >= 600) {
			this.level.broadcastEntityEvent(this, (byte) 0);
		}
	}

	/*********************************************************** Arrow Functionalities ********************************************************/

	@SuppressWarnings("deprecation")
	private void placeRopes(BlockRayTraceResult result) {
		hitBlockfaceDirection = result.getDirection();
		// TODO Temporarily disabled, because there are no models to be placed like this yet.
		if (!hitBlockfaceDirection.equals(Direction.DOWN) && !hitBlockfaceDirection.equals(Direction.UP)) {
			BlockPos hitPos = result.getBlockPos();
			currentRopePos = hitPos.relative(hitBlockfaceDirection);
			BlockState hitBlockState = this.level.getBlockState(hitPos);
			// Check if the block that the arrow hit can hold the Rope.
			if (hitBlockState.isFaceSturdy(this.level, currentRopePos, hitBlockfaceDirection)) {
				// Check if there's space to place a Rope.
				if (this.level.getBlockState(currentRopePos).isAir()) {
					this.level.setBlock(currentRopePos, ModBlocks.ROPE.get().defaultBlockState().setValue(RopeBlock.FACING, hitBlockfaceDirection).setValue(RopeBlock.HAS_ARROW, totalRope == 0 ? true : false), BlockFlags.DEFAULT);
					this.totalRope++;
					this.placeRope = true;
					// Prevent the arrow from being picked up while the ropes are being placed.
					this.pickup = PickupStatus.DISALLOWED;
				}
			}
		}
	}

	/**
	 * Referenced from {@link EnderPearlEntity}
	 */
	private void teleportShooter() {
		// Create teleportation particles.
		for (int i = 0; i < 32; ++i) {
			this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
		}

		Entity shooter = this.getOwner();
		if (!this.level.isClientSide && this.isAlive()) {
			// Check if the shooter is a Player.
			if (shooter instanceof ServerPlayerEntity) {
				ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) shooter;
				if (serverplayerentity.connection.getConnection().isConnected() && serverplayerentity.level == this.level && !serverplayerentity.isSleeping()) {
					net.minecraftforge.event.entity.living.EntityTeleportEvent.EnderPearl event = net.minecraftforge.event.ForgeEventFactory.onEnderPearlLand(serverplayerentity, this.getX(), this.getY(), this.getZ(), EntityType.ENDER_PEARL.create(this.level), 5.0F);
					if (!event.isCanceled()) {
						// Small chance to spawn an Endermite.
						if (this.random.nextFloat() < 0.05F && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
							EndermiteEntity endermiteentity = EntityType.ENDERMITE.create(this.level);
							endermiteentity.setPlayerSpawned(true);
							endermiteentity.moveTo(shooter.getX(), shooter.getY(), shooter.getZ(), shooter.yRot, shooter.xRot);
							this.level.addFreshEntity(endermiteentity);
						}

						if (shooter.isPassenger()) {
							shooter.stopRiding();
						}

						shooter.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
						shooter.fallDistance = 0.0F;
					}
				}
			} else if (shooter != null) {
				shooter.teleportTo(this.getX(), this.getY(), this.getZ());
				shooter.fallDistance = 0.0F;
			}

			this.remove();
		}
	}

	/**
	 * Spawn a small Slime and remove the arrow.
	 */
	private void spawnSmallSlime() {
		if (!this.level.isClientSide && this.isAlive()) {
			if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
				// Slime default size is small so we don't need to set a size.
				SlimeEntity slimeEntity = EntityType.SLIME.create(this.level);
				// slime was spawning with wrong bounding box and too much health, though visually small
				slimeEntity.refreshDimensions();
				slimeEntity.setHealth(1);
				// Set the Slime's position to the center of the block.
				slimeEntity.moveTo(this.blockPosition().getX()+.5f, this.blockPosition().getY() + 0.05f, this.blockPosition().getZ()+.5f, this.yRot, this.xRot);
				this.level.addFreshEntity(slimeEntity);

				// Remove this arrow only if a Slime spawns
				this.remove();
			}
		}
	}

	/**
	 *	function used to set the block 1 off of the face of the block the arrow hit to the given block
	 * @param state (block to change the block the arrow hit into)
	 * @param result (RaytraceResult which is used to find the block the arrow hit)
	 */
	public void setBlockAtArrowFace(BlockState state, BlockRayTraceResult result) {
		Direction face = result.getDirection();
		BlockPos newPos;
		switch (face) {
		case UP:
			newPos = result.getBlockPos().above();
			break;
		case DOWN:
			newPos = result.getBlockPos().below();
			break;
		case NORTH:
			newPos = result.getBlockPos().north();
			break;
		case SOUTH:
			newPos = result.getBlockPos().south();
			break;
		case EAST:
			newPos = result.getBlockPos().east();
			break;
		case WEST:
			newPos = result.getBlockPos().west();
			break;
		default:
			newPos = result.getBlockPos();
		}
		this.level.setBlock(newPos, state, BlockFlags.DEFAULT);
	}

	/**
	 *	Function used only for Grass arrow to change a dirt block into an arrow
	 * @param state (block to change the block the arrow hit into)
	 * @param result (RaytraceResult which is used to find the block the arrow hit)
	 */
	public void setBlockAtArrow(BlockState state, BlockRayTraceResult result) {
		Direction face = result.getDirection();
		BlockPos newPos = result.getBlockPos();
		this.level.setBlock(newPos, state, BlockFlags.DEFAULT);
	}

	/**
	 * rework of freezeNearby from {@link FrostWalkerEnchantment}
	 * @param worldIn current world
	 * @param pos current arrow position
	 */
	public void freezeNearby(World worldIn, BlockPos pos) {
		BlockState blockstate = Blocks.ICE.defaultBlockState();
		float f = (float) Math.min(16, 3);
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

		for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset((double) (-f), 0.0D, (double) (-f)), pos.offset((double) f, 0.0D, (double) f))) {
			if (blockpos.closerThan(this.position(), (double) f)) {
				blockpos$mutable.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
				BlockState blockstate1 = worldIn.getBlockState(blockpos$mutable);
				if (blockstate1.isAir(worldIn, blockpos$mutable)) {
					BlockState blockstate2 = worldIn.getBlockState(blockpos);
					boolean isFull = blockstate2.getBlock() == Blocks.WATER && blockstate2.getValue(FlowingFluidBlock.LEVEL) == 0; //TODO: Forge, modded waters?
					if (blockstate2.getMaterial() == Material.WATER && isFull && blockstate.canSurvive(worldIn, blockpos) && worldIn.isUnobstructed(blockstate, blockpos, ISelectionContext.empty()) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(this, net.minecraftforge.common.util.BlockSnapshot.create(worldIn.dimension(), worldIn, blockpos), net.minecraft.util.Direction.UP)) {
						worldIn.setBlock(blockpos, blockstate, BlockFlags.DEFAULT);
						worldIn.getBlockTicks().scheduleTick(blockpos, Blocks.ICE, MathHelper.nextInt(this.random, 60, 120));
					}
				}
			}
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
					this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.DIRT.defaultBlockState()), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
				}
				break;
			case EXPLOSION:
			case FIRE:
				for (int j = 0; j < particleCount; ++j) {
					this.level.addParticle(ParticleTypes.FLAME, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
				}
				break;
			case FROST:
				for (int j = 0; j < particleCount; ++j) {
					this.level.addParticle(ParticleTypes.DRIPPING_WATER, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
				}
				break;
			case LASER:
				for (int j = 0; j < particleCount; ++j) {
					this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
				}
				break;
			case SLIME:
				for (int j = 0; j < particleCount; ++j) {
					this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState()), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
				}
				break;
			case WARP:
				for (int j = 0; j < particleCount; ++j) {
					this.level.addParticle(ParticleTypes.REVERSE_PORTAL, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
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
	public void setBaseDamage(double damageIn) {
		this.baDamage = damageIn;
	}

	@Override
	public double getBaseDamage() {
		return this.baDamage;
	}

	/**
	 * Returns the correct Item when picking up an arrow.
	 */
	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(ArrowType.getModItemFor(this.getArrowType()));
	}

	/*********************************************************** Networking ********************************************************/

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
