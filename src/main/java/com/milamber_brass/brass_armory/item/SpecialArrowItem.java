package com.milamber_brass.brass_armory.item;

import java.util.function.BiFunction;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.data.models.blockstates.PropertyDispatch.QuadFunction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class SpecialArrowItem extends ArrowItem {
    public final BiFunction<Level, LivingEntity, AbstractArrow> newArrowFunction;
    public final QuadFunction<Level, Double, Double, Double, AbstractArrow> newPosArrowFunction;

    public SpecialArrowItem(Properties builder, BiFunction<Level, LivingEntity, AbstractArrow> newArrowFunction, QuadFunction<Level, Double, Double, Double, AbstractArrow> newPosArrowFunction) {
        super(builder);
        this.newArrowFunction = newArrowFunction;
        this.newPosArrowFunction = newPosArrowFunction;
    }

    @Override
    public @NotNull AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity living, @Nullable ItemStack weapon) {
        return this.newArrowFunction.apply(level, living);
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, LivingEntity player) {
        return false;
    }
}
