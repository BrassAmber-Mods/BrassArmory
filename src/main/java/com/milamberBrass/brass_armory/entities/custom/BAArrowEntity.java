package com.milamberBrass.brass_armory.entities.custom;

import com.milamberBrass.brass_armory.entities.ModEntityTypes;
import com.milamberBrass.brass_armory.entities.dispenser.CustomDispenserBehavior;
import com.milamberBrass.brass_armory.util.ArrowType;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BAArrowEntity extends AbstractArrowEntity {
	private static final DataParameter<String> ARROW_TYPE = EntityDataManager.createKey(BAArrowEntity.class, DataSerializers.STRING);
	private static final String ARROW_TYPE_STRING = "ArrowType";

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
	}

	/**
	 * Used when fired by a Player.
	 */
	public BAArrowEntity(World worldIn, LivingEntity shooter, ArrowType typeIn) {
		this(ModEntityTypes.BA_ARROW.get(), worldIn, shooter, typeIn);
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

	@Override
	protected void arrowHit(LivingEntity living) {
		super.arrowHit(living);
		if (this.getArrowType() == ArrowType.FROST) {
			living.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 2, 3));
		}

	}

	/**
	 * Called when this projectile hits a Block.
	 */
	@Override
	protected void func_230299_a_(BlockRayTraceResult result) {
		super.func_230299_a_(result);
		if (this.getArrowType() == ArrowType.DIRT) {
			this.world.setBlockState(new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ()), Blocks.DIRT.getDefaultState());
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void tick() {
		super.tick();
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

	private void spawnArrowParticles(int particleCount) {
		if (particleCount > 0) {
			double d0 = 100.0;
			double d1 = 200.0;
			double d2 = 100.0;

			for (int j = 0; j < particleCount; ++j) {
				this.world.addParticle(ParticleTypes.FLAME, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
			}

		}
	}

	/*********************************************************** Networking ********************************************************/

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
