package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.FlailHeadEntity;
import com.milamber_brass.brass_armory.item.FlailItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class FlailHeadEntityRenderer<T extends FlailHeadEntity> extends EntityRenderer<T> {
    protected static final ResourceLocation IRON_LINK = new ResourceLocation(BrassArmory.MOD_ID,"textures/item/iron/iron_link.png");
    protected static final ResourceLocation NETHERITE_LINK = new ResourceLocation(BrassArmory.MOD_ID,"textures/item/netherite/netherite_link.png");
    protected static final ResourceLocation GOLD_LINK = new ResourceLocation(BrassArmory.MOD_ID,"textures/item/gold/golden_link.png");
    protected final ItemRenderer itemRenderer;

    public FlailHeadEntityRenderer(Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    @Override
    public void render(T flailHead, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (flailHead.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(flailHead) < 12.25D)) {
            poseStack.pushPose();
            ItemStack stack = flailHead.getItem();
            if (stack.getItem() instanceof FlailItem flailItem) stack = flailItem.getHead().getDefaultInstance();
            stack.setTag(flailHead.getItem().getTag());
            poseStack.translate(0D, 0.1D, 0D);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            poseStack.translate(0D, -0.1D, 0D);
            BakedModel bakedmodel = this.itemRenderer.getModel(stack, flailHead.level, null, flailHead.getId());
            this.itemRenderer.render(stack, ItemTransforms.TransformType.GROUND, false, poseStack, bufferSource, light, OverlayTexture.NO_OVERLAY, bakedmodel);
            poseStack.popPose();
            super.render(flailHead, yaw, partialTicks, poseStack, bufferSource, light);
            Entity entity = flailHead.getOwner();
            if (entity instanceof LivingEntity living) this.renderLinks(flailHead, partialTicks, poseStack, bufferSource, light, flailHead.getItem(), living);
        }
    }

    private <E extends LivingEntity> void renderLinks(T flailHead, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, ItemStack stack, E owner) {
        poseStack.pushPose();
        int right = owner.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        if (!(owner.getMainHandItem().getItem() instanceof FlailItem)) right = -right;

        float attackAnim = owner.getAttackAnim(partialTicks);
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
            Vec3 pointOnPlane = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float)right * 0.525F, -0.1F);
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

        poseStack.scale(1.5F, 1.5F, 1.5F);
        ResourceLocation linkLocation = this.getLinkTextureLocation(flailHead, (stack.getItem() instanceof TieredItem tieredItem ? tieredItem.getTier() : Tiers.IRON));

        int maxR = flailHead.isNoPhysics() ? (int)Math.min(owner.distanceTo(flailHead) * 2.2D, 11D) : 11;
        for (int r = 0; r < maxR; r++) {
            poseStack.pushPose();
            Vec3 linkPos = new Vec3(finalX, finalY, finalZ).scale(0.0636D * r);
            poseStack.translate(linkPos.x, linkPos.y + ((maxR - r) * 0.008), linkPos.z);
            poseStack.scale(0.08F, 0.08F, 0.08F);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            Pose pose = poseStack.last();
            Matrix4f matrix4f = pose.pose();
            Matrix3f matrix3f = pose.normal();
            VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(linkLocation));
            vertex(vertexconsumer, matrix4f, matrix3f, light, 0.0F, 0, 0, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, light, 1.0F, 0, 1, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, light, 1.0F, 1, 1, 0);
            vertex(vertexconsumer, matrix4f, matrix3f, light, 0.0F, 1, 0, 0);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int light, float v, int i, int j, int k) {
        vertexConsumer.vertex(matrix4f, v - 0.5F, (float)i - 0.5F, 0.0F).color(255, 255, 255, 255).uv((float)j, (float)k).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(FlailHeadEntity flailHead) {
        return IRON_LINK;
    }

    @Nonnull
    protected ResourceLocation getLinkTextureLocation(FlailHeadEntity flailHead, Tier tier) {
        if (Tiers.NETHERITE.equals(tier)) return NETHERITE_LINK;
        else if (Tiers.GOLD.equals(tier) || Tiers.DIAMOND.equals(tier)) return GOLD_LINK;
        return getTextureLocation(flailHead);
    }
}
