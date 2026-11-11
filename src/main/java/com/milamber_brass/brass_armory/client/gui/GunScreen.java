package com.milamber_brass.brass_armory.client.gui;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.inventory.GunContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class GunScreen extends AbstractContainerScreen<GunContainer<iGun>> {
    private static final Identifier TEXTURE = BrassArmory.locate("textures/gui/reload.png");

    public GunScreen(GunContainer<iGun> gunContainer, Inventory inventory, Component title) {
        super(gunContainer, inventory, title, 176, 166);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        graphics.item(this.menu.iconSlot.getItem(), this.leftPos + 80, this.topPos + 24);
    }
}
