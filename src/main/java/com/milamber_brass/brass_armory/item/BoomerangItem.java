package com.milamber_brass.brass_armory.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.milamber_brass.brass_armory.entity.projectile.BoomerangEntity;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;

public class BoomerangItem extends TieredItem implements Vanishable, ICustomAnimationItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    private final ImmutableSet<Enchantment> properEnchantments = ImmutableSet.of(Enchantments.LOYALTY, Enchantments.FLAMING_ARROWS, Enchantments.QUICK_CHARGE, Enchantments.RIPTIDE);
    public final static String critTag = "BABoomerangCrit";
    public final static String timeTag = "BABoomerangTime";

    private static Field toolHighlightTimerField = null;

    public BoomerangItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", attackDamage + tier.getAttackDamageBonus(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack boomerangStack = player.getItemInHand(interactionHand);
        player.startUsingItem(interactionHand);
        return InteractionResultHolder.consume(boomerangStack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void releaseUsing(ItemStack boomerangStack, Level level, LivingEntity livingEntity, int useDurationLeft) {
        if (livingEntity instanceof Player player) {
            float time = this.getUseDuration(boomerangStack) - useDurationLeft;
            float power = Math.min(time / this.getChargeDuration(boomerangStack), 1F);

            if ((double)power >= 0.1D && !level.isClientSide) {
                boomerangStack.hurtAndBreak(1, player, (player1) -> player1.broadcastBreakEvent(livingEntity.getUsedItemHand()));
                boolean noGravity = power > 0.25F;
                BoomerangEntity boomerangEntity = new BoomerangEntity(level, player, boomerangStack);
                boomerangEntity.setPower(power);

                float xRot = livingEntity.getXRot();
                float yRot = livingEntity.getYRot();
                boomerangEntity.shootFromRotation(livingEntity, xRot, yRot, 0.0F, power, 1.0F);
                boomerangEntity.setXRot(xRot);
                boomerangEntity.setYRot(yRot);
                boomerangEntity.setNoGravity(noGravity);

                boomerangEntity.setCritArrow(level.random.nextFloat(100F) <= getCrit(boomerangStack));

                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, boomerangStack) > 0) boomerangEntity.setSecondsOnFire(100);

                level.addFreshEntity(boomerangEntity);
                level.playSound(null, boomerangEntity, BrassArmorySounds.BOMB_THROW.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                player.getInventory().removeItem(boomerangStack);
            }
        }
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public Component getName(ItemStack boomerangStack) {
        int crit = (int)getCrit(boomerangStack);
        if (crit <= 0) return super.getName(boomerangStack);
        return new TextComponent("Critical Strike: " + crit + "%").withStyle(Style.EMPTY.withBold(true).withColor(13107200 + ((5 - (crit / 20)) * 10000)));
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hurtEnemy(ItemStack boomerangStack, LivingEntity victim, LivingEntity player) {
        boomerangStack.hurtAndBreak(3, player, (living) -> living.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean mineBlock(ItemStack boomerangStack, Level level, BlockState blockState, BlockPos pos, LivingEntity livingEntity) {
        if ((double)blockState.getDestroySpeed(level, pos) != 0.0D) {
            boomerangStack.hurtAndBreak(2, livingEntity, (living) -> living.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack boomerangStack, Level level, Entity entity, int i, boolean b) {
        float crit = getCrit(boomerangStack);
        if (crit > 0F && entity instanceof LivingEntity living) {
            int drain = (int)Math.min(level.getGameTime() - getTime(boomerangStack), 1000L) / 20;
            if (!level.isClientSide && drain > 0 && !living.isUsingItem()) {
                boomerangStack.getOrCreateTag().putFloat(BoomerangItem.critTag, Math.max(crit - drain, 0F));
            }
            if (level.isClientSide && living.getMainHandItem() == boomerangStack) {
                if (toolHighlightTimerField == null) {
                    toolHighlightTimerField = ObfuscationReflectionHelper.findField(Gui.class, "f_92993_");
                    toolHighlightTimerField.setAccessible(true);
                }
                try {
                    toolHighlightTimerField.set(Minecraft.getInstance().gui, 40);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        super.inventoryTick(boomerangStack, level, entity, i, b);
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    @SuppressWarnings("deprecation")
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || properEnchantments.contains(enchantment);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.getItem().equals(newStack.getItem()) || slotChanged;
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public UseAnim getUseAnimation(ItemStack boomerangStack) {
        return UseAnim.SPEAR;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getUseDuration(ItemStack boomerangStack) {
        return 72000;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getCustomUseDuration(ItemStack boomerangStack, Player player) {
        return this.getUseDuration(boomerangStack) - player.getUseItemRemainingTicks();
    }

    @Override
    public int getChargeDuration(ItemStack boomerangStack) {
        int quickCharge = Math.min(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, boomerangStack), 3);
        return quickCharge == 0 ? 24 : 24 - 4 * quickCharge;
    }

    @ParametersAreNonnullByDefault
    public static void setCrit(ItemStack boomerangStack, float crit, long time) {
        boomerangStack.getOrCreateTag().putFloat(BoomerangItem.critTag, Math.min(crit, 100F));
        boomerangStack.getOrCreateTag().putLong(BoomerangItem.timeTag, time);
    }

    @ParametersAreNonnullByDefault
    public static float getCrit(ItemStack boomerangStack) {
        if (boomerangStack.getTag() != null && boomerangStack.getTag().contains(BoomerangItem.critTag)) {
            return boomerangStack.getOrCreateTag().getFloat(BoomerangItem.critTag);
        }
        return 0F;
    }

    @ParametersAreNonnullByDefault
    public static long getTime(ItemStack boomerangStack) {
        if (boomerangStack.getTag() != null && boomerangStack.getTag().contains(BoomerangItem.timeTag)) {
            return boomerangStack.getOrCreateTag().getLong(BoomerangItem.timeTag);
        }
        return 0L;
    }
}
