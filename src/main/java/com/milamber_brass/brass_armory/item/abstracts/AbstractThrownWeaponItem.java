package com.milamber_brass.brass_armory.item.abstracts;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public abstract class AbstractThrownWeaponItem extends AbstractTieredWeaponItem {
    public static final UUID BASE_ATTACK_RANGE_UUID = UUID.fromString("88a8a0f7-2afd-48b5-8169-2e1f30fd408d");
    public final float chargeDuration;
    protected final float throwMultiplier;

    public AbstractThrownWeaponItem(Tier tier, float attackDamage, float attackSpeed, float chargeDuration, float throwMultiplier, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
        this.chargeDuration = chargeDuration;
        this.throwMultiplier = throwMultiplier;
    }

    public float getThrowMultiplier() {
        return throwMultiplier;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public UseAnim getUseAnimation(ItemStack boomerangStack) {
        return UseAnim.SPEAR;
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player playerEntity) {
            float time = this.getUseDuration(stack) - timeLeft;
            if (time >= 5) {
                if (!level.isClientSide) {
                    stack.hurtAndBreak(1, playerEntity, (player) -> player.broadcastBreakEvent(entityLiving.getUsedItemHand()));
                    AbstractThrownWeaponEntity thrownWeapon = getEntity(level, playerEntity, stack.copy());
                    float power = Math.min(time / this.chargeDuration, 1F);
                    thrownWeapon.setPower(power);
                    thrownWeapon.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, power * this.throwMultiplier, 1.0F);
                    if (playerEntity.getAbilities().instabuild) thrownWeapon.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    else if (this.shrinkItem()) stack.shrink(1);
                    level.addFreshEntity(thrownWeapon);
                    level.playSound(null, thrownWeapon, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                playerEntity.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    protected boolean shrinkItem() {
        return true;
    }

    @Nonnull
    protected abstract AbstractThrownWeaponEntity getEntity(Level level, LivingEntity living, ItemStack weaponStack);

    @Override
    @ParametersAreNonnullByDefault
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment == Enchantments.LOYALTY;
    }
}
