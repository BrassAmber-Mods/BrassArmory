package com.milamber_brass.brass_armory.item;

import com.google.common.collect.Multimap;
import com.milamber_brass.brass_armory.util.Constants;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

public class HalberdItem extends SwordItem {

	private final double reachDistanceBonus;
	
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
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
    	Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EquipmentSlotType.MAINHAND) {
			multimap.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(Constants.REACH_DISTANCE_MODIFIER, "Weapon modifier", this.reachDistanceBonus, Operation.ADDITION));
		}
    	return super.getAttributeModifiers(slot, stack);
    }
    
}
