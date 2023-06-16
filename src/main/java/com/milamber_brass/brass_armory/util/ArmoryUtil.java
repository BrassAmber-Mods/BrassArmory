package com.milamber_brass.brass_armory.util;

import com.google.common.collect.Lists;
import com.milamber_brass.brass_armory.data.BrassArmoryDamageTypes;
import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.effect.BleedEffect;
import com.milamber_brass.brass_armory.item.KatanaItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ArmoryUtil {
    public static void impaleLivingEntity(LivingEntity living, float damage, RandomSource random) {
        float damageAfterAbsorb = CombatRules.getDamageAfterAbsorb(damage, living.getArmorValue(), (float)living.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        float finalDamage = damage - damageAfterAbsorb;
        finalDamage = Math.min(finalDamage, (float)Math.sqrt(finalDamage * 10F) / 10F);

        living.hurt(ArmoryUtil.getDamageSource(living.level(), BrassArmoryDamageTypes.IMPALE), finalDamage);

        for (EquipmentSlot slot : new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET }) {
            if (random.nextBoolean()) break;
            ItemStack slotStack = living.getItemBySlot(slot);
            if (!slotStack.isEmpty()) {
                slotStack.hurtAndBreak(random.nextInt(4), living, (livingEntity) -> livingEntity.broadcastBreakEvent(slot));
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
        if (thisLiving.level() instanceof ServerLevel serverLevel && stack.getItem() instanceof KatanaItem katanaItem && katanaItem.canWither() && KatanaItem.getWither(stack) < 100) {
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
            if (BaseFireBlock.canBePlacedAt(level, relativePos, blockHitResult.getDirection()) || level.getBlockState(relativePos).canBeReplaced()) {
                BlockState relativeState = BaseFireBlock.getState(level, relativePos);
                level.setBlock(relativePos, relativeState, 11);
                level.gameEvent(owner, GameEvent.BLOCK_PLACE, pos);
            }
        }
    }

    @Nonnull
    public static List<ItemStack> loadStackList(CompoundTag compoundtag, String name) {
        List<ItemStack> list = Lists.newArrayList();
        if (compoundtag.contains(name, 9)) {
            ListTag listtag = compoundtag.getList(name, 10);
            for(int i = 0; i < listtag.size(); ++i) {
                CompoundTag ammoCompoundTag = listtag.getCompound(i);
                list.add(ItemStack.of(ammoCompoundTag));
            }
        }
        return list;
    }

    @Nonnull
    public static ItemStack loadStack(CompoundTag compoundtag, String name, ItemStack ifEmpty) {
        List<ItemStack> list = loadStackList(compoundtag, name);
        if (list.isEmpty()) return ifEmpty;
        else return list.get(0);
    }

    public static void addStack(CompoundTag compoundTag, ItemStack itemStack, String name) {
        ListTag listtag;
        if (compoundTag.contains(name, 9)) listtag = compoundTag.getList(name, 10);
        else listtag = new ListTag();

        CompoundTag ammoCompoundTag = new CompoundTag();
        itemStack.save(ammoCompoundTag);
        listtag.add(ammoCompoundTag);
        compoundTag.put(name, listtag);
    }

    public static void clearStack(CompoundTag compoundtag, String name) {
        compoundtag.remove(name);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static @NotNull Explosion explode(Level level, Entity entity, float power, boolean burn, Level.ExplosionInteraction interaction) {
        return explode(level, entity, entity.position().add(0.0D, entity.getBbHeight() * 0.5F, 0.0D), power, burn, interaction);
    }

    public static @NotNull Explosion explode(Level level, @Nullable Entity entity, Vec3 position, float power, boolean burn, Level.ExplosionInteraction interaction) {
        return level.explode(entity, position.x, position.y, position.z, power, burn, interaction);
    }

    public static boolean isFuseLighter(ItemStack stack) {
        return stack.is(BrassArmoryTags.Items.FUSE_LIGHTER) || (stack.isEnchanted() && (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack) > 0 || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0));
    }

    public static void addCooldownToList(Player player, List<Item> items, int ticks) {
        if (!player.getAbilities().instabuild) {
            for (Item item : items) player.getCooldowns().addCooldown(item, ticks);
        }
    }

    public static float frameTime(Level level) {
        return !level.isClientSide ? 1.0F : Minecraft.getInstance().getFrameTime();
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type) {
        return getEntityDamageSource(level, type, null);
    }

    public static DamageSource getEntityDamageSource(Level level, ResourceKey<DamageType> type, @org.jetbrains.annotations.Nullable Entity attacker) {
        return getIndirectEntityDamageSource(level, type, attacker, attacker);
    }

    public static DamageSource getIndirectEntityDamageSource(Level level, ResourceKey<DamageType> type, @org.jetbrains.annotations.Nullable Entity attacker, @org.jetbrains.annotations.Nullable Entity indirectAttacker) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), attacker, indirectAttacker);
    }
}
