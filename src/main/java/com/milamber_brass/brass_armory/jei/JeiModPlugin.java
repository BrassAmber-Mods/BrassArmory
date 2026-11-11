package com.milamber_brass.brass_armory.jei;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.util.ItemData;
import com.milamber_brass.brass_armory.util.PotionData;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.TorchBlock;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JeiModPlugin implements IModPlugin {
    @Override
    public @NotNull Identifier getPluginUid() {
        return BrassArmory.locate("jei");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BrassArmoryItems.CARCASS_ROUND.get(), (stack, context) -> List.of(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY), ItemData.getTag(stack).getBooleanOr("BADragonRound", false)));
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BrassArmoryItems.TORCH_ARROW.get(), TorchSubtypeInterpreter.INSTANCE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();
        Ingredient stone = Ingredient.of(level.registryAccess().lookupOrThrow(Registries.ITEM).getOrThrow(Tags.BlockItems.STONES.item()));
        for (var potion : BuiltInRegistries.POTION.listElements().toList()) {
            ItemStack input = PotionData.setPotion(new ItemStack(Items.LINGERING_POTION), potion);
            ItemStack result = PotionData.setPotion(new ItemStack(BrassArmoryItems.CARCASS_ROUND.get()), potion);
            addRound(recipes, "carcass_round/" + potion.key().identifier().getPath(), stone, DataComponentIngredient.of(input.getComponentsPatch(), Items.LINGERING_POTION), result);
        }
        ItemStack dragon = new ItemStack(BrassArmoryItems.CARCASS_ROUND.get());
        ItemData.update(dragon, tag -> tag.putBoolean("BADragonRound", true));
        addRound(recipes, "dragon_round", stone, Ingredient.of(Items.DRAGON_BREATH), dragon);
        for (var item : BuiltInRegistries.ITEM) {
            if (item instanceof StandingAndWallBlockItem torch && torch.getBlock() instanceof TorchBlock && item != Items.TORCH) {
                ItemStack result = new ItemStack(BrassArmoryItems.TORCH_ARROW.get());
                ArmoryUtil.addStack(result, item.getDefaultInstance(), "BATorch");
                var id = BuiltInRegistries.ITEM.getKey(item);
                recipes.add(new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, BrassArmory.locate("torch_arrow/" + id.getNamespace() + "/" + id.getPath())), new ShapelessRecipe(new Recipe.CommonInfo(true), new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.EQUIPMENT, "brass_armory.jei.torch_arrow"), ItemStackTemplate.fromStack(result), List.of(Ingredient.of(Items.ARROW), Ingredient.of(item)))));
            }
        }
        registration.addRecipes(RecipeTypes.CRAFTING, recipes);
    }

    private static void addRound(List<RecipeHolder<CraftingRecipe>> recipes, String id, Ingredient stone, Ingredient filling, ItemStack result) {
        var empty = Optional.<Ingredient>empty();
        var rock = Optional.of(stone);
        var pattern = new ShapedRecipePattern(3, 3, List.of(empty, rock, empty, rock, Optional.of(filling), rock, empty, rock, empty), Optional.empty());
        recipes.add(new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, BrassArmory.locate(id)), new ShapedRecipe(new Recipe.CommonInfo(true), new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.EQUIPMENT, "brass_armory.jei.carcass_round"), pattern, ItemStackTemplate.fromStack(result))));
    }
}
