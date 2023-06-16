package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class SpearEntityRenderer extends EntityRenderer<AbstractThrownWeaponEntity> {
    private final ItemRenderer itemRenderer;

    public SpearEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.3F;
        this.shadowStrength = 0.75F;
    }

    @Override
    public void render(AbstractThrownWeaponEntity spearEntity, float v, float v1, PoseStack stack, MultiBufferSource bufferSource, int light) {
        if (spearEntity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(spearEntity) < 12.5D)) {
            stack.pushPose();
            stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(v1, spearEntity.yRotO, spearEntity.getYRot()) - 90.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(v1, spearEntity.xRotO, spearEntity.getXRot()) - 45F));
            stack.translate(-0.6D, -0.6D, 0D);
            stack.scale(2F, 2F, 1);
            this.itemRenderer.renderStatic(spearEntity.getItem(), ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, stack, bufferSource, spearEntity.level(), spearEntity.getId());
            stack.popPose();
            super.render(spearEntity, v, v1, stack, bufferSource, light);
        }
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(AbstractThrownWeaponEntity spearEntity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
