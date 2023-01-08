package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.behaviour.GunBehaviours;
import com.milamber_brass.brass_armory.client.model.CannonModel;
import com.milamber_brass.brass_armory.entity.CannonEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class CannonEntityRenderer extends EntityRenderer<CannonEntity> {
    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/cannon.png");
    private static final ResourceLocation[] RESOURCE_LOCATIONS = {
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/0.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/1.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/2.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/3.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/4.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/5.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/6.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/7.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/8.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/9.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/10.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/11.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/12.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/13.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/14.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/15.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/16.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/17.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/18.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/19.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/20.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/21.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/22.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/23.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/24.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/25.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/26.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/27.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/28.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/29.png"),
            new ResourceLocation(BrassArmory.MOD_ID,"textures/entity/cannon/30.png")
    };
    private final CannonModel<CannonEntity> model;

    public CannonEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CannonModel<>(context.bakeLayer(BrassArmoryModels.CANNON_MODEL));
    }

    @Override
    public void render(CannonEntity cannon, float yaw, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int light) {
        stack.pushPose();

        float yRot = Mth.lerp(partialTicks, cannon.yRotO, cannon.getYRot());
        float xRot = cannon.getViewXRot(partialTicks);

        if (cannon.getControllingPassenger() instanceof LivingEntity living) {
            yRot = living.getViewYRot(partialTicks);
            xRot = Mth.clamp(living.getViewXRot(partialTicks), -45F, 15F);

            living.yBodyRot = living.getYRot();
            living.yBodyRotO = living.yRotO;
        }

        stack.pushPose();

        float f = (float)cannon.getHurtTime() - partialTicks;
        float f1 = cannon.getDamage() - partialTicks;

        if (f > 0.0F) {
            stack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(f) * f * f1 / 200.0F * (float)cannon.getHurtDir()));
        }

        stack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));

        BlockPos pos = cannon.blockPosition();
        double offset = (double)((pos.getX() % 2) + ((pos.getZ() % 2) * 2)) * 0.0003D;//This is so if you put multiple cannons next to each other, they don't Z-fight

        stack.translate(offset, -1.5D + offset, offset);
        float z = this.model.cannon_end.z;
        int fuse = cannon.getFuse();
        this.model.setupAnim(cannon, yaw, fuse, partialTicks, yRot, xRot);
        if (fuse > 28 && GunBehaviours.getPowderBehaviour(cannon.getPowder()).isPresent()) {
            this.model.cannon_end.z = z - Mth.lerp(partialTicks, this.getCannonZOffset(fuse - 1), this.getCannonZOffset(fuse));
        }

        VertexConsumer vertexConsumer = bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(cannon)));
        this.model.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        if (fuse <= 30) {
            VertexConsumer fuseConsumer = bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(fuse)));
            this.model.renderToBuffer(stack, fuseConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        this.model.cannon_end.z = z;
        stack.popPose();


        stack.popPose();
        super.render(cannon, yaw, partialTicks, stack, bufferSource, light);
    }

    private float getCannonZOffset(int i) {
        return switch (i) {
            case 30 -> 3.0F;
            case 31 -> 1.5F;
            default -> 0.0F;
        };
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(CannonEntity cannon) {
        return RESOURCE_LOCATION;
    }

    public @NotNull ResourceLocation getTextureLocation(int fuse) {
        return RESOURCE_LOCATIONS[Mth.clamp(fuse, 0, 30)];
    }
}
