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
		DispenserBlock.registerBehavior(ModItems.DIRT_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectile(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.DIRT);
				abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerBehavior(ModItems.EX_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectile(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.EXPLOSION);
				abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerBehavior(ModItems.FROST_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectile(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.FROST);
				abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerBehavior(ModItems.GRASS_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectile(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.GRASS);
				abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerBehavior(ModItems.LASER_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectile(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.LASER);
				abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerBehavior(ModItems.ROPE_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectile(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.ROPE);
				abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerBehavior(ModItems.SLIME_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectile(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.SLIME);
				abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerBehavior(ModItems.WARP_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectile(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.WARP);
				abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerBehavior(ModItems.FIRE_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectile(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.FIRE);
				abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
		DispenserBlock.registerBehavior(ModItems.CONCUSSION_ARROW.get(), new ProjectileDispenseBehavior() {
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			@Override
			protected ProjectileEntity getProjectile(World worldIn, IPosition position, ItemStack stackIn) {
				AbstractArrowEntity abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.CONCUSSION);
				abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
				return abstractarrowentity;
			}
		});
	}
}
