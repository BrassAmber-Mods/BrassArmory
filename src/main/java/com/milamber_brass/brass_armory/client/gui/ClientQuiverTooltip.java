package com.milamber_brass.brass_armory.client.gui;

import com.milamber_brass.brass_armory.inventory.QuiverTooltip;
import com.milamber_brass.brass_armory.item.QuiverItem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class ClientQuiverTooltip implements ClientTooltipComponent {
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/gui/container/bundle.png");
    private final NonNullList<ItemStack> items;
    private final int weight;

    public ClientQuiverTooltip(QuiverTooltip quiverTooltip) {
        this.items = quiverTooltip.getItems();
        this.weight = quiverTooltip.getWeight();
    }

    public int getHeight() {
        return this.gridSizeY() * 20 + 2 + 4;
    }

    public int getWidth(Font font) {
        return this.gridSizeX() * 18 + 2;
    }

    public void renderImage(Font font, int x, int y, PoseStack stack, ItemRenderer itemRenderer, int o) {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        boolean flag = this.weight >= QuiverItem.MAX_WEIGHT;
        int k = 0;

        for(int l = 0; l < j; ++l) {
            for(int i1 = 0; i1 < i; ++i1) {
                int j1 = x + i1 * 18 + 1;
                int k1 = y + l * 20 + 1;
                this.renderSlot(j1, k1, k++, flag, font, stack, itemRenderer, o);
            }
        }

        this.drawBorder(x, y, i, j, stack, o);
    }

    private void renderSlot(int x, int y, int i, boolean blocked, Font font, PoseStack stack, ItemRenderer itemRenderer, int o) {
        if (i >= this.items.size()) {
            this.blit(stack, x, y, o, blocked ? ClientQuiverTooltip.Texture.BLOCKED_SLOT : ClientQuiverTooltip.Texture.SLOT);
        } else {
            ItemStack itemstack = this.items.get(i);
            this.blit(stack, x, y, o, ClientQuiverTooltip.Texture.SLOT);
            itemRenderer.renderAndDecorateItem(itemstack, x + 1, y + 1, i);
            itemRenderer.renderGuiItemDecorations(font, itemstack, x + 1, y + 1);
            if (i == 0) AbstractContainerScreen.renderSlotHighlight(stack, x + 1, y + 1, o);

        }
    }

    private void drawBorder(int x, int y, int xSize, int ySize, PoseStack stack, int o) {
        this.blit(stack, x, y, o, ClientQuiverTooltip.Texture.BORDER_CORNER_TOP);
        this.blit(stack, x + xSize * 18 + 1, y, o, ClientQuiverTooltip.Texture.BORDER_CORNER_TOP);

        for(int i = 0; i < xSize; ++i) {
            this.blit(stack, x + 1 + i * 18, y, o, ClientQuiverTooltip.Texture.BORDER_HORIZONTAL_TOP);
            this.blit(stack, x + 1 + i * 18, y + ySize * 20, o, ClientQuiverTooltip.Texture.BORDER_HORIZONTAL_BOTTOM);
        }

        for(int j = 0; j < ySize; ++j) {
            this.blit(stack, x, y + j * 20 + 1, o, ClientQuiverTooltip.Texture.BORDER_VERTICAL);
            this.blit(stack, x + xSize * 18 + 1, y + j * 20 + 1, o, ClientQuiverTooltip.Texture.BORDER_VERTICAL);
        }

        this.blit(stack, x, y + ySize * 20, o, ClientQuiverTooltip.Texture.BORDER_CORNER_BOTTOM);
        this.blit(stack, x + xSize * 18 + 1, y + ySize * 20, o, ClientQuiverTooltip.Texture.BORDER_CORNER_BOTTOM);
    }

    private void blit(PoseStack stack, int x, int y, int o, ClientQuiverTooltip.Texture texture) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        GuiComponent.blit(stack, x, y, o, (float)texture.x, (float)texture.y, texture.w, texture.h, 128, 128);
    }

    private int gridSizeX() {
        return Math.max(2, (int)Math.ceil(Math.sqrt((double)this.items.size() + 1.0D)));
    }

    private int gridSizeY() {
        return (int)Math.ceil(((double)this.items.size() + 1.0D) / (double)this.gridSizeX());
    }

    enum Texture {
        SLOT(0, 0, 18, 20),
        BLOCKED_SLOT(0, 40, 18, 20),
        BORDER_VERTICAL(0, 18, 1, 20),
        BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
        BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
        BORDER_CORNER_TOP(0, 20, 1, 1),
        BORDER_CORNER_BOTTOM(0, 60, 1, 1);

        public final int x;
        public final int y;
        public final int w;
        public final int h;

        Texture(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }
}