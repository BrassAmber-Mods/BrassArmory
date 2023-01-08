package com.milamber_brass.brass_armory.entity.projectile.arrow;

import com.milamber_brass.brass_armory.client.render.SpecialArrowRenderer;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractSpecialArrowEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DirtArrowEntity extends AbstractSpecialArrowEntity {
    public DirtArrowEntity(EntityType<? extends DirtArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public DirtArrowEntity(Level level, LivingEntity shooter) {
        super(BrassArmoryEntityTypes.DIRT_ARROW.get(), level, shooter);
        this.setBaseDamage(0.25D);
        if (shooter instanceof Player) this.pickup = Pickup.ALLOWED;
    }

    public DirtArrowEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.DIRT_ARROW.get(), level, x, y, z);
        this.setBaseDamage(0.25D);
        this.pickup = AbstractArrow.Pickup.ALLOWED;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        if (!this.dealtDamage) {
            this.level.setBlockAndUpdate(this.blockPosition(), Blocks.DIRT.defaultBlockState());
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.dealtDamage) {
            BlockPos relative = result.getBlockPos().relative(result.getDirection());
            if (this.level.getBlockState(relative).getMaterial().isReplaceable()) {
                this.level.setBlock(relative, Blocks.DIRT.defaultBlockState(), 2);
                this.level.gameEvent(this.getOwner(), GameEvent.BLOCK_PLACE, relative);
                this.discard();
            }
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return BrassArmoryItems.DIRT_ARROW.get().getDefaultInstance();
    }

    @Override
    protected void spawnArrowParticles(int particleCount) {
        for (int j = 0; j < particleCount; ++j) {
            this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DIRT.defaultBlockState()), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 40, 75, 40);
        }
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return SpecialArrowRenderer.DIRT_ARROW_TEXTURE;
    }
}
