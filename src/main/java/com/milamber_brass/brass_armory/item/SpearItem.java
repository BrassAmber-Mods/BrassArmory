package com.milamber_brass.brass_armory.item;


import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.milamber_brass.brass_armory.entity.projectile.SpearEntity;
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
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;


public class SpearItem extends TridentItem implements Vanishable, ICustomReachItem, ICustomTieredItem {

    //private static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("99f7541c-a163-437c-8c25-bd685549b305");
    private static final float SPECIAL_REACH_MULTIPLIER = 1.5F;
    private final double reachDistanceBonus = 1.0D;
    private final float attackDamage;
    private final Tier tier;

    /**
     * Modifiers applied when the item is in the mainhand of a user.
     */
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private Multimap<Attribute, AttributeModifier> customAttributes;

    public SpearItem(Tier tier, int attackDamageIn, Properties builderIn) {
        super(builderIn.defaultDurability(tier.getUses()));
        this.attackDamage = attackDamageIn + tier.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.6D, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
        this.tier = tier;
    }

    public double getReach() {
        return this.reachDistanceBonus;
    }

    public double getReachExtended() {
        return this.reachDistanceBonus * SPECIAL_REACH_MULTIPLIER;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canAttackBlock(BlockState state, Level worldIn, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    @ParametersAreNonnullByDefault
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.WATER_PLANT && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
    }

    @Override
    public Tier getTier() {
        return this.tier;
    }

    /**
     * @return The action that specifies what animation to play when the item is being used.
     */
    @ParametersAreNonnullByDefault
    @Nonnull
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    /**
     * How long it takes to use or consume an item.
     */
    @ParametersAreNonnullByDefault
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @ParametersAreNonnullByDefault
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player playerEntity) {
            int i = this.getUseDuration(stack) - timeLeft;
            if (i >= 10) {
                if (!worldIn.isClientSide) {
                    stack.hurtAndBreak(1, playerEntity, (player) -> player.broadcastBreakEvent(entityLiving.getUsedItemHand()));
                    SpearEntity spear;
                    try {
                        spear = new SpearEntity(worldIn, playerEntity, stack);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    spear.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, 2.5F /* 0.6F*/, 1.0F);

                    if (playerEntity.getAbilities().instabuild) spear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    worldIn.addFreshEntity(spear);
                    worldIn.playSound(null, spear, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!playerEntity.getAbilities().instabuild) playerEntity.getInventory().removeItem(stack);
                }
                playerEntity.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @ParametersAreNonnullByDefault
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            playerIn.startUsingItem(handIn);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    @ParametersAreNonnullByDefault
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @ParametersAreNonnullByDefault
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double) state.getDestroySpeed(worldIn, pos) != 0.0D) {
            stack.hurtAndBreak(2, entityLiving, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) && enchantment != Enchantments.RIPTIDE && enchantment != Enchantments.CHANNELING;
    }

    @ParametersAreNonnullByDefault
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getCustomAttributesField() {
        return this.customAttributes;
    }

    @Override
    public void setCustomAttributesField(Multimap<Attribute, AttributeModifier> value) {
        this.customAttributes = value;
    }

    @Override
    @Nonnull
    public Multimap<Attribute, AttributeModifier> execSuperGetAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return ICustomReachItem.super.getAttributeModifiers(slot, stack);
    }

    @Override
    public double getReachDistanceBonus() {
        return 1;
    }
}
