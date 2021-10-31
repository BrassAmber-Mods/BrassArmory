package com.milamberBrass.brass_armory.entities.dispenser;

import com.milamberBrass.brass_armory.entities.custom.BAArrowEntity;
import com.milamberBrass.brass_armory.items.ModItems;
import com.milamberBrass.brass_armory.util.ArrowType;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface CustomDispenserBehavior extends IDispenseItemBehavior {

	static void init() {
		DispenserBlock.registerDispenseBehavior(ModItems.DIRT_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.getX(), position.getY(), position.getZ(), ArrowType.DIRT);
				abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(ModItems.EX_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.getX(), position.getY(), position.getZ(), ArrowType.EXPLOSION);
				abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(ModItems.FROST_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.getX(), position.getY(), position.getZ(), ArrowType.FROST);
				abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(ModItems.GRASS_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.getX(), position.getY(), position.getZ(), ArrowType.GRASS);
				abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(ModItems.LASER_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.getX(), position.getY(), position.getZ(), ArrowType.LASER);
				abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(ModItems.ROPE_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.getX(), position.getY(), position.getZ(), ArrowType.ROPE);
				abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(ModItems.SLIME_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.getX(), position.getY(), position.getZ(), ArrowType.SLIME);
				abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(ModItems.WARP_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.getX(), position.getY(), position.getZ(), ArrowType.WARP);
				abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
	}
}
