package com.milamber_brass.brass_armory.entity.projectile.arrow;

import com.milamber_brass.brass_armory.client.render.SpecialArrowRenderer;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractSpecialArrowEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LaserArrowEntity extends AbstractSpecialArrowEntity {
    public static final ParticleOptions REDSTONE_PARTICLE = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState());

    public LaserArrowEntity(EntityType<? extends LaserArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public LaserArrowEntity(Level level, LivingEntity shooter) {
        super(BrassArmoryEntityTypes.LASER_ARROW.get(), level, shooter);
        this.setBaseDamage(4D);
        this.setPierceLevel((byte)5);
        this.setNoGravity(true);
    }

    public LaserArrowEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.LASER_ARROW.get(), level, x, y, z);
        this.setBaseDamage(4D);
        this.setPierceLevel((byte)5);
        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        if (!this.inGround) {
            Vec3 deltaMovement = this.getDeltaMovement();
            super.tick();
            if (this.isNoGravity()) {
                super.tick();
                this.setDeltaMovement(deltaMovement);
            }

            if (!this.level().hasChunk(this.chunkPosition().x, this.chunkPosition().z)) {
                this.remove(RemovalReason.UNLOADED_TO_CHUNK);
            }
        } else {
            super.tick();
            this.setNoGravity(false);
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return BrassArmoryItems.LASER_ARROW.get().getDefaultInstance();
    }

    @Override
    protected void spawnArrowParticles(int particleCount) {
        for (int j = 0; j < particleCount; ++j) {
            this.level().addParticle(REDSTONE_PARTICLE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 40, 75, 40);
        }
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return SpecialArrowRenderer.LASER_ARROW_TEXTURE;
    }
}