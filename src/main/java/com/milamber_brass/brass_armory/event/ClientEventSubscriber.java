package com.milamber_brass.brass_armory.event;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.item.LongBowItem;
import com.milamber_brass.brass_armory.item.MaceItem;
import com.milamber_brass.brass_armory.item.abstracts.AbstractGunItem;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import com.milamber_brass.brass_armory.item.interfaces.ICustomAnimationItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, value = Dist.CLIENT)
public class ClientEventSubscriber {
    @SubscribeEvent
    @ParametersAreNonnullByDefault
    public static void RenderHandEvent(RenderHandEvent event) {Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player != null && player.isUsingItem()) {
            ItemStack eventItem = event.getItemStack();
            if (eventItem.getItem() instanceof ICustomAnimationItem || eventItem.getItem() instanceof LongBowItem || (eventItem.getItem() instanceof AbstractThrownWeaponItem && eventItem.getItem().getUseAnimation(player.getUseItem()) == UseAnim.SPEAR)) {
                event.setCanceled(true);
                boolean doubleMace = player.getMainHandItem().getItem() instanceof MaceItem && player.getOffhandItem().getItem() instanceof MaceItem;


                if (player.getUsedItemHand() == event.getHand() || doubleMace) {
                    boolean mainHandFlag = event.getHand() == InteractionHand.MAIN_HAND;
                    HumanoidArm humanoidarm = mainHandFlag ? player.getMainArm() : player.getMainArm().getOpposite();
                    boolean rightHandFlag = humanoidarm == HumanoidArm.RIGHT;
                    float k = rightHandFlag ? 1F : -1F;
                    TransformType transformType = rightHandFlag ? TransformType.FIRST_PERSON_RIGHT_HAND : TransformType.FIRST_PERSON_LEFT_HAND;
                    PoseStack poseStack = event.getPoseStack();
                    ItemStack itemStack = event.getItemStack();
                    if (doubleMace) poseStack.pushPose();

                    float f8 = (float) itemStack.getUseDuration() - (float) player.getUseItemRemainingTicks();
                    poseStack.translate(k * 0.56F, -0.52F, -0.72F);
                    if (itemStack.getItem() instanceof ICustomAnimationItem animationItem) {
                        float maxCharge = animationItem.getChargeDuration(itemStack);
                        float v = Math.min(f8, maxCharge);
                        event.setCanceled(true);
                        float m = v / (maxCharge * 10F);
                        double sineWave = Math.sin(f8 * 200D) * 0.002D;

                        poseStack.translate(0, sineWave + m, m);
                        poseStack.mulPose(Vector3f.XP.rotationDegrees(-13.935F + m * 200F));
                        poseStack.mulPose(Vector3f.YN.rotationDegrees(k * 9.7F));
                        poseStack.mulPose(Vector3f.ZP.rotationDegrees(k * -9.785F));
                        poseStack.scale(1 + (float) sineWave, 1 + (float) sineWave, 1 + (float) sineWave);
                    } else if (itemStack.getItem() instanceof LongBowItem) {
                        poseStack.translate(k * -0.2785682F, 0.18344387F, 0.15731531F);
                        poseStack.mulPose(Vector3f.XP.rotationDegrees(-13.935F));
                        poseStack.mulPose(Vector3f.YP.rotationDegrees(k * 35.3F));
                        poseStack.mulPose(Vector3f.ZP.rotationDegrees(k * -9.785F));
                        float f12 = f8 / 30.0F;
                        f12 = (f12 * f12 + f12 * 2.0F) / 3.0F;
                        if (f12 > 1.0F) f12 = 1.0F;

                        if (f12 > 0.1F) {
                            float f15 = Mth.sin((f8 - 0.1F) * 1.3F);
                            float f18 = f12 - 0.1F;
                            float f20 = f15 * f18;
                            poseStack.translate(f20 * 0.0F, f20 * 0.004F, f20 * 0.0F);
                        }

                        poseStack.translate(f12 * 0.0F, f12 * 0.0F, f12 * 0.04F);
                        poseStack.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);
                        poseStack.mulPose(Vector3f.YN.rotationDegrees(k * 45.0F));
                    } else {
                        poseStack.translate(k * -0.5F, 0.7F, 0.1F);
                        poseStack.mulPose(Vector3f.XP.rotationDegrees(-55.0F));
                        poseStack.mulPose(Vector3f.YP.rotationDegrees(k * 35.3F));
                        poseStack.mulPose(Vector3f.ZP.rotationDegrees(k * -9.785F));
                        float f11 = f8 / (eventItem.getItem() instanceof AbstractThrownWeaponItem item ? item.chargeDuration : 10.0F);
                        if (f11 > 1.0F) f11 = 1.0F;

                        if (f11 > 0.1F) {
                            float f14 = Mth.sin((f8 - 0.1F) * 1.3F);
                            float f17 = f11 - 0.1F;
                            float f19 = f14 * f17;
                            poseStack.translate(f19 * 0.0F, f19 * 0.004F, f19 * 0.0F);
                        }

                        poseStack.translate(0.0D, 0.0D, f11 * 0.2F);
                        poseStack.scale(1.0F, 1.0F, 1.0F + f11 * 0.2F);
                        poseStack.mulPose(Vector3f.YN.rotationDegrees(k * 45.0F));
                    }
                    new ItemInHandRenderer(mc).renderItem(player, itemStack, transformType, !rightHandFlag, poseStack, event.getMultiBufferSource(), event.getPackedLight());
                    if (doubleMace) poseStack.popPose();
                }
            }
        }
    }

    @SubscribeEvent
    @ParametersAreNonnullByDefault
    public static void RenderPlayerEvent(RenderPlayerEvent.Pre event) {
        Player player = event.getPlayer();
        if (player.getMainHandItem().getItem() instanceof AbstractGunItem && AbstractGunItem.getLoad(player.getMainHandItem()) == 2) {
            if (player.getMainArm() == HumanoidArm.RIGHT)
                event.getRenderer().getModel().rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            else event.getRenderer().getModel().leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
        } else if (player.getOffhandItem().getItem() instanceof AbstractGunItem && AbstractGunItem.getLoad(player.getOffhandItem()) == 2) {
            if (player.getMainArm() == HumanoidArm.LEFT)
                event.getRenderer().getModel().rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            else event.getRenderer().getModel().leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
        }
    }

    @SubscribeEvent
    @ParametersAreNonnullByDefault
    public static void FOVUpdate(EntityViewRenderEvent.FieldOfView event) {
        if (event.getCamera().getEntity() instanceof LivingEntity living && living.isUsingItem()) {
            ItemStack useStack = living.getUseItem();
            Item useItem = useStack.getItem();
            if (useItem instanceof LongBowItem || useItem instanceof ICustomAnimationItem || useItem instanceof AbstractThrownWeaponItem) {
                float i = living.getTicksUsingItem() + (float) event.getPartialTicks();

                float f9;
                if (useItem instanceof AbstractThrownWeaponItem thrownWeaponItem) f9 = thrownWeaponItem.chargeDuration;
                else if (useItem instanceof ICustomAnimationItem customAnimationItem) f9 = customAnimationItem.getChargeDuration(useStack);
                else f9 = 30F;

                float f1 = i / f9;
                f1 = f1 > 1.0F ? 1.0F : f1 * f1;
                event.setFOV(event.getFOV() * (1.0F - f1 * (useItem instanceof LongBowItem ? 0.15F : 0.075F)));
            }
        }
    }
}
