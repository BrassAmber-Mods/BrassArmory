package com.milamber_brass.brass_armory.item.abstracts;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.milamber_brass.brass_armory.entity.projectile.AbstractThrownWeaponEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public abstract class AbstractThrownWeaponItem extends TieredItem implements Vanishable {
    protected Multimap<Attribute, AttributeModifier> defaultModifiers;
    public static final UUID BASE_ATTACK_RANGE_UUID = UUID.fromString("88a8a0f7-2afd-48b5-8169-2e1f30fd408d");
    public final float chargeDuration;
    private final float throwMultiplier;

    public AbstractThrownWeaponItem(Tier tier, int attackDamage, float attackSpeed, float chargeDuration, float throwMultiplier, Properties properties) {
        super(tier, properties);
        this.chargeDuration = chargeDuration;
        this.throwMultiplier = throwMultiplier;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = this.setDefaultModifiers(tier, attackDamage, attackSpeed);
        this.defaultModifiers = builder == null ? null : builder.build();
    }

    @ParametersAreNonnullByDefault
    protected ImmutableMultimap.Builder<Attribute, AttributeModifier> setDefaultModifiers(Tier tier, int attackDamage, float attackSpeed) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon damage modifier", (float)attackDamage + tier.getAttackDamageBonus(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon speed modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        return builder;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    @Override
    @ParametersAreNonnullByDefault
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.WATER_PLANT && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
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
                    AbstractThrownWeaponEntity thrownWeapon = getEntity(level, playerEntity, stack);
                    float power = Math.min(time / this.chargeDuration, 1F);
                    thrownWeapon.setPower(power);
                    thrownWeapon.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, power * this.throwMultiplier, 1.0F);
                    if (playerEntity.getAbilities().instabuild) thrownWeapon.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    else playerEntity.getInventory().removeItem(stack);
                    level.addFreshEntity(thrownWeapon);
                    level.playSound(null, thrownWeapon, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                playerEntity.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Nonnull
    protected abstract AbstractThrownWeaponEntity getEntity(Level level, LivingEntity living, ItemStack weaponStack);

    @Override
    @ParametersAreNonnullByDefault
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double) state.getDestroySpeed(worldIn, pos) != 0.0D) {
            stack.hurtAndBreak(2, entityLiving, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment == Enchantments.LOYALTY || (enchantment.category == EnchantmentCategory.WEAPON && enchantment != Enchantments.SWEEPING_EDGE);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
    }
}
