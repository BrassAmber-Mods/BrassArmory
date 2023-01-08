package com.milamber_brass.brass_armory.behaviour.ammo;

import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.behaviour.powder.AbstractPowderBehaviour;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class ArrowAmmoBehaviour extends AbstractAmmoBehaviour {
    public ArrowAmmoBehaviour() {
        super();
    }

    @Override
    public boolean onShoot(Level level, LivingEntity owner, Entity shooter, ItemStack ammoStack, @Nullable AbstractPowderBehaviour powderBehaviour, iGun gun) {
        super.onShoot(level, owner, shooter, ammoStack, powderBehaviour, gun);
        AbstractArrow abstractarrow = ((ArrowItem)(ammoStack.getItem() instanceof ArrowItem ? ammoStack.getItem() : Items.ARROW)).createArrow(level, ammoStack, owner);
        if (powderBehaviour != null) powderBehaviour.applyEffect(abstractarrow);
        if (!level.isClientSide) {
            float speed = gun.speed();
            abstractarrow.setPos(shooter.getEyePosition());
            abstractarrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, speed * 3.0F, gun.accuracy());
            abstractarrow.setCritArrow(true);

            if (owner instanceof Player player && player.getAbilities().instabuild && (ammoStack.is(Items.SPECTRAL_ARROW) || ammoStack.is(Items.TIPPED_ARROW))) {
                abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            abstractarrow.setBaseDamage(gun.damage() / speed);
            gun.onShoot(level, owner, shooter, ammoStack, abstractarrow);
            return level.addFreshEntity(abstractarrow);
        }
        return true;
    }

    @NotNull
    @Override
    public Predicate<ItemStack> getAmmoItems() {
        return itemStack -> itemStack.getItem() instanceof ArrowItem;
    }
}
