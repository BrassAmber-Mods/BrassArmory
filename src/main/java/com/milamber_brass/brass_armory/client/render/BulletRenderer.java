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
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class BulletRenderer<T extends BulletEntity> extends EntityRenderer<T, EntityRenderState> {
    private static final Identifier RESOURCE_LOCATION = Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/item/guns/cannonball.png");
    private static final RenderType RENDER_TYPE = RenderTypes.entityCutout(RESOURCE_LOCATION);

    private final ModelPart core;

    public BulletRenderer(Context context) {
        super(context);
        this.shadowRadius = 0.025F;

        ModelPart modelpart = context.bakeLayer(BrassArmoryModels.BULLET_MODEL);
        this.core = modelpart.getChild("core");
    }

        public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        meshdefinition.getRoot().addOrReplaceChild("core", CubeListBuilder.create().texOffs(8, 6).addBox(-0.5F, -0.5F, -0.5F, 1F, 1F, 1F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public EntityRenderState createRenderState() { return new EntityRenderState(); }

    @Override
    public void submit(EntityRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
        stack.pushPose();
        stack.scale(1.5F, 1.5F, 1.5F);
        collector.submitModelPart(core, stack, RENDER_TYPE, state.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, state.outlineColor);
        stack.popPose();
        super.submit(state, stack, collector, camera);
    }
}
