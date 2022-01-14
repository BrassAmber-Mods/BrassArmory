package com.milamber_brass.brass_armory.client;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.item.BoomerangItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID)
public class RenderLivingEventSubscriber {
    @SubscribeEvent
    public static void RenderLivingEvent(RenderLivingEvent<?, ?> event) {
        Minecraft mc = Minecraft.getInstance();
        Entity playerEntity = mc.getCameraEntity();
        if (playerEntity instanceof LivingEntity livingPlayer && livingPlayer.isUsingItem()) {
            ItemStack itemStack = livingPlayer.getUseItem();
            if (itemStack.getItem() instanceof BoomerangItem) {
                Entity target = BoomerangItem.getTargetEntity(itemStack, livingPlayer.level);
                if (target == event.getEntity() && target != null) {
                    double yOffset = (double)target.getBbHeight() + 1D;
                    Random ran = target.level.getRandom();
                    if (ran.nextFloat(100F) + 1F <= BoomerangItem.getCrit(itemStack)) {
                        Vec3 randomVec = target.position().add(ran.nextDouble(8D) / 10D - 0.4D, yOffset + ran.nextDouble(8D) / 10D - 0.4D, ran.nextDouble(8D) / 10D - 0.4D);
                        target.level.addParticle(ParticleTypes.CRIT, randomVec.x, randomVec.y, randomVec.z, 0, 0, 0);
                    }

                    PoseStack poseStack = event.getPoseStack();
                    poseStack.pushPose();
                    poseStack.translate(0D, yOffset, 0D);
                    poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                    new ItemInHandRenderer(mc).renderItem(livingPlayer, itemStack, ItemTransforms.TransformType.FIXED, false, poseStack, event.getMultiBufferSource(), event.getPackedLight());
                    poseStack.popPose();
                }
            }
        }
    }

}