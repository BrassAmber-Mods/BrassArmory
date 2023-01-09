package com.milamber_brass.brass_armory.jei;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.library.plugins.vanilla.brewing.PotionSubtypeInterpreter;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@JeiPlugin
@ParametersAreNonnullByDefault
public class JeiModPlugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return BrassArmory.locate("jei");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BrassArmoryItems.CARCASS_ROUND.get(), PotionSubtypeInterpreter.INSTANCE);
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

    public static @NotNull List<CraftingRecipe> regCarcassRounds(RecipeManager recipeManager) {
        List<CraftingRecipe> recipes = new ArrayList<>();

        for (Recipe<?> recipe : recipeManager.getRecipes()) {
            if (recipe instanceof CraftingRecipe craftingRecipe && craftingRecipe.getResultItem().is(BrassArmoryItems.CARCASS_ROUND.get()) && !craftingRecipe.getIngredients().isEmpty()) {
                NonNullList<Ingredient> ingredients = craftingRecipe.getIngredients();
                ItemStack result = craftingRecipe.getResultItem();

                for (Potion potion : ForgeRegistries.POTIONS.getValues()) {
                    NonNullList<Ingredient> newIngredients = NonNullList.create();

                    boolean flag = false;
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient.test(new ItemStack(Items.LINGERING_POTION))) {
                            newIngredients.add(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), potion)));
                            flag = true;
                        } else newIngredients.add(ingredient);
                    }

                    if (flag) {
                        if (craftingRecipe instanceof ShapedRecipe shapedRecipe) {
                            recipes.add(new ShapedRecipe(
                                    BrassArmory.locate(potion.getName("carcass_round.")),
                                    "brass_armory.jei.carcass_round",
                                    shapedRecipe.getWidth(),
                                    shapedRecipe.getHeight(),
                                    newIngredients,
                                    PotionUtils.setPotion(result.copy(), potion)));
                        } else if (craftingRecipe instanceof ShapelessRecipe) {
                            recipes.add(new ShapelessRecipe(
                                    BrassArmory.locate(potion.getName("carcass_round.")),
                                    "brass_armory.jei.carcass_round",
                                    PotionUtils.setPotion(result.copy(), potion),
                                    newIngredients));
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
                        recipes.add(new ShapedRecipe(
                                BrassArmory.locate("dragon_round"),
                                "brass_armory.jei.dragon_round",
                                shapedRecipe.getWidth(),
                                shapedRecipe.getHeight(),
                                newIngredients,
                                Util.make(result.copy(), stack -> stack.getOrCreateTag().putBoolean("BADragonRound", true))));
                    } else if (craftingRecipe instanceof ShapelessRecipe) {
                        recipes.add(new ShapelessRecipe(
                                BrassArmory.locate("dragon_round"),
                                "brass_armory.jei.dragon_round",
                                Util.make(result.copy(), stack -> stack.getOrCreateTag().putBoolean("BADragonRound", true)),
                                newIngredients));
                    }
                }
            }
        }

        return recipes;
    }


    public static @NotNull List<CraftingRecipe> regTorchArrows(RecipeManager recipeManager) {
        List<CraftingRecipe> recipes = new ArrayList<>();

        for (Recipe<?> recipe : recipeManager.getRecipes()) {
            if (recipe instanceof CraftingRecipe craftingRecipe && craftingRecipe.getResultItem().is(BrassArmoryItems.TORCH_ARROW.get()) && !craftingRecipe.getIngredients().isEmpty()) {
                NonNullList<Ingredient> ingredients = craftingRecipe.getIngredients();
                ItemStack result = craftingRecipe.getResultItem();


                for (Item item : ForgeRegistries.ITEMS.getValues()) {
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
                                recipes.add(new ShapedRecipe(
                                        BrassArmory.locate("carcass_round." + torchItem),
                                        "brass_armory.jei.carcass_round",
                                        shapedRecipe.getWidth(),
                                        shapedRecipe.getHeight(),
                                        newIngredients,
                                        Util.make(result.copy(), stack -> ArmoryUtil.addStack(stack.getOrCreateTag(), torchItem.getDefaultInstance(), "BATorch"))));
                            } else if (craftingRecipe instanceof ShapelessRecipe) {
                                recipes.add(new ShapelessRecipe(
                                        BrassArmory.locate("carcass_round." + torchItem),
                                        "brass_armory.jei.carcass_round",
                                        Util.make(result.copy(), stack -> ArmoryUtil.addStack(stack.getOrCreateTag(), torchItem.getDefaultInstance(), "BATorch")),
                                        newIngredients));
                            }
                        }
                    }
                }
            }
        }

        return recipes;
    }
}
