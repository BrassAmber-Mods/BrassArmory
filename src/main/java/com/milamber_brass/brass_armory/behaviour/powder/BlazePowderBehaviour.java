package com.milamber_brass.brass_armory.behaviour.powder;

import com.milamber_brass.brass_armory.behaviour.iGun;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class BlazePowderBehaviour extends GunpowderBehaviour {
    public BlazePowderBehaviour(ParticleOptions particle, Predicate<ItemStack> powderItems) {
        super(particle, powderItems);
    }

    @Override
    public void onShoot(Level level, LivingEntity owner, Entity shooter, ItemStack ammoStack, iGun gun) {
        if (!level.isClientSide) {
            Vec3 ahead = shooter.getEyePosition().add(shooter.getViewVector(1.0F));
            for (Entity entity : level.getEntitiesOfClass(Entity.class, shooter.getBoundingBox().inflate(3D), Entity::isAlive)) {
                if (!entity.equals(shooter) && (entity.position().distanceTo(ahead) < 3.0D || entity.getEyePosition().distanceTo(ahead) < 3.0D)) entity.setSecondsOnFire(3);
            }
        }
        super.onShoot(level, owner, shooter, ammoStack, gun);
    }

    @Override
    public void applyEffect(Entity entity) {
        super.applyEffect(entity);
        entity.setRemainingFireTicks(200);
    }
}
