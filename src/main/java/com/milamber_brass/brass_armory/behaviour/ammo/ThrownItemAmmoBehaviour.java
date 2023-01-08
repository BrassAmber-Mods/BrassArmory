package com.milamber_brass.brass_armory.behaviour.ammo;

import com.milamber_brass.brass_armory.behaviour.powder.AbstractPowderBehaviour;
import com.milamber_brass.brass_armory.behaviour.iGun;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class ThrownItemAmmoBehaviour extends AbstractAmmoBehaviour {
    protected final BiFunction<Level, LivingEntity, ThrowableItemProjectile> newProjectileFunction;
    protected final Predicate<ItemStack> ammoItems;

    public ThrownItemAmmoBehaviour(Predicate<ItemStack> ammoItems, BiFunction<Level, LivingEntity, ThrowableItemProjectile> newProjectileFunction) {
        super();
        this.ammoItems = ammoItems;
        this.newProjectileFunction = newProjectileFunction;
    }

    @Override
    public boolean onShoot(Level level, LivingEntity owner, Entity shooter, ItemStack ammoStack, @Nullable AbstractPowderBehaviour powderBehaviour, iGun gun) {
        super.onShoot(level, owner, shooter, ammoStack, powderBehaviour, gun);
        ThrowableItemProjectile thrownItem = this.newProjectileFunction.apply(level, owner);
        if (powderBehaviour != null) powderBehaviour.applyEffect(thrownItem);
        if (!level.isClientSide) {
            thrownItem.setItem(ammoStack);
            float speed = gun.speed();
            thrownItem.setPos(shooter.getEyePosition());
            thrownItem.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, speed * 0.5F, gun.accuracy());

            gun.onShoot(level, owner, shooter, ammoStack, thrownItem);
            return level.addFreshEntity(thrownItem);
        }
        return true;
    }

    @NotNull
    @Override
    public Predicate<ItemStack> getAmmoItems() {
        return this.ammoItems;
    }
}
