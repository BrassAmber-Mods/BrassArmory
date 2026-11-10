package com.milamber_brass.brass_armory.behaviour.ammo;

import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

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
