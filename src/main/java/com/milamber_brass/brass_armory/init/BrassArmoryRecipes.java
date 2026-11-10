package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.recipe.CarcassRoundRecipe;
import com.milamber_brass.brass_armory.recipe.TorchArrowRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BrassArmoryRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, BrassArmory.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TorchArrowRecipe>> TORCH_ARROW_CUSTOM_RECIPE = REGISTRY.register("torch_arrow_custom_recipe", () -> new SimpleCraftingRecipeSerializer<>(TorchArrowRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CarcassRoundRecipe>> CARCASS_ROUND_CUSTOM_RECIPE = REGISTRY.register("carcass_round_custom_recipe", () -> new SimpleCraftingRecipeSerializer<>(CarcassRoundRecipe::new));

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
