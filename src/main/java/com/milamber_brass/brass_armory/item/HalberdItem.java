package com.milamber_brass.brass_armory.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.milamber_brass.brass_armory.ArmoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem.BASE_ATTACK_RANGE_UUID;

public class HalberdItem extends TieredItem implements Vanishable {//Uses vanilla TieredItem since its so different anyway, might change later though
    protected Multimap<Attribute, AttributeModifier> axeModifiers;
    protected final float axeDamage;
    protected final float axeSpeed;
    protected final float axeReach;

    protected Multimap<Attribute, AttributeModifier> spearModifiers;
    protected final float spearDamage;
    protected final float spearSpeed;
    protected final float spearReach;

    public HalberdItem(Tier tier, int axeDamage, float axeSpeed, float axeReach, int spearDamage, float spearSpeed, float spearReach, Properties properties) {
        super(tier, properties);
        this.axeDamage = (float) axeDamage + tier.getAttackDamageBonus();
        this.axeSpeed = axeSpeed;
        this.axeReach = axeReach;
        this.spearDamage = (float) spearDamage + tier.getAttackDamageBonus();
        this.spearSpeed = spearSpeed;
        this.spearReach = spearReach;
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        setStance(itemstack, !getStance(itemstack));
        player.getCooldowns().addCooldown(this, 10);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (this.axeModifiers == null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon damage modifier", this.axeDamage, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon speed modifier", this.axeSpeed, AttributeModifier.Operation.ADDITION));
            builder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(BASE_ATTACK_RANGE_UUID, "Weapon range modifier", this.axeReach, AttributeModifier.Operation.ADDITION));
            this.axeModifiers = builder.build();
        }
        if (this.spearModifiers == null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon damage modifier", this.spearDamage, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon speed modifier", this.spearSpeed, AttributeModifier.Operation.ADDITION));
            builder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(BASE_ATTACK_RANGE_UUID, "Weapon range modifier", this.spearReach, AttributeModifier.Operation.ADDITION));
            this.spearModifiers = builder.build();
        }
        return slot == EquipmentSlot.MAINHAND ? (getStance(stack) ? this.spearModifiers : this.axeModifiers) : super.getAttributeModifiers(slot, stack);
    }

    @Override
    @ParametersAreNonnullByDefault //Deal bonus damage when attacking if the stance is on spear mode
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!entity.level.isClientSide && entity instanceof LivingEntity living && getStance(stack)) {
            ArmoryUtil.impaleLivingEntity(living, (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE), player.level.random);
        }
        return false;
    }

    @Override //Disable shields if the stance is axe mode
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return !getStance(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || (enchantment.category == EnchantmentCategory.WEAPON && enchantment != Enchantments.SWEEPING_EDGE);
    }

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

    @ParametersAreNonnullByDefault
    public static void setStance(ItemStack stack, boolean stance) {
        stack.getOrCreateTag().putBoolean("BrassArmoryStance", stance);
    }

    @ParametersAreNonnullByDefault
    public static boolean getStance(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("BrassArmoryStance");
    }
}
