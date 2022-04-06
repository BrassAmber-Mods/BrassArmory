package com.milamber_brass.brass_armory;

import com.milamber_brass.brass_armory.event.ClientEventBusSubscriber;
import com.milamber_brass.brass_armory.init.*;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

@Mod(BrassArmory.MOD_ID)
@EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Bus.MOD)
public class BrassArmory {

    public static final String MOD_ID = "brass_armory";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BrassArmory() {
        // Register
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        //TODO: Move block renderers into the client package and their registration too!
        BrassArmoryBlocks.register(eventBus);
        BrassArmoryItems.register(eventBus);
        BrassArmoryEntityTypes.register(eventBus);
        BrassArmorySounds.register(eventBus);
        BrassArmoryMenus.REGISTRY.register(eventBus);
        BrassArmoryAmmoBehaviours.register();
        eventBus.addListener(ClientEventBusSubscriber::clientSetup);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.debug("Running common setup.");
        // Register custom dispenser behavior
        BrassArmoryDispenseBehaviors.init();
    }

    // Helper method for resource locations
    @Nonnull
    public static ResourceLocation locate(String name) {
        return new ResourceLocation(BrassArmory.MOD_ID, name);
    }

}
