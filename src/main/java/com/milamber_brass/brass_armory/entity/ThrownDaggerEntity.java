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

public class ThrownDaggerEntity extends ThrowableWeaponProjectileBase {

	public ThrownDaggerEntity(EntityType<? extends ThrowableWeaponProjectileBase> type, World worldIn) {
		super(type, worldIn);
	}
	
	public ThrownDaggerEntity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn, IItemTier tier) {
		super(BrassArmoryEntityTypes.DAGGER.get(), worldIn, thrower, thrownStackIn, tier);
    }

    public ThrownDaggerEntity(World worldIn, double x, double y, double z) {
    	super(BrassArmoryEntityTypes.DAGGER.get(), worldIn, x, y, z);
    }

    static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/wood/wood_dagger.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/stone/stone_dagger.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/iron/iron_dagger.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/diamond/diamond_dagger.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/gold/gold_dagger.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/netherite/netherite_dagger.png") };

	@Override
	public ResourceLocation getTierResourceLocation() {
		if (this.tridentItem.getItem() instanceof ITieredItem && ((ITieredItem)this.tridentItem.getItem()).getTier() instanceof ItemTier) {
			return TEXTURES[((ItemTier) ((ITieredItem)this.tridentItem.getItem()).getTier()).ordinal()];
		}
		return TEXTURES[2];// Iron
	}

}
