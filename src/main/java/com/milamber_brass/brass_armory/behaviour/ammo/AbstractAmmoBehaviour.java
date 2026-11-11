package com.milamber_brass.brass_armory.behaviour.ammo;

import org.jetbrains.annotations.Nullable;
import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.behaviour.powder.AbstractPowderBehaviour;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AbstractAmmoBehaviour {

    public AbstractAmmoBehaviour() {

    }

    public boolean onShoot(Level level, LivingEntity owner, Entity shooter, ItemStack ammoStack, @Nullable AbstractPowderBehaviour powderBehaviour, iGun gun) {
        if (powderBehaviour != null) powderBehaviour.onShoot(level, owner, shooter, ammoStack, gun);
        return true;
    }

        public abstract Predicate<ItemStack> getAmmoItems();
}
