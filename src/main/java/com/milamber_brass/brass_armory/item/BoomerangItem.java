package com.milamber_brass.brass_armory.item;

import com.google.common.collect.ImmutableSet;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.entity.projectile.BoomerangEntity;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import com.milamber_brass.brass_armory.item.interfaces.ICustomAnimationItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;

public class BoomerangItem extends AbstractThrownWeaponItem implements ICustomAnimationItem {
    private final ImmutableSet<Enchantment> properEnchantments = ImmutableSet.of(Enchantments.FLAMING_ARROWS, Enchantments.QUICK_CHARGE);
    public final static String critTag = "BABoomerangCrit";
    public final static String timeTag = "BABoomerangTime";

    private static Field toolHighlightTimerField = null;

    public BoomerangItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, 18F, 1.25F, properties);
    }

    @Nonnull
    @Override
    protected AbstractThrownWeaponEntity getEntity(Level level, LivingEntity living, ItemStack boomerangStack) {
        BoomerangEntity boomerangEntity = new BoomerangEntity(level, living, boomerangStack);
        boomerangEntity.setCritArrow(level.random.nextFloat(100F) <= getCrit(boomerangStack));
        return boomerangEntity;
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
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || properEnchantments.contains(enchantment);
    }

    @Override
    public float getChargeDuration(ItemStack boomerangStack) {
        int quickCharge = Math.min(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, boomerangStack), 4);
        return quickCharge == 0 ? this.chargeDuration : this.chargeDuration - 3 * quickCharge;
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
