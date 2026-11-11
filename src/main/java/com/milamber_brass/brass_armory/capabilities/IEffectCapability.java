package com.milamber_brass.brass_armory.capabilities;

import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import com.milamber_brass.brass_armory.util.ArmoryNBTSerializable;

public interface IEffectCapability extends ArmoryNBTSerializable {

    Identifier ID = BrassArmory.locate("effect_capability");

    void setEntity(Player player);

    void setShake(double shake);

    void reduceShake(double d);

    double getShake();

    void setSlow(float slow);

    float getSlow();

    void tick();

}
