package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.BulletEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryModels;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class BulletRenderer<T extends BulletEntity> extends EntityRenderer<T> {
    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(BrassArmory.MOD_ID,"textures/item/guns/cannonball.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(RESOURCE_LOCATION);

    private final ModelPart core;

    public BulletRenderer(Context context) {
        super(context);
        this.shadowRadius = 0.025F;

        ModelPart modelpart = context.bakeLayer(BrassArmoryModels.BULLET_MODEL);
        this.core = modelpart.getChild("core");
    }

    @Nonnull
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        meshdefinition.getRoot().addOrReplaceChild("core", CubeListBuilder.create().texOffs(8, 6).addBox(-0.5F, -0.5F, -0.5F, 1F, 1F, 1F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return RESOURCE_LOCATION;
    }

    @Override
    public void render(T rift, float v, float v1, PoseStack stack, MultiBufferSource buffer, int light) {
        stack.pushPose();
        stack.scale(1.5F, 1.5F, 1.5F);
        this.core.render(stack, buffer.getBuffer(RENDER_TYPE), light, OverlayTexture.NO_OVERLAY);
        stack.popPose();
        super.render(rift, v, v1, stack, buffer, light);
    }
}