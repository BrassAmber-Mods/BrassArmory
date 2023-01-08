package com.milamber_brass.brass_armory.client.gui;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.inventory.GunContainer;
import com.milamber_brass.brass_armory.behaviour.iGun;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class GunScreen extends AbstractContainerScreen<GunContainer<iGun>> {
    private static final ResourceLocation TEXTURE = BrassArmory.locate("textures/gui/reload.png");

    public GunScreen(GunContainer<iGun> gunContainer, Inventory inventory, Component title) {
        super(gunContainer, inventory, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseZ) {
        renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        this.itemRenderer.blitOffset = 100.0F;

        this.itemRenderer.renderAndDecorateItem(this.menu.iconSlot.getItem(), i + 80, j + 24);
        this.itemRenderer.blitOffset = 0.0F;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseZ, float partialTick) {
        super.render(poseStack, mouseX, mouseZ, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseZ);
    }
}
