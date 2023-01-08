package com.milamber_brass.brass_armory.behaviour.ammo;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class EmptyAmmoBehaviour extends AbstractAmmoBehaviour {
    public EmptyAmmoBehaviour() {
        super();
    }

    @NotNull
    @Override
    public Predicate<ItemStack> getAmmoItems() {
        return ItemStack::isEmpty;
    }
}
