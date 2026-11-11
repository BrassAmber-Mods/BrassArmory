package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BrassArmoryDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, BrassArmory.MOD_ID);
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<CompoundTag>> CANNON_DATA = REGISTRY.register("cannon_data", () -> new EntityDataSerializer<>() {
        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, CompoundTag> codec() { return ByteBufCodecs.COMPOUND_TAG; }
        @Override
        public CompoundTag copy(CompoundTag value) { return value.copy(); }
    });
}
