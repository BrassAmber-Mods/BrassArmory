package com.milamber_brass.brass_armory.jei;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.util.ItemData;
import com.milamber_brass.brass_armory.util.PotionData;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.TorchBlock;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
@ParametersAreNonnullByDefault
public class JeiModPlugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return BrassArmory.locate("jei");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BrassArmoryItems.CARCASS_ROUND.get(), (IIngredientSubtypeInterpreter<ItemStack>) (stack, context) -> stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).toString());
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BrassArmoryItems.TORCH_ARROW.get(), TorchSubtypeInterpreter.INSTANCE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            registration.addRecipes(RecipeTypes.CRAFTING, regCarcassRounds(level.getRecipeManager()));
            registration.addRecipes(RecipeTypes.CRAFTING, regTorchArrows(level.getRecipeManager()));
        }
    }

    public static @NotNull List<RecipeHolder<CraftingRecipe>> regCarcassRounds(RecipeManager recipeManager) {
        List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();
        RegistryAccess access = Minecraft.getInstance().level.registryAccess();

        for (RecipeHolder<?> holder : recipeManager.getRecipes()) {
            Recipe<?> recipe = holder.value();
            if (recipe instanceof CraftingRecipe craftingRecipe && craftingRecipe.getResultItem(access).is(BrassArmoryItems.CARCASS_ROUND.get()) && !craftingRecipe.getIngredients().isEmpty()) {
                NonNullList<Ingredient> ingredients = craftingRecipe.getIngredients();

                ItemStack result = craftingRecipe.getResultItem(access);

                for(var potion : BuiltInRegistries.POTION.holders().toList()) {
                    NonNullList<Ingredient> newIngredients = NonNullList.create();

                    boolean flag = false;
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient.test(new ItemStack(Items.LINGERING_POTION))) {
                            newIngredients.add(Ingredient.of(PotionData.setPotion(new ItemStack(Items.LINGERING_POTION), potion)));
                            flag = true;
                        } else newIngredients.add(ingredient);
                    }

                    if (flag) {
                        if (craftingRecipe instanceof ShapedRecipe shapedRecipe) {
                            recipes.add(new RecipeHolder<>(BrassArmory.locate(Potion.getName(Optional.of(potion), "carcass_round.")), new ShapedRecipe("brass_armory.jei.carcass_round", CraftingBookCategory.EQUIPMENT, new ShapedRecipePattern(shapedRecipe.getWidth(), shapedRecipe.getHeight(), newIngredients, Optional.empty()), PotionData.setPotion(result.copy(), potion))));
                        } else if (craftingRecipe instanceof ShapelessRecipe) {
                            recipes.add(new RecipeHolder<>(BrassArmory.locate(Potion.getName(Optional.of(potion), "carcass_round.")), new ShapelessRecipe("brass_armory.jei.carcass_round", CraftingBookCategory.EQUIPMENT, PotionData.setPotion(result.copy(), potion), newIngredients)));
                        }
                    }
                }

                NonNullList<Ingredient> newIngredients = NonNullList.create();

                boolean flag = false;
                for (Ingredient ingredient : ingredients) {
                    if (ingredient.test(new ItemStack(Items.LINGERING_POTION))) {
                        newIngredients.add(Ingredient.of(Items.DRAGON_BREATH));
                        flag = true;
                    } else newIngredients.add(ingredient);
                }

                if (flag) {
                    if (craftingRecipe instanceof ShapedRecipe shapedRecipe) {
                        recipes.add(new RecipeHolder<>(BrassArmory.locate("dragon_round"), new ShapedRecipe("brass_armory.jei.dragon_round", CraftingBookCategory.EQUIPMENT, new ShapedRecipePattern(shapedRecipe.getWidth(), shapedRecipe.getHeight(), newIngredients, Optional.empty()), Util.make(result.copy(), stack -> ItemData.update(stack, tag -> tag.putBoolean("BADragonRound", true))))));
                    } else if (craftingRecipe instanceof ShapelessRecipe) {
                        recipes.add(new RecipeHolder<>(BrassArmory.locate("dragon_round"), new ShapelessRecipe("brass_armory.jei.dragon_round", CraftingBookCategory.EQUIPMENT, Util.make(result.copy(), stack -> ItemData.update(stack, tag -> tag.putBoolean("BADragonRound", true))), newIngredients)));
                    }
                }
            }
        }

        return recipes;
    }


    public static @NotNull List<RecipeHolder<CraftingRecipe>> regTorchArrows(RecipeManager recipeManager) {
        List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();
        RegistryAccess access = Minecraft.getInstance().level.registryAccess();

        for (RecipeHolder<?> holder : recipeManager.getRecipes()) {
            Recipe<?> recipe = holder.value();
            if (recipe instanceof CraftingRecipe craftingRecipe && craftingRecipe.getResultItem(access).is(BrassArmoryItems.TORCH_ARROW.get()) && !craftingRecipe.getIngredients().isEmpty()) {
                NonNullList<Ingredient> ingredients = craftingRecipe.getIngredients();
                ItemStack result = craftingRecipe.getResultItem(access);


                for (Item item : BuiltInRegistries.ITEM) {
                    if (item instanceof StandingAndWallBlockItem torchItem && torchItem.getBlock() instanceof TorchBlock && !torchItem.equals(Items.TORCH)) {
                        NonNullList<Ingredient> newIngredients = NonNullList.create();

                        boolean flag = false;
                        for (Ingredient ingredient : ingredients) {
                            if (ingredient.test(new ItemStack(Items.TORCH))) {
                                newIngredients.add(Ingredient.of(torchItem));
                                flag = true;
                            } else newIngredients.add(ingredient);
                        }

                        if (flag) {
                            if (craftingRecipe instanceof ShapedRecipe shapedRecipe) {
                                recipes.add(new RecipeHolder<>(BrassArmory.locate("torch_arrow/" + BuiltInRegistries.ITEM.getKey(torchItem).getNamespace() + "/" + BuiltInRegistries.ITEM.getKey(torchItem).getPath()), new ShapedRecipe("brass_armory.jei.carcass_round", CraftingBookCategory.EQUIPMENT, new ShapedRecipePattern(shapedRecipe.getWidth(), shapedRecipe.getHeight(), newIngredients, Optional.empty()), Util.make(result.copy(), stack -> ArmoryUtil.addStack(stack, torchItem.getDefaultInstance(), "BATorch")))));
                            } else if (craftingRecipe instanceof ShapelessRecipe) {
                                recipes.add(new RecipeHolder<>(BrassArmory.locate("torch_arrow/" + BuiltInRegistries.ITEM.getKey(torchItem).getNamespace() + "/" + BuiltInRegistries.ITEM.getKey(torchItem).getPath()), new ShapelessRecipe("brass_armory.jei.carcass_round", CraftingBookCategory.EQUIPMENT, Util.make(result.copy(), stack -> ArmoryUtil.addStack(stack, torchItem.getDefaultInstance(), "BATorch")), newIngredients)));
                            }
                        }
                    }
                }
            }
        }

        return recipes;
    }
}
