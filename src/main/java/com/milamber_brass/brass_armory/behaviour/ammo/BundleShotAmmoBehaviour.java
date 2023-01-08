package com.milamber_brass.brass_armory.behaviour.ammo;

import com.milamber_brass.brass_armory.behaviour.powder.AbstractPowderBehaviour;
import com.milamber_brass.brass_armory.entity.projectile.BulletEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.behaviour.iGun;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class BundleShotAmmoBehaviour extends AbstractAmmoBehaviour {
    public BundleShotAmmoBehaviour() {
        super();
    }

    @Override
    public boolean onShoot(Level level, LivingEntity owner, Entity shooter, ItemStack ammoStack, @Nullable AbstractPowderBehaviour powderBehaviour, iGun gun) {
        super.onShoot(level, owner, shooter, ammoStack, powderBehaviour, gun);
        int iMax = 10;
        for (int i = 0; i < iMax; i++) {
            BulletEntity bullet = new BulletEntity(level, owner);
            if (powderBehaviour != null) powderBehaviour.applyEffect(bullet);
            if (!level.isClientSide) {
                float speed = gun.speed();
                bullet.setPos(shooter.getEyePosition());
                bullet.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, speed, gun.accuracy());

                bullet.setBaseDamage((gun.damage() / iMax) / speed);
                gun.onShoot(level, owner, shooter, ammoStack, bullet);
                if (!level.addFreshEntity(bullet)) return false;
            }
        }
        return true;
    }

    @NotNull
    @Override
    public Predicate<ItemStack> getAmmoItems() {
        return itemStack -> itemStack.is(BrassArmoryItems.BUNDLE_SHOT.get()) || itemStack.is(Items.GRAVEL);
    }
}
