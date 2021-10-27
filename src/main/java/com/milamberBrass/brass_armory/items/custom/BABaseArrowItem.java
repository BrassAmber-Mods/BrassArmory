package com.milamberBrass.brass_armory.items.custom;

import com.milamberBrass.brass_armory.entities.custom.BAArrowEntity;
import com.milamberBrass.brass_armory.util.ArrowType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BABaseArrowItem extends ArrowItem {
    private ArrowType arrowType;
    public BABaseArrowItem(Properties builder, ArrowType typeIn) {
        super(builder);
        this.arrowType = typeIn;
    }

    public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
        return new BAArrowEntity(worldIn, shooter, this.arrowType);

    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.PlayerEntity player) {
        return false;
    }

}
