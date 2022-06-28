package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.entity.projectile.ArrowType;
import com.milamber_brass.brass_armory.entity.projectile.BAArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BABaseArrowItem extends ArrowItem {

    private final ArrowType arrowType;

    public BABaseArrowItem(Properties builder, ArrowType typeIn) {
        super(builder);
        this.arrowType = typeIn;
    }

    @Override
    public @NotNull AbstractArrow createArrow(Level worldIn, ItemStack stack, LivingEntity shooter) {
        return new BAArrowEntity(worldIn, shooter, this.arrowType);
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
        return false;
    }

}
