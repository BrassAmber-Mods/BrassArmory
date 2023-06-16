package com.milamber_brass.brass_armory.client.gui;

import com.milamber_brass.brass_armory.inventory.QuiverTooltip;
import com.milamber_brass.brass_armory.item.QuiverItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
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

    @Override
    public int getHeight() {
        return this.gridSizeY() * 20 + 2 + 4;
    }

    @Override
    public int getWidth(Font font) {
        return this.gridSizeX() * 18 + 2;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        boolean flag = this.weight >= QuiverItem.MAX_WEIGHT;
        int k = 0;

        for(int l = 0; l < j; ++l) {
            for(int i1 = 0; i1 < i; ++i1) {
                int j1 = x + i1 * 18 + 1;
                int k1 = y + l * 20 + 1;
                this.renderSlot(j1, k1, k++, flag, graphics, font);
            }
        }

        this.drawBorder(x, y, i, j, graphics);
    }

    private void renderSlot(int x, int y, int i, boolean blocked, GuiGraphics graphics, Font font) {
        if (i >= this.items.size()) {
            this.blit(graphics, x, y, blocked ? ClientQuiverTooltip.Texture.BLOCKED_SLOT : ClientQuiverTooltip.Texture.SLOT);
        } else {
            ItemStack itemstack = this.items.get(i);
            this.blit(graphics, x, y, ClientQuiverTooltip.Texture.SLOT);
            graphics.renderItem(itemstack, x + 1, y + 1, i);
            graphics.renderItemDecorations(font, itemstack, x + 1, y + 1);
            if (i == 0) AbstractContainerScreen.renderSlotHighlight(graphics, x + 1, y + 1, 0);
        }
    }

    private void drawBorder(int x, int y, int xSize, int ySize, GuiGraphics graphics) {
        this.blit(graphics, x, y, ClientQuiverTooltip.Texture.BORDER_CORNER_TOP);
        this.blit(graphics, x + xSize * 18 + 1, y, ClientQuiverTooltip.Texture.BORDER_CORNER_TOP);

        for(int i = 0; i < xSize; ++i) {
            this.blit(graphics, x + 1 + i * 18, y, ClientQuiverTooltip.Texture.BORDER_HORIZONTAL_TOP);
            this.blit(graphics, x + 1 + i * 18, y + ySize * 20, ClientQuiverTooltip.Texture.BORDER_HORIZONTAL_BOTTOM);
        }

        for(int j = 0; j < ySize; ++j) {
            this.blit(graphics, x, y + j * 20 + 1, ClientQuiverTooltip.Texture.BORDER_VERTICAL);
            this.blit(graphics, x + xSize * 18 + 1, y + j * 20 + 1, ClientQuiverTooltip.Texture.BORDER_VERTICAL);
        }

        this.blit(graphics, x, y + ySize * 20, ClientQuiverTooltip.Texture.BORDER_CORNER_BOTTOM);
        this.blit(graphics, x + xSize * 18 + 1, y + ySize * 20, ClientQuiverTooltip.Texture.BORDER_CORNER_BOTTOM);
    }

    private void blit(GuiGraphics graphics, int x, int y, ClientQuiverTooltip.Texture texture) {
        graphics.blit(TEXTURE_LOCATION, x, y, 0, (float)texture.x, (float)texture.y, texture.w, texture.h, 128, 128);
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