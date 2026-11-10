package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.inventory.GunContainer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BrassArmoryMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.MENU, BrassArmory.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<GunContainer<iGun>>> GUN_MENU = REGISTRY.register("gun_menu", () -> new MenuType<>(GunContainer::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
