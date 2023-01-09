package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.entity.projectile.cannon_balls.CarcassRoundEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow @Nullable private ClientLevel level;

    @Shadow @Nullable protected abstract Particle addParticleInternal(ParticleOptions p_109796_, boolean p_109797_, double p_109798_, double p_109799_, double p_109800_, double p_109801_, double p_109802_, double p_109803_);

    @Inject(method = "levelEvent", at = @At(value = "HEAD"), cancellable = true, remap = true)
    private void levelEvent(int id, BlockPos pos, int color, CallbackInfo ci) {
        if (id == CarcassRoundEntity.LEVEL_EVENT_CONSTANT && this.level != null) {
            Vec3 vec3 = Vec3.atBottomCenterOf(pos);

            float r = (float)(color >> 16 & 255) / 255.0F;
            float g = (float)(color >> 8 & 255) / 255.0F;
            float b = (float)(color & 255) / 255.0F;

            for (ParticleOptions particleOptions : new ParticleOptions[] { ParticleTypes.INSTANT_EFFECT, ParticleTypes.EFFECT }) {
                for (int j = 0; j < 100; ++j) {
                    double d23 = this.level.random.nextDouble() * 4.0D;
                    double d27 = this.level.random.nextDouble() * Math.PI * 2.0D;
                    double d29 = Math.cos(d27) * d23;
                    double d5 = 0.01D + this.level.random.nextDouble() * 0.5D;
                    double d7 = Math.sin(d27) * d23;
                    Particle particle1 = this.addParticleInternal(particleOptions, particleOptions.getType().getOverrideLimiter(), vec3.x + d29 * 0.1D, vec3.y + 0.3D, vec3.z + d7 * 0.1D, d29, d5, d7);
                    if (particle1 != null) {
                        float rand = 0.75F + this.level.random.nextFloat() * 0.25F;
                        particle1.setColor(r * rand, g * rand, b * rand);
                        particle1.setPower((float) d23);
                    }
                }
            }

            ci.cancel();
        }
    }

}
