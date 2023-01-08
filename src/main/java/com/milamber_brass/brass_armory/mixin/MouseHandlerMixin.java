package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.entity.CannonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow private double accumulatedDX;
    @Shadow private double accumulatedDY;

    @Inject(method = "turnPlayer", at = @At(value = "HEAD"), remap = true)
    private void turnPlayer(CallbackInfo ci) {
        if (this.minecraft.player != null && this.minecraft.player.isPassenger() && this.minecraft.player.getVehicle() instanceof CannonEntity) {
            this.accumulatedDX = Mth.clamp(this.accumulatedDX * 0.25D, -5.0D, 5.0D);
            this.accumulatedDY = Mth.clamp(this.accumulatedDY * 0.25D, -5.0D, 5.0D);
        }
    }
}
