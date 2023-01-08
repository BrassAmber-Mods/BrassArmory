package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractSpecialArrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class SpecialArrowRenderer extends ArrowRenderer<AbstractSpecialArrowEntity> {
    public static final ResourceLocation DIRT_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/dirt_arrow.png");
    public static final ResourceLocation EX_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/ex_arrow.png");
    public static final ResourceLocation FROST_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/frost_arrow.png");
    public static final ResourceLocation GRASS_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/grass_arrow.png");
    public static final ResourceLocation LASER_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/laser_arrow.png");
    public static final ResourceLocation ROPE_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/rope_arrow.png");
    public static final ResourceLocation SLIME_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/slime_arrow.png");
    public static final ResourceLocation WARP_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/warp_arrow.png");
    public static final ResourceLocation FIRE_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/fire_arrow.png");
    public static final ResourceLocation CONFUSION_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/confusion_arrow.png");
    public static final ResourceLocation TORCH_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/torch_arrow.png");

    public SpecialArrowRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    @Override
    public void render(AbstractSpecialArrowEntity arrow, float yaw, float partialTicks, PoseStack stack, MultiBufferSource source, int light) {
        if (!arrow.isInvisible()) {
            super.render(arrow, yaw, partialTicks, stack, source, light);
        }
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(AbstractSpecialArrowEntity entity) {
        return entity.getTextureLocation();
    }
}