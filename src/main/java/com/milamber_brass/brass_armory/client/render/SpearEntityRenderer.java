package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SpearEntityRenderer extends EntityRenderer<AbstractThrownWeaponEntity> {
    private final ItemRenderer itemRenderer;

    public SpearEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.3F;
        this.shadowStrength = 0.75F;
    }

    @ParametersAreNonnullByDefault
    public void render(AbstractThrownWeaponEntity spearEntity, float v, float v1, PoseStack stack, MultiBufferSource bufferSource, int light) {
        if (spearEntity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(spearEntity) < 12.5D)) {
            stack.pushPose();
            stack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(v1, spearEntity.yRotO, spearEntity.getYRot()) - 90.0F));
            stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(v1, spearEntity.xRotO, spearEntity.getXRot()) - 45F));
            stack.translate(-0.6D, -0.6D, 0D);
            stack.scale(2F, 2F, 1);
            this.itemRenderer.renderStatic(spearEntity.getItem(), ItemTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, stack, bufferSource, spearEntity.getId());
            stack.popPose();
            super.render(spearEntity, v, v1, stack, bufferSource, light);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public ResourceLocation getTextureLocation(AbstractThrownWeaponEntity spearEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
