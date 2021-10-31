package com.milamberBrass.brass_armory.entities.custom;

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
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.network.NetworkHooks;

public class BAArrowEntity extends AbstractArrowEntity {
	private static final DataParameter<String> ARROW_TYPE = EntityDataManager.createKey(BAArrowEntity.class, DataSerializers.STRING);
	private static final String ARROW_TYPE_STRING = "ArrowType";
	private static boolean hitEntity = false;

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
		this.setArrowType(typeIn.getString());
		if (ArrowType.byName(this.getDataManager().get(ARROW_TYPE)) == ArrowType.LASER) {
			this.setPierceLevel((byte) 5);
		}
	}

	/**
	 * Used when fired by a Player.
	 */
	public BAArrowEntity(World worldIn, LivingEntity shooter, ArrowType typeIn) {
		this(ModEntityTypes.BA_ARROW.get(), worldIn, shooter, typeIn);
		if (ArrowType.byName(this.getDataManager().get(ARROW_TYPE)) == ArrowType.LASER) {
			this.setPierceLevel((byte) 5);
		}
	}

	/**
	 * Used for Dispensers in: {@link CustomDispenserBehavior}
	 */
	public BAArrowEntity(World worldIn, double x, double y, double z, ArrowType typeIn) {
		super(ModEntityTypes.BA_ARROW.get(), x, y, z, worldIn);
		this.setArrowType(typeIn.getString());
	}

	/*********************************************************** Data ********************************************************/

	public ArrowType getArrowType() {
		return ArrowType.byName(this.getDataManager().get(ARROW_TYPE));
	}



	public void setArrowType(String arrowTypeName) {
		if (arrowTypeName != null) {
			this.getDataManager().set(ARROW_TYPE, arrowTypeName);
		}
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(ARROW_TYPE, ArrowType.EMPTY.getString());
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (this.getArrowType() != null) {
			compound.putString(ARROW_TYPE_STRING, this.getArrowType().getString());
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		if (compound.contains(ARROW_TYPE_STRING)) {
			this.setArrowType(compound.getString(ARROW_TYPE_STRING));
		}
	}

	/*********************************************************** Arrow Functionality ********************************************************/

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ArrowType.getModItemFor(this.getArrowType()));
	}

	protected void onImpact(RayTraceResult result) {
		super.onImpact(result);
		Entity entity = this.getShooter();

		if (ArrowType.byName(this.getDataManager().get(ARROW_TYPE)) == ArrowType.WARP) {
			for(int i = 0; i < 32; ++i) {
				this.world.addParticle(ParticleTypes.PORTAL, this.getPosX(), this.getPosY() + this.rand.nextDouble() * 2.0D, this.getPosZ(), this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
			}

			if (!this.world.isRemote && !this.removed) {
				if (entity instanceof ServerPlayerEntity) {
					ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entity;
					if (serverplayerentity.connection.getNetworkManager().isChannelOpen() && serverplayerentity.world == this.world && !serverplayerentity.isSleeping()) {
						net.minecraftforge.event.entity.living.EntityTeleportEvent.EnderPearl event = net.minecraftforge.event.ForgeEventFactory.onEnderPearlLand(serverplayerentity, this.getPosX(), this.getPosY(), this.getPosZ(), EntityType.ENDER_PEARL.create(this.world), 5.0F);
						if (!event.isCanceled()) { // Don't indent to lower patch size
							if (this.rand.nextFloat() < 0.05F && this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
								EndermiteEntity endermiteentity = EntityType.ENDERMITE.create(this.world);
								endermiteentity.setSpawnedByPlayer(true);
								endermiteentity.setLocationAndAngles(entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.rotationYaw, entity.rotationPitch);
								this.world.addEntity(endermiteentity);
							}

							if (entity.isPassenger()) {
								entity.stopRiding();
							}

							entity.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
							entity.fallDistance = 0.0F;

						} //Forge: End
					}
				} else if (entity != null) {
					entity.setPositionAndUpdate(this.getPosX(), this.getPosY(), this.getPosZ());
					entity.fallDistance = 0.0F;
				}

				this.remove();
			}
		}



	}

	@Override
	protected void arrowHit(LivingEntity living) {
		super.arrowHit(living);
		this.hitEntity = true;
		switch (ArrowType.byName(this.getDataManager().get(ARROW_TYPE))) {

			case DIRT:
				this.world.setBlockState(new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ()), Blocks.DIRT.getDefaultState());
				break;
			case EXPLOSION:
				this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 2.0F, Explosion.Mode.BREAK);
				break;
			case FROST:
				living.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 2, 3));
				if (living.getType() == EntityType.PLAYER) {
					PlayerEntity player = (PlayerEntity) living;
					player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 2, 3));
				}
				break;
			case SLIME:
				break;
			case FIRE:
				living.setFire(16);
				break;
			case CONCUSSION:
				break;
			case LASER:
			case WARP:
			case ROPE:
			case GRASS:
			default:
				break;
		}
	}

	/**
	 * Called when this projectile hits a Block.
	 */
	@Override
	protected void func_230299_a_(BlockRayTraceResult result) {
		super.func_230299_a_(result);
		switch (ArrowType.byName(this.getDataManager().get(ARROW_TYPE))) {

			case DIRT:
				setBlockAtArrowFace(Blocks.DIRT.getDefaultState(), result);
				this.remove();
				break;
			case EXPLOSION:
				this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 2.0F, Explosion.Mode.BREAK);
				this.remove();
				break;
			case FROST:
				break;
			case GRASS:
				if (this.world.getBlockState(result.getPos()) == Blocks.DIRT.getDefaultState()) {
					setBlockAtArrow(Blocks.GRASS_BLOCK.getDefaultState(), result);
					this.remove();
				} else if (this.world.getBlockState(result.getPos()) == Blocks.GRASS_BLOCK.getDefaultState()) {
					this.world.setBlockState(result.getPos().up(), Blocks.GRASS.getDefaultState());
					this.remove();
				}
				break;
			case ROPE:
				break;
			case SLIME:
				break;
			case FIRE:
				setBlockAtArrowFace(Blocks.FIRE.getDefaultState(), result);
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
	 *	function used to set the block 1 off of the face of the block the arrow hit to the given block
	 * @param state (block to change the block the arrow hit into)
	 * @param result (RaytraceResult which is used to find the block the arrow hit)
	 */
	public void setBlockAtArrowFace(BlockState state, BlockRayTraceResult result) {
		Direction face = result.getFace();
		BlockPos newPos;
		switch (face) {
			case UP:
				newPos = result.getPos().up();
				break;
			case DOWN:
				newPos = result.getPos().down();
				break;
			case NORTH:
				newPos = result.getPos().north();
				break;
			case SOUTH:
				newPos = result.getPos().south();
				break;
			case EAST:
				newPos = result.getPos().east();
				break;
			case WEST:
				newPos = result.getPos().west();
				break;
			default:
				newPos = result.getPos();
		}
		this.world.setBlockState(newPos, state);
	}

	/**
	 *	Function used only for Grass arrow to change a dirt block into an arrow
	 * @param state (block to change the block the arrow hit into)
	 * @param result (RaytraceResult which is used to find the block the arrow hit)
	 */
	public void setBlockAtArrow(BlockState state, BlockRayTraceResult result) {
		Direction face = result.getFace();
		BlockPos newPos = result.getPos();
		this.world.setBlockState(newPos, state);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void tick() {
		super.tick();
		if (ArrowType.byName(this.getDataManager().get(ARROW_TYPE)) == ArrowType.FROST) {
			freezeNearby(this.world, this.getPosition());
			if (this.inGround && this.timeInGround != 0) {
				this.remove();
			}
		}
		if (this.world.isRemote) {
			if (this.inGround) {
				if (this.timeInGround % 5 == 0) {
					this.spawnArrowParticles(1);
				}
			} else {
				this.spawnArrowParticles(2);
			}
		} else if (this.inGround && this.timeInGround != 0 && this.timeInGround >= 600) {
			this.world.setEntityState(this, (byte) 0);
			// Causes crashes.
			// this.dataManager.set(COLOR, -1);
		}

	}

	/**
	 * rework of freezeNearby from {@link FrostWalkerEnchantment}
	 * @param worldIn current world
	 * @param pos current arrow position
	 */
	public void freezeNearby(World worldIn, BlockPos pos) {
		BlockState blockstate = Blocks.ICE.getDefaultState();
		float f = (float)Math.min(16, 3);
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

		for(BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add((double)(-f), 0.0D, (double)(-f)), pos.add((double)f, 0.0D, (double)f))) {
			if (blockpos.withinDistance(this.getPositionVec(), (double)f)) {
				blockpos$mutable.setPos(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
				BlockState blockstate1 = worldIn.getBlockState(blockpos$mutable);
				if (blockstate1.isAir(worldIn, blockpos$mutable)) {
					BlockState blockstate2 = worldIn.getBlockState(blockpos);
					boolean isFull = blockstate2.getBlock() == Blocks.WATER && blockstate2.get(FlowingFluidBlock.LEVEL) == 0; //TODO: Forge, modded waters?
					if (blockstate2.getMaterial() == Material.WATER && isFull && blockstate.isValidPosition(worldIn, blockpos) && worldIn.placedBlockCollides(blockstate, blockpos, ISelectionContext.dummy()) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(this, net.minecraftforge.common.util.BlockSnapshot.create(worldIn.getDimensionKey(), worldIn, blockpos), net.minecraft.util.Direction.UP)) {
						worldIn.setBlockState(blockpos, blockstate);
						worldIn.getPendingBlockTicks().scheduleTick(blockpos, Blocks.ICE, MathHelper.nextInt(this.rand, 60, 120));
					}
				}
			}
		}
	}

	private void spawnArrowParticles(int particleCount) {
		if (particleCount > 0) {
			double d0 = 40.0;
			double d1 = 75.0;
			double d2 = 40.0;
			switch (ArrowType.byName(this.getDataManager().get(ARROW_TYPE))) {

				case DIRT:
				case GRASS:
					for (int j = 0; j < particleCount; ++j) {
						this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.DIRT.getDefaultState()), this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
					}
					break;
				case EXPLOSION:
				case FIRE:
					for (int j = 0; j < particleCount; ++j) {
						this.world.addParticle(ParticleTypes.FLAME, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
					}
					break;
				case FROST:
					for (int j = 0; j < particleCount; ++j) {
						this.world.addParticle(ParticleTypes.DRIPPING_WATER, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
					}
					break;
				case LASER:
					for (int j = 0; j < particleCount; ++j) {
						this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState()), this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
					}
					break;
				case SLIME:
					for (int j = 0; j < particleCount; ++j) {
						this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.getDefaultState()), this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
					}
					break;
				case WARP:
					for (int j = 0; j < particleCount; ++j) {
						this.world.addParticle(ParticleTypes.REVERSE_PORTAL, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
					}
					break;
				case ROPE:
				case CONCUSSION:
				default:
					break;
			}
		}
	}



	/*********************************************************** Networking ********************************************************/

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
