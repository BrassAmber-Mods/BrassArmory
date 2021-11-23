package com.milamberBrass.brass_armory;

import com.milamberBrass.brass_armory.blocks.ModBlocks;
import com.milamberBrass.brass_armory.entities.ModEntityTypes;
import com.milamberBrass.brass_armory.entities.dispenser.CustomDispenserBehavior;
import com.milamberBrass.brass_armory.entities.render.BAArrowRenderer;
import com.milamberBrass.brass_armory.entities.render.Spear_Entity_Renderer;
import com.milamberBrass.brass_armory.items.ModItems;
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
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModEntityTypes.register(eventBus);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.debug("Running common setup.");

        // Register custom dispenser behavior
        CustomDispenserBehavior.init();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.debug("Running client setup.");

        // Register spear and arrow entity rendering handlers
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SPEAR.get(), Spear_Entity_Renderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BA_ARROW.get(), BAArrowRenderer::new);
    }

    // Helper method for resource locations
    public static ResourceLocation locate(String name) {
        return new ResourceLocation(BrassArmory.MOD_ID, name);
    }

}
