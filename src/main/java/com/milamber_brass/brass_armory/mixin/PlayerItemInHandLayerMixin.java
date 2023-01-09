package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.item.WarpCrystalItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mixin(PlayerItemInHandLayer.class)
public abstract class PlayerItemInHandLayerMixin<T extends Player, M extends EntityModel<T> & ArmedModel & HeadedModel> extends ItemInHandLayer<T, M> {
    @Shadow @Final private ItemInHandRenderer itemInHandRenderer;

    public PlayerItemInHandLayerMixin(RenderLayerParent<T, M> layerParent, ItemInHandRenderer inHandRenderer) {
        super(layerParent, inHandRenderer);
    }

    @Inject(method = "renderArmWithItem", at = @At(value = "HEAD"), cancellable = true, remap = true)
    private void renderArmWithWarpCrystal(LivingEntity living, ItemStack stack, ItemTransforms.TransformType transformType, HumanoidArm arm, PoseStack poseStack, MultiBufferSource source, int light, CallbackInfo ci) {
        if (living.isUsingItem() && stack.getItem() instanceof WarpCrystalItem && !living.getItemInHand(living.getUsedItemHand().equals(InteractionHand.MAIN_HAND) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND).equals(stack) && living.swingTime == 0) {
            poseStack.pushPose();
            ModelPart modelpart = this.getParentModel().getHead();
            float f = modelpart.xRot;
            modelpart.xRot = Mth.clamp(modelpart.xRot, (-(float)Math.PI / 6F), ((float)Math.PI / 2F));
            modelpart.translateAndRotate(poseStack);
            modelpart.xRot = f;
            CustomHeadLayer.translateToHead(poseStack, false);
            boolean flag = arm == HumanoidArm.LEFT;
            poseStack.translate((flag ? -2.5F : 2.5F) / 16.0F, -0.75D, -1.1D);
            this.itemInHandRenderer.renderItem(living, stack, ItemTransforms.TransformType.HEAD, false, poseStack, source, light);
            poseStack.popPose();
            ci.cancel();
        }
    }
}
