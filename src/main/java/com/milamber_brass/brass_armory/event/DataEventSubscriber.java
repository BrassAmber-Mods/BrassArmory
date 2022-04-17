package com.milamber_brass.brass_armory.event;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.data.BrassArmoryRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataEventSubscriber {
    @SubscribeEvent
    @ParametersAreNonnullByDefault
    public static void gatherData(GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        generator.addProvider(new BrassArmoryRecipeProvider(generator));
    }
}