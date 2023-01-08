package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.init.BrassArmoryCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSoundInstance.class)
public abstract class AbstractSoundInstanceMixin {

    @Inject(method = "getVolume", at = @At(value = "RETURN"), cancellable = true, remap = true)
    private void setVolume(CallbackInfoReturnable<Float> cir) {
        if (Minecraft.getInstance().cameraEntity instanceof Player player) {
            player.getCapability(BrassArmoryCapabilities.EFFECT_CAPABILITY).ifPresent(iEffectCapability ->
                    cir.setReturnValue(cir.getReturnValue() * Math.max(0.01F, 1.0F - iEffectCapability.getSlow())));
        }
    }

    @Inject(method = "getPitch", at = @At(value = "RETURN"), cancellable = true, remap = true)
    private void setPitch(CallbackInfoReturnable<Float> cir) {
        if (Minecraft.getInstance().cameraEntity instanceof Player player) {
            player.getCapability(BrassArmoryCapabilities.EFFECT_CAPABILITY).ifPresent(iEffectCapability ->
                    cir.setReturnValue(cir.getReturnValue() * Math.max(0.01F, 1.0F - (iEffectCapability.getSlow() * 3.0F))));
        }
    }
}
