package com.milamberBrass.brass_armory.entities.custom;

import com.milamberBrass.brass_armory.items.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DirtArrowEntity extends ArrowEntity {
    public DirtArrowEntity(EntityType<? extends ArrowEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public DirtArrowEntity(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public DirtArrowEntity(World worldIn, LivingEntity shooter) {
        super(worldIn, shooter);
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ModItems.DIRT_ARROW.get());
    }

    @Override
    protected void arrowHit(LivingEntity living) {
        super.arrowHit(living);
        living.getEntityWorld().setBlockState(new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ()), Blocks.DIRT.getDefaultState());
    }
}
