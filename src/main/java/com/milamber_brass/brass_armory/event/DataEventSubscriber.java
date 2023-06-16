package com.milamber_brass.brass_armory.event;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.data.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataEventSubscriber {
    @SubscribeEvent
    @ParametersAreNonnullByDefault
    public static void gatherData(GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = evt.getLookupProvider();
        ExistingFileHelper helper = evt.getExistingFileHelper();

        generator.addProvider(true, new BrassArmoryRecipeProvider(output));
        BlockTagsProvider blockTagsProvider = new BrassArmoryTags.Blocks(output, provider, helper);
        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new BrassArmoryTags.Items(output, provider, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(true, new BrassArmoryTags.Entities(output, provider, helper));
        generator.addProvider(true, new BrassAdvancementProvider(output, provider, helper));
        generator.addProvider(true, new BrassArmoryLootModifierProvider(output));
        generator.addProvider(true, new BrassArmorySpriteSourceProvider(output, helper));
        generator.addProvider(true, new BrassArmoryBlockModelBuilder(output, helper));
        BrassArmoryDamageTypes.addProviders(evt.includeServer(), generator, output, provider, helper);
    }
}