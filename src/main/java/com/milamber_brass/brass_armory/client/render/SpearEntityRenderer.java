package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
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

public class SpearEntityRenderer extends EntityRenderer<AbstractThrownWeaponEntity, SpearEntityRenderer.State> {
    private final ItemModelResolver resolver;
    public static class State extends ThrownItemRenderState {
        public float xRot, yRot, spin;
        public boolean dagger, inGround, visible;
    }
    public SpearEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        resolver = context.getItemModelResolver();
        shadowRadius = .3F;
        shadowStrength = .75F;
    }
    @Override
    public State createRenderState() { return new State(); }
    @Override
    public void extractRenderState(AbstractThrownWeaponEntity entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        resolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.FIXED, entity);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.visible = entity.tickCount >= 2 || state.distanceToCameraSq >= 12.25;
        
    }
    @Override
    public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.visible) return;
        pose.pushPose();
        pose.rotateDegrees(Axis.YP, state.yRot - 90);
        pose.rotateDegrees(Axis.ZP, state.xRot - 45);
        pose.translate(-.6, -.6, 0);
        pose.scale(2, 2, 1);
        state.item.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        pose.popPose();
        super.submit(state, pose, collector, camera);
    }
}
