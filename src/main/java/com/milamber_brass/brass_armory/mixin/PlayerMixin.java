package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.effect.BleedEffect;
import com.milamber_brass.brass_armory.item.KatanaItem;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique private boolean attackStrengthScaleFlag;

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT, remap = true)
    private void getAttackStrengthScaleFlag(Entity entity, CallbackInfo ci, float f, float f1, float f2, boolean flag, boolean flag1, float i, boolean flag2, CriticalHitEvent hitResult, boolean flag3, double d0, float f4, boolean flag4, int j, Vec3 vec3) {
        this.attackStrengthScaleFlag = flag;
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostDamageEffects(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/Entity;)V"), remap = true)
    private void postDamageSpecialEffects(Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity living) {
            ArmoryUtil.getWitherFromLivingEntity(living, this);
            if (this.attackStrengthScaleFlag) ArmoryUtil.bleedLivingEntity(living, this);
        }
    }

    @SuppressWarnings("rawtypes")
    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT, remap = true)
    private void sweepHit(Entity entity, CallbackInfo ci, float f, float f1, float f2, boolean flag, boolean flag1, float i, boolean flag2, CriticalHitEvent hitResult, boolean flag3, double d0, float f4, boolean flag4, int j, Vec3 vec3, boolean flag5, float f3, Iterator var19, LivingEntity living) {
        ItemStack stack = this.getMainHandItem();
        if (stack.getItem() instanceof KatanaItem katanaItem) {
            if (j > 0 && !living.isOnFire()) living.setSecondsOnFire(j * 4);
            EnchantmentHelper.doPostHurtEffects(living, this);
            EnchantmentHelper.doPostDamageEffects(this, living);

            if (stack.is(BrassArmoryTags.Items.BLEEDING_EDGE)) BleedEffect.bleedHarder(living, 50, 0);
            if (katanaItem.canWither() && KatanaItem.getWither(stack) >= 100) BleedEffect.witherHarder(living, 50);
        }
    }
}
