package com.milamber_brass.brass_armory.recipe;

import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmoryRecipes;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.util.ItemData;
import com.milamber_brass.brass_armory.util.PotionData;
import java.util.List;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;

public class CarcassRoundRecipe extends CustomRecipe {
    private List<CraftingRecipe> recipes = List.of();

    private static boolean isSpecialItem(Item item) {
        return item instanceof LingeringPotionItem || item == Items.DRAGON_BREATH;
    }

    private static CraftingInput normalize(CraftingInput input) {
        return CraftingInput.of(input.width(), input.height(), input.items().stream()
                .map(stack -> isSpecialItem(stack.getItem()) ? new ItemStack(Items.LINGERING_POTION, stack.getCount()) : stack)
                .toList());
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (level.getServer() == null) return false;
        recipes = level.getServer().getRecipeManager().getRecipes().stream()
                .map(RecipeHolder::value)
                .filter(recipe -> recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe)
                .map(recipe -> (CraftingRecipe) recipe).toList();
        CraftingInput normalized = normalize(input);
        recipes = recipes.stream().filter(recipe -> recipe.matches(normalized, level)
                && recipe.assemble(normalized).is(BrassArmoryItems.CARCASS_ROUND.get())).toList();
        return !recipes.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        CraftingInput normalized = normalize(input);
        ItemStack result = recipes.stream().map(recipe -> recipe.assemble(normalized))
                .filter(stack -> stack.is(BrassArmoryItems.CARCASS_ROUND.get())).findFirst().orElse(ItemStack.EMPTY).copy();
        if (result.isEmpty()) return result;
        ItemStack special = input.items().stream().filter(stack -> isSpecialItem(stack.getItem()))
                .findFirst().orElse(ItemStack.EMPTY);
        if (special.is(Items.DRAGON_BREATH)) ItemData.update(result, tag -> tag.putBoolean("BADragonRound", true));
        else PotionData.setPotion(result, PotionData.getPotion(special));
        return result;
    }

    @Override
    public RecipeSerializer<CarcassRoundRecipe> getSerializer() {
        return BrassArmoryRecipes.CARCASS_ROUND_CUSTOM_RECIPE.get();
    }
}
