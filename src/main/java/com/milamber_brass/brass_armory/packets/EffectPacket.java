package com.milamber_brass.brass_armory.packets;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.capabilities.IEffectCapability;
import com.milamber_brass.brass_armory.init.BrassArmoryCapabilities;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class EffectPacket implements CustomPacketPayload {
    public static final Type<EffectPacket> TYPE = new Type<>(BrassArmory.locate("effectpacket"));
    public static final StreamCodec<RegistryFriendlyByteBuf, EffectPacket> STREAM_CODEC = StreamCodec.of((buf, packet) -> packet.encode(buf), EffectPacket::new);
    @Override
    public Type<EffectPacket> type() { return TYPE; }
    private final int entityID;
    private final double shake;
    private final float slow;

    public EffectPacket(int id, IEffectCapability cap) {
        this.entityID = id;
        this.shake = cap.getShake();
        this.slow = cap.getSlow();
    }

    public EffectPacket(Entity entity, IEffectCapability cap) {
        this(entity.getId(), cap);
    }

    public EffectPacket(RegistryFriendlyByteBuf buf) {
        this.entityID = buf.readInt();
        this.shake = buf.readDouble();
        this.slow = buf.readFloat();
    }

    public void encode(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeDouble(this.shake);
        buf.writeFloat(this.slow);
    }

    public static class Handler {
        public static void onMessage(EffectPacket message, IPayloadContext ctx) {
            ctx.enqueueWork(() -> {
                Entity entity = null;
                if (Minecraft.getInstance().level != null) entity = Minecraft.getInstance().level.getEntity(message.entityID);
                if (entity instanceof Player) Optional.of(entity.getData(BrassArmoryCapabilities.EFFECT_CAPABILITY)).ifPresent(cap -> {
                    cap.setShake(message.shake);
                    cap.setSlow(message.slow);
                });
            });


        }
    }
}
