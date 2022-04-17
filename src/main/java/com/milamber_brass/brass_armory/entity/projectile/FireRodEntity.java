package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.FireRodItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class FireRodEntity extends AbstractThrownWeaponEntity {
    public FireRodEntity(EntityType<FireRodEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FireRodEntity(Level level, LivingEntity livingEntity, ItemStack spearStack) {
        super(BrassArmoryEntityTypes.FIRE_ROD.get(), livingEntity, level, spearStack);
        this.setRemainingFireTicks(Integer.MAX_VALUE);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide && this.getSharedFlag(0) && (this.inGround || level.getRandom().nextInt(2) == 1)) {
            Vec3 smokeVec = this.position();
            this.level.addParticle(ParticleTypes.SMOKE, smokeVec.x, smokeVec.y, smokeVec.z, 0.0D, 0.0D, 0.0D);
            this.level.addParticle(this.getItem().getItem() instanceof FireRodItem fireRod ? fireRod.flameParticle : ParticleTypes.FLAME, smokeVec.x, smokeVec.y, smokeVec.z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = this.level.getBlockState(pos);
        Entity owner = this.getOwner();
        if (CampfireBlock.canLight(state) || CandleBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
            this.level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
            this.level.gameEvent(owner, GameEvent.BLOCK_PLACE, pos);
        } else {
            BlockPos relativePos = pos.relative(blockHitResult.getDirection());
            if (BaseFireBlock.canBePlacedAt(level, relativePos, blockHitResult.getDirection())) {
                BlockState relativeState = BaseFireBlock.getState(level, relativePos);
                this.level.setBlock(relativePos, relativeState, 11);
                this.level.gameEvent(owner, GameEvent.BLOCK_PLACE, pos);
            }
        }
    }

    @Override
    public boolean isOnFire() {
        return !this.level.isClientSide && super.isOnFire();
    }

    public boolean hasBeenExtinguished() {
        return !this.getSharedFlag(0);
    }

    @Override
    protected String onHitDamageSource() {
        return "BAFireRod";
    }

    @Override
    protected SoundEvent onHitSoundEvent() {
        return SoundEvents.TRIDENT_HIT;//TODO:SOUNDS
    }

    @NotNull
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    protected Item getDefaultItem() {
        return BrassArmoryItems.FIRE_ROD.get();
    }
}
