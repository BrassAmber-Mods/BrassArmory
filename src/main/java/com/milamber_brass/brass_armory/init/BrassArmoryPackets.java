package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.packets.ParticlePacket;
import com.milamber_brass.brass_armory.packets.EffectPacket;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class BrassArmoryPackets {
    private static final String V = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(BrassArmory.locate("channel"), () -> V, V::equals, V::equals);

    public static void init() {
        CHANNEL.registerMessage(0, EffectPacket.class, EffectPacket::encode, EffectPacket::new, EffectPacket.Handler::onMessage);
        CHANNEL.registerMessage(1, ParticlePacket.class, ParticlePacket::encode, ParticlePacket::new, ParticlePacket.Handler::onMessage);
    }
}
