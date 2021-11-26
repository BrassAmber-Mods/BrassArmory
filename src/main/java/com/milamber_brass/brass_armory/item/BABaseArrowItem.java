package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.entity.projectile.ArrowType;
import com.milamber_brass.brass_armory.entity.projectile.BAArrowEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BABaseArrowItem extends ArrowItem {

    private final ArrowType arrowType;

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
