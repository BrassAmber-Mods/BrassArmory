package com.milamber_brass.brass_armory.client.particle;

import com.milamber_brass.brass_armory.init.BrassArmoryParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class BloodParticle extends TextureSheetParticle {
    BloodParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.08F;
    }

    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.preMoveUpdate();
        if (!this.removed) {
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.postMoveUpdate();
            if (!this.removed) {
                this.xd *= 0.98D;
                this.yd *= 0.98D;
                this.zd *= 0.98D;
            }
        }
    }

    protected void preMoveUpdate() {
        if (this.lifetime-- <= 0) this.remove();
    }

    protected void postMoveUpdate() { }

    static class FallParticle extends BloodParticle {
        protected final ParticleOptions landParticle;

        FallParticle(ClientLevel level, double x, double y, double z, ParticleOptions particleOptions) {
            super(level, x, y, z);
            this.lifetime = (int)(64.0D / (Math.random() * 0.8D + 0.1D));
            this.landParticle = particleOptions;
        }

        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    static class LandParticle extends BloodParticle {
        LandParticle(ClientLevel level, double x, double y, double z) {
            super(level, x, y, z);
            this.lifetime = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
        }
    }

    public static class BloodFallProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;

        public BloodFallProvider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double x1, double y1, double z1) {
            BloodParticle particle = new FallParticle(level, x, y, z, BrassArmoryParticles.BLOOD_LAND_PARTICLE.get());
            particle.setColor(0.54F, 0.01F, 0.01F);
            particle.pickSprite(this.sprite);
            return particle;
        }
    }

    public static class BloodLandProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;

        public BloodLandProvider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double x1, double y1, double z1) {
            BloodParticle particle = new LandParticle(level, x, y, z);
            particle.lifetime = (int)(28.0D / (Math.random() * 0.8D + 0.2D));
            particle.setColor(0.54F, 0.01F, 0.01F);
            particle.pickSprite(this.sprite);
            return particle;
        }
    }
}