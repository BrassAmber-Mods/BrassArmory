package com.milamber_brass.brass_armory.entity.projectile.cannon_balls;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractBulletEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryAdvancements;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SiegeRoundEntity extends AbstractBulletEntity {
    public SiegeRoundEntity(EntityType<? extends AbstractBulletEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SiegeRoundEntity(Level level, LivingEntity livingEntity) {
        super(BrassArmoryEntityTypes.SIEGE_ROUND.get(), livingEntity, level);
    }

    public SiegeRoundEntity(double x, double y, double z, Level level) {
        super(BrassArmoryEntityTypes.SIEGE_ROUND.get(), x, y, z, level);
    }

    @Override
    public void shootFromRotation(Entity shooter, float xRot, float yRot, float v, float speed, float accuracy) {
        super.shootFromRotation(shooter, xRot, yRot, v, speed * 0.5F, accuracy);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        if (!this.level.isClientSide) {
            ArmoryUtil.explode(this.level, this, 4.0F, this.isOnFire(), Explosion.BlockInteraction.BREAK);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!this.level.isClientSide) {
            ArmoryUtil.explode(this.level, this, 4.0F, this.isOnFire(), Explosion.BlockInteraction.BREAK);
            if (entityHitResult.getEntity() instanceof Creeper && this.getOwner() instanceof ServerPlayer serverPlayer) {
                BrassArmoryAdvancements.SIEGE.trigger(serverPlayer);
            }
            this.discard();
        }
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return BrassArmoryItems.SIEGE_ROUND.get();
    }
}
