package com.milamber_brass.brass_armory.recipe;

import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmoryRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class CarcassRoundRecipe extends CustomRecipe {
    private List<Recipe<?>> CARCASS_ROUND_RECIPES = new ArrayList<>();

    public CarcassRoundRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        this.CARCASS_ROUND_RECIPES = level.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getResultItem().is(BrassArmoryItems.CARCASS_ROUND.get())).toList();

        for (Recipe<?> recipe : this.CARCASS_ROUND_RECIPES) {
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                if (shapedRecipe.matches(inv, level)) {
                    return true;
                } else {
                    for (int i = 0; i <= inv.getWidth() - shapedRecipe.getWidth(); ++i) {
                        for (int j = 0; j <= inv.getHeight() - shapedRecipe.getHeight(); ++j) {
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

    private static @NotNull ItemStack matches(CraftingContainer container, ShapedRecipe shapedRecipe, int m, int n, boolean b) {
        ItemStack itemStack = ItemStack.EMPTY;

        for(int i = 0; i < container.getWidth(); ++i) {
            for(int j = 0; j < container.getHeight(); ++j) {
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

                ItemStack stack = container.getItem(i + j * container.getWidth());

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

    public static @NotNull ItemStack matches(CraftingContainer container, ShapelessRecipe recipe) {
        ItemStack itemStack = ItemStack.EMPTY;

        StackedContents stackedcontents = new StackedContents();
        List<ItemStack> inputs = new ArrayList<>();
        boolean isSimple = recipe.getIngredients().stream().allMatch(Ingredient::isSimple);
        int i = 0;

        for(int j = 0; j < container.getContainerSize(); ++j) {
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

        if (i == recipe.getIngredients().size() && (isSimple ? stackedcontents.canCraft(recipe, null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  recipe.getIngredients()) != null)) {
            return itemStack;
        } else return ItemStack.EMPTY;
    }

    private static boolean isLingeringPotionItem(Item item) {
        return item instanceof LingeringPotionItem || item.equals(Items.DRAGON_BREATH);
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer inv) {
        for (Recipe<?> recipe : this.CARCASS_ROUND_RECIPES) {
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                for (int i = 0; i <= inv.getWidth() - shapedRecipe.getWidth(); ++i) {
                    for (int j = 0; j <= inv.getHeight() - shapedRecipe.getHeight(); ++j) {
                        for (int b = 0; b < 2; b++) {
                            ItemStack stack = matches(inv, shapedRecipe, i, j, b == 0);
                            if (!stack.isEmpty()) {
                                ItemStack result = shapedRecipe.getResultItem().copy();

                                if (stack.is(Items.DRAGON_BREATH)) result.getOrCreateTag().putBoolean("BADragonRound", true);
                                else PotionUtils.setPotion(result, PotionUtils.getPotion(stack));

                                return result;
                            }
                        }
                    }
                }
            } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                ItemStack stack = matches(inv, shapelessRecipe);
                if (!stack.isEmpty()) {
                    ItemStack result = shapelessRecipe.getResultItem().copy();

                    if (stack.is(Items.DRAGON_BREATH)) result.getOrCreateTag().putBoolean("BADragonRound", true);
                    else PotionUtils.setPotion(result, PotionUtils.getPotion(stack));

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
