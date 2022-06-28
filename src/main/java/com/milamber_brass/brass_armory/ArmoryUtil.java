package com.milamber_brass.brass_armory;

import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.effect.BleedEffect;
import com.milamber_brass.brass_armory.item.KatanaItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public class ArmoryUtil {
    public static void impaleLivingEntity(LivingEntity living, float damage, Random random) {
        float damageAfterAbsorb = CombatRules.getDamageAfterAbsorb(damage, living.getArmorValue(), (float)living.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        float finalDamage = damage - damageAfterAbsorb;
        finalDamage = Math.min(finalDamage, (float)Math.sqrt(finalDamage * 10F) / 10F);

        living.hurt((new DamageSource("brass_armory.impale")).bypassArmor(), finalDamage);

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

    public static void bleedLivingEntity(LivingEntity livingTarget, LivingEntity thisLiving) {
        ItemStack stack = thisLiving.getMainHandItem();
        if (stack.is(BrassArmoryTags.Items.BLEEDING_EDGE)) {
            BleedEffect.bleedHarder(livingTarget, 70, 1);
        }
        if (stack.getItem() instanceof KatanaItem katanaItem && katanaItem.canWither() && KatanaItem.getWither(stack) >= 100) {
            BleedEffect.witherHarder(livingTarget, 70);
        }
    }

    public static void getWitherFromLivingEntity(LivingEntity livingTarget, LivingEntity thisLiving) {
        ItemStack stack = thisLiving.getMainHandItem();
        if (thisLiving.level instanceof ServerLevel serverLevel && stack.getItem() instanceof KatanaItem katanaItem && katanaItem.canWither() && KatanaItem.getWither(stack) < 100) {
            if (livingTarget.isDeadOrDying() && livingTarget.getType().is(BrassArmoryTags.Entities.WITHER)) {
                KatanaItem.addWither(stack, thisLiving, serverLevel, livingTarget instanceof WitherBoss ? 50 : 2);
            }
        }
    }

    public static void blockHitSetOnFire(BlockHitResult blockHitResult, Level level, @Nullable Entity owner) {
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (CampfireBlock.canLight(state) || CandleBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
            level.gameEvent(owner, GameEvent.BLOCK_PLACE, pos);
        } else {
            BlockPos relativePos = pos.relative(blockHitResult.getDirection());
            Material material = level.getBlockState(relativePos).getMaterial();
            if (BaseFireBlock.canBePlacedAt(level, relativePos, blockHitResult.getDirection()) || material.equals(Material.PLANT) || material.equals(Material.REPLACEABLE_PLANT)) {
                BlockState relativeState = BaseFireBlock.getState(level, relativePos);
                level.setBlock(relativePos, relativeState, 11);
                level.gameEvent(owner, GameEvent.BLOCK_PLACE, pos);
            }
        }
    }
}
