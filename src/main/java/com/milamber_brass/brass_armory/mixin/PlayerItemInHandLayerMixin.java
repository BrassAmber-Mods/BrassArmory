package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.item.WarpCrystalItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerItemInHandLayer.class)
public abstract class PlayerItemInHandLayerMixin<S extends AvatarRenderState, M extends EntityModel<S> & ArmedModel<S> & HeadedModel> extends ItemInHandLayer<S, M> {
    public PlayerItemInHandLayerMixin(RenderLayerParent<S, M> parent) {
        super(parent);
    }

    @Inject(method = "submitArmWithItem(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", at = @At("HEAD"), cancellable = true)
    private void renderArmWithWarpCrystal(S state, ItemStackRenderState item, ItemStack stack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector collector, int light, CallbackInfo ci) {
        if (state.isUsingItem && stack.getItem() instanceof WarpCrystalItem && state.useItemHand.asArm(state.mainArm) == arm && state.swingAnimation < 1.0E-5F) {
            poseStack.pushPose();
            this.getParentModel().root().translateAndRotate(poseStack);
            ModelPart head = this.getParentModel().getHead();
            float previous = head.xRot;
            head.xRot = Mth.clamp(head.xRot, -(float) Math.PI / 6F, (float) Math.PI / 2F);
            head.translateAndRotate(poseStack);
            head.xRot = previous;
            CustomHeadLayer.translateToHead(poseStack, CustomHeadLayer.Transforms.DEFAULT);
            poseStack.translate((arm == HumanoidArm.LEFT ? -2.5F : 2.5F) / 16F, -0.75D, -1.1D);
            state.heldOnHead.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
            ci.cancel();
        }
    }
}
