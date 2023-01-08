package com.milamber_brass.brass_armory.capabilities;

import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface IEffectCapability extends INBTSerializable<CompoundTag> {

    ResourceLocation ID = BrassArmory.locate("effect_capability");

    void setEntity(Player player);

    void setShake(double shake);

    void reduceShake(double d);

    double getShake();

    void setSlow(float slow);

    float getSlow();

    void tick();

}
