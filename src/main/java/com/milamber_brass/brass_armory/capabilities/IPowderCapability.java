package com.milamber_brass.brass_armory.capabilities;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.behaviour.powder.AbstractPowderBehaviour;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPowderCapability extends INBTSerializable<CompoundTag> {

    ResourceLocation ID = BrassArmory.locate("powder_capability");

    void setEntity(Projectile projectile);

    void setPowderBehaviour(AbstractPowderBehaviour powderBehaviour);

    AbstractPowderBehaviour getPowderBehaviour();

    void tick();

}
