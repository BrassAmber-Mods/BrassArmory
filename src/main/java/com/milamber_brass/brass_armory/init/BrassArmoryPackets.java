package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.packets.EffectPacket;
import com.milamber_brass.brass_armory.packets.ParticlePacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class BrassArmoryPackets {
    public static void init(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToClient(EffectPacket.TYPE, EffectPacket.STREAM_CODEC, EffectPacket.Handler::onMessage);
        registrar.playToClient(ParticlePacket.TYPE, ParticlePacket.STREAM_CODEC, ParticlePacket.Handler::onMessage);
    }
}
