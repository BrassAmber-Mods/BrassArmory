package com.milamber_brass.brass_armory;

import com.milamber_brass.brass_armory.init.*;
import com.milamber_brass.brass_armory.util.ArmoryCooldownCache;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
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
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, BrassArmoryCapabilities::addCapabilities);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BrassArmoryBlocks.register(eventBus);
        BrassArmoryItems.register(eventBus);
        BrassArmoryEntityTypes.register(eventBus);
        BrassArmorySounds.register(eventBus);
        BrassArmoryMenus.register(eventBus);
        BrassArmoryEffects.register(eventBus);
        BrassArmoryParticles.register(eventBus);
        BrassArmoryRecipes.register(eventBus);
        BrassArmoryLootModifiers.register(eventBus);
        BrassArmoryItemGroups.register(eventBus);

        eventBus.addListener(BrassArmoryCapabilities::capabilitySetup);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        BrassArmoryDispenseBehaviors.init();
        BrassArmoryGunBehaviours.init();
        BrassArmoryAdvancements.init();
        ArmoryCooldownCache.init();
        BrassArmoryPackets.init();
    }

    @Nonnull
    public static ResourceLocation locate(String name) {
        return new ResourceLocation(BrassArmory.MOD_ID, name);
    }
}
