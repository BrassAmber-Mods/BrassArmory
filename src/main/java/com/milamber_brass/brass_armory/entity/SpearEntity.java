package com.milamber_brass.brass_armory.entity;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.item.ITieredItem;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SpearEntity extends ThrowableWeaponProjectileBase {

	public SpearEntity(EntityType<? extends ThrowableWeaponProjectileBase> type, World worldIn) {
		super(BrassArmoryEntityTypes.SPEAR.get(), worldIn);
	}
	
	public SpearEntity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn, IItemTier tier) {
		super(BrassArmoryEntityTypes.SPEAR.get(), worldIn, thrower, thrownStackIn, tier);
    }

    public SpearEntity(World worldIn, double x, double y, double z) {
    	super(BrassArmoryEntityTypes.SPEAR.get(), worldIn, x, y, z);
    }

	static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/wood_spear.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/stone_spear.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/iron_spear.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/diamond_spear.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/gold_spear.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/netherite_spear.png") };

	@Override
	public ResourceLocation getTierResourceLocation() {
		if (this.tridentItem.getItem() instanceof ITieredItem && ((ITieredItem)this.tridentItem.getItem()).getTier() instanceof ItemTier) {
			return TEXTURES[((ItemTier) ((ITieredItem)this.tridentItem.getItem()).getTier()).ordinal()];
		}
		return TEXTURES[2];// Iron
	}

}
