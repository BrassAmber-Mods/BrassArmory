package com.milamberBrass.brass_armory.items.custom;

import com.milamberBrass.brass_armory.entities.custom.BAArrowEntity;
import com.milamberBrass.brass_armory.util.ArrowType;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BABaseArrowItem extends ArrowItem {
    private ArrowType arrowType;
    
    public BABaseArrowItem(Properties builder, ArrowType typeIn) {
        super(builder);
        this.arrowType = typeIn;
    }

    @Override
    public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
        return new BAArrowEntity(worldIn, shooter, this.arrowType);

    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, PlayerEntity player) {
        return false;
    }

}
