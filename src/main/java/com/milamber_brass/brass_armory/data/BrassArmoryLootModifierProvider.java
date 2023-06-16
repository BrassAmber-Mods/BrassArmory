package com.milamber_brass.brass_armory.data;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.loot.WarpCrystalLootModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class BrassArmoryLootModifierProvider extends GlobalLootModifierProvider {
    public BrassArmoryLootModifierProvider(PackOutput output) {
        super(output, BrassArmory.MOD_ID);
    }

    @Override
    protected void start() {
        add("ender_pearl_warp_crystal", new WarpCrystalLootModifier(new LootItemCondition[]{ LootItemRandomChanceCondition.randomChance(0.5F).build() }, Items.ENDER_PEARL));
        add("ender_eye_warp_crystal", new WarpCrystalLootModifier(new LootItemCondition[]{ LootItemRandomChanceCondition.randomChance(1.0F).build() }, Items.ENDER_EYE));
    }
}
