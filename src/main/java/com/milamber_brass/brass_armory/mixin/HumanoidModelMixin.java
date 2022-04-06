package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.container.GunContainer;
import com.milamber_brass.brass_armory.item.abstracts.AbstractGunItem;
import com.milamber_brass.brass_armory.item.MaceItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart head;
    @Shadow public HumanoidModel.ArmPose rightArmPose;
    @Shadow public HumanoidModel.ArmPose leftArmPose;

    @Unique float savedLeftRot;
    @Unique float savedRightRot;

    @ParametersAreNonnullByDefault
    @Inject(method = "setupAnim*", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/HumanoidArm;RIGHT:Lnet/minecraft/world/entity/HumanoidArm;", shift = At.Shift.AFTER, ordinal = 0), remap = true)
    private void saveRotsMixin(LivingEntity living, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_, CallbackInfo ci) {
        this.savedLeftRot = this.leftArm.xRot;
        this.savedRightRot = this.rightArm.xRot;
    }

    @ParametersAreNonnullByDefault
    @Inject(method = "setupAnim*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/HumanoidModel;setupAttackAnimation(Lnet/minecraft/world/entity/LivingEntity;F)V", shift = At.Shift.AFTER), remap = true)
    private void gunRePose(LivingEntity living, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_, CallbackInfo ci) {
        if (living.isUsingItem()) {
            if (living.getMainHandItem().getItem() instanceof MaceItem && living.getOffhandItem().getItem() instanceof MaceItem) {
                this.rightArm.xRot = this.savedRightRot * 0.5F - (float) Math.PI;
                this.rightArm.yRot = 0.0F;
                this.leftArm.xRot = this.savedLeftRot * 0.5F - (float) Math.PI;
                this.leftArm.yRot = 0.0F;
            } else {
                if (living.getUseItem().getItem() instanceof AbstractGunItem) {
                    HumanoidArm mainArm = living.getMainArm();
                    InteractionHand hand = living.getUsedItemHand();
                    boolean rightArm = (mainArm == HumanoidArm.RIGHT && hand == InteractionHand.MAIN_HAND) || (mainArm == HumanoidArm.LEFT && hand == InteractionHand.OFF_HAND);
                    this.holdLoadingGun(rightArm, AbstractGunItem.getLoadProgress(living.getUseItem()), true);
                }
            }
        } else {
            if (living.getMainHandItem().getItem() instanceof AbstractGunItem gunItem) {
                switch(AbstractGunItem.getLoad(living.getMainHandItem())) {
                    case 0:
                        if (living instanceof Player player && player.containerMenu instanceof GunContainer)
                            this.holdLoadingGun(living.getMainArm() == HumanoidArm.RIGHT, AbstractGunItem.getLoadProgress(living.getUseItem()), false);
                        break;
                    case 2:
                        if (gunItem.isOneHanded())
                            this.holdLoadedGun(living.getMainArm() != HumanoidArm.RIGHT, true, !living.getOffhandItem().isEmpty());
                }
            }
            if (living.getOffhandItem().getItem() instanceof AbstractGunItem gunItem) {
                switch(AbstractGunItem.getLoad(living.getOffhandItem())) {
                    case 0:
                        if (living instanceof Player player && player.containerMenu instanceof GunContainer)
                            this.holdLoadingGun(living.getMainArm() == HumanoidArm.LEFT, AbstractGunItem.getLoadProgress(living.getUseItem()), false);
                        break;
                    case 2:
                        if (gunItem.isOneHanded())
                            this.holdLoadedGun(living.getMainArm() != HumanoidArm.LEFT, false, !living.getMainHandItem().isEmpty());
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
            if (!mainHand && this.rightArmPose.isTwoHanded()) return;
            this.leftArm.yRot = 0.1F + this.head.yRot;
            this.leftArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
            if (mainHand) {
                this.rightArm.xRot = this.savedRightRot;
                if (otherHandHasItem) this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float)Math.PI / 10F);
                this.rightArm.yRot = 0.0F;
            }
        } else {
            if (!mainHand && this.leftArmPose.isTwoHanded()) return;
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
