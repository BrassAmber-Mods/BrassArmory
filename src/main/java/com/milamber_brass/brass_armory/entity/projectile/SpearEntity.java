package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.SpearItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpearEntity extends AbstractThrownWeaponEntity {
    public SpearEntity(EntityType<SpearEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SpearEntity(Level level, LivingEntity livingEntity, ItemStack spearStack) {
        super(BrassArmoryEntityTypes.SPEAR.get(), livingEntity, level, spearStack);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity living && living.getType() != EntityType.ENDERMAN && this.getItem().getItem() instanceof SpearItem) {
            ArmoryUtil.impaleLivingEntity(living, this.entityData.get(DATA_DAMAGE_VALUE), this.level.random);
        }
        super.onHitEntity(entityHitResult);
    }

    @Override
    protected String onHitDamageSource() {
        return "spear";
    }

    @Override
    protected SoundEvent onHitSoundEvent() {
        return BrassArmorySounds.SPEAR_HIT.get();
    }

    @NotNull
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return BrassArmorySounds.SPEAR_HIT_GROUND.get();
    }

    @Override
    protected Item getDefaultItem() {
        return BrassArmoryItems.WOODEN_SPEAR.get();
    }
}