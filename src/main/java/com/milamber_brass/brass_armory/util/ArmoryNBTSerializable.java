package com.milamber_brass.brass_armory.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public interface ArmoryNBTSerializable extends ValueIOSerializable {
    CompoundTag serializeNBT(HolderLookup.Provider registries);
    void deserializeNBT(HolderLookup.Provider registries, CompoundTag tag);

    @Override
    default void serialize(ValueOutput output) {
        output.store("data", CompoundTag.CODEC, serializeNBT(ItemData.registries()));
    }

    @Override
    default void deserialize(ValueInput input) {
        deserializeNBT(ItemData.registries(), input.read("data", CompoundTag.CODEC).orElseGet(CompoundTag::new));
    }
}
