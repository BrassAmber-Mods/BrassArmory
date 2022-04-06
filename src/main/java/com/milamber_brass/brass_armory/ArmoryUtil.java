package com.milamber_brass.brass_armory;

import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class ArmoryUtil {
    @ParametersAreNonnullByDefault
    public static void impaleLivingEntity(LivingEntity living, float damage, Random random) {
        float damageAfterAbsorb = CombatRules.getDamageAfterAbsorb(damage, living.getArmorValue(), (float)living.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        float finalDamage = damage - damageAfterAbsorb;
        finalDamage = Math.min(finalDamage, (float)Math.sqrt(finalDamage * 10F) / 10F);

        living.hurt(new DamageSource("SpearImpale").bypassArmor(), finalDamage);

        for (EquipmentSlot slot : new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET }) {
            if (random.nextBoolean()) break;
            ItemStack slotStack = living.getItemBySlot(slot);
            if (!slotStack.isEmpty()) {
                slotStack.hurtAndBreak(random.nextInt(4), living, (livingEntity) -> {
                    livingEntity.broadcastBreakEvent(slot);
                });
            }
        }
    }
}
