package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.item.abstracts.ArmoryTieredItem;
import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.FlailHeadEntity;
import com.milamber_brass.brass_armory.item.FlailItem;
import com.milamber_brass.brass_armory.util.ItemData;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FlailHeadEntityRenderer<T extends FlailHeadEntity> extends EntityRenderer<T, FlailHeadEntityRenderer.State> {
    protected static final Identifier IRON_LINK = Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/item/iron/iron_link.png");
    protected static final Identifier NETHERITE_LINK = Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/item/netherite/netherite_link.png");
    protected static final Identifier GOLD_LINK = Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/item/gold/golden_link.png");
    protected final ItemModelResolver resolver;
    public static class State extends ThrownItemRenderState {
        public boolean visible;
        public Vec3 linkOffset;
        public int linkCount;
        public Identifier texture;
    }

    public FlailHeadEntityRenderer(Context context) {
        super(context);
        this.resolver = context.getItemModelResolver();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    @Override
    public State createRenderState() { return new State(); }

    @Override
    public void extractRenderState(T flailHead, State state, float partialTicks) {
        super.extractRenderState(flailHead, state, partialTicks);
        state.visible = flailHead.tickCount >= 2 || state.distanceToCameraSq >= 12.25;
        ItemStack stack = flailHead.getItem();
        if (stack.getItem() instanceof FlailItem flailItem) stack = flailItem.getHead().getDefaultInstance();
        ItemData.setTag(stack, ItemData.getTag(flailHead.getItem()));
        resolver.updateForNonLiving(state.item, stack, ItemDisplayContext.GROUND, flailHead);
        state.linkOffset = null;
        if (flailHead.getOwner() instanceof LivingEntity owner) extractLinks(flailHead, state, partialTicks, flailHead.getItem(), owner);
    }

    private void extractLinks(T flailHead, State state, float partialTicks, ItemStack stack, LivingEntity owner) {
        int right = owner.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        if (!(owner.getMainHandItem().getItem() instanceof FlailItem)) right = -right;

        float attackAnim = owner.getSwingAnimation(partialTicks);
        float sin1 = Mth.sin(Mth.sqrt(attackAnim) * (float)Math.PI);
        float v4 = Mth.lerp(partialTicks, owner.yBodyRotO, owner.yBodyRot) * Mth.DEG_TO_RAD;
        double sin = Mth.sin(v4);
        double cos = Mth.cos(v4);
        double v = (double)right * 0.35D;
        double flailX;
        double flailY;
        double flailZ;
        float bonusY;
        if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && owner == Minecraft.getInstance().player) {
            double d7 = 960.0D / this.entityRenderDispatcher.options.fov().get();
            Vec3 pointOnPlane = this.entityRenderDispatcher.camera.getNearPlane(this.entityRenderDispatcher.options.fov().get()).getPointOnPlane((float)right * 0.525F, -0.1F);
            pointOnPlane = pointOnPlane.scale(d7);
            pointOnPlane = pointOnPlane.yRot(sin1 * 0.5F);
            pointOnPlane = pointOnPlane.xRot(-sin1 * 0.7F);

            flailX = Mth.lerp(partialTicks, owner.xo, owner.getX()) + pointOnPlane.x;
            flailY = Mth.lerp(partialTicks, owner.yo, owner.getY()) + pointOnPlane.y + 0.25D;
            flailZ = Mth.lerp(partialTicks, owner.zo, owner.getZ()) + pointOnPlane.z;
            bonusY = owner.getEyeHeight();
        } else {
            flailX = Mth.lerp(partialTicks, owner.xo, owner.getX()) - cos * v - sin * 0.8D;
            flailY = owner.yo + (double)owner.getEyeHeight() + (owner.getY() - owner.yo) * (double)partialTicks - 0.2D;
            flailZ = Mth.lerp(partialTicks, owner.zo, owner.getZ()) - sin * v + cos * 0.8D;
            bonusY = owner.isCrouching() ? -0.1875F : 0.0F;
        }

        double headX = Mth.lerp(partialTicks, flailHead.xo, flailHead.getX());
        double headY = Mth.lerp(partialTicks, flailHead.yo, flailHead.getY());
        double headZ = Mth.lerp(partialTicks, flailHead.zo, flailHead.getZ());
        float finalX = (float)(flailX - headX);
        float finalY = (float)(flailY - headY) + bonusY;
        float finalZ = (float)(flailZ - headZ);

        state.linkOffset = new Vec3(finalX, finalY, finalZ);
        state.texture = getLinkTextureLocation(flailHead, stack.getItem() instanceof ArmoryTieredItem tieredItem ? tieredItem.getTier() : ToolMaterial.IRON);
        state.linkCount = flailHead.isNoPhysics() ? (int)Math.min(owner.distanceTo(flailHead) * 2.2, 11) : 11;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.visible) return;
        poseStack.pushPose();
        poseStack.rotate(camera.orientation);
        poseStack.rotateDegrees(Axis.YP, 180);
        state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
        if (state.linkOffset == null) return;
        poseStack.pushPose();
        poseStack.scale(1.5F, 1.5F, 1.5F);
        for (int r = 0; r < state.linkCount; r++) {
            poseStack.pushPose();
            Vec3 link = state.linkOffset.scale(.0636 * r);
            poseStack.translate(link.x, link.y + (state.linkCount - r) * .008, link.z);
            poseStack.scale(.08F, .08F, .08F);
            poseStack.rotate(camera.orientation);
            poseStack.rotateDegrees(Axis.YP, 180);
            collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(state.texture), (pose, buffer) -> {
                vertex(buffer, pose.pose(), pose.normal(), state.lightCoords, 0, 0, 0, 1);
                vertex(buffer, pose.pose(), pose.normal(), state.lightCoords, 1, 0, 1, 1);
                vertex(buffer, pose.pose(), pose.normal(), state.lightCoords, 1, 1, 1, 0);
                vertex(buffer, pose.pose(), pose.normal(), state.lightCoords, 0, 1, 0, 0);
            });
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int light, float v, int i, int j, int k) {
        Vector3f normal = matrix3f.transform(new Vector3f(0.0F, 1.0F, 0.0F));
        vertexConsumer.addVertex(matrix4f, v - 0.5F, (float)i - 0.5F, 0.0F).setColor(255, 255, 255, 255).setUv((float)j, (float)k).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(normal.x, normal.y, normal.z);
    }

    public Identifier getTextureLocation(FlailHeadEntity flailHead) {
        return IRON_LINK;
    }

        protected Identifier getLinkTextureLocation(FlailHeadEntity flailHead, ToolMaterial tier) {
        if (ToolMaterial.NETHERITE.equals(tier)) return NETHERITE_LINK;
        else if (ToolMaterial.GOLD.equals(tier) || ToolMaterial.DIAMOND.equals(tier)) return GOLD_LINK;
        return getTextureLocation(flailHead);
    }
}
