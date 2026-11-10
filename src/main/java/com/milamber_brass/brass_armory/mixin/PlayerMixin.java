package com.milamber_brass.brass_armory.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.effect.BleedEffect;
import com.milamber_brass.brass_armory.item.KatanaItem;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean postDamageSpecialEffects(Entity target, DamageSource source, float damage, Operation<Boolean> original) {
        boolean hurt = original.call(target, source, damage);
        if (hurt && !this.level().isClientSide && target instanceof LivingEntity living) {
            ArmoryUtil.getWitherFromLivingEntity(living, this);
            if (((Player) (Object) this).getAttackStrengthScale(0.5F) > 0.9F) {
                ArmoryUtil.bleedLivingEntity(living, this);
            }
        }
        return hurt;
    }

    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean sweepHit(LivingEntity living, DamageSource source, float damage, Operation<Boolean> original) {
        boolean hurt = original.call(living, source, damage);
        ItemStack stack = this.getMainHandItem();
        if (hurt && !this.level().isClientSide && stack.getItem() instanceof KatanaItem katanaItem) {
            // Vanilla 1.21.1 already applies enchantment effects to sweep targets.
            if (stack.is(BrassArmoryTags.Items.BLEEDING_EDGE)) BleedEffect.bleedHarder(living, 50, 0);
            if (katanaItem.canWither() && KatanaItem.getWither(stack) >= 100) BleedEffect.witherHarder(living, 50);
        }
        return hurt;
    }
}
