package com.milamber_brass.brass_armory.client.sound;

import com.milamber_brass.brass_armory.entity.CannonEntity;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class CannonSoundInstance extends AbstractTickableSoundInstance {
    private static final float VOLUME_MIN = 0.0F;
    private static final float VOLUME_MAX = 0.3F;
    private static final float PITCH_MIN = 0.0F;
    private static final float PITCH_MAX = 0.01F;
    private static final float PITCH_DELTA = 0.000025F;
    private final CannonEntity cannon;

    public CannonSoundInstance(CannonEntity cannon) {
        super(BrassArmorySounds.CANNON_MOVE.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.cannon = cannon;
        this.looping = true;
        this.delay = 0;
        this.volume = VOLUME_MIN;
        this.x = cannon.getX();
        this.y = cannon.getY();
        this.z = cannon.getZ();
    }

    @Override
    public boolean canPlaySound() {
        return !this.cannon.isSilent();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (this.cannon.isRemoved()) {
            this.stop();
        } else {
            this.x = this.cannon.getX();
            this.y = this.cannon.getY();
            this.z = this.cannon.getZ();
            int moveTicks = this.cannon.getMoveTicks();
            if (moveTicks >= 35) {
                moveTicks -= 35;
                this.pitch = Mth.clamp(this.pitch + PITCH_DELTA, PITCH_MIN, PITCH_MAX);
                this.volume = Mth.lerp(Mth.clamp((float)Math.min(moveTicks, this.cannon.getSoundTicks()) / 5.0F, 0.0F, 1.0F), VOLUME_MIN, VOLUME_MAX);
            } else {
                this.pitch = PITCH_MIN;
                this.volume = VOLUME_MIN;
            }
        }
    }
}