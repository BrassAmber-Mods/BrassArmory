package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.item.WarpCrystalItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private void extractWarpCrystal(Avatar entity, AvatarRenderState state, float partialTick, CallbackInfo ci) {
        if (state.isUsingItem && entity.getUseItem().getItem() instanceof WarpCrystalItem) {
            Minecraft.getInstance().getItemModelResolver().updateForLiving(state.heldOnHead, entity.getUseItem(), ItemDisplayContext.HEAD, entity);
        }
    }
}
