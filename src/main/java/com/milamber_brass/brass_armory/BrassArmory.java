package com.milamber_brass.brass_armory;

import com.milamber_brass.brass_armory.init.*;
import com.milamber_brass.brass_armory.util.ArmoryCooldownCache;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(BrassArmory.MOD_ID)
@EventBusSubscriber(modid = BrassArmory.MOD_ID)
public class BrassArmory {

    public static final String MOD_ID = "brass_armory";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BrassArmory(IEventBus eventBus) {
        BrassArmoryDataSerializers.REGISTRY.register(eventBus);
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

        BrassArmoryCapabilities.REGISTRY.register(eventBus);
        BrassArmoryAdvancements.REGISTRY.register(eventBus);
        eventBus.addListener(BrassArmoryPackets::init);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BrassArmoryDispenseBehaviors.init();
            BrassArmoryGunBehaviours.init();
            ArmoryCooldownCache.init();
        });
    }

        public static Identifier locate(String name) {
        return Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, name);
    }
}
