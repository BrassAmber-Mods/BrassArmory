package com.milamber_brass.brass_armory.client.gui;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.inventory.GunContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseZ) {
        this.renderBackground(graphics);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        graphics.renderItem(this.menu.iconSlot.getItem(), i + 80, j + 24);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseZ, float partialTick) {
        super.render(graphics, mouseX, mouseZ, partialTick);
        this.renderTooltip(graphics, mouseX, mouseZ);
    }
}
