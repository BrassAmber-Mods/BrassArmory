package com.milamber_brass.brass_armory.item;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class DaggerItem extends SwordItem implements ICustomReachItem {

    private Multimap<Attribute, AttributeModifier> customAttributes;

    public DaggerItem(Tiers tier, int attackDamageIn, Properties builderIn) {
        super(tier, attackDamageIn, -2.0F, builderIn);
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
