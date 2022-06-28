package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.container.GunContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrassArmoryMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, BrassArmory.MOD_ID);

    public static final RegistryObject<MenuType<GunContainer>> GUN_MENU = REGISTRY.register("gun_menu", () -> new MenuType<>(GunContainer::new));

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
