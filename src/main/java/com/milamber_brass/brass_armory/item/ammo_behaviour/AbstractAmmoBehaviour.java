package com.milamber_brass.brass_armory.item.ammo_behaviour;

import com.milamber_brass.brass_armory.init.BrassArmoryAmmoBehaviours;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractAmmoBehaviour {
    public final int stackShrink;

    public AbstractAmmoBehaviour(int stackShrink) {
        this.stackShrink = stackShrink;
    }

    @Nonnull
    public static Optional<AbstractAmmoBehaviour> getStackBehaviour(ItemStack stack) {
        return BrassArmoryAmmoBehaviours.ammoBehaviours.stream().filter(b -> b.getAmmoItems().test(stack)).findFirst();
    }

    public static boolean itemHasAmmoBehaviour(Item item) {
        return BrassArmoryAmmoBehaviours.ammoBehaviours.stream().anyMatch(b -> b.getAmmoItems().test(item.getDefaultInstance()));
    }

    public abstract boolean onShoot(Level level, LivingEntity livingEntity, ItemStack weaponStack, ItemStack ammoStack, double damage, float accuracy, float speed);

    @ParametersAreNonnullByDefault
    public void onLoad(Level level, LivingEntity livingEntity, ItemStack weaponStack, ItemStack ammoStack) {

    }

    @Nonnull
    public abstract Predicate<ItemStack> getAmmoItems();
}
