package com.milamber_brass.brass_armory.item;

import org.jetbrains.annotations.Nullable;
import java.util.function.BiFunction;
import com.mojang.datafixers.util.Function4;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SpecialArrowItem extends ArrowItem {
    public final BiFunction<Level, LivingEntity, AbstractArrow> newArrowFunction;
    public final Function4<Level, Double, Double, Double, AbstractArrow> newPosArrowFunction;

    public SpecialArrowItem(Properties builder, BiFunction<Level, LivingEntity, AbstractArrow> newArrowFunction, Function4<Level, Double, Double, Double, AbstractArrow> newPosArrowFunction) {
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
