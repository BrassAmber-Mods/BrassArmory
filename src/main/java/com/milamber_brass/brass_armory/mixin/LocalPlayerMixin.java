package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.init.BrassArmoryEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends LivingEntity {
    @Shadow protected abstract boolean isControlledCamera();

    @Shadow @Final protected Minecraft minecraft;

    protected LocalPlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "serverAiStep", at = @At(value = "TAIL"), remap = true)
    private void getSpeed(CallbackInfo ci) {
        if (this.isControlledCamera() && this.hasEffect(BrassArmoryEffects.CONFUSION.get())) {
            this.zza *= this.tickCount % 40 <= 20 ? 0.8F : -0.5F;
            this.xxa *= this.tickCount % 30 <= 15 ? 0.8F : -0.8F;
        }
    }
}
