package com.milamber_brass.brass_armory;

import com.milamber_brass.brass_armory.client.render.BAArrowRenderer;
import com.milamber_brass.brass_armory.client.render.SpearEntityRenderer;
import com.milamber_brass.brass_armory.init.BrassArmoryBlocks;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmoryDispenseBehaviors;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BrassArmory.MOD_ID)
@EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Bus.MOD)
public class BrassArmory {

    public static final String MOD_ID = "brass_armory";
    public static final Logger LOGGER = LogManager.getLogger(BrassArmory.MOD_ID);

    public BrassArmory() {
        // Register
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BrassArmoryItems.register(eventBus);
        BrassArmoryBlocks.register(eventBus);
        BrassArmoryEntityTypes.register(eventBus);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.debug("Running common setup.");

        // Register custom dispenser behavior
        BrassArmoryDispenseBehaviors.init();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.debug("Running client setup.");

        // Register spear and arrow entity rendering handlers
        RenderingRegistry.registerEntityRenderingHandler(BrassArmoryEntityTypes.SPEAR.get(), SpearEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(BrassArmoryEntityTypes.BA_ARROW.get(), BAArrowRenderer::new);
    }

    // Helper method for resource locations
    public static ResourceLocation locate(String name) {
        return new ResourceLocation(BrassArmory.MOD_ID, name);
    }

}
