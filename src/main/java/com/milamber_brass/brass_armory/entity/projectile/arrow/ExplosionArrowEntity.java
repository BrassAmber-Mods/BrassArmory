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
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ExplosionArrowEntity extends AbstractSpecialArrowEntity {
    public ExplosionArrowEntity(EntityType<? extends ExplosionArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ExplosionArrowEntity(Level level, LivingEntity shooter) {
        super(BrassArmoryEntityTypes.EXPLOSION_ARROW.get(), level, shooter);
    }

    public ExplosionArrowEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.EXPLOSION_ARROW.get(), level, x, y, z);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        if (!this.level.isClientSide) {
            ArmoryUtil.explode(this.level, this, hitResult.getLocation(), 2.0F, this.isOnFire(), Explosion.BlockInteraction.DESTROY);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!this.level.isClientSide) {
            ArmoryUtil.explode(this.level, this, entityHitResult.getLocation(), 2.0F, this.isOnFire(), Explosion.BlockInteraction.DESTROY);
            this.discard();
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return BrassArmoryItems.EX_ARROW.get().getDefaultInstance();
    }

    @Override
    protected void spawnArrowParticles(int particleCount) {
        if (this.level.getRandom().nextInt(particleCount) == 1) {
            Vec3 smokeVec = this.position().add(this.getDeltaMovement().multiply(-1.5D, -1.5D, -1.5D)).add(0, 0.125D, 0);
            this.level.addParticle(ParticleTypes.SMOKE, smokeVec.x, smokeVec.y, smokeVec.z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return SpecialArrowRenderer.EX_ARROW_TEXTURE;
    }
}