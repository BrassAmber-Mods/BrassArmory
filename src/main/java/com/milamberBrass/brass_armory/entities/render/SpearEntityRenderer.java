package com.milamberBrass.brass_armory.entities.render;

import com.milamberBrass.brass_armory.BrassArmory;
import com.milamberBrass.brass_armory.entities.SpearEntity;
import com.milamberBrass.brass_armory.entities.model.SpearModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SpearEntityRenderer extends EntityRenderer<SpearEntity> {

    public static final ResourceLocation SPEAR = new ResourceLocation(BrassArmory.MOD_ID,"textures/item/wood_spear.png");
    private final SpearModel spear_model = new SpearModel(SPEAR);

    public SpearEntityRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @ParametersAreNonnullByDefault
    public void render(SpearEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.yRotO, entityIn.yRot) - 90.0F));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot) + 90.0F));
        IVertexBuilder ivertexbuilder = net.minecraft.client.renderer.ItemRenderer.getFoilBufferDirect(bufferIn, this.spear_model.renderType(this.getTextureLocation(entityIn)), false, false);
        this.spear_model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * @return Returns the location of an entity's texture.
     */
    @Nonnull
    public ResourceLocation getTextureLocation(SpearEntity entity) {
        return  entity.getTierResourceLocation();
    }

}
