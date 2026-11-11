package com.milamber_brass.brass_armory.event;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.client.sound.CannonSoundInstance;
import com.milamber_brass.brass_armory.entity.CannonEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryCapabilities;
import com.milamber_brass.brass_armory.init.BrassArmoryEffects;
import com.milamber_brass.brass_armory.init.BrassArmoryParticles;
import com.milamber_brass.brass_armory.item.FlintlockItem;
import com.milamber_brass.brass_armory.item.LongBowItem;
import com.milamber_brass.brass_armory.item.MaceItem;
import com.milamber_brass.brass_armory.item.WarpCrystalItem;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import com.milamber_brass.brass_armory.item.interfaces.ICustomAnimationItem;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = BrassArmory.MOD_ID, value = Dist.CLIENT)
public class ClientEventSubscriber {
    @SubscribeEvent
    public static void onBleedingTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity living) || !living.level().isClientSide()) return;
        var effect = living.getEffect(BrassArmoryEffects.BLEEDING);
        if (effect == null) return;
        int amplifier = effect.getAmplifier();
        int count = amplifier > 100 ? 100 : (living.getRandom().nextInt(100) <= effect.getDuration() ? 1 : 0) + amplifier;
        double y = living.isDeadOrDying() ? living.getY() + 0.15D : living.getRandomY() - 0.25D;
        for (int i = 0; i < count; i++) {
            living.level().addParticle(BrassArmoryParticles.BLOOD_FALL_PARTICLE.get(), living.getRandomX(0.5D), y, living.getRandomZ(0.5D), 0D, 0D, 0D);
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player != null && player.isUsingItem()) {
            ItemStack eventItem = event.getItemStack();
            if (eventItem.getItem() instanceof ICustomAnimationItem || eventItem.getItem() instanceof LongBowItem || (eventItem.getItem() instanceof AbstractThrownWeaponItem && eventItem.getItem().getUseAnimation(player.getUseItem()) == ItemUseAnimation.SPEAR)) {
                event.setCanceled(true);
                boolean doubleMace = player.getMainHandItem().getItem() instanceof MaceItem && player.getOffhandItem().getItem() instanceof MaceItem;


                if (player.getUsedItemHand() == event.getHand() || doubleMace) {
                    boolean mainHandFlag = event.getHand() == InteractionHand.MAIN_HAND;
                    HumanoidArm humanoidarm = mainHandFlag ? player.getMainArm() : player.getMainArm().getOpposite();
                    boolean rightHandFlag = humanoidarm == HumanoidArm.RIGHT;
                    float k = rightHandFlag ? 1F : -1F;
                    ItemDisplayContext transformType = rightHandFlag ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
                    PoseStack poseStack = event.getPoseStack();
                    ItemStack itemStack = event.getItemStack();
                    if (doubleMace) poseStack.pushPose();

                    float f8 = (float)itemStack.getUseDuration(player) - (float)player.getUseItemRemainingTicks();
                    float f9 = f8 + event.getPartialTick();
                    poseStack.translate(k * 0.56F, -0.52F, -0.72F);
                    if (itemStack.getItem() instanceof ICustomAnimationItem animationItem) {
                        float maxCharge = animationItem.getChargeDuration(itemStack);
                        float v = Math.min(f9, maxCharge);
                        event.setCanceled(true);
                        float m = v / (maxCharge * 10F);
                        double sineWave = Math.sin(f8 * 75D) * 0.004D;

                        poseStack.translate(0, sineWave + m, m);
                        poseStack.rotate(Axis.XP.rotationDegrees(-13.935F + m * 200F));
                        poseStack.rotate(Axis.YN.rotationDegrees(k * 9.7F));
                        poseStack.rotate(Axis.ZP.rotationDegrees(k * -9.785F));
                        poseStack.scale(1 + (float) sineWave, 1 + (float) sineWave, 1 + (float) sineWave);
                    } else if (itemStack.getItem() instanceof LongBowItem) {
                        poseStack.translate(k * -0.2785682F, 0.18344387F, 0.15731531F);
                        poseStack.rotate(Axis.XP.rotationDegrees(-13.935F));
                        poseStack.rotate(Axis.YP.rotationDegrees(k * 35.3F));
                        poseStack.rotate(Axis.ZP.rotationDegrees(k * -9.785F));
                        float f12 = f9 / 30.0F;
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
                        poseStack.rotate(Axis.YN.rotationDegrees(k * 45.0F));
                    } else {
                        poseStack.translate(k * -0.5F, 0.7F, 0.1F);
                        poseStack.rotate(Axis.XP.rotationDegrees(-55.0F));
                        poseStack.rotate(Axis.YP.rotationDegrees(k * 35.3F));
                        poseStack.rotate(Axis.ZP.rotationDegrees(k * -9.785F));
                        float f11 = f9 / (eventItem.getItem() instanceof AbstractThrownWeaponItem item ? item.chargeDuration : 10.0F);
                        if (f11 > 1.0F) f11 = 1.0F;

                        if (f11 > 0.1F) {
                            float f14 = Mth.sin((f8 - 0.1F) * 1.3F);
                            float f17 = f11 - 0.1F;
                            float f19 = f14 * f17;
                            poseStack.translate(f19 * 0.0F, f19 * 0.004F, f19 * 0.0F);
                        }

                        poseStack.translate(0.0D, 0.0D, f11 * 0.2F);
                        poseStack.scale(1.0F, 1.0F, 1.0F + f11 * 0.2F);
                        poseStack.rotate(Axis.YN.rotationDegrees(k * 45.0F));
                    }


                    ItemStackRenderState itemState = new ItemStackRenderState();
                    mc.getItemModelResolver().updateForLiving(itemState, itemStack, transformType, player);
                    itemState.submit(poseStack, event.getSubmitNodeCollector(), event.getPackedLight(), OverlayTexture.NO_OVERLAY, 0);
                    if (doubleMace) poseStack.popPose();
                }
            } else if (eventItem.getItem() instanceof WarpCrystalItem) {
                event.setCanceled(true);
                PoseStack poseStack = event.getPoseStack();
                boolean mainHandFlag = event.getHand() == InteractionHand.MAIN_HAND;
                HumanoidArm humanoidarm = mainHandFlag ? player.getMainArm() : player.getMainArm().getOpposite();
                boolean rightHandFlag = humanoidarm == HumanoidArm.RIGHT;
                float f1 = (player.getTicksUsingItem() + event.getPartialTick()) / (float) WarpCrystalItem.WARP_TICKS;
                f1 = f1 > 1.0F ? 1.0F + (float)Math.sin(f1 * 7) * 0.0035F : (float)Math.pow(f1, 1.6D);
                poseStack.translate(0F, 0F, -1F - f1 / 4F);

                ItemStackRenderState itemState = new ItemStackRenderState();
                mc.getItemModelResolver().updateForLiving(itemState, eventItem, ItemDisplayContext.FIXED, player);
                itemState.submit(poseStack, event.getSubmitNodeCollector(), 15728880, OverlayTexture.NO_OVERLAY, 0);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (!(event.getRenderState() instanceof net.minecraft.client.renderer.entity.state.AvatarRenderState state)) return;
        if (state.rightHandItemStack.getItem() instanceof FlintlockItem && FlintlockItem.getLoad(state.rightHandItemStack) == 2) {
            state.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
        } else if (state.leftHandItemStack.getItem() instanceof FlintlockItem && FlintlockItem.getLoad(state.leftHandItemStack) == 2) {
            state.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
        }
    }

    @SubscribeEvent
    public static void onFOVModifier(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        if (player.isUsingItem()) {
            ItemStack useStack = player.getUseItem();
            Item useItem = useStack.getItem();
            if (useItem instanceof LongBowItem || useItem instanceof ICustomAnimationItem || useItem instanceof AbstractThrownWeaponItem) {
                float f9;
                if (useItem instanceof AbstractThrownWeaponItem thrownWeaponItem) f9 = thrownWeaponItem.chargeDuration;
                else if (useItem instanceof ICustomAnimationItem customAnimationItem) f9 = customAnimationItem.getChargeDuration(useStack);
                else f9 = 30F;

                float f1 = player.getTicksUsingItem() / f9;
                f1 = f1 > 1.0F ? 1.0F : (float)Math.pow(f1, 1.6D);
                event.setNewFovModifier(event.getFovModifier() * (1.0F - f1 * (useItem instanceof LongBowItem ? 0.15F : 0.075F)));
            } else if (useItem instanceof WarpCrystalItem) {
                float f1 = (float) player.getTicksUsingItem() / (float) WarpCrystalItem.WARP_TICKS;
                f1 = f1 > 1.0F ? 1.0F + (float)Math.sin(f1 * 7) * 0.0035F : f1 * f1;
                event.setNewFovModifier(event.getFovModifier() * (1.0F - f1 * 0.75F));
            }
        }
    }

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getCameraEntity() instanceof Player player && !minecraft.isPaused()) {
            Optional.of(player.getData(BrassArmoryCapabilities.EFFECT_CAPABILITY)).ifPresent(iShakeCapability -> {
                float intensity = (float)iShakeCapability.getShake() * 1.5F;
                if (intensity > 0) {
                    float partialTicks = (float) event.getPartialTick();
                    event.setYaw(Mth.lerp(partialTicks, event.getYaw(), event.getYaw() + (player.getRandom().nextFloat() * 2.0F - 1.0F) * intensity));
                    event.setPitch(Mth.lerp(partialTicks, event.getPitch(), event.getPitch() + (player.getRandom().nextFloat() * 2.0F - 1.0F) * intensity));
                    event.setRoll(Mth.lerp(partialTicks, event.getRoll(), event.getRoll() + (player.getRandom().nextFloat() * 2.0F - 1.0F) * intensity));
                    iShakeCapability.reduceShake(iShakeCapability.getShake() * 0.1D);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof CannonEntity cannonEntity && cannonEntity.level().isClientSide()) {
            Minecraft.getInstance().getSoundManager().play(new CannonSoundInstance(cannonEntity));
        }
    }
}
