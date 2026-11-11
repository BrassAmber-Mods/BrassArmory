package com.milamber_brass.brass_armory.item.abstracts;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractThrownWeaponItem extends AbstractTieredWeaponItem {
    public static final UUID BASE_ATTACK_RANGE_UUID = UUID.fromString("88a8a0f7-2afd-48b5-8169-2e1f30fd408d");
    public final float chargeDuration;
    protected final float throwMultiplier;

    public AbstractThrownWeaponItem(ToolMaterial tier, float attackDamage, float attackSpeed, float chargeDuration, float throwMultiplier, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
        this.chargeDuration = chargeDuration;
        this.throwMultiplier = throwMultiplier;
    }

    public float getThrowMultiplier() {
        return throwMultiplier;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity living) {
        return 72000;
    }

        @Override
    public ItemUseAnimation getUseAnimation(ItemStack boomerangStack) {
        return ItemUseAnimation.SPEAR;
    }

        @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResult.FAIL;
        } else {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player playerEntity) {
            float time = this.getUseDuration(stack, entityLiving) - timeLeft;
            if (time >= 5) {
                if (!level.isClientSide()) {
                    stack.hurtAndBreak(1, playerEntity, entityLiving.getUsedItemHand());
                    AbstractThrownWeaponEntity thrownWeapon = getEntity(level, playerEntity, stack.copy());
                    float power = Math.min(time / this.chargeDuration, 1F);
                    thrownWeapon.setPower(power);
                    thrownWeapon.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, power * this.throwMultiplier, 1.0F);
                    if (playerEntity.getAbilities().instabuild) thrownWeapon.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    else if (this.shrinkItem()) stack.shrink(1);
                    level.addFreshEntity(thrownWeapon);
                    level.playSound(null, thrownWeapon, this.throwSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                playerEntity.awardStat(Stats.ITEM_USED.get(this));
            }
        }
        return true;
    }

    public abstract @NotNull SoundEvent throwSound();

    protected boolean shrinkItem() {
        return true;
    }

        protected abstract AbstractThrownWeaponEntity getEntity(Level level, LivingEntity living, ItemStack weaponStack);

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return super.supportsEnchantment(stack, enchantment) || enchantment.is(Enchantments.LOYALTY);
    }
}
