package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.entity.projectile.ArrowType;
import com.milamber_brass.brass_armory.entity.projectile.BAArrowEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombType;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface BrassArmoryDispenseBehaviors extends DispenseItemBehavior {

    static void init() {
        DispenserBlock.registerBehavior(BrassArmoryItems.DIRT_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                AbstractArrow abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.DIRT);
                abstractarrowentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return abstractarrowentity;
            }
        });
        DispenserBlock.registerBehavior(BrassArmoryItems.EX_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                AbstractArrow abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.EXPLOSION);
                abstractarrowentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return abstractarrowentity;
            }
        });
        DispenserBlock.registerBehavior(BrassArmoryItems.FROST_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                AbstractArrow abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.FROST);
                abstractarrowentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return abstractarrowentity;
            }
        });
        DispenserBlock.registerBehavior(BrassArmoryItems.GRASS_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                AbstractArrow abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.GRASS);
                abstractarrowentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return abstractarrowentity;
            }
        });
        DispenserBlock.registerBehavior(BrassArmoryItems.LASER_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                AbstractArrow abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.LASER);
                abstractarrowentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return abstractarrowentity;
            }
        });
        DispenserBlock.registerBehavior(BrassArmoryItems.ROPE_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                AbstractArrow abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.ROPE);
                abstractarrowentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return abstractarrowentity;
            }
        });
        DispenserBlock.registerBehavior(BrassArmoryItems.SLIME_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                AbstractArrow abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.SLIME);
                abstractarrowentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return abstractarrowentity;
            }
        });
        DispenserBlock.registerBehavior(BrassArmoryItems.WARP_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                AbstractArrow abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.WARP);
                abstractarrowentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return abstractarrowentity;
            }
        });
        DispenserBlock.registerBehavior(BrassArmoryItems.FIRE_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                AbstractArrow abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.FIRE);
                abstractarrowentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return abstractarrowentity;
            }
        });
        DispenserBlock.registerBehavior(BrassArmoryItems.CONCUSSION_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                AbstractArrow abstractarrowentity = new BAArrowEntity(worldIn, position.x(), position.y(), position.z(), ArrowType.CONCUSSION);
                abstractarrowentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return abstractarrowentity;
            }
        });

        for (BombType bombType : BombType.values()) {
            DispenserBlock.registerBehavior(BombType.getBombItem(bombType), new AbstractProjectileDispenseBehavior() {
                @Override
                @ParametersAreNonnullByDefault
                protected @NotNull Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                    BombEntity bomb = BombType.vec3BombEntityFromType(bombType, worldIn, position.x(), position.y(), position.z());
                    bomb.setItem(stackIn);
                    return bomb;
                }

                @Override
                protected float getPower() {
                    return 0.5F;
                }
            });
        }
    }

}
