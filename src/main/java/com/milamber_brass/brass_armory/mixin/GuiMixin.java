package com.milamber_brass.brass_armory.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Unique
    private static final ResourceLocation BLEEDING_ICONS_LOCATION = BrassArmory.locate("textures/gui/bleed.png");

    @WrapOperation(method = "renderHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V"))
    private void renderBleedingHeart(Gui gui, GuiGraphics graphics, Gui.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, Operation<Void> original, @Local(argsOnly = true) Player player) {
        if (!player.hasEffect(BrassArmoryEffects.BLEEDING)) {
            original.call(gui, graphics, type, x, y, hardcore, blinking, half);
            return;
        }

        // Keep the existing bleeding atlas while vanilla hearts use individual sprites.
        int index = switch (type) {
            case CONTAINER -> 0;
            case NORMAL -> 2;
            case POISIONED -> 4;
            case WITHERED -> 6;
            case ABSORBING -> 8;
            case FROZEN -> 9;
            default -> -1;
        };
        if (index < 0) {
            original.call(gui, graphics, type, x, y, hardcore, blinking, half);
            return;
        }
        int variant = type == Gui.HeartType.CONTAINER ? (blinking ? 1 : 0) : (half ? 1 : 0) + (blinking && type != Gui.HeartType.ABSORBING && type != Gui.HeartType.FROZEN ? 2 : 0);
        RenderSystem.enableBlend();
        graphics.blit(BLEEDING_ICONS_LOCATION, x, y, 16 + (index * 2 + variant) * 9, hardcore ? 45 : 0, 9, 9);
        RenderSystem.disableBlend();
    }
}
