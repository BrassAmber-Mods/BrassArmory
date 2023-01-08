package com.milamber_brass.brass_armory.jei;

import com.milamber_brass.brass_armory.util.ArmoryUtil;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TorchSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final TorchSubtypeInterpreter INSTANCE = new TorchSubtypeInterpreter();

    private TorchSubtypeInterpreter() {

    }

    @Override
    public @NotNull String apply(ItemStack itemStack, UidContext context) {
        ItemStack torch = ArmoryUtil.loadStack(itemStack.getOrCreateTag(), "BATorch", Items.TORCH.getDefaultInstance());
        return torch.toString();
    }
}
