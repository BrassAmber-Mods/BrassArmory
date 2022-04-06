package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrassArmorySounds {
    public static final DeferredRegister<SoundEvent> SOUND_TYPES = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BrassArmory.MOD_ID);

    public static final RegistryObject<SoundEvent> BOMB_LIT = SOUND_TYPES.register("entity.bomb.lit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "entity.bomb.lit")));
    public static final RegistryObject<SoundEvent> BOMB_THROW = SOUND_TYPES.register("entity.bomb.throw", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "entity.bomb.throw")));
    public static final RegistryObject<SoundEvent> BOMB_FUSE = SOUND_TYPES.register("entity.bomb.fuse", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "entity.bomb.fuse")));
    public static final RegistryObject<SoundEvent> BOMB_HIT = SOUND_TYPES.register("entity.bomb.hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "entity.bomb.hit")));
    public static final RegistryObject<SoundEvent> BOUNCY_BOMB_HIT = SOUND_TYPES.register("entity.bomb.bouncy_hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "entity.bomb.bouncy_hit")));
    public static final RegistryObject<SoundEvent> STICKY_BOMB_HIT = SOUND_TYPES.register("entity.bomb.sticky_hit", () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, "entity.bomb.sticky_hit")));

    public static void register(IEventBus eventBus) {
        SOUND_TYPES.register(eventBus);
    }
}
