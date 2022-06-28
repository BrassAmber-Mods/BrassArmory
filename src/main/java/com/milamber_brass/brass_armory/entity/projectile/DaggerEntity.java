package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DaggerEntity extends AbstractThrownWeaponEntity {
    public DaggerEntity(EntityType<DaggerEntity> entityType, Level level) {
        super(entityType, level);
    }

    public DaggerEntity(LivingEntity livingEntity, Level level, ItemStack daggerStack) {
        super(BrassArmoryEntityTypes.DAGGER.get(), livingEntity, level, daggerStack);
    }

    @Override
    protected String onHitDamageSource() {
        return "dagger";
    }

    @Override
    protected SoundEvent onHitSoundEvent() {
        return BrassArmorySounds.DAGGER_HIT.get();
    }

    @NotNull
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return BrassArmorySounds.DAGGER_HIT_GROUND.get();
    }

    @Override
    protected Item getDefaultItem() {
        return BrassArmoryItems.WOODEN_DAGGER.get();
    }
}
