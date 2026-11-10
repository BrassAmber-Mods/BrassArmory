package com.milamber_brass.brass_armory.recipe;

import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmoryRecipes;
import com.milamber_brass.brass_armory.util.ItemData;
import com.milamber_brass.brass_armory.util.PotionData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CarcassRoundRecipe extends CustomRecipe {
    private List<? extends Recipe<?>> CARCASS_ROUND_RECIPES = new ArrayList<>();

    public CarcassRoundRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level level) {
        CARCASS_ROUND_RECIPES = level.getRecipeManager().getRecipes().stream().map(RecipeHolder::value).filter(recipe -> recipe.getResultItem(level.registryAccess()).is(BrassArmoryItems.CARCASS_ROUND.get())).toList();

        for (Recipe<?> recipe : this.CARCASS_ROUND_RECIPES) {
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                if (shapedRecipe.matches(inv, level)) {
                    return true;
                } else {
                    for (int i = 0; i <= inv.width() - shapedRecipe.getWidth(); ++i) {
                        for (int j = 0; j <= inv.height() - shapedRecipe.getHeight(); ++j) {
                            if (!matches(inv, shapedRecipe, i, j, true).isEmpty()) return true;
                            if (!matches(inv, shapedRecipe, i, j, false).isEmpty()) return true;
                        }
                    }
                }
            } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                if (shapelessRecipe.matches(inv, level) || !matches(inv, shapelessRecipe).isEmpty()) return true;
            }
        }
        return false;
    }

    private static @NotNull ItemStack matches(CraftingInput container, ShapedRecipe shapedRecipe, int m, int n, boolean b) {
        ItemStack itemStack = ItemStack.EMPTY;

        for(int i = 0; i < container.width(); ++i) {
            for(int j = 0; j < container.height(); ++j) {
                int k = i - m;
                int l = j - n;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < shapedRecipe.getWidth() && l < shapedRecipe.getHeight()) {
                    if (b) {
                        ingredient = shapedRecipe.getIngredients().get(shapedRecipe.getWidth() - k - 1 + l * shapedRecipe.getWidth());
                    } else {
                        ingredient = shapedRecipe.getIngredients().get(k + l * shapedRecipe.getWidth());
                    }
                }

                ItemStack stack = container.getItem(i + j * container.width());

                if (!ingredient.test(stack) && !(!ingredient.isEmpty()
                        && ingredient.getItems().length > 0
                        && isLingeringPotionItem(stack.getItem())
                        && isLingeringPotionItem(ingredient.getItems()[0].getItem()))) {
                    return ItemStack.EMPTY;
                }
                if (isLingeringPotionItem(stack.getItem())) itemStack = stack.copy();
            }
        }

        return itemStack;
    }

    public static @NotNull ItemStack matches(CraftingInput container, ShapelessRecipe recipe) {
        ItemStack itemStack = ItemStack.EMPTY;

        StackedContents stackedcontents = new StackedContents();
        List<ItemStack> inputs = new ArrayList<>();
        boolean isSimple = recipe.getIngredients().stream().allMatch(Ingredient::isSimple);
        int i = 0;

        for(int j = 0; j < container.size(); ++j) {
            ItemStack itemstack = container.getItem(j);
            if (!itemstack.isEmpty()) {
                if (isLingeringPotionItem(itemstack.getItem())) {
                    itemStack = itemstack.copy();
                    itemstack = new ItemStack(Items.LINGERING_POTION, itemstack.getCount());
                }
                ++i;
                if (isSimple) stackedcontents.accountStack(itemstack, 1);
                else inputs.add(itemstack);
            }
        }

        if (i == recipe.getIngredients().size() && (isSimple ? stackedcontents.canCraft(recipe, null) : RecipeMatcher.findMatches(inputs,  recipe.getIngredients()) != null)) {
            return itemStack;
        } else return ItemStack.EMPTY;
    }

    private static boolean isLingeringPotionItem(Item item) {
        return item instanceof LingeringPotionItem || item.equals(Items.DRAGON_BREATH);
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput inv, HolderLookup.Provider access) {
        for (Recipe<?> recipe : this.CARCASS_ROUND_RECIPES) {
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                for (int i = 0; i <= inv.width() - shapedRecipe.getWidth(); ++i) {
                    for (int j = 0; j <= inv.height() - shapedRecipe.getHeight(); ++j) {
                        for (int b = 0; b < 2; b++) {
                            ItemStack stack = matches(inv, shapedRecipe, i, j, b == 0);
                            if (!stack.isEmpty()) {
                                ItemStack result = shapedRecipe.getResultItem(access).copy();

                                if (stack.is(Items.DRAGON_BREATH)) ItemData.update(result, tag -> tag.putBoolean("BADragonRound", true));
                                else PotionData.setPotion(result, PotionData.getPotion(stack));

                                return result;
                            }
                        }
                    }
                }
            } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                ItemStack stack = matches(inv, shapelessRecipe);
                if (!stack.isEmpty()) {
                    ItemStack result = shapelessRecipe.getResultItem(access).copy();

                    if (stack.is(Items.DRAGON_BREATH)) ItemData.update(result, tag -> tag.putBoolean("BADragonRound", true));
                    else PotionData.setPotion(result, PotionData.getPotion(stack));

                    return result;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return BrassArmoryRecipes.CARCASS_ROUND_CUSTOM_RECIPE.get();
    }
}
