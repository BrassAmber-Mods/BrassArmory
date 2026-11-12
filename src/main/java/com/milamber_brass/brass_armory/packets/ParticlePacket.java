package com.milamber_brass.brass_armory.packets;

import com.milamber_brass.brass_armory.BrassArmory;
import io.netty.handler.codec.DecoderException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
*Special thanks to Tamaized for helping me write this class, which was originally made for the Twilight Forest mod
*/

@ParametersAreNonnullByDefault
public class ParticlePacket implements CustomPacketPayload {
    private static final int MAX_PARTICLES = 4096;
    public static final Type<ParticlePacket> TYPE = new Type<>(BrassArmory.locate("particlepacket"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ParticlePacket> STREAM_CODEC = StreamCodec.of((buf, packet) -> packet.encode(buf), ParticlePacket::new);
    @Override
    public Type<ParticlePacket> type() { return TYPE; }
	private final List<QueuedParticle> queuedParticles = new ArrayList<>();

	public ParticlePacket() {

	}

	@SuppressWarnings("deprecation")
	public ParticlePacket(RegistryFriendlyByteBuf buf) {
		int size = buf.readInt();
		if (size < 0 || size > MAX_PARTICLES) throw new DecoderException("Invalid particle count: " + size);
		for (int i = 0; i < size; i++) {
			ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.byId(buf.readInt());
			if (type == null) throw new DecoderException("Unknown particle type");
			this.queuedParticles.add(new QueuedParticle(readParticle(type, buf), buf.readBoolean(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble()));
		}
	}

	private <T extends ParticleOptions> @NotNull T readParticle(ParticleType<T> particleType, RegistryFriendlyByteBuf buf) {
		return particleType.streamCodec().decode(buf);
	}

	@SuppressWarnings("deprecation")
	public void encode(RegistryFriendlyByteBuf buf) {
		buf.writeInt(this.queuedParticles.size());
		for (QueuedParticle queuedParticle : this.queuedParticles) {
			int d = BuiltInRegistries.PARTICLE_TYPE.getId(queuedParticle.particleOptions.getType());
			buf.writeInt(d);
			writeParticle(buf, queuedParticle.particleOptions);
			buf.writeBoolean(queuedParticle.b);
			buf.writeDouble(queuedParticle.x);
			buf.writeDouble(queuedParticle.y);
			buf.writeDouble(queuedParticle.z);
			buf.writeDouble(queuedParticle.x2);
			buf.writeDouble(queuedParticle.y2);
			buf.writeDouble(queuedParticle.z2);
		}
	}

    private static <T extends ParticleOptions> void writeParticle(RegistryFriendlyByteBuf buf, T particle) {
        ((ParticleType<T>) particle.getType()).streamCodec().encode(buf, particle);
    }

	public void queueParticle(ParticleOptions particleOptions, boolean b, double x, double y, double z, double x2, double y2, double z2) {
		if (this.queuedParticles.size() >= MAX_PARTICLES) throw new IllegalStateException("Too many queued particles");
		this.queuedParticles.add(new QueuedParticle(particleOptions, b, x, y, z, x2, y2, z2));
	}

	public void queueParticle(ParticleOptions particleOptions, boolean b, Vec3 xyz, Vec3 xyz2) {
		this.queueParticle(particleOptions, b, xyz.x, xyz.y, xyz.z, xyz2.x, xyz2.y, xyz2.z);
	}

	private record QueuedParticle(ParticleOptions particleOptions, boolean b, double x, double y, double z, double x2, double y2, double z2) {

	}

	public static class Handler {
		public static void onMessage(ParticlePacket message, IPayloadContext ctx) {
			ctx.enqueueWork(() -> {
				ClientLevel level = Minecraft.getInstance().level;
				if (level == null) return;
				for (QueuedParticle queuedParticle : message.queuedParticles) {
					level.addParticle(queuedParticle.particleOptions, queuedParticle.b, queuedParticle.x, queuedParticle.y, queuedParticle.z, queuedParticle.x2, queuedParticle.y2, queuedParticle.z2);
				}
			});

		}
	}
}
