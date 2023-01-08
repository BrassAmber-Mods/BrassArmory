package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.QuiverItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(ItemRenderer.class)
@ParametersAreNonnullByDefault
public abstract class ItemRendererMixin {
    @Shadow public abstract BakedModel getModel(ItemStack stack, @Nullable Level level, @Nullable LivingEntity living, int i);
    @Shadow public abstract void renderModelLists(BakedModel bakedModel, ItemStack stack, int x, int y, PoseStack poseStack, VertexConsumer vertexConsumer);

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT, remap = true)
    private void renderGuiItem(ItemStack stack, ItemTransforms.TransformType transformType, boolean leftHandHackery, PoseStack poseStack, MultiBufferSource bufferSource, int x, int y, BakedModel model, CallbackInfo ci, boolean flag, boolean flag1, RenderType rendertype, VertexConsumer vertexconsumer) {
        if (stack.is(BrassArmoryItems.TORCH_ARROW.get())) {
            this.renderTorch(poseStack, ArmoryUtil.loadStack(stack.getOrCreateTag(), "BATorch", Items.TORCH.getDefaultInstance()), x, y, vertexconsumer);
        } else if (stack.getItem() instanceof QuiverItem) {
            QuiverItem.getContents(stack).findFirst().ifPresent(itemStack -> {
                poseStack.pushPose();
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                poseStack.translate(-0.9375D, 0.0625D, -0.875D);
                poseStack.scale(1.0F, 1.0F, 0.75F);
                this.renderModelLists(this.getModel(itemStack, null, null, 0), itemStack, x, y, poseStack, vertexconsumer);
                if (itemStack.is(BrassArmoryItems.TORCH_ARROW.get())) {
                    this.renderTorch(poseStack, ArmoryUtil.loadStack(itemStack.getOrCreateTag(), "BATorch", Items.TORCH.getDefaultInstance()), x, y, vertexconsumer);
                }
                poseStack.popPose();
            });
        }
    }

    @Unique
    private void renderTorch(PoseStack poseStack, ItemStack stack, int x, int y, VertexConsumer vertexconsumer) {
        poseStack.pushPose();
        poseStack.translate(0.25D, 0.25D, 0.0D);
        this.renderModelLists(this.getModel(stack, null, null, 0), stack, x, y, poseStack, vertexconsumer);
        poseStack.popPose();
    }
}
