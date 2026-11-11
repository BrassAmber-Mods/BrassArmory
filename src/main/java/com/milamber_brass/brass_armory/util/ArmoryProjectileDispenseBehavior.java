package com.milamber_brass.brass_armory.util;

import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public abstract class ArmoryProjectileDispenseBehavior extends DefaultDispenseItemBehavior {
    protected abstract Projectile getProjectile(Level level, Position position, ItemStack stack);
    protected float getPower() { return 1.1F; }
    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        var direction = source.state().getValue(DispenserBlock.FACING);
        var projectile = getProjectile(source.level(), DispenserBlock.getDispensePosition(source), stack);
        projectile.shoot(direction.getStepX(), direction.getStepY() + 0.1F, direction.getStepZ(), getPower(), 6.0F);
        if (projectile instanceof AbstractArrow arrow) arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        source.level().addFreshEntity(projectile);
        stack.shrink(1);
        return stack;
    }
    @Override
    protected void playSound(BlockSource source) {
        source.level().levelEvent(1002, source.pos(), 0);
    }
}
