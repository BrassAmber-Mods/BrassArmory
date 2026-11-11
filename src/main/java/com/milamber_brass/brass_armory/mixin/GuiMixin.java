package com.milamber_brass.brass_armory.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryEffects;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public abstract class GuiMixin {
    @Unique
    private static final Identifier BLEEDING_ICONS_LOCATION = BrassArmory.locate("textures/gui/bleed.png");

    @WrapOperation(method = "extractHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;extractHeart(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Hud$HeartType;IIZZZ)V"))
    private void renderBleedingHeart(Hud gui, GuiGraphicsExtractor graphics, Hud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, Operation<Void> original, @Local(argsOnly = true) Player player) {
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
        int variant = type == Hud.HeartType.CONTAINER ? (blinking ? 1 : 0) : (half ? 1 : 0) + (blinking && type != Hud.HeartType.ABSORBING && type != Hud.HeartType.FROZEN ? 2 : 0);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BLEEDING_ICONS_LOCATION, x, y, 16 + (index * 2 + variant) * 9, hardcore ? 45 : 0, 9, 9, 256, 256);
    }
}
