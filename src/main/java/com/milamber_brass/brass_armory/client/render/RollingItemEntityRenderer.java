package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractRollableItemProjectileEntity;
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

public class RollingItemEntityRenderer<T extends AbstractRollableItemProjectileEntity> extends EntityRenderer<T, RollingItemEntityRenderer.State> {
    private final ItemModelResolver resolver;
    public static class State extends ThrownItemRenderState {
        public float xRot, yRot, spin;
        public boolean dagger, inGround, visible;
    }
    public RollingItemEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        resolver = context.getItemModelResolver();
        shadowRadius = .15F;
        shadowStrength = .75F;
    }
    @Override
    public State createRenderState() { return new State(); }
    @Override
    public void extractRenderState(T entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        resolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.GROUND, entity);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.visible = entity.tickCount >= 2 || state.distanceToCameraSq >= 12.25;
        state.spin = Mth.lerp(partialTicks, entity.getRotation0(), entity.getRotation());
    }
    @Override
    public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.visible) return;
        pose.pushPose();
        pose.translate(0, .1, 0);
        pose.rotate(camera.orientation);
        pose.rotateDegrees(Axis.YP, 180);
        pose.rotateDegrees(Axis.ZP, state.spin);
        pose.translate(0, -.1, 0);
        state.item.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        pose.popPose();
        super.submit(state, pose, collector, camera);
    }
}
