package com.milamber_brass.brass_armory.entity;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.item.ITieredItem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemTier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class ThrownBattleAxeEntity extends ThrowableWeaponProjectileBase {

	public ThrownBattleAxeEntity(EntityType<? extends ThrowableWeaponProjectileBase> type, World worldIn) {
		super(type, worldIn);
	}

	static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/wood/wood_battleaxe.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/stone/stone_battleaxe.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/iron/iron_battleaxe.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/diamond/diamond_battleaxe.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/gold/gold_battleaxe.png"),
			new ResourceLocation(BrassArmory.MOD_ID, "textures/item/netherite/netherite_battleaxe.png") };

	@Override
	public ResourceLocation getTierResourceLocation() {
		if (this.finalTier instanceof ItemTier) {
			return TEXTURES[((ItemTier) this.finalTier).ordinal()];
		}
		return TEXTURES[2];// Iron
	}
	
	@Override
	protected void onHitBlock(BlockRayTraceResult brtc) {
		BlockState hitBlockState = this.level.getBlockState(brtc.getBlockPos());
		if(hitBlockState != null && this.tridentItem.getItem() instanceof ITieredItem) {
			Block hitBlock = hitBlockState.getBlock();
			ITieredItem tieredItem = (ITieredItem) this.tridentItem.getItem();
			if(hitBlock.is(BlockTags.DOORS)) {
				if(hitBlock.is(BlockTags.WOODEN_DOORS) || tieredItem.getTier().getLevel() >= ItemTier.DIAMOND.getLevel()) {
					this.level.removeBlock(brtc.getBlockPos(), false);
				}
			}
		}
		super.onHitBlock(brtc);
	}
	
	@Override
	public void playerTouch(PlayerEntity entityIn) {
		 Entity entity = this.getOwner();
        if (entity == null || entity.getUUID() == entityIn.getUUID()) {
            super.playerTouch(entityIn);
            entityIn.getCooldowns().addCooldown(this.tridentItem.getItem(), 5 * 20);
        }
	}

}
