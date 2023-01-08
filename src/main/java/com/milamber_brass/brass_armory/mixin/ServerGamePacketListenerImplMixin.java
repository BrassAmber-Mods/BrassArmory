package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.entity.CannonEntity;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;

    @Inject(method = "handlePlayerCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getVehicle()Lnet/minecraft/world/entity/Entity;", ordinal = 4, shift = At.Shift.BEFORE), remap = true)
    private void handlePlayerCommand(ServerboundPlayerCommandPacket p_9891_, CallbackInfo ci) {
        if (this.player.getVehicle() instanceof CannonEntity cannon && cannon.getFuse() == 0) cannon.openInventory(this.player);
    }
}
