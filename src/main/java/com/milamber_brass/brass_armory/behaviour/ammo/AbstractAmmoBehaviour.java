package com.milamber_brass.brass_armory.behaviour.ammo;

import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.behaviour.powder.AbstractPowderBehaviour;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public abstract class AbstractAmmoBehaviour {

    public AbstractAmmoBehaviour() {

    }

    public boolean onShoot(Level level, LivingEntity owner, Entity shooter, ItemStack ammoStack, @Nullable AbstractPowderBehaviour powderBehaviour, iGun gun) {
        if (powderBehaviour != null) powderBehaviour.onShoot(level, owner, shooter, ammoStack, gun);
        return true;
    }

    @Nonnull
    public abstract Predicate<ItemStack> getAmmoItems();
}
