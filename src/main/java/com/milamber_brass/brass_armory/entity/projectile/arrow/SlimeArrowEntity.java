package com.milamber_brass.brass_armory.entity.projectile.arrow;

import com.milamber_brass.brass_armory.client.render.SpecialArrowRenderer;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractRollableItemProjectileEntity;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractSpecialArrowEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SlimeArrowEntity extends AbstractSpecialArrowEntity {
    public SlimeArrowEntity(EntityType<? extends SlimeArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SlimeArrowEntity(Level level, LivingEntity shooter) {
        super(BrassArmoryEntityTypes.SLIME_ARROW.get(), level, shooter);
        this.setBaseDamage(0.5D);
    }

    public SlimeArrowEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.SLIME_ARROW.get(), level, x, y, z);
        this.setBaseDamage(0.5D);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        if (!this.dealtDamage) {
            living.knockback(this.getDeltaMovement().length() * 0.75D, this.getX() - living.getX(), this.getZ() - living.getZ());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.dealtDamage) {
            this.setDeltaMovement(AbstractRollableItemProjectileEntity.bounce(this.getDeltaMovement(), result.getDirection().getAxis(), 0.75));
            return;
        } else if (this.random.nextInt(10) == 0) this.spawnSmallSlime();
        super.onHitBlock(result);
    }

    private void spawnSmallSlime() {
        if (this.isAlive()) {
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                Slime slimeEntity = EntityType.SLIME.create(this.level);
                if (slimeEntity == null) return;
                slimeEntity.addTag("no_loot");
                slimeEntity.refreshDimensions();
                slimeEntity.setHealth(1);
                slimeEntity.moveTo(this.blockPosition().getX() + .5f, this.blockPosition().getY() + 0.05f, this.blockPosition().getZ() + .5f, this.getYRot(), this.getXRot());
                this.level.addFreshEntity(slimeEntity);
                this.discard();
            }
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return BrassArmoryItems.SLIME_ARROW.get().getDefaultInstance();
    }

    @Override
    protected void spawnArrowParticles(int particleCount) {
        if (this.random.nextInt(2) >= particleCount) return;
        for (int j = 0; j < particleCount; ++j) {
            this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState()), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 40, 75, 40);
        }
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return SpecialArrowRenderer.SLIME_ARROW_TEXTURE;
    }
}