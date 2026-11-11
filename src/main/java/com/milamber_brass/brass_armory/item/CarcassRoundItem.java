package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.util.ItemData;
import com.milamber_brass.brass_armory.util.PotionData;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.NotNull;

public class CarcassRoundItem extends Item {
    public CarcassRoundItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> components, TooltipFlag tooltipFlag) {
        if (!isADragonRound(itemStack)) {
            components.accept((itemStack.getOrDefault(net.minecraft.core.component.DataComponents.POTION_CONTENTS, net.minecraft.world.item.alchemy.PotionContents.EMPTY).getName(Items.LINGERING_POTION.getDescriptionId() + ".effect.")).copy().withStyle(ChatFormatting.GRAY));
            PotionData.addPotionTooltip(itemStack, components, 0.25F);
        }
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        if (isADragonRound(stack)) return Component.translatable("item.brass_armory.dragon_round");
        else return super.getName(stack);
    }


    public static boolean isADragonRound(ItemStack stack) {
        return !ItemData.getTag(stack).isEmpty() && ItemData.getTag(stack).getBooleanOr("BADragonRound", false);
    }
}
