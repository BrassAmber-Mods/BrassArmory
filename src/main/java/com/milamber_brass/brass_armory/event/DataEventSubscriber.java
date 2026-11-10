package com.milamber_brass.brass_armory.event;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.data.*;
import java.util.concurrent.CompletableFuture;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataEventSubscriber {
    @SubscribeEvent
    @ParametersAreNonnullByDefault
    public static void gatherData(GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = evt.getLookupProvider();
        ExistingFileHelper helper = evt.getExistingFileHelper();

        generator.addProvider(evt.includeServer(), new BrassArmoryRecipeProvider(output, provider));
        BlockTagsProvider blockTagsProvider = new BrassArmoryTags.Blocks(output, provider, helper);
        generator.addProvider(evt.includeServer(), blockTagsProvider);
        generator.addProvider(evt.includeServer(), new BrassArmoryTags.Items(output, provider, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(evt.includeServer(), new BrassArmoryTags.Entities(output, provider, helper));
        generator.addProvider(evt.includeServer(), new BrassAdvancementProvider(output, provider, helper));
        generator.addProvider(evt.includeServer(), new BrassArmoryLootModifierProvider(output, provider));
        generator.addProvider(evt.includeClient(), new BrassArmorySpriteSourceProvider(output, provider, helper));
        generator.addProvider(evt.includeClient(), new BrassArmoryBlockModelBuilder(output, helper));
        BrassArmoryDamageTypes.addProviders(evt.includeServer(), generator, output, provider, helper);
    }
}