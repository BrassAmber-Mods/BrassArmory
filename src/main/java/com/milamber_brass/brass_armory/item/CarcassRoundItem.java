package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.util.ItemData;
import com.milamber_brass.brass_armory.util.PotionData;
import java.util.List;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CarcassRoundItem extends Item {
    public CarcassRoundItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        if (!isADragonRound(itemStack)) {
            components.add((Component.translatable(Potion.getName(Optional.of(PotionData.getPotion(itemStack)), Items.LINGERING_POTION.getDescriptionId() + ".effect."))).withStyle(ChatFormatting.GRAY));
            PotionData.addPotionTooltip(itemStack, components, 0.25F);
        }
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        if (isADragonRound(stack)) return Component.translatable("item.brass_armory.dragon_round");
        else return super.getName(stack);
    }


    public static boolean isADragonRound(ItemStack stack) {
        return !ItemData.getTag(stack).isEmpty() && ItemData.getTag(stack).getBoolean("BADragonRound");
    }
}
