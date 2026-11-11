package com.milamber_brass.brass_armory.event;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.data.*;
import java.util.Set;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;

@EventBusSubscriber(modid = BrassArmory.MOD_ID)
public class DataEventSubscriber {
    @SubscribeEvent
    public static void gatherRegistries(GatherDataRegistryEntriesEvent event) {
        event.add(Registries.DAMAGE_TYPE, BrassArmoryDamageTypes::bootstrap);
        event.add(new MultiRegistryBootstrap() {
            @Override
            public Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
                return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
            }

            @Override
            public void run(BootstrapGetter registries) {
                new BrassArmoryRecipeProvider(registries.get(Registries.RECIPE), registries.get(Registries.ADVANCEMENT)).buildRecipes();
                BrassAdvancementProvider.bootstrap(registries.get(Registries.ADVANCEMENT));
            }
        });
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        var output = event.getGenerator().getPackOutput();
        var lookup = event.getWorldLookupProvider();
        event.addProvider(new BrassArmoryTags.Blocks(output, lookup));
        event.addProvider(new BrassArmoryTags.Items(output, lookup));
        event.addProvider(new BrassArmoryTags.Entities(output, lookup));
        event.addProvider(new BrassArmoryTags.DamageTypes(output, lookup));
        event.addProvider(new BrassArmoryLootModifierProvider(output, lookup));
        event.addProvider(new BrassArmorySpriteSourceProvider(output, lookup));
        event.addProvider(new BrassArmoryBlockModelBuilder(output));
    }
}
