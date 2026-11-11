package com.milamber_brass.brass_armory.jei;

import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.util.ItemData;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class TorchSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final TorchSubtypeInterpreter INSTANCE = new TorchSubtypeInterpreter();

    private TorchSubtypeInterpreter() {

    }

    @Override
    public @NotNull String getSubtypeData(ItemStack itemStack, UidContext context) {
        ItemStack torch = ArmoryUtil.loadStack(ItemData.getTag(itemStack), "BATorch", Items.TORCH.getDefaultInstance());
        return torch.toString();
    }
}
