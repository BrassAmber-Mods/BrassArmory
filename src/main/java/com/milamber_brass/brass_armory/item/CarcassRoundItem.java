package com.milamber_brass.brass_armory.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class CarcassRoundItem extends Item {
    public CarcassRoundItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if (!isADragonRound(itemStack)) {
            components.add((Component.translatable(PotionUtils.getPotion(itemStack).getName(Items.LINGERING_POTION.getDescriptionId() + ".effect."))).withStyle(ChatFormatting.GRAY));
            PotionUtils.addPotionTooltip(itemStack, components, 0.25F);
        }
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        if (isADragonRound(stack)) return Component.translatable("item.brass_armory.dragon_round");
        else return super.getName(stack);
    }

    @Override
    public @NotNull Rarity getRarity(ItemStack stack) {
        if (isADragonRound(stack)) return Rarity.UNCOMMON;
        else return super.getRarity(stack);
    }

    public static boolean isADragonRound(ItemStack stack) {
        return stack.hasTag() && stack.getOrCreateTag().getBoolean("BADragonRound");
    }
}
