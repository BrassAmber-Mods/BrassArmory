package com.milamber_brass.brass_armory.behaviour.ammo;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

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
