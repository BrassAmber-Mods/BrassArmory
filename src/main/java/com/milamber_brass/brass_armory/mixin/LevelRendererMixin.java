package com.milamber_brass.brass_armory.mixin;

import org.jetbrains.annotations.Nullable;
import com.milamber_brass.brass_armory.entity.projectile.cannon_balls.CarcassRoundEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.particles.SpellParticleOption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelEventHandler.class)
public abstract class LevelRendererMixin {
    @Shadow @Nullable private ClientLevel level;

    @Inject(method = "levelEvent", at = @At(value = "HEAD"), cancellable = true, remap = true)
    private void levelEvent(int id, BlockPos pos, int color, CallbackInfo ci) {
        if (id == CarcassRoundEntity.LEVEL_EVENT_CONSTANT && this.level != null) {
            Vec3 vec3 = Vec3.atBottomCenterOf(pos);

            float r = (float)(color >> 16 & 255) / 255.0F;
            float g = (float)(color >> 8 & 255) / 255.0F;
            float b = (float)(color & 255) / 255.0F;

            for (ParticleOptions particleOptions : new ParticleOptions[] { SpellParticleOption.create(ParticleTypes.INSTANT_EFFECT, color, 1F), SpellParticleOption.create(ParticleTypes.EFFECT, color, 1F) }) {
                for (int j = 0; j < 100; ++j) {
                    double d23 = this.level.getRandom().nextDouble() * 4.0D;
                    double d27 = this.level.getRandom().nextDouble() * Math.PI * 2.0D;
                    double d29 = Math.cos(d27) * d23;
                    double d5 = 0.01D + this.level.getRandom().nextDouble() * 0.5D;
                    double d7 = Math.sin(d27) * d23;
                    float rand = 0.75F + this.level.getRandom().nextFloat() * 0.25F;
                    SpellParticleOption option = SpellParticleOption.create(((SpellParticleOption) particleOptions).getType(), r * rand, g * rand, b * rand, (float) d23);
                    this.level.addParticle(option, vec3.x + d29 * 0.1D, vec3.y + 0.3D, vec3.z + d7 * 0.1D, d29, d5, d7);
                }
            }

            ci.cancel();
        }
    }

}
