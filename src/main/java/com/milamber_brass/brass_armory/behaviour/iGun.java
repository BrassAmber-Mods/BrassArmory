package com.milamber_brass.brass_armory.behaviour;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public interface iGun {
     default void onOpen(Player owner, CompoundTag tag) {

     }

     void onLoad(Player owner, CompoundTag tag);

     Predicate<ItemStack> ammoPredicate();

     Predicate<ItemStack> powderPredicate();

     double damage();

     float accuracy();

     float speed();

     float particleMultiplier();

     double particleOffset();

     default void onShoot(Level level, LivingEntity owner, Entity shooter, ItemStack ammoStack, @Nullable Projectile projectile) {

     }
}
