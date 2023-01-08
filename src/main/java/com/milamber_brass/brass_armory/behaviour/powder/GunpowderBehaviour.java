package com.milamber_brass.brass_armory.behaviour.powder;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class GunpowderBehaviour extends AbstractPowderBehaviour {
    private final Predicate<ItemStack> powderItems;

    public GunpowderBehaviour(ParticleOptions particle, Predicate<ItemStack> powderItems) {
        super(particle);
        this.powderItems = powderItems;
    }

    @NotNull
    @Override
    public Predicate<ItemStack> getPowderItems() {
        return this.powderItems;
    }
}
