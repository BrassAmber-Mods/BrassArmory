package com.milamberBrass.brass_armory.entities.custom;

import com.milamberBrass.brass_armory.items.ModItems;
import com.milamberBrass.brass_armory.util.ArrowType;
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

public class BAArrowEntity extends ArrowEntity {
    public ArrowType arrowType;
    public BAArrowEntity(EntityType<? extends ArrowEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public BAArrowEntity(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public BAArrowEntity(World worldIn, LivingEntity shooter, ArrowType typeIn) {
        super(worldIn, shooter);
        this.arrowType = typeIn;
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(arrowType.getModItemFor(arrowType));
    }

    @Override
    protected void arrowHit(LivingEntity living) {
        super.arrowHit(living);
        if (arrowType == ArrowType.DIRT) {
            living.getEntityWorld().setBlockState(new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ()), Blocks.DIRT.getDefaultState());
        }

    }
}
