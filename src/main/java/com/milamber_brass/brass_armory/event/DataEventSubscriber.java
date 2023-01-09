package com.milamber_brass.brass_armory.event;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.data.BrassAdvancementProvider;
import com.milamber_brass.brass_armory.data.BrassArmoryRecipeProvider;
import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataEventSubscriber {
    @SubscribeEvent
    @ParametersAreNonnullByDefault
    public static void gatherData(GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        ExistingFileHelper helper = evt.getExistingFileHelper();

        generator.addProvider(true, new BrassArmoryRecipeProvider(generator));
        BlockTagsProvider blockTagsProvider = new BrassArmoryTags.Blocks(generator, BrassArmory.MOD_ID, helper);
        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new BrassArmoryTags.Items(generator, blockTagsProvider, BrassArmory.MOD_ID, helper));
        generator.addProvider(true, new BrassArmoryTags.Entities(generator, BrassArmory.MOD_ID, helper));
        generator.addProvider(true, new BrassAdvancementProvider(generator, helper));
    }
}