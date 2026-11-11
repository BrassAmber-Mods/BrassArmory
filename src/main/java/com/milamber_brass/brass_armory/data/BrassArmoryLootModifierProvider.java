package com.milamber_brass.brass_armory.data;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.loot.WarpCrystalLootModifier;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

public class BrassArmoryLootModifierProvider extends GlobalLootModifierProvider {
    public BrassArmoryLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BrassArmory.MOD_ID);
    }

    @Override
    protected void start() {
        add("ender_pearl_warp_crystal", new WarpCrystalLootModifier(java.util.Optional.of(net.minecraft.core.Holder.direct(LootItemRandomChanceCondition.randomChance(0.5F).build())), 0, Items.ENDER_PEARL));
        add("ender_eye_warp_crystal", new WarpCrystalLootModifier(java.util.Optional.of(net.minecraft.core.Holder.direct(LootItemRandomChanceCondition.randomChance(1.0F).build())), 0, Items.ENDER_EYE));
    }
}
