package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DaggerEntity extends AbstractThrownWeaponEntity {
    public DaggerEntity(EntityType<DaggerEntity> entityType, Level level) {
        super(entityType, level);
    }

    public DaggerEntity(LivingEntity livingEntity, Level level, ItemStack daggerStack) {
        super(BrassArmoryEntityTypes.DAGGER.get(), livingEntity, level, daggerStack);
    }

    @Override
    protected String onHitDamageSource() {
        return "BADagger";
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
        return BrassArmoryItems.WOODEN_DAGGER.get();
    }
}
