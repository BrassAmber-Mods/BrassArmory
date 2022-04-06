package com.milamber_brass.brass_armory.item.ammo_behaviour;

import com.milamber_brass.brass_armory.entity.projectile.BulletEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.abstracts.AbstractGunItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

public class MusketBallAmmoBehaviour extends AbstractAmmoBehaviour {
    public MusketBallAmmoBehaviour() {
        super(1);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onShoot(Level level, LivingEntity livingEntity, ItemStack weaponStack, ItemStack ammoStack, double damage, float accuracy, float speed) {
        BulletEntity bullet = new BulletEntity(livingEntity, level);
        bullet.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), 0.0F, speed, accuracy);
        if (AbstractGunItem.loadStack(weaponStack, "BAPowder").stream().anyMatch(i -> i.getItem().equals(Items.FIRE_CHARGE))) bullet.setRemainingFireTicks(200);
        bullet.setBaseDamage(damage / speed);
        return level.addFreshEntity(bullet);
    }

    @NotNull
    @Override
    public Predicate<ItemStack> getAmmoItems() {
        return itemStack -> itemStack.is(BrassArmoryItems.MUSKET_BALL.get());
    }
}
