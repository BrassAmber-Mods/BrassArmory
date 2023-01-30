package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(method = "getCategory", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void getCategory(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> cir) {
        if (recipe.getType() == RecipeType.CRAFTING) {
            ItemStack item = recipe.getResultItem();
            if (item.is(BrassArmoryTags.Items.BUILDING_BLOCKS)) {
                cir.setReturnValue(RecipeBookCategories.CRAFTING_BUILDING_BLOCKS);
            }
            if (item.is(BrassArmoryTags.Items.REDSTONE)) {
                cir.setReturnValue(RecipeBookCategories.CRAFTING_REDSTONE);
            }
            if (item.is(BrassArmoryTags.Items.MISC)) {
                cir.setReturnValue(RecipeBookCategories.CRAFTING_MISC);
            }
            if (item.is(BrassArmoryTags.Items.EQUIPMENT)) {
                cir.setReturnValue(RecipeBookCategories.CRAFTING_EQUIPMENT);
            }
        }
    }
}
