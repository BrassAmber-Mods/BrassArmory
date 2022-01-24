package com.milamber_brass.brass_armory.client;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.item.ICustomAnimationItem;
import com.milamber_brass.brass_armory.item.MaceItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, value = Dist.CLIENT)
public class RenderHandEventSubscriber {
    @SubscribeEvent
    @ParametersAreNonnullByDefault
    public static void RenderHandEvent(RenderHandEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() instanceof ICustomAnimationItem animationItem) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player != null && player.isUsingItem() && (player.getUsedItemHand() == event.getHand() || itemStack.getItem() instanceof MaceItem)) {
                float maxCharge = animationItem.getChargeDuration(itemStack);
                float v = Math.min(animationItem.getCustomUseDuration(itemStack, player), maxCharge);
                if (v > 0) {
                    event.setCanceled(true);
                    boolean mainHandFlag = event.getHand() == InteractionHand.MAIN_HAND;
                    HumanoidArm humanoidarm = mainHandFlag ? player.getMainArm() : player.getMainArm().getOpposite();
                    boolean rightHandFlag = humanoidarm == HumanoidArm.RIGHT;
                    float rightHandFloat = rightHandFlag ? 1.0F : -1.0F;
                    TransformType transformType = rightHandFlag ? TransformType.FIRST_PERSON_RIGHT_HAND : TransformType.FIRST_PERSON_LEFT_HAND;
                    float m = v / (maxCharge * 10F);
                    double sineWave = Math.sin(animationItem.getCustomUseDuration(itemStack, player) * 200D) * 0.002D;

                    PoseStack poseStack = event.getPoseStack();
                    poseStack.pushPose();
                    poseStack.translate(0.56F * rightHandFloat, -0.52F + sineWave, -0.72F + m);
                    poseStack.mulPose(Vector3f.XP.rotationDegrees(-13.935F + m * 100F));
                    poseStack.mulPose(Vector3f.YN.rotationDegrees(rightHandFloat * 9.7F));
                    poseStack.mulPose(Vector3f.ZP.rotationDegrees(rightHandFloat * -9.785F));
                    poseStack.scale(1 + (float) sineWave, 1 + (float) sineWave, 1 + (float) sineWave);
                    new ItemInHandRenderer(mc).renderItem(player, itemStack, transformType, !rightHandFlag, poseStack, event.getMultiBufferSource(), event.getPackedLight());
                    poseStack.popPose();
                }
            }
        }
    }
}