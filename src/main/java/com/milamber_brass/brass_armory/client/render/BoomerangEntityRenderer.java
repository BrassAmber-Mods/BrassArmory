package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.entity.projectile.BoomerangEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

public class BoomerangEntityRenderer extends EntityRenderer<BoomerangEntity, BoomerangEntityRenderer.State> {
    private final ItemModelResolver resolver;
    public static class State extends ThrownItemRenderState {
        public float xRot, yRot, spin;
        public boolean dagger, inGround, visible;
    }
    public BoomerangEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        resolver = context.getItemModelResolver();
        shadowRadius = .3F;
        shadowStrength = .75F;
    }
    @Override
    public State createRenderState() { return new State(); }
    @Override
    public void extractRenderState(BoomerangEntity entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        resolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.FIXED, entity);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.visible = entity.tickCount >= 2 || state.distanceToCameraSq >= 12.25;
        state.spin = Math.min(entity.tickCount, entity.spin) * 8F;
    }
    @Override
    public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.visible) return;
        pose.pushPose();
        pose.translate(0, .1, 0);
        pose.rotateDegrees(Axis.XP, 90);
        pose.rotateDegrees(Axis.ZN, state.spin);
        state.item.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        pose.popPose();
        super.submit(state, pose, collector, camera);
    }
}
