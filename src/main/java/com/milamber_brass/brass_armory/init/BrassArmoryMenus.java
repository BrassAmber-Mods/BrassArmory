package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.inventory.GunContainer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrassArmoryMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, BrassArmory.MOD_ID);

    public static final RegistryObject<MenuType<GunContainer<iGun>>> GUN_MENU = REGISTRY.register("gun_menu", () -> new MenuType<>(GunContainer::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
