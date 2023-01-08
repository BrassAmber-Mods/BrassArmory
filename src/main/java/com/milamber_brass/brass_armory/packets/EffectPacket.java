package com.milamber_brass.brass_armory.packets;

import com.milamber_brass.brass_armory.capabilities.IEffectCapability;
import com.milamber_brass.brass_armory.init.BrassArmoryCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class EffectPacket {
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

    public EffectPacket(FriendlyByteBuf buf) {
        this.entityID = buf.readInt();
        this.shake = buf.readDouble();
        this.slow = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeDouble(this.shake);
        buf.writeFloat(this.slow);
    }

    public static class Handler {
        public static void onMessage(EffectPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Entity entity = null;
                if (Minecraft.getInstance().level != null) entity = Minecraft.getInstance().level.getEntity(message.entityID);
                if (entity instanceof Player) entity.getCapability(BrassArmoryCapabilities.EFFECT_CAPABILITY).ifPresent(cap -> {
                    cap.setShake(message.shake);
                    cap.setSlow(message.slow);
                });
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
