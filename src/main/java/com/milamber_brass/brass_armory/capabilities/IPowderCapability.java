package com.milamber_brass.brass_armory.capabilities;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.behaviour.powder.AbstractPowderBehaviour;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.projectile.Projectile;
import com.milamber_brass.brass_armory.util.ArmoryNBTSerializable;

public interface IPowderCapability extends ArmoryNBTSerializable {

    Identifier ID = BrassArmory.locate("powder_capability");

    void setEntity(Projectile projectile);

    void setPowderBehaviour(AbstractPowderBehaviour powderBehaviour);

    AbstractPowderBehaviour getPowderBehaviour();

    void tick();

}
