package com.milamber_brass.brass_armory.capabilities;

import com.milamber_brass.brass_armory.behaviour.GunBehaviours;
import com.milamber_brass.brass_armory.behaviour.powder.AbstractPowderBehaviour;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PowderCapabilityHandler implements IPowderCapability {
    private Projectile projectile;
    private AbstractPowderBehaviour powderBehaviour;

    @Override
    public void setEntity(Projectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public void setPowderBehaviour(AbstractPowderBehaviour powderBehaviour) {
        this.powderBehaviour = powderBehaviour;
    }

    public AbstractPowderBehaviour getPowderBehaviour() {
        return this.powderBehaviour;
    }

    @Override
    public void tick() {
        if (this.projectile.level instanceof ServerLevel serverLevel && this.powderBehaviour != null) {
            if (this.projectile instanceof AbstractArrow abstractArrow && abstractArrow.inGround && abstractArrow.shakeTime <= 0) return;
            this.powderBehaviour.sendParticles(serverLevel, this.projectile.getEyePosition());
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        GunBehaviours.resourceLocationByPowderBehaviour(this.powderBehaviour).ifPresent(resourceLocation -> tag.putString("BAPowderBehaviour", resourceLocation.toString()));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        ResourceLocation resourceLocation = ResourceLocation.tryParse(tag.getString("BAPowderBehaviour"));
        if (resourceLocation != null) this.powderBehaviour = GunBehaviours.getPowderBehaviours().get(resourceLocation);
    }
}
