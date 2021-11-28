package com.milamber_brass.brass_armory.item;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class HalberdItem extends SwordItem implements ICustomReachItem {

    private final double reachDistanceBonus;

    protected Multimap<Attribute, AttributeModifier> reachModifiers = null;

    public HalberdItem(IItemTier tier, int attackDamageIn, Properties builderIn) {
        this(tier, attackDamageIn, builderIn, 2.0D);
    }


    public HalberdItem(IItemTier tier, int attackDamageIn, Properties builderIn, double reachDistanceBonus) {
        super(tier, attackDamageIn, -3.4F, builderIn);

        this.reachDistanceBonus = reachDistanceBonus;
    }

    @Override
    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.BLOCK;
    }

    @Override
    public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
        ItemStack itemstack = p_77659_2_.getItemInHand(p_77659_3_);
        p_77659_2_.startUsingItem(p_77659_3_);
        return ActionResult.consume(itemstack);
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
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return ICustomReachItem.super.getAttributeModifiers(slot, stack);
    }

    @Nonnull
    @Override
    public Multimap<Attribute, AttributeModifier> execSuperGetAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
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
