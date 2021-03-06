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

    public static final RegistryObject<SoundEvent> BOMB_LIT = registerSound("entity.bomb.lit");
    public static final RegistryObject<SoundEvent> BOMB_THROW = registerSound("entity.bomb.throw");
    public static final RegistryObject<SoundEvent> BOMB_FUSE = registerSound("entity.bomb.fuse");
    public static final RegistryObject<SoundEvent> BOMB_HIT = registerSound("entity.bomb.hit");
    public static final RegistryObject<SoundEvent> BOUNCY_BOMB_HIT = registerSound("entity.bomb.bouncy_hit");
    public static final RegistryObject<SoundEvent> STICKY_BOMB_HIT = registerSound("entity.bomb.sticky_hit");

    private static RegistryObject<SoundEvent> registerSound(String key) {
        return SOUND_TYPES.register(key, () -> new SoundEvent(new ResourceLocation(BrassArmory.MOD_ID, key)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_TYPES.register(eventBus);
    }
}
