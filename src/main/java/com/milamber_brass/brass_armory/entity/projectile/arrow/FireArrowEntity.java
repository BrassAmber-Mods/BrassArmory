package com.milamber_brass.brass_armory.entity.projectile.arrow;

import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.client.render.SpecialArrowRenderer;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractSpecialArrowEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FireArrowEntity extends AbstractSpecialArrowEntity {
    public FireArrowEntity(EntityType<? extends FireArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FireArrowEntity(Level level, LivingEntity shooter) {
        super(BrassArmoryEntityTypes.FIRE_ARROW.get(), level, shooter);
        this.setBaseDamage(2.5D);
    }

    public FireArrowEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.FIRE_ARROW.get(), level, x, y, z);
        this.setBaseDamage(2.5D);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        if (!this.dealtDamage) {
            living.setSecondsOnFire(16);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.dealtDamage) {
            ArmoryUtil.blockHitSetOnFire(result, this.level, this.getOwner());
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return BrassArmoryItems.FIRE_ARROW.get().getDefaultInstance();
    }

    @Override
    protected void spawnArrowParticles(int particleCount) {
        for (int j = 0; j < particleCount; ++j) {
            this.level.addParticle(ParticleTypes.FLAME, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0D, 0D, 0D);
        }
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return SpecialArrowRenderer.FIRE_ARROW_TEXTURE;
    }
}