package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.behaviour.GunBehaviours;
import com.milamber_brass.brass_armory.client.model.CannonModel;
import com.milamber_brass.brass_armory.entity.CannonEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class CannonEntityRenderer extends EntityRenderer<CannonEntity, CannonEntityRenderer.State> {
    private static final Identifier RESOURCE_LOCATION = Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/cannon.png");
    private static final Identifier[] RESOURCE_LOCATIONS = {
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/0.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/1.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/2.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/3.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/4.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/5.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/6.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/7.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/8.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/9.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/10.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/11.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/12.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/13.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/14.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/15.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/16.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/17.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/18.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/19.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/20.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/21.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/22.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/23.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/24.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/25.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/26.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/27.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/28.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/29.png"),
            Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/cannon/30.png")
    };
    private final CannonModel model;

    public CannonEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CannonModel(context.bakeLayer(BrassArmoryModels.CANNON_MODEL));
    }

    public static class State extends EntityRenderState {
        public float xRot, yRot, hurt, recoil;
        public int fuse;
        public double offset;
    }

    @Override
    protected net.minecraft.world.phys.AABB getBoundingBoxForCulling(CannonEntity entity, float partialTicks) { return entity.getBoundingBox().inflate(1.2D); }

    @Override
    public State createRenderState() { return new State(); }

    @Override
    public void extractRenderState(CannonEntity cannon, State state, float partialTicks) {
        super.extractRenderState(cannon, state, partialTicks);
        state.yRot = cannon.getYRot(partialTicks);
        state.xRot = cannon.getViewXRot(partialTicks);
        LivingEntity living = cannon.getControllingPassenger();
        if (living != null) {
            state.yRot = living.getViewYRot(partialTicks);
            state.xRot = Mth.clamp(living.getViewXRot(partialTicks), -45F, 15F);
        }
        float hurt = cannon.getHurtTime() - partialTicks;
        state.hurt = hurt > 0 ? Mth.sin(hurt) * hurt * (cannon.getDamage() - partialTicks) / 200F * cannon.getHurtDir() : 0;
        BlockPos pos = cannon.blockPosition();
        state.offset = ((pos.getX() % 2) + (pos.getZ() % 2) * 2) * .0003;
        state.fuse = cannon.getFuse();
        state.recoil = state.fuse > 28 && GunBehaviours.getPowderBehaviour(cannon.getPowder()).isPresent()
                ? Mth.lerp(partialTicks, getCannonZOffset(state.fuse - 1), getCannonZOffset(state.fuse)) : 0;
    }

    @Override
    public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        pose.pushPose();
        pose.rotateDegrees(Axis.XP, state.hurt);
        pose.rotateDegrees(Axis.ZP, 180);
        pose.translate(state.offset, -1.5 + state.offset, state.offset);
        collector.submitModel(model, state, pose, RESOURCE_LOCATION, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        if (state.fuse <= 30) collector.submitModel(model, state, pose, RESOURCE_LOCATIONS[Mth.clamp(state.fuse, 0, 30)], state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        pose.popPose();
        super.submit(state, pose, collector, camera);
    }

    private float getCannonZOffset(int i) {
        return switch (i) { case 30 -> 3F; case 31 -> 1.5F; default -> 0F; };
    }
}
