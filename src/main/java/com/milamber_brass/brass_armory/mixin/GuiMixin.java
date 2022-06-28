package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.client.gui.GuiComponent.GUI_ICONS_LOCATION;

@Mixin(Gui.class)
@ParametersAreNonnullByDefault
public class GuiMixin {
    private static final ResourceLocation BLEEDING_ICONS_LOCATION = new ResourceLocation(BrassArmory.MOD_ID, "textures/gui/bleed.png");

    @Inject(method = "renderHearts", at = @At(value = "HEAD"), remap = true)
    private void head(PoseStack stack, Player player, int left, int top, int rowHeight, int regen, float healthMax, int health, int healthLast, int absorb, boolean highlight, CallbackInfo ci) {
        if (player.hasEffect(BrassArmoryEffects.BLEEDING.get())) {
            RenderSystem.setShaderTexture(0, BLEEDING_ICONS_LOCATION);
        }
    }

    @Inject(method = "renderHearts", at = @At(value = "TAIL"), remap = true)
    private void tail(PoseStack stack, Player player, int left, int top, int rowHeight, int regen, float healthMax, int health, int healthLast, int absorb, boolean highlight, CallbackInfo ci) {
        if (player.hasEffect(BrassArmoryEffects.BLEEDING.get())) {
            RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
        }
    }
}
