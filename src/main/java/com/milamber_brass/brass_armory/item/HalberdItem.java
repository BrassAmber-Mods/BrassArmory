package com.milamber_brass.brass_armory.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class HalberdItem extends SwordItem {

    public HalberdItem(IItemTier tier, int attackDamageIn, Properties builderIn) {
        super(tier, attackDamageIn, -3.4F, builderIn);
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
    
}
