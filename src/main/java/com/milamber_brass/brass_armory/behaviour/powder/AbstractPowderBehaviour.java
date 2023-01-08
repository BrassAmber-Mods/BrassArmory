package com.milamber_brass.brass_armory.behaviour.powder;

import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.init.BrassArmoryCapabilities;
import com.milamber_brass.brass_armory.init.BrassArmoryPackets;
import com.milamber_brass.brass_armory.packets.ParticlePacket;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public abstract class AbstractPowderBehaviour {
    protected final ParticleOptions particle;

    public AbstractPowderBehaviour(ParticleOptions particleOptions) {
        this.particle = particleOptions;
    }

    public void onShoot(Level level, LivingEntity owner, Entity shooter, ItemStack ammoStack, iGun gun) {
        if (!level.isClientSide) {
            Vec3 eyePosition = shooter.getEyePosition();
            Vec3 viewVector = shooter.getViewVector(1.0F);
            ParticlePacket particlePacket = new ParticlePacket();
            for (double d = 0; d < (int) (4.55F * gun.particleMultiplier()); d++) {
                for (int i = 0; i < (int) (8F * gun.particleMultiplier()); i++) {
                    for (double o = 0; o < 1; o += 0.1) {
                        Vec3 vec32 = eyePosition
                                .add(
                                        viewVector.x * d + level.random.nextGaussian() * o,
                                        viewVector.y * d + level.random.nextGaussian() * o,
                                        viewVector.z * d + level.random.nextGaussian() * o)
                                .add(viewVector.scale(gun.particleOffset()));

                        Vec3 particlePos = level.clip(new ClipContext(eyePosition, vec32, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, shooter)).getLocation();
                        particlePacket.queueParticle(this.particle, false, particlePos.x, particlePos.y, particlePos.z, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
            BrassArmoryPackets.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> shooter), particlePacket);
        }
    }

    public void sendParticles(ServerLevel serverLevel, Vec3 vec3) {
        if (this.getParticle() != null) {
            serverLevel.sendParticles(this.getParticle(), vec3.x, vec3.y, vec3.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    @Nullable
    public ParticleOptions getParticle() {
        return this.particle;
    }

    public void applyEffect(Entity entity) {
        if (entity instanceof Projectile projectile) {
            projectile.getCapability(BrassArmoryCapabilities.POWDER_CAPABILITY).ifPresent(capability -> capability.setPowderBehaviour(this));
        }
    }

    @Nonnull
    public abstract Predicate<ItemStack> getPowderItems();
}
