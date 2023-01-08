package com.milamber_brass.brass_armory.behaviour;

import com.google.common.collect.Maps;
import com.milamber_brass.brass_armory.behaviour.ammo.AbstractAmmoBehaviour;
import com.milamber_brass.brass_armory.behaviour.powder.AbstractPowderBehaviour;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class GunBehaviours {

    private static final Map<ResourceLocation, AbstractAmmoBehaviour> AMMO_BEHAVIOURS = Maps.newLinkedHashMap();

    public static void registerAmmoBehavior(ResourceLocation resourceLocation, AbstractAmmoBehaviour ammoBehaviour) {
        AMMO_BEHAVIOURS.put(resourceLocation, ammoBehaviour);

    }

    @Nonnull
    public static Optional<AbstractAmmoBehaviour> getAmmoBehavior(ItemStack stack) {
        return AMMO_BEHAVIOURS.values().stream().filter(b -> b.getAmmoItems().test(stack)).findFirst();
    }

    public static boolean itemHasAmmoBehaviour(Item item) {
        return AMMO_BEHAVIOURS.values().stream().anyMatch(b -> b.getAmmoItems().test(item.getDefaultInstance()));
    }

    public static Map<ResourceLocation, AbstractAmmoBehaviour> getAmmoBehaviours() {
        return AMMO_BEHAVIOURS;
    }

    private static final Map<ResourceLocation, AbstractPowderBehaviour> POWDER_BEHAVIOURS = Maps.newLinkedHashMap();

    public static void registerPowderBehaviour(ResourceLocation resourceLocation, AbstractPowderBehaviour powderBehaviour) {
        POWDER_BEHAVIOURS.put(resourceLocation, powderBehaviour);
    }

    @Nonnull
    public static Optional<AbstractPowderBehaviour> getPowderBehaviour(ItemStack stack) {
        return POWDER_BEHAVIOURS.values().stream().filter(b -> b.getPowderItems().test(stack)).findFirst();
    }

    public static boolean itemHasPowderBehaviour(Item item) {
        return POWDER_BEHAVIOURS.values().stream().anyMatch(b -> b.getPowderItems().test(item.getDefaultInstance()));
    }

    public static Map<ResourceLocation, AbstractPowderBehaviour> getPowderBehaviours() {
        return POWDER_BEHAVIOURS;
    }

    public static Optional<ResourceLocation> resourceLocationByPowderBehaviour(AbstractPowderBehaviour powderBehaviour) {
        for (Map.Entry<ResourceLocation, AbstractPowderBehaviour> entry : POWDER_BEHAVIOURS.entrySet()) {
            if (entry.getValue().equals(powderBehaviour)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
}
