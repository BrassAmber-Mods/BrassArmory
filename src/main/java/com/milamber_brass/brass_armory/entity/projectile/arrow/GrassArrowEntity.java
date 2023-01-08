package com.milamber_brass.brass_armory.entity.projectile.arrow;

import com.milamber_brass.brass_armory.client.render.SpecialArrowRenderer;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractSpecialArrowEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GrassArrowEntity extends AbstractSpecialArrowEntity {
    public GrassArrowEntity(EntityType<? extends GrassArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public GrassArrowEntity(Level level, LivingEntity shooter) {
        super(BrassArmoryEntityTypes.GRASS_ARROW.get(), level, shooter);
        this.setBaseDamage(0.25D);
    }

    public GrassArrowEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.GRASS_ARROW.get(), level, x, y, z);
        this.setBaseDamage(0.25D);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.dealtDamage) {
            BlockPos resultPos = result.getBlockPos();
            if (this.level.getBlockState(resultPos).is(Blocks.DIRT)) {
                this.level.setBlock(resultPos, Blocks.GRASS_BLOCK.defaultBlockState(), 2);
            }
            if (this.level instanceof ServerLevel serverLevel && serverLevel.getBlockState(resultPos).is(BlockTags.DIRT)) {
                if (!BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), this.level, resultPos, FakePlayerFactory.getMinecraft(serverLevel))) {
                    BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), this.level, resultPos.above(), FakePlayerFactory.getMinecraft(serverLevel));
                }
            }
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return BrassArmoryItems.GRASS_ARROW.get().getDefaultInstance();
    }

    @Override
    protected void spawnArrowParticles(int particleCount) {
        for (int j = 0; j < particleCount; ++j) {
            this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SEAGRASS.defaultBlockState()), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 40, 75, 40);
        }
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return SpecialArrowRenderer.GRASS_ARROW_TEXTURE;
    }
}