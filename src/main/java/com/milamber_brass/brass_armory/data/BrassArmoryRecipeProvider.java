package com.milamber_brass.brass_armory.data;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.recipe.CarcassRoundRecipe;
import com.milamber_brass.brass_armory.recipe.TorchArrowRecipe;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.advancements.Advancement;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

public class BrassArmoryRecipeProvider extends RecipeProvider {

    public BrassArmoryRecipeProvider(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements, BrassArmory.MOD_ID);
    }

    @Override
    public void buildRecipes() {
        RecipeOutput consumer = this.output;
        //BOOMERANGS
        boomerangRecipe(consumer, Tags.Items.GEMS_DIAMOND, Tags.Items.INGOTS_GOLD, BrassArmoryItems.DIAMOND_BOOMERANG.get());
        boomerangRecipe(consumer, Tags.Items.INGOTS_GOLD, Tags.Items.INGOTS_IRON, BrassArmoryItems.GOLDEN_BOOMERANG.get());
        boomerangRecipe(consumer, Tags.Items.INGOTS_IRON, ItemTags.STONE_TOOL_MATERIALS, BrassArmoryItems.IRON_BOOMERANG.get());
        boomerangRecipe(consumer, ItemTags.STONE_TOOL_MATERIALS, ItemTags.PLANKS, BrassArmoryItems.STONE_BOOMERANG.get());
        boomerangRecipe(consumer, ItemTags.PLANKS, Tags.Items.RODS_WOODEN,  BrassArmoryItems.WOODEN_BOOMERANG.get());
        netheriteRecipe(consumer, BrassArmoryItems.DIAMOND_BOOMERANG.get(), BrassArmoryItems.NETHERITE_BOOMERANG.get());

        //SPEARS
        spearRecipe(consumer, BrassArmoryItems.DIAMOND_DAGGER.get(), BrassArmoryItems.DIAMOND_SPEAR.get());
        spearRecipe(consumer, BrassArmoryItems.GOLDEN_DAGGER.get(), BrassArmoryItems.GOLDEN_SPEAR.get());
        spearRecipe(consumer, BrassArmoryItems.IRON_DAGGER.get(), BrassArmoryItems.IRON_SPEAR.get());
        spearRecipe(consumer, BrassArmoryItems.STONE_DAGGER.get(), BrassArmoryItems.STONE_SPEAR.get());
        spearRecipe(consumer, BrassArmoryItems.WOODEN_DAGGER.get(), BrassArmoryItems.WOODEN_SPEAR.get());
        netheriteRecipe(consumer, BrassArmoryItems.DIAMOND_SPEAR.get(), BrassArmoryItems.NETHERITE_SPEAR.get());

        shaped(RecipeCategory.COMBAT, BrassArmoryItems.FIRE_ROD.get())
                .pattern("T  ")
                .pattern(" S ")
                .pattern("  S")
                .define('T', Ingredient.of(Items.COAL, Items.CHARCOAL))
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_coal", has(Items.COAL))
                .unlockedBy("has_charcoal", has(Items.CHARCOAL))
                .save(consumer, shapedLocate(BrassArmoryItems.FIRE_ROD.get()));

        //DAGGERS
        daggerRecipe(consumer, Tags.Items.GEMS_DIAMOND, BrassArmoryItems.DIAMOND_DAGGER.get());
        daggerRecipe(consumer, Tags.Items.INGOTS_GOLD, BrassArmoryItems.GOLDEN_DAGGER.get());
        daggerRecipe(consumer, Tags.Items.INGOTS_IRON, BrassArmoryItems.IRON_DAGGER.get());
        daggerRecipe(consumer, ItemTags.STONE_TOOL_MATERIALS, BrassArmoryItems.STONE_DAGGER.get());
        daggerRecipe(consumer, ItemTags.PLANKS, BrassArmoryItems.WOODEN_DAGGER.get());
        netheriteRecipe(consumer, BrassArmoryItems.DIAMOND_DAGGER.get(), BrassArmoryItems.NETHERITE_DAGGER.get());

        //BATTLEAXES
        battleaxeRecipe(consumer, Tags.Items.GEMS_DIAMOND, BrassArmoryItems.DIAMOND_BATTLEAXE.get());
        battleaxeRecipe(consumer, Tags.Items.INGOTS_GOLD, BrassArmoryItems.GOLDEN_BATTLEAXE.get());
        battleaxeRecipe(consumer, Tags.Items.INGOTS_IRON, BrassArmoryItems.IRON_BATTLEAXE.get());
        battleaxeRecipe(consumer, ItemTags.STONE_TOOL_MATERIALS, BrassArmoryItems.STONE_BATTLEAXE.get());
        battleaxeRecipe(consumer, ItemTags.PLANKS, BrassArmoryItems.WOODEN_BATTLEAXE.get());
        netheriteRecipe(consumer, BrassArmoryItems.DIAMOND_BATTLEAXE.get(), BrassArmoryItems.NETHERITE_BATTLEAXE.get());

        //HALBERDS
        halberdRecipe(consumer, BrassArmoryItems.DIAMOND_DAGGER.get(), Items.DIAMOND_AXE, BrassArmoryItems.DIAMOND_HALBERD.get());
        halberdRecipe(consumer, BrassArmoryItems.GOLDEN_DAGGER.get(), Items.GOLDEN_AXE, BrassArmoryItems.GOLDEN_HALBERD.get());
        halberdRecipe(consumer, BrassArmoryItems.IRON_DAGGER.get(), Items.IRON_AXE, BrassArmoryItems.IRON_HALBERD.get());
        halberdRecipe(consumer, BrassArmoryItems.STONE_DAGGER.get(), Items.STONE_AXE, BrassArmoryItems.STONE_HALBERD.get());
        halberdRecipe(consumer, BrassArmoryItems.WOODEN_DAGGER.get(), Items.WOODEN_AXE, BrassArmoryItems.WOODEN_HALBERD.get());
        netheriteRecipe(consumer, BrassArmoryItems.DIAMOND_HALBERD.get(), BrassArmoryItems.NETHERITE_HALBERD.get());

        //MACES
        maceRecipe(consumer, BrassArmoryItems.DIAMOND_SPIKY_BALL.get(), BrassArmoryItems.DIAMOND_MACE.get());
        maceRecipe(consumer, BrassArmoryItems.GOLDEN_SPIKY_BALL.get(), BrassArmoryItems.GOLDEN_MACE.get());
        maceRecipe(consumer, BrassArmoryItems.IRON_SPIKY_BALL.get(), BrassArmoryItems.IRON_MACE.get());
        maceRecipe(consumer, BrassArmoryItems.STONE_SPIKY_BALL.get(), BrassArmoryItems.STONE_MACE.get());
        maceRecipe(consumer, BrassArmoryItems.WOODEN_SPIKY_BALL.get(), BrassArmoryItems.WOODEN_MACE.get());
        netheriteRecipe(consumer, BrassArmoryItems.DIAMOND_MACE.get(), BrassArmoryItems.NETHERITE_MACE.get());

        //FLAILS
        flailRecipe(consumer, BrassArmoryItems.DIAMOND_SPIKY_BALL.get(), BrassArmoryItems.DIAMOND_FLAIL.get());
        flailRecipe(consumer, BrassArmoryItems.GOLDEN_SPIKY_BALL.get(), BrassArmoryItems.GOLDEN_FLAIL.get());
        flailRecipe(consumer, BrassArmoryItems.IRON_SPIKY_BALL.get(), BrassArmoryItems.IRON_FLAIL.get());
        flailRecipe(consumer, BrassArmoryItems.STONE_SPIKY_BALL.get(), BrassArmoryItems.STONE_FLAIL.get());
        flailRecipe(consumer, BrassArmoryItems.WOODEN_SPIKY_BALL.get(), BrassArmoryItems.WOODEN_FLAIL.get());
        netheriteRecipe(consumer, BrassArmoryItems.DIAMOND_FLAIL.get(), BrassArmoryItems.NETHERITE_FLAIL.get());

        //SPIKY BALLS
        ballRecipe(consumer, Tags.Items.GEMS_DIAMOND, Tags.Items.INGOTS_GOLD, BrassArmoryItems.DIAMOND_SPIKY_BALL.get());
        ballRecipe(consumer, Tags.Items.INGOTS_GOLD, Tags.Items.INGOTS_IRON, BrassArmoryItems.GOLDEN_SPIKY_BALL.get());
        ballRecipe(consumer, Tags.Items.INGOTS_IRON, ItemTags.STONE_TOOL_MATERIALS, BrassArmoryItems.IRON_SPIKY_BALL.get());
        ballRecipe(consumer, ItemTags.STONE_TOOL_MATERIALS, ItemTags.PLANKS, BrassArmoryItems.STONE_SPIKY_BALL.get());

        shaped(RecipeCategory.COMBAT, BrassArmoryItems.WOODEN_SPIKY_BALL.get())
                .pattern(" M ")
                .pattern("M M")
                .pattern(" M ")
                .define('M', ItemTags.PLANKS)
                .unlockedBy("has_item", has(ItemTags.PLANKS))
                .save(consumer, shapedLocate(BrassArmoryItems.WOODEN_SPIKY_BALL.get()));

        netheriteRecipe(consumer, BrassArmoryItems.DIAMOND_SPIKY_BALL.get(), BrassArmoryItems.NETHERITE_SPIKY_BALL.get());

        //ARROWS
        arrowRecipe(consumer, Ingredient.of(Items.ENDER_PEARL), Items.ENDER_PEARL, BrassArmoryItems.WARP_ARROW.get());
        arrowRecipe(consumer, Tags.BlockItems.SANDS.item(), BrassArmoryItems.CONFUSION_ARROW.get());
        arrowRecipe(consumer, Ingredient.of(Items.DIRT, Items.COARSE_DIRT), Items.DIRT, BrassArmoryItems.DIRT_ARROW.get());
        arrowRecipe(consumer, Ingredient.of(BrassArmoryItems.BOMB.get()), BrassArmoryItems.BOMB.get(), BrassArmoryItems.EX_ARROW.get());
        arrowRecipe(consumer, Ingredient.of(Items.COAL, Items.CHARCOAL), Items.COAL, BrassArmoryItems.FIRE_ARROW.get());
        arrowRecipe(consumer, Ingredient.of(Items.ICE), Items.ICE, BrassArmoryItems.FROST_ARROW.get());
        arrowRecipe(consumer, Tags.Items.SEEDS, BrassArmoryItems.GRASS_ARROW.get());
        arrowRecipe(consumer, Tags.BlockItems.STORAGE_BLOCKS_REDSTONE.item(), BrassArmoryItems.LASER_ARROW.get());
        arrowRecipe(consumer, Ingredient.of(BrassArmoryItems.EXPLORERS_ROPE.get()), BrassArmoryItems.EXPLORERS_ROPE.get(), BrassArmoryItems.ROPE_ARROW.get());
        arrowRecipe(consumer, Tags.Items.SLIME_BALLS, BrassArmoryItems.SLIME_ARROW.get());

        shapeless(RecipeCategory.COMBAT, BrassArmoryItems.TORCH_ARROW.get())
                .requires(Items.ARROW)
                .requires(Items.TORCH)
                .unlockedBy("has_arrows", has(ItemTags.ARROWS))
                .save(consumer, shapelessLocate(BrassArmoryItems.TORCH_ARROW.get()));

        SpecialRecipeBuilder.special(TorchArrowRecipe::new).save(consumer, BrassArmory.locate("torch_arrow_custom_recipe").toString());
        SpecialRecipeBuilder.special(CarcassRoundRecipe::new).save(consumer, BrassArmory.locate("carcass_round_custom_recipe").toString());

        //LONGBOW
        shaped(RecipeCategory.COMBAT, BrassArmoryItems.LONGBOW.get())
                .pattern(" SI")
                .pattern("SBI")
                .pattern(" SI")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I', Tags.Items.STRINGS)
                .define('B', Items.BOW)
                .unlockedBy("has_item", has(Items.BOW))
                .save(consumer, shapedLocate(BrassArmoryItems.LONGBOW.get()));

        //QUIVER
        shaped(RecipeCategory.COMBAT, BrassArmoryItems.QUIVER.get())
                .pattern("LS ")
                .pattern("LLS")
                .pattern(" LL")
                .define('L', Tags.Items.LEATHERS)
                .define('S', Tags.Items.STRINGS)
                .unlockedBy("has_arrow", has(ItemTags.ARROWS))
                .save(consumer, shapedLocate(BrassArmoryItems.QUIVER.get()));

        //BOMBS
        shaped(RecipeCategory.COMBAT, BrassArmoryItems.BOMB.get(), 4)
                .pattern(" IS")
                .pattern("IGI")
                .pattern(" I ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.STRINGS)
                .define('G', Tags.Items.GUNPOWDERS)
                .unlockedBy("has_item", has(Tags.Items.GUNPOWDERS))
                .save(consumer, shapedLocate(BrassArmoryItems.BOMB.get()));

        shaped(RecipeCategory.COMBAT, BrassArmoryItems.BOUNCY_BOMB.get(), 4)
                .pattern(" B ")
                .pattern("BSB")
                .pattern(" B ")
                .define('B', BrassArmoryItems.BOMB.get())
                .define('S', Tags.Items.SLIME_BALLS)
                .unlockedBy("has_item", has(BrassArmoryItems.BOMB.get()))
                .save(consumer, shapedLocate(BrassArmoryItems.BOUNCY_BOMB.get()));

        shaped(RecipeCategory.COMBAT, BrassArmoryItems.STICKY_BOMB.get(), 4)
                .pattern(" B ")
                .pattern("BHB")
                .pattern(" B ")
                .define('B', BrassArmoryItems.BOMB.get())
                .define('H', Items.HONEY_BOTTLE)
                .unlockedBy("has_item", has(BrassArmoryItems.BOMB.get()))
                .save(consumer, shapedLocate(BrassArmoryItems.STICKY_BOMB.get()));

        //GUNS
        shapeless(RecipeCategory.COMBAT, BrassArmoryItems.BLUNDERBUSS.get())
                .requires(BrassArmoryItems.BLUNDERBUSS_PARTS.get())
                .requires(BrassArmoryItems.GUN_STOCK.get())
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .unlockedBy("has_parts", has(BrassArmoryItems.BLUNDERBUSS_PARTS.get()))
                .save(consumer, shapelessLocate(BrassArmoryItems.BLUNDERBUSS.get()));

        shapeless(RecipeCategory.COMBAT, BrassArmoryItems.MUSKET.get())
                .requires(BrassArmoryItems.MUSKET_PARTS.get())
                .requires(BrassArmoryItems.GUN_STOCK.get())
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .unlockedBy("has_parts", has(BrassArmoryItems.MUSKET_PARTS.get()))
                .save(consumer, shapelessLocate(BrassArmoryItems.MUSKET.get()));

        shaped(RecipeCategory.COMBAT, BrassArmoryItems.FLINTLOCK_PISTOL.get())
                .pattern("IIF")
                .pattern(" NP")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('F', Items.FLINT_AND_STEEL)
                .define('N', Tags.Items.NUGGETS_IRON)
                .define('P', ItemTags.PLANKS)
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .unlockedBy("has_item", has(Items.FLINT_AND_STEEL))
                .save(consumer, shapedLocate(BrassArmoryItems.FLINTLOCK_PISTOL.get()));

        //GUN PARTS
        shaped(RecipeCategory.COMBAT, BrassArmoryItems.BLUNDERBUSS_PARTS.get())
                .pattern("N  ")
                .pattern("IIF")
                .pattern("N N")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('F', Items.FLINT_AND_STEEL)
                .define('N', Tags.Items.NUGGETS_IRON)
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .unlockedBy("has_item", has(Items.FLINT_AND_STEEL))
                .save(consumer, shapedLocate(BrassArmoryItems.BLUNDERBUSS_PARTS.get()));

        shaped(RecipeCategory.COMBAT, BrassArmoryItems.MUSKET_PARTS.get())
                .pattern("IIF")
                .pattern("  N")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('F', Items.FLINT_AND_STEEL)
                .define('N', Tags.Items.NUGGETS_IRON)
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .unlockedBy("has_item", has(Items.FLINT_AND_STEEL))
                .save(consumer, shapedLocate(BrassArmoryItems.MUSKET_PARTS.get()));

        shaped(RecipeCategory.COMBAT, BrassArmoryItems.GUN_STOCK.get())
                .pattern("SSP")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('P', ItemTags.PLANKS)
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .save(consumer, shapedLocate(BrassArmoryItems.GUN_STOCK.get()));

        //AMMO
        shapeless(RecipeCategory.COMBAT, BrassArmoryItems.BUNDLE_SHOT.get(), 4)
                .requires(Tags.Items.NUGGETS_IRON)
                .requires(Tags.Items.NUGGETS_IRON)
                .requires(Tags.Items.NUGGETS_IRON)
                .requires(Tags.Items.NUGGETS_IRON)
                .requires(Tags.Items.NUGGETS_IRON)
                .requires(Tags.Items.NUGGETS_IRON)
                .requires(Items.PAPER)
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .save(consumer, shapelessLocate(BrassArmoryItems.BUNDLE_SHOT.get()));

        shapeless(RecipeCategory.COMBAT, BrassArmoryItems.MUSKET_BALL.get(), 8)
                .requires(Tags.Items.INGOTS_IRON)
                .requires(Items.PAPER)
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .save(consumer, shapelessLocate(BrassArmoryItems.MUSKET_BALL.get()));

        //ROPE
        shaped(RecipeCategory.COMBAT, BrassArmoryItems.EXPLORERS_ROPE.get(), 3)
                .pattern("SS")
                .pattern("SS")
                .pattern("SS")
                .define('S', Tags.Items.STRINGS)
                .unlockedBy("has_item", has(Tags.Items.STRINGS))
                .save(consumer, shapedLocate(BrassArmoryItems.EXPLORERS_ROPE.get()));

        //GLIDER
        shaped(RecipeCategory.COMBAT, BrassArmoryItems.GLIDER.get())
                .pattern("HRH")
                .pattern("LSL")
                .pattern("LSL")
                .define('H', Items.TRIPWIRE_HOOK)
                .define('R', BrassArmoryItems.EXPLORERS_ROPE.get())
                .define('L', Tags.Items.LEATHERS)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_rope", has(BrassArmoryItems.EXPLORERS_ROPE.get()))
                .save(consumer, shapedLocate(BrassArmoryItems.GLIDER.get()));

        //CANNON SHIT
        shaped(RecipeCategory.COMBAT, BrassArmoryItems.CANNON.get())
                .pattern("IIS")
                .pattern("  I")
                .pattern("IIL")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.STRINGS)
                .define('L', ItemTags.LOGS)
                .unlockedBy("has_item", has(Tags.Items.GUNPOWDERS))
                .save(consumer, shapedLocate(BrassArmoryItems.CANNON.get()));

        shaped(RecipeCategory.COMBAT, BrassArmoryItems.CANNON_BALL.get())
                .pattern(" S ")
                .pattern("SSS")
                .pattern(" S ")
                .define('S', Tags.BlockItems.STONES.item())
                .unlockedBy("has_item", has(Tags.Items.GUNPOWDERS))
                .save(consumer, shapedLocate(BrassArmoryItems.CANNON_BALL.get()));

        shaped(RecipeCategory.COMBAT, BrassArmoryItems.CARCASS_ROUND.get())
                .pattern(" S ")
                .pattern("SPS")
                .pattern(" S ")
                .define('S', Tags.BlockItems.STONES.item())
                .define('P', Items.LINGERING_POTION)
                .unlockedBy("has_item", has(Tags.Items.GUNPOWDERS))
                .save(consumer, shapedLocate(BrassArmoryItems.CARCASS_ROUND.get()));

        shaped(RecipeCategory.COMBAT, BrassArmoryItems.SIEGE_ROUND.get())
                .pattern(" S ")
                .pattern("STS")
                .pattern(" S ")
                .define('S', Tags.BlockItems.STONES.item())
                .define('T', Items.TNT)
                .unlockedBy("has_item", has(Tags.Items.GUNPOWDERS))
                .save(consumer, shapedLocate(BrassArmoryItems.SIEGE_ROUND.get()));
    }

    protected final void boomerangRecipe(RecipeOutput consumer, TagKey<Item> material, TagKey<Item> lesserMaterial, Item result) {
        shaped(RecipeCategory.COMBAT, result)
                .pattern("MLM")
                .pattern("L  ")
                .pattern("M  ")
                .define('M', material)
                .define('L', lesserMaterial)
                .unlockedBy("has_item", has(material == ItemTags.PLANKS ? lesserMaterial : material))
                .save(consumer, shapedLocate(result));
    }

    protected final void spearRecipe(RecipeOutput consumer, Item tipItem, Item result) {
        shaped(RecipeCategory.COMBAT, result)
                .pattern("T  ")
                .pattern(" S ")
                .pattern("  S")
                .define('T', tipItem)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(tipItem))
                .save(consumer, shapedLocate(result));
    }

    protected final void daggerRecipe(RecipeOutput consumer, TagKey<Item> material, Item result) {
        shaped(RecipeCategory.COMBAT, result)
                .pattern("SM")
                .define('M', material)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(material == ItemTags.PLANKS ? Tags.Items.RODS_WOODEN : material))
                .save(consumer, shapedLocate(result));
    }

    protected final void battleaxeRecipe(RecipeOutput consumer, TagKey<Item> material, Item result) {
        shaped(RecipeCategory.COMBAT, result)
                .pattern("MSM")
                .pattern("MSM")
                .pattern(" S ")
                .define('M', material)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(material == ItemTags.PLANKS ? Tags.Items.RODS_WOODEN : material))
                .save(consumer, shapedLocate(result));
    }

    protected final void halberdRecipe(RecipeOutput consumer, Item tipItem, Item baseItem, Item result) {
        shaped(RecipeCategory.COMBAT, result)
                .pattern("T  ")
                .pattern(" B ")
                .pattern("  S")
                .define('T', tipItem)
                .define('B', baseItem)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(baseItem))
                .save(consumer, shapedLocate(result));
    }

    protected final void maceRecipe(RecipeOutput consumer, Item material, Item result) {
        shaped(RecipeCategory.COMBAT, result)
                .pattern("M")
                .pattern("S")
                .pattern("S")
                .define('M', material)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(material))
                .save(consumer, shapedLocate(result));
    }

    protected final void flailRecipe(RecipeOutput consumer, Item material, Item result) {
        shaped(RecipeCategory.COMBAT, result)
                .pattern("  S")
                .pattern(" SC")
                .pattern("S M")
                .define('M', material)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Items.IRON_CHAIN)
                .unlockedBy("has_item", has(material))
                .save(consumer, shapedLocate(result));
    }

    protected final void ballRecipe(RecipeOutput consumer, TagKey<Item> material, TagKey<Item> lesserMaterial, Item result) {
        shaped(RecipeCategory.COMBAT, result)
                .pattern(" M ")
                .pattern("MLM")
                .pattern(" M ")
                .define('M', material)
                .define('L', lesserMaterial)
                .unlockedBy("has_item", has(material == ItemTags.PLANKS ? lesserMaterial : material))
                .save(consumer, shapedLocate(result));
    }

    protected final void arrowRecipe(RecipeOutput consumer, Ingredient material, Item unlock, Item result) {
        shaped(RecipeCategory.COMBAT, result, 4)
                .pattern("M")
                .pattern("S")
                .pattern("F")
                .define('M', material)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('F', Tags.Items.FEATHERS)
                .unlockedBy("has_item", has(unlock))
                .save(consumer, shapedLocate(result));
    }
    //Same as above, just uses TagKey<Item> instead
    protected final void arrowRecipe(RecipeOutput consumer, TagKey<Item> material, Item result) {
        shaped(RecipeCategory.COMBAT, result, 4)
                .pattern("M")
                .pattern("S")
                .pattern("F")
                .define('M', material)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('F', Tags.Items.FEATHERS)
                .unlockedBy("has_item", has(material))
                .save(consumer, shapedLocate(result));
    }

    protected final void netheriteRecipe(RecipeOutput consumer, Item ingredient, Item result) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ingredient), Ingredient.of(Items.NETHERITE_INGOT), RecipeCategory.COMBAT,  result)
                .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(consumer, ResourceKey.create(Registries.RECIPE, BrassArmory.locate("smithing/" + BuiltInRegistries.ITEM.getKey(result).getPath())));
    }

        private static ResourceKey<Recipe<?>> shapedLocate(Item item) {
        return ResourceKey.create(Registries.RECIPE, BrassArmory.locate("shaped/" + BuiltInRegistries.ITEM.getKey(item).getPath()));
    }

        private static ResourceKey<Recipe<?>> shapelessLocate(Item item) {
        return ResourceKey.create(Registries.RECIPE, BrassArmory.locate("shapeless/" + BuiltInRegistries.ITEM.getKey(item).getPath()));
    }
}
