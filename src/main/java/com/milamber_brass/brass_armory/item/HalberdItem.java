package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.util.ItemData;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HalberdItem extends TieredItem {//Uses vanilla TieredItem since its so different anyway, might change later though
    protected ItemAttributeModifiers axeModifiers;
    protected final float axeDamage;
    protected final float axeSpeed;
    protected final float axeReach;

    protected ItemAttributeModifiers spearModifiers;
    protected final float spearDamage;
    protected final float spearSpeed;
    protected final float spearReach;

    public HalberdItem(Tier tier, float axeDamage, float axeSpeed, float axeReach, float spearDamage, float spearSpeed, float spearReach, Properties properties) {
        super(tier, properties);
        this.axeDamage = axeDamage + tier.getAttackDamageBonus();
        this.axeSpeed = axeSpeed;
        this.axeReach = axeReach;
        this.spearDamage = spearDamage + tier.getAttackDamageBonus();
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
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        if (this.axeModifiers == null) {
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, this.axeDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            builder.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, this.axeSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            builder.add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(BrassArmory.locate("attack_range"), this.axeReach, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            this.axeModifiers = builder.build();
        }
        if (this.spearModifiers == null) {
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, this.spearDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            builder.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, this.spearSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            builder.add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(BrassArmory.locate("attack_range"), this.spearReach, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            this.spearModifiers = builder.build();
        }
        return getStance(stack) ? this.spearModifiers : this.axeModifiers;
    }

    @Override
    @ParametersAreNonnullByDefault //Deal bonus damage when attacking if the stance is on spear mode
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!entity.level().isClientSide && entity instanceof LivingEntity living && getStance(stack)) {
            ArmoryUtil.impaleLivingEntity(living, (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE), player.level().random);
        }
        return false;
    }

    @Override //Disable shields if the stance is axe mode
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return !getStance(stack);
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return super.supportsEnchantment(stack, enchantment) || (enchantment.value().definition().supportedItems().contains(BuiltInRegistries.ITEM.wrapAsHolder(Items.IRON_SWORD)) && !enchantment.is(Enchantments.SWEEPING_EDGE));
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double) state.getDestroySpeed(worldIn, pos) != 0.0D) {
            stack.hurtAndBreak(2, entityLiving, EquipmentSlot.MAINHAND);
        }
        return true;
    }

    @ParametersAreNonnullByDefault
    public static void setStance(ItemStack stack, boolean stance) {
        ItemData.update(stack, tag -> tag.putBoolean("BrassArmoryStance", stance));
    }

    @ParametersAreNonnullByDefault
    public static boolean getStance(ItemStack stack) {
        return ItemData.getTag(stack).getBoolean("BrassArmoryStance");
    }
}
