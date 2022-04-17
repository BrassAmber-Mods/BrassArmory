package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractRollableItemProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class RollingItemEntityRenderer<T extends AbstractRollableItemProjectile> extends EntityRenderer<T> {
    private final ItemRenderer itemRenderer;

    public RollingItemEntityRenderer(Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    @ParametersAreNonnullByDefault //Pretty much like Thrown Item Renderer, just makes it roll
    public void render(T itemProjectile, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (itemProjectile.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(itemProjectile) < 12.25D)) {
            poseStack.pushPose();
            ItemStack stack = itemProjectile.getItem();
            poseStack.translate(0D, 0.1D, 0D);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            //poseStack.last().pose()
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(itemProjectile.getRotation() + partialTicks));
            poseStack.translate(0D, -0.1D, 0D);
            BakedModel bakedmodel = this.itemRenderer.getModel(stack, itemProjectile.level, null, itemProjectile.getId());
            this.itemRenderer.render(stack, ItemTransforms.TransformType.GROUND, false, poseStack, bufferSource, light, OverlayTexture.NO_OVERLAY, bakedmodel);
            poseStack.popPose();
            super.render(itemProjectile, yaw, partialTicks, poseStack, bufferSource, light);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public ResourceLocation getTextureLocation(AbstractRollableItemProjectile itemProjectile) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
