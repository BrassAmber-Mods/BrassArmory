package com.milamber_brass.brass_armory.item;

import com.google.common.collect.Multimap;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ThrowingKnifeItem extends SwordItem implements ICustomReachItem {

    private Multimap<Attribute, AttributeModifier> customAttributes;

    public ThrowingKnifeItem(Tiers tier, int attackDamageIn, Properties builderIn) {
        super(tier, attackDamageIn, -2.0F, builderIn);
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level.isClientSide) {
            player.displayClientMessage(new TextComponent("Not actually throwable yet! Join our discord for updates!"), true);
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getCustomAttributesField() {
        return this.customAttributes;
    }

    @Override
    public void setCustomAttributesField(Multimap<Attribute, AttributeModifier> value) {
        this.customAttributes = value;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> execSuperGetAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return ICustomReachItem.super.getAttributeModifiers(slot, stack);
    }

    @Override
    public double getReachDistanceBonus() {
        return -1;
    }

}
