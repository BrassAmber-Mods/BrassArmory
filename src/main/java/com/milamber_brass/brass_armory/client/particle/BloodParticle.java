package com.milamber_brass.brass_armory.client.particle;

import com.milamber_brass.brass_armory.init.BrassArmoryParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class BloodParticle extends SingleQuadParticle {
    BloodParticle(ClientLevel level, double x, double y, double z, net.minecraft.client.renderer.texture.TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.08F;
    }

    @NotNull
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
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

        FallParticle(ClientLevel level, double x, double y, double z, ParticleOptions particleOptions, net.minecraft.client.renderer.texture.TextureAtlasSprite sprite) {
            super(level, x, y, z, sprite);
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
        LandParticle(ClientLevel level, double x, double y, double z, net.minecraft.client.renderer.texture.TextureAtlasSprite sprite) {
            super(level, x, y, z, sprite);
            this.lifetime = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
        }
    }

    public static class BloodFallProvider implements ParticleProvider.Sprite<SimpleParticleType> {
        protected final SpriteSet sprite;

        public BloodFallProvider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public SingleQuadParticle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double x1, double y1, double z1, net.minecraft.util.RandomSource random) {
            BloodParticle particle = new FallParticle(level, x, y, z, BrassArmoryParticles.BLOOD_LAND_PARTICLE.get(), this.sprite.get(random));
            particle.setColor(0.54F, 0.01F, 0.01F);
            return particle;
        }
    }

    public static class BloodLandProvider implements ParticleProvider.Sprite<SimpleParticleType> {
        protected final SpriteSet sprite;

        public BloodLandProvider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public SingleQuadParticle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double x1, double y1, double z1, net.minecraft.util.RandomSource random) {
            BloodParticle particle = new LandParticle(level, x, y, z, this.sprite.get(random));
            particle.lifetime = (int)(28.0D / (Math.random() * 0.8D + 0.2D));
            particle.setColor(0.54F, 0.01F, 0.01F);
            return particle;
        }
    }
}