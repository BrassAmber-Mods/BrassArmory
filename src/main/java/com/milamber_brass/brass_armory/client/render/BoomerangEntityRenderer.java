package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.entity.projectile.BoomerangEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class BoomerangEntityRenderer extends EntityRenderer<BoomerangEntity> {
    private final ItemRenderer itemRenderer;

    public BoomerangEntityRenderer(Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.3F;
        this.shadowStrength = 0.75F;
    }

    @ParametersAreNonnullByDefault //Pretty much like Thrown Item Renderer, just makes it spin and updates the fuse state
    public void render(BoomerangEntity boomerangEntity, float v, float v1, PoseStack stack, MultiBufferSource bufferSource, int light) {
        stack.pushPose();
        stack.translate(0D, 0.1D, 0D);
        stack.mulPose(Vector3f.XP.rotationDegrees(90F));
        stack.mulPose(Vector3f.ZN.rotationDegrees(boomerangEntity.tickCount * 8F));
        this.itemRenderer.renderStatic(boomerangEntity.getItem(), ItemTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, stack, bufferSource, boomerangEntity.getId());
        stack.popPose();
    }

    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public ResourceLocation getTextureLocation(BoomerangEntity bombEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
