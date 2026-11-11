package com.milamber_brass.brass_armory.item;

import com.google.common.collect.ImmutableSet;
import com.milamber_brass.brass_armory.entity.projectile.BoomerangEntity;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import com.milamber_brass.brass_armory.item.interfaces.ICustomAnimationItem;
import com.milamber_brass.brass_armory.util.ItemData;
import java.lang.reflect.Field;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.NotNull;

public class BoomerangItem extends AbstractThrownWeaponItem implements ICustomAnimationItem {
    private final ImmutableSet<ResourceKey<Enchantment>> properEnchantments = ImmutableSet.of(Enchantments.FLAME, Enchantments.QUICK_CHARGE);
    public final static String critTag = "BABoomerangCrit";
    public final static String timeTag = "BABoomerangTime";

    private static Field toolHighlightTimerField = null;

    public BoomerangItem(ToolMaterial tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, 18F, 1.25F, properties);
    }

    @Override
    public @NotNull SoundEvent throwSound() {
        return BrassArmorySounds.BOOMERANG_THROW.get();
    }

        @Override
    protected AbstractThrownWeaponEntity getEntity(Level level, LivingEntity living, ItemStack boomerangStack) {
        return Util.make(new BoomerangEntity(level, living, boomerangStack), boomerangEntity -> boomerangEntity.setCritArrow(level.getRandom().nextFloat() * 100F <= getCrit(boomerangStack)));
    }

        @Override
    public Component getName(ItemStack boomerangStack) {
        int crit = (int)getCrit(boomerangStack);
        if (crit <= 0) return super.getName(boomerangStack);
        return Component.translatable("Critical Strike: " + crit + "%").withStyle(Style.EMPTY.withBold(true).withColor(13107200 + ((5 - (crit / 20)) * 10000)));
    }

    @Override
    public void inventoryTick(ItemStack boomerangStack, net.minecraft.server.level.ServerLevel level, Entity entity, net.minecraft.world.entity.EquipmentSlot slot) {
        float crit = getCrit(boomerangStack);
        if (crit > 0F && entity instanceof LivingEntity living) {
            int drain = (int)Math.min(level.getGameTime() - getTime(boomerangStack), 1000L) / 20;
            if (!level.isClientSide() && drain > 0 && !living.isUsingItem()) {
                ItemData.update(boomerangStack, tag -> tag.putFloat(BoomerangItem.critTag, Math.max(crit - drain, 0F)));
            }
            if (level.isClientSide() && living.getMainHandItem() == boomerangStack) {
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
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return super.supportsEnchantment(stack, enchantment) || properEnchantments.stream().anyMatch(enchantment::is);
    }

    @Override
    public float getChargeDuration(ItemStack boomerangStack) {
        int quickCharge = Math.min(boomerangStack.getEnchantmentLevel(ItemData.registries().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.QUICK_CHARGE)), 4);
        return quickCharge == 0 ? this.chargeDuration : this.chargeDuration - 3 * quickCharge;
    }

    public static void setCrit(ItemStack boomerangStack, float crit, long time) {
        ItemData.update(boomerangStack, tag -> tag.putFloat(BoomerangItem.critTag, Math.min(crit, 100F)));
        ItemData.update(boomerangStack, tag -> tag.putLong(BoomerangItem.timeTag, time));
    }

    public static float getCrit(ItemStack boomerangStack) {
        if (ItemData.getTag(boomerangStack) != null && ItemData.getTag(boomerangStack).contains(BoomerangItem.critTag)) {
            return ItemData.getTag(boomerangStack).getFloatOr(BoomerangItem.critTag, 0F);
        }
        return 0F;
    }

    public static long getTime(ItemStack boomerangStack) {
        if (ItemData.getTag(boomerangStack) != null && ItemData.getTag(boomerangStack).contains(BoomerangItem.timeTag)) {
            return ItemData.getTag(boomerangStack).getLongOr(BoomerangItem.timeTag, 0L);
        }
        return 0L;
    }
}
