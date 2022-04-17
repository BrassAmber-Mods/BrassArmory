package com.milamber_brass.brass_armory.client.gui;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.container.GunContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

public class GunScreen extends AbstractContainerScreen<GunContainer> {
    private static final ResourceLocation TEXTURE = BrassArmory.locate("textures/gui/reload.png");

    public GunScreen(GunContainer gunContainer, Inventory inventory, Component title) {
        super(gunContainer, inventory, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    @ParametersAreNonnullByDefault
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
        Minecraft mc = this.minecraft;
        if (mc != null) {
            LocalPlayer player = mc.player;
            if (player == null) return;
            ItemStack stack = ItemStack.EMPTY;
            if (player.getMainHandItem().hasTag() && player.getMainHandItem().getOrCreateTag().getBoolean("InGunContainerMenu")) {
                stack = player.getMainHandItem();
            } else if (player.getOffhandItem().hasTag() && player.getOffhandItem().getOrCreateTag().getBoolean("InGunContainerMenu")) {
                stack = player.getOffhandItem();
            }
            this.itemRenderer.renderAndDecorateItem(stack, i + 80, j + 24);
        }
        this.itemRenderer.blitOffset = 0.0F;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(PoseStack poseStack, int mouseX, int mouseZ, float partialTick) {
        super.render(poseStack, mouseX, mouseZ, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseZ);
    }
}
