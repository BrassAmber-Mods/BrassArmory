package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.entity.CannonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "isServerControlledInventory", at = @At(value = "HEAD"), remap = true, cancellable = true)
    private void isServerControlledInventory(CallbackInfoReturnable<Boolean> cir) {
        if (this.minecraft.player != null && this.minecraft.player.isPassenger() && this.minecraft.player.getVehicle() instanceof CannonEntity cannon) {
            cir.setReturnValue(cannon.getControllingPassenger() == this.minecraft.player);
        }
    }
}
