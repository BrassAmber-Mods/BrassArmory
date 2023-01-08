package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.init.BrassArmoryEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;doEnchantDamageEffects(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/Entity;)V"), remap = true)
    private void mainHit(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity living) {
            ArmoryUtil.getWitherFromLivingEntity(living, this);
            ArmoryUtil.bleedLivingEntity(living, this);
        }
    }

    @Inject(method = "setZza", at = @At(value = "TAIL"), remap = true)
    private void setZza(CallbackInfo ci) {
        if (!this.getType().is(BrassArmoryTags.Entities.FOCUSED) && this.hasEffect(BrassArmoryEffects.CONFUSION.get())) {
            this.zza *= this.tickCount % 40 <= 20 ? 0.8F : -0.5F;
        }
    }

    @Inject(method = "setXxa", at = @At(value = "TAIL"), remap = true)
    private void setXxa(CallbackInfo ci) {
        if (!this.getType().is(BrassArmoryTags.Entities.FOCUSED) && this.hasEffect(BrassArmoryEffects.CONFUSION.get())) {
            this.xxa *= this.tickCount % 30 <= 15 ? 0.8F : -0.8F;
        }
    }
}
