package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.entity.projectile.BattleaxeEntity;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import com.milamber_brass.brass_armory.item.interfaces.ICustomAnimationItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class BattleaxeItem extends AbstractThrownWeaponItem implements ICustomAnimationItem {
    public BattleaxeItem(Tiers tier, int attackDamageIn, Properties properties) {
        super(tier, attackDamageIn, -3.1F, 20F, 0.75F, properties);
    }

    @Nonnull
    @Override
    protected AbstractThrownWeaponEntity getEntity(Level level, LivingEntity living, ItemStack weaponStack) {
        return new BattleaxeEntity(living, level, weaponStack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_AXE) ? Math.max(this.getTier().getSpeed() * 0.8F, super.getDestroySpeed(stack, state)) : super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return true;
    }

    @Override
    public float getChargeDuration(ItemStack itemStack) {
        return this.chargeDuration;
    }
}


