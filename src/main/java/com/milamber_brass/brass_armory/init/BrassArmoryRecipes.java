package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.recipe.CarcassRoundRecipe;
import com.milamber_brass.brass_armory.recipe.TorchArrowRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrassArmoryRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BrassArmory.MOD_ID);

    public static final RegistryObject<SimpleRecipeSerializer<?>> TORCH_ARROW_CUSTOM_RECIPE = REGISTRY.register("torch_arrow_custom_recipe", () -> new SimpleRecipeSerializer<>(TorchArrowRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<?>> CARCASS_ROUND_CUSTOM_RECIPE = REGISTRY.register("carcass_round_custom_recipe", () -> new SimpleRecipeSerializer<>(CarcassRoundRecipe::new));

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
