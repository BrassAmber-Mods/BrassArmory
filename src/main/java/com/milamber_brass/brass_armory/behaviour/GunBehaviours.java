package com.milamber_brass.brass_armory.behaviour;

import com.google.common.collect.Maps;
import com.milamber_brass.brass_armory.behaviour.ammo.AbstractAmmoBehaviour;
import com.milamber_brass.brass_armory.behaviour.powder.AbstractPowderBehaviour;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GunBehaviours {

    private static final Map<Identifier, AbstractAmmoBehaviour> AMMO_BEHAVIOURS = Maps.newLinkedHashMap();

    public static void registerAmmoBehavior(Identifier resourceLocation, AbstractAmmoBehaviour ammoBehaviour) {
        AMMO_BEHAVIOURS.put(resourceLocation, ammoBehaviour);
    }

        public static Optional<AbstractAmmoBehaviour> getAmmoBehavior(ItemStack stack) {
        return AMMO_BEHAVIOURS.values().stream().filter(b -> b.getAmmoItems().test(stack)).findFirst();
    }

    public static boolean itemHasAmmoBehaviour(Item item) {
        return AMMO_BEHAVIOURS.values().stream().anyMatch(b -> b.getAmmoItems().test(item.getDefaultInstance()));
    }

    public static Map<Identifier, AbstractAmmoBehaviour> getAmmoBehaviours() {
        return AMMO_BEHAVIOURS;
    }

    private static final Map<Identifier, AbstractPowderBehaviour> POWDER_BEHAVIOURS = Maps.newLinkedHashMap();

    public static void registerPowderBehaviour(Identifier resourceLocation, AbstractPowderBehaviour powderBehaviour) {
        POWDER_BEHAVIOURS.put(resourceLocation, powderBehaviour);
    }

        public static Optional<AbstractPowderBehaviour> getPowderBehaviour(ItemStack stack) {
        return POWDER_BEHAVIOURS.values().stream().filter(b -> b.getPowderItems().test(stack)).findFirst();
    }

    public static boolean itemHasPowderBehaviour(Item item) {
        return POWDER_BEHAVIOURS.values().stream().anyMatch(b -> b.getPowderItems().test(item.getDefaultInstance()));
    }

    public static Map<Identifier, AbstractPowderBehaviour> getPowderBehaviours() {
        return POWDER_BEHAVIOURS;
    }

    public static Optional<Identifier> resourceLocationByPowderBehaviour(AbstractPowderBehaviour powderBehaviour) {
        for (Map.Entry<Identifier, AbstractPowderBehaviour> entry : POWDER_BEHAVIOURS.entrySet()) {
            if (entry.getValue().equals(powderBehaviour)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
}
