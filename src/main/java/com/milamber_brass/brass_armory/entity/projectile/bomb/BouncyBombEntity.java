package com.milamber_brass.brass_armory.entity.projectile.bomb;

import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BouncyBombEntity extends BombEntity {

    public BouncyBombEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public BouncyBombEntity(Level level, LivingEntity livingEntity, @Nullable HumanoidArm arm) {
        super(level, livingEntity, arm);
    }

    public BouncyBombEntity(Level level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Override
    public BombType getBombType() {
        return BombType.BOUNCY;
    }

    @Override
    public boolean isOnGround() {
        return false;
    }

    @Override
    protected SoundEvent getSoundEvent() {
        return BrassArmorySounds.BOUNCY_BOMB_HIT.get();
    }
}
