package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.ArmoryUtil;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.SpearItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class SpearEntity extends AbstractThrownWeaponEntity {
    public SpearEntity(EntityType<SpearEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SpearEntity(Level level, LivingEntity livingEntity, ItemStack spearStack) {
        super(BrassArmoryEntityTypes.SPEAR.get(), livingEntity, level, spearStack);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity living && living.getType() != EntityType.ENDERMAN && this.getItem().getItem() instanceof SpearItem) {
            ArmoryUtil.impaleLivingEntity(living, this.entityData.get(DATA_DAMAGE_VALUE), this.level.random);
        }
        super.onHitEntity(entityHitResult);
    }

    @Override
    protected String onHitDamageSource() {
        return "BASpear";
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
        return BrassArmoryItems.WOODEN_SPEAR.get();
    }
}