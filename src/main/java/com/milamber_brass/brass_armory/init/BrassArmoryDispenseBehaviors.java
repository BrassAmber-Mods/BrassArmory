package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.entity.projectile.SpikyBallEntity;
import com.milamber_brass.brass_armory.entity.projectile.arrow.*;
import com.milamber_brass.brass_armory.entity.projectile.cannon_balls.CannonBallEntity;
import com.milamber_brass.brass_armory.entity.projectile.cannon_balls.CarcassRoundEntity;
import com.milamber_brass.brass_armory.entity.projectile.cannon_balls.SiegeRoundEntity;
import com.milamber_brass.brass_armory.item.BombItem;
import com.milamber_brass.brass_armory.item.SpecialArrowItem;
import com.milamber_brass.brass_armory.item.SpikyBallItem;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface BrassArmoryDispenseBehaviors extends DispenseItemBehavior {

    static void init() {
        for (SpecialArrowItem arrowItem : new SpecialArrowItem[] {
                BrassArmoryItems.DIRT_ARROW.get(), BrassArmoryItems.EX_ARROW.get(),
                BrassArmoryItems.FROST_ARROW.get(), BrassArmoryItems.GRASS_ARROW.get(),
                BrassArmoryItems.LASER_ARROW.get(), BrassArmoryItems.ROPE_ARROW.get(),
                BrassArmoryItems.SLIME_ARROW.get(), BrassArmoryItems.WARP_ARROW.get(),
                BrassArmoryItems.FIRE_ARROW.get(), BrassArmoryItems.CONFUSION_ARROW.get()
        }) {
            DispenserBlock.registerBehavior(arrowItem, new AbstractProjectileDispenseBehavior() {
                @Override
                protected @NotNull Projectile getProjectile(Level level, Position pos, ItemStack stack) {
                    return arrowItem.newPosArrowFunction.apply(level, pos.x(), pos.y(), pos.z());
                }
            });
        }

        DispenserBlock.registerBehavior(BrassArmoryItems.TORCH_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected @NotNull Projectile getProjectile(Level level, Position pos, ItemStack stack) {
                return Util.make(new TorchArrowEntity(level, pos.x(), pos.y(), pos.z()), torchArrow -> torchArrow.setTorch(stack.getOrCreateTag()));
            }
        });

        for (SpikyBallItem spikyBall : new SpikyBallItem[] {
                BrassArmoryItems.WOODEN_SPIKY_BALL.get(), BrassArmoryItems.STONE_SPIKY_BALL.get(),
                BrassArmoryItems.IRON_SPIKY_BALL.get(), BrassArmoryItems.GOLDEN_SPIKY_BALL.get(),
                BrassArmoryItems.DIAMOND_SPIKY_BALL.get(), BrassArmoryItems.NETHERITE_SPIKY_BALL.get()
        }) {
            DispenserBlock.registerBehavior(spikyBall, new AbstractProjectileDispenseBehavior() {
                @Override
                protected @NotNull Projectile getProjectile(Level level, Position pos, ItemStack stack) {
                    return Util.make(new SpikyBallEntity(level, pos.x(), pos.y(), pos.z()), spikyBallEntity -> spikyBallEntity.setItem(stack));
                }

                @Override
                protected float getPower() {
                    return 0.5F;
                }
            });
        }

        for (BombItem bomb : new BombItem[] { BrassArmoryItems.BOMB.get(), BrassArmoryItems.BOUNCY_BOMB.get(), BrassArmoryItems.STICKY_BOMB.get() }) {
            DispenserBlock.registerBehavior(bomb, new AbstractProjectileDispenseBehavior() {
                @Override
                protected @NotNull Projectile getProjectile(Level level, Position pos, ItemStack stack) {
                    return Util.make(bomb.newPosBombFunction.apply(level, pos.x(), pos.y(), pos.z()), spikyBallEntity -> spikyBallEntity.setItem(stack));
                }

                @Override
                protected float getPower() {
                    return 0.5F;
                }
            });
        }

        DispenserBlock.registerBehavior(BrassArmoryItems.CANNON_BALL.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected @NotNull Projectile getProjectile(Level level, Position pos, ItemStack stack) {
                return Util.make(new CannonBallEntity(pos.x(), pos.y(), pos.z(), level), cannonBall -> cannonBall.setItem(stack));
            }

            @Override
            protected float getPower() {
                return 0.1F;
            }
        });

        DispenserBlock.registerBehavior(BrassArmoryItems.CARCASS_ROUND.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected @NotNull Projectile getProjectile(Level level, Position pos, ItemStack stack) {
                return Util.make(new CarcassRoundEntity(pos.x(), pos.y(), pos.z(), level), carcassRound -> carcassRound.setItem(stack));
            }

            @Override
            protected float getPower() {
                return 0.1F;
            }
        });

        DispenserBlock.registerBehavior(BrassArmoryItems.SIEGE_ROUND.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected @NotNull Projectile getProjectile(Level level, Position pos, ItemStack stack) {
                return Util.make(new SiegeRoundEntity(pos.x(), pos.y(), pos.z(), level), siegeRound -> siegeRound.setItem(stack));
            }

            @Override
            protected float getPower() {
                return 0.1F;
            }
        });
    }
}
