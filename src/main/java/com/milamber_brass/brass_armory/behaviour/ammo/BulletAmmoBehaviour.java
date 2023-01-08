package com.milamber_brass.brass_armory.behaviour.ammo;

import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.behaviour.powder.AbstractPowderBehaviour;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractBulletEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class BulletAmmoBehaviour extends AbstractAmmoBehaviour {
    protected final BiFunction<Level, LivingEntity, AbstractBulletEntity> newProjectileFunction;
    protected final Predicate<ItemStack> ammoItems;

    public BulletAmmoBehaviour(Predicate<ItemStack> ammoItems, BiFunction<Level, LivingEntity, AbstractBulletEntity> newProjectileFunction) {
        super();
        this.ammoItems = ammoItems;
        this.newProjectileFunction = newProjectileFunction;
    }

    @Override
    public boolean onShoot(Level level, LivingEntity owner, Entity shooter, ItemStack ammoStack, @Nullable AbstractPowderBehaviour powderBehaviour, iGun gun) {
        super.onShoot(level, owner, shooter, ammoStack, powderBehaviour, gun);
        AbstractBulletEntity bullet = this.newProjectileFunction.apply(level, owner);
        bullet.setItem(ammoStack);
        if (powderBehaviour != null) powderBehaviour.applyEffect(bullet);
        if (!level.isClientSide) {
            float speed = gun.speed();
            bullet.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, speed, gun.accuracy());
            bullet.setPos(shooter.getEyePosition());

            bullet.setBaseDamage(gun.damage() / speed);
            gun.onShoot(level, owner, shooter, ammoStack, bullet);
            return level.addFreshEntity(bullet);
        }
        return true;
    }

    @NotNull
    @Override
    public Predicate<ItemStack> getAmmoItems() {
        return this.ammoItems;
    }
}
