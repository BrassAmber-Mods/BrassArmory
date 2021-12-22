package com.milamber_brass.brass_armory.item;

import com.google.common.collect.Multimap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class HalberdItem extends SwordItem implements ICustomReachItem {

    private final double reachDistanceBonus;

    protected Multimap<Attribute, AttributeModifier> reachModifiers = null;

    public HalberdItem(Tiers tier, int attackDamageIn, Properties builderIn) {
        this(tier, attackDamageIn, builderIn, 2.0D);
    }


    public HalberdItem(Tiers tier, int attackDamageIn, Properties builderIn, double reachDistanceBonus) {
        super(tier, attackDamageIn, -3.4F, builderIn);

        this.reachDistanceBonus = reachDistanceBonus;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_77661_1_) {
        return UseAnim.BLOCK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_77659_1_, Player p_77659_2_, InteractionHand p_77659_3_) {
        ItemStack itemstack = p_77659_2_.getItemInHand(p_77659_3_);
        p_77659_2_.startUsingItem(p_77659_3_);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    @Override
    public double getReachDistanceBonus() {
        return this.reachDistanceBonus;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return ICustomReachItem.super.getAttributeModifiers(slot, stack);
    }

    @Nonnull
    @Override
    public Multimap<Attribute, AttributeModifier> execSuperGetAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return super.getAttributeModifiers(slot, stack);
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getCustomAttributesField() {
        return this.reachModifiers;
    }


    @Override
    public void setCustomAttributesField(Multimap<Attribute, AttributeModifier> value) {
        this.reachModifiers = value;
    }

}
