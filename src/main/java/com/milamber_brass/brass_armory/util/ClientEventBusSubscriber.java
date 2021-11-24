package com.milamber_brass.brass_armory.util;


import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.entities.render.SpearEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(BrassArmoryEntityTypes.SPEAR.get(), SpearEntityRenderer::new);
    }

}
