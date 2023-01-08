package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.entity.projectile.BattleaxeEntity;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import com.milamber_brass.brass_armory.item.interfaces.ICustomAnimationItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BattleaxeItem extends AbstractThrownWeaponItem implements ICustomAnimationItem {
    public BattleaxeItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, 20F, 0.75F, properties);
    }

    @Override
    public @NotNull SoundEvent throwSound() {
        return BrassArmorySounds.BATTLEAXE_THROW.get();
    }

    @Nonnull
    @Override
    protected AbstractThrownWeaponEntity getEntity(Level level, LivingEntity living, ItemStack weaponStack) {
        return new BattleaxeEntity(living, level, weaponStack);
    }

    @Override
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


