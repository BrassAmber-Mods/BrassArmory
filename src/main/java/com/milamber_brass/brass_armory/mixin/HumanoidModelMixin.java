package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.inventory.GunContainer;
import com.milamber_brass.brass_armory.item.FlintlockItem;
import com.milamber_brass.brass_armory.item.MaceItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart head;
    @Unique private HumanoidRenderState armoryState;

    @Unique float savedLeftRot;
    @Unique float savedRightRot;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;isUsingItem:Z", ordinal = 0))
    private void saveRotsMixin(HumanoidRenderState state, CallbackInfo ci) {
        this.savedLeftRot = this.leftArm.xRot;
        this.savedRightRot = this.rightArm.xRot;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/HumanoidModel;setupAttackAnimation(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", shift = At.Shift.AFTER))
    private void gunRePose(HumanoidRenderState state, CallbackInfo ci) {
        this.armoryState = state;
        ItemStack mainHand = state.getMainHandItemStack();
        ItemStack offHand = state.getUseItemStackForArm(state.mainArm.getOpposite());
        ItemStack useItem = state.getUseItemStackForArm(state.useItemHand.asArm(state.mainArm));
        var player = Minecraft.getInstance().player;
        boolean loadingMenu = state instanceof AvatarRenderState avatar && player != null && avatar.id == player.getId() && player.containerMenu instanceof GunContainer<?>;
        if (state.isUsingItem) {
            if (mainHand.getItem() instanceof MaceItem && offHand.getItem() instanceof MaceItem) {
                this.rightArm.xRot = this.savedRightRot * 0.5F - (float) Math.PI;
                this.rightArm.yRot = 0.0F;
                this.leftArm.xRot = this.savedLeftRot * 0.5F - (float) Math.PI;
                this.leftArm.yRot = 0.0F;
            } else {
                if (useItem.getItem() instanceof FlintlockItem) {
                    HumanoidArm mainArm = state.mainArm;
                    InteractionHand hand = state.useItemHand;
                    boolean rightArm = (mainArm == HumanoidArm.RIGHT && hand == InteractionHand.MAIN_HAND) || (mainArm == HumanoidArm.LEFT && hand == InteractionHand.OFF_HAND);
                    this.holdLoadingGun(rightArm, FlintlockItem.getLoadProgress(useItem), true);
                }
            }
        } else {
            if (mainHand.getItem() instanceof FlintlockItem gunItem) {
                switch(FlintlockItem.getLoad(mainHand)) {
                    case 0:
                        if (loadingMenu)
                            this.holdLoadingGun(state.mainArm == HumanoidArm.RIGHT, FlintlockItem.getLoadProgress(useItem), false);
                        break;
                    case 2:
                        if (gunItem.isOneHanded())
                            this.holdLoadedGun(state.mainArm != HumanoidArm.RIGHT, true, !offHand.isEmpty());
                }
            }
            if (offHand.getItem() instanceof FlintlockItem gunItem) {
                switch(FlintlockItem.getLoad(offHand)) {
                    case 0:
                        if (loadingMenu)
                            this.holdLoadingGun(state.mainArm == HumanoidArm.LEFT, FlintlockItem.getLoadProgress(useItem), false);
                        break;
                    case 2:
                        if (gunItem.isOneHanded())
                            this.holdLoadedGun(state.mainArm != HumanoidArm.LEFT, false, !mainHand.isEmpty());
                }
            }
        }
    }

    @Unique
    private void holdLoadingGun(boolean rightArm, float loadProgress, boolean using) {
        ModelPart holdArm = rightArm ? this.rightArm : this.leftArm;
        holdArm.yRot = rightArm ? -0.8F : 0.8F;
        holdArm.xRot = -0.97079635F;

        ModelPart loadArm = rightArm ? this.leftArm : this.rightArm;
        loadArm.xRot = holdArm.xRot;

        float maxLoad = 20F;
        float clampLoad = Mth.clamp(loadProgress, 0.0F, maxLoad);
        float finalLoad = clampLoad / maxLoad;
        if (!using) finalLoad += 0.43;
        loadArm.yRot = Mth.lerp(finalLoad, 0.4F, 0.85F) * (float) (rightArm ? 1 : -1);
        loadArm.xRot = Mth.lerp(finalLoad, loadArm.xRot, (-(float) Math.PI / 2F));
    }

    @Unique
    private void holdLoadedGun(boolean leftHand, boolean mainHand, boolean otherHandHasItem) {
        if (leftHand) {
            if (!mainHand && this.armoryState.rightArmPose.isTwoHanded()) return;
            this.leftArm.yRot = 0.1F + this.head.yRot;
            this.leftArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
            if (mainHand) {
                this.rightArm.xRot = this.savedRightRot;
                if (otherHandHasItem) this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float)Math.PI / 10F);
                this.rightArm.yRot = 0.0F;
            }
        } else {
            if (!mainHand && this.armoryState.leftArmPose.isTwoHanded()) return;
            this.rightArm.yRot = -0.1F + this.head.yRot;
            this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
            if (mainHand) {
                this.leftArm.xRot = this.savedLeftRot;
                if (otherHandHasItem) this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
                this.leftArm.yRot = 0.0F;
            }
        }
    }
}
