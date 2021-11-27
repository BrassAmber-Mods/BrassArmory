package com.milamber_brass.brass_armory.item;

import java.util.UUID;

import javax.annotation.Nonnull;

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

public interface ICustomReachItem extends IForgeItem {

	//Needs to be unique
	static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("b7d3df52-d360-491b-9bb5-2e8e3b5b279a");

	//Setter and getter for custom multimap
	public Multimap<Attribute, AttributeModifier> getCustomAttributesField();
	public void setCustomAttributesField(Multimap<Attribute, AttributeModifier> value);
	
	//Getter for the actual reach bonus value
	double getReachDistanceBonus();

	@Nonnull
	Multimap<Attribute, AttributeModifier> execSuperGetAttributeModifiers(EquipmentSlotType slot, ItemStack stack);

	public default Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		if (slot == EquipmentSlotType.MAINHAND) {
			if(this.getCustomAttributesField() == null) {
				Builder<Attribute, AttributeModifier> attributeBuilder = ImmutableMultimap.builder();
				//!!ONLY call get when you need it according to "dieSieben07"...
		        attributeBuilder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(ICustomReachItem.REACH_DISTANCE_MODIFIER, "Weapon modifier", this.getReachDistanceBonus(), Operation.ADDITION));
		        
		        this.setCustomAttributesField(attributeBuilder.build());
			}
			return this.getCustomAttributesField();
		}

		return this.execSuperGetAttributeModifiers(slot, stack);
	}

}
