package com.milamber_brass.brass_armory.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.extensions.IForgeItem;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface ICustomReachItem extends IForgeItem {

    //Needs to be unique
    UUID REACH_DISTANCE_MODIFIER = UUID.fromString("b7d3df52-d360-491b-9bb5-2e8e3b5b279a");

    //Setter and getter for custom multimap
    Multimap<Attribute, AttributeModifier> getCustomAttributesField();

    void setCustomAttributesField(Multimap<Attribute, AttributeModifier> value);

    //Getter for the actual reach bonus value
    double getReachDistanceBonus();

    @Nonnull
    Multimap<Attribute, AttributeModifier> execSuperGetAttributeModifiers(EquipmentSlotType slot, ItemStack stack);

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        if (slot == EquipmentSlotType.MAINHAND) {
            if (this.getCustomAttributesField() == null) {
                Builder<Attribute, AttributeModifier> attributeBuilder = ImmutableMultimap.builder();

                //Add in all the other attributes...
                this.execSuperGetAttributeModifiers(slot, stack).forEach((Attribute attribute, AttributeModifier attributeMod) -> {
                    attributeBuilder.put(attribute, attributeMod);
                });

                //!!ONLY call get when you need it according to "dieSieben07"...
                attributeBuilder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(ICustomReachItem.REACH_DISTANCE_MODIFIER, "Weapon modifier", this.getReachDistanceBonus(), Operation.ADDITION));

                this.setCustomAttributesField(attributeBuilder.build());
            }
            return this.getCustomAttributesField();
        }

        return this.execSuperGetAttributeModifiers(slot, stack);
    }

}
