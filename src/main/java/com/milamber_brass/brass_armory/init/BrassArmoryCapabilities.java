package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.capabilities.*;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BrassArmoryCapabilities {
    public static final Capability<IQuiverCapability> QUIVER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IPowderCapability> POWDER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IEffectCapability> EFFECT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public static void capabilitySetup(RegisterCapabilitiesEvent event) {
        event.register(IQuiverCapability.class);
        event.register(IPowderCapability.class);
        event.register(IEffectCapability.class);
    }

    public static void addCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            event.addCapability(IQuiverCapability.ID, new ICapabilitySerializable<CompoundTag>() {
                final LazyOptional<IQuiverCapability> inst = LazyOptional.of(() -> {
                    QuiverCapabilityHandler quiverCapabilityHandler = new QuiverCapabilityHandler();
                    quiverCapabilityHandler.setEntity(player);
                    return quiverCapabilityHandler;
                });

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
                    return QUIVER_CAPABILITY.orEmpty(capability, inst.cast());
                }

                @Override
                public CompoundTag serializeNBT() {
                    return inst.orElseThrow(NullPointerException::new).serializeNBT();
                }

                @Override
                public void deserializeNBT(CompoundTag compoundTag) {
                    inst.orElseThrow(NullPointerException::new).deserializeNBT(compoundTag);
                }
            });
            event.addCapability(IEffectCapability.ID, new ICapabilitySerializable<CompoundTag>() {
                final LazyOptional<IEffectCapability> inst = LazyOptional.of(() -> {
                    EffectCapabilityHandler shakeCapabilityHandler = new EffectCapabilityHandler();
                    shakeCapabilityHandler.setEntity(player);
                    return shakeCapabilityHandler;
                });

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
                    return EFFECT_CAPABILITY.orEmpty(capability, inst.cast());
                }

                @Override
                public CompoundTag serializeNBT() {
                    return inst.orElseThrow(NullPointerException::new).serializeNBT();
                }

                @Override
                public void deserializeNBT(CompoundTag compoundTag) {
                    inst.orElseThrow(NullPointerException::new).deserializeNBT(compoundTag);
                }
            });
        } else if (event.getObject() instanceof Projectile projectile) {
            event.addCapability(IPowderCapability.ID, new ICapabilitySerializable<CompoundTag>() {
                final LazyOptional<IPowderCapability> inst = LazyOptional.of(() -> {
                    PowderCapabilityHandler powderCapabilityHandler = new PowderCapabilityHandler();
                    powderCapabilityHandler.setEntity(projectile);
                    return powderCapabilityHandler;
                });

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
                    return POWDER_CAPABILITY.orEmpty(capability, inst.cast());
                }

                @Override
                public CompoundTag serializeNBT() {
                    return inst.orElseThrow(NullPointerException::new).serializeNBT();
                }

                @Override
                public void deserializeNBT(CompoundTag compoundTag) {
                    inst.orElseThrow(NullPointerException::new).deserializeNBT(compoundTag);
                }
            });
        }
    }
}
