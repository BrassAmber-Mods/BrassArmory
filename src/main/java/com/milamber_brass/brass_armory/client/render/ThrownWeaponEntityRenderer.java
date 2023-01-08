package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.entity.projectile.DaggerEntity;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class ThrownWeaponEntityRenderer extends EntityRenderer<AbstractThrownWeaponEntity> {
    private final ItemRenderer itemRenderer;

    public ThrownWeaponEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0F;
    }

    @Override
    public void render(AbstractThrownWeaponEntity thrownWeaponEntity, float v, float v1, PoseStack stack, MultiBufferSource bufferSource, int light) {
        if (thrownWeaponEntity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(thrownWeaponEntity) < 12.25D)) {
            stack.pushPose();
            stack.translate(0D, 0.25D, 0D);
            stack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(v1, thrownWeaponEntity.yRotO, thrownWeaponEntity.getYRot()) - 90.0F));
            boolean dagger = thrownWeaponEntity instanceof DaggerEntity;
            stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(v1, thrownWeaponEntity.xRotO, thrownWeaponEntity.getXRot()) - (dagger ? 45F : 35F)));
            if (!thrownWeaponEntity.isInGround())
                stack.mulPose(Vector3f.ZN.rotationDegrees(((float) thrownWeaponEntity.tickCount + v1) * (dagger ? 32F : 16F)));
            stack.translate(dagger ? 0.4D : -0.2D, dagger ? 0D : -0.2D, 0D);

            this.itemRenderer.renderStatic(thrownWeaponEntity.getItem(), ItemTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, stack, bufferSource, thrownWeaponEntity.getId());
            stack.popPose();
        }
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public ResourceLocation getTextureLocation(AbstractThrownWeaponEntity bombEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}