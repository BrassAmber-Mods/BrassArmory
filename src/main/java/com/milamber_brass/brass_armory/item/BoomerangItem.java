package com.milamber_brass.brass_armory.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.milamber_brass.brass_armory.entity.projectile.BoomerangEntity;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class BoomerangItem extends TieredItem implements Vanishable, ICustomAnimationItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    private final ImmutableSet<Enchantment> properEnchantments = ImmutableSet.of(Enchantments.LOYALTY, Enchantments.FLAMING_ARROWS, Enchantments.QUICK_CHARGE, Enchantments.RIPTIDE);
    private final float attackDamage;
    public final static String targetID = "BABoomerangTargetID";
    public final static String targetUUID = "BABoomerangTargetUUID";
    public final static String critTag = "BABoomerangCrit";

    public BoomerangItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, properties);
        this.attackDamage = attackDamage + tier.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack boomerangStack = player.getItemInHand(interactionHand);
        player.startUsingItem(interactionHand);
        setTarget(boomerangStack, null);
        boomerangStack.removeTagKey(targetUUID);
        setCrit(boomerangStack, 0);
        return InteractionResultHolder.consume(boomerangStack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void releaseUsing(ItemStack boomerangStack, Level level, LivingEntity livingEntity, int useDurationLeft) {
        if (livingEntity instanceof Player player) {
            float time = this.getUseDuration(boomerangStack) - useDurationLeft;
            float chargeTime = this.getChargeDuration(boomerangStack);
            float power = Math.min(time / chargeTime, 1F);

            CompoundTag itemTag = boomerangStack.getTag();
            if ((double)power >= 0.1D && level instanceof ServerLevel serverLevel && itemTag != null) {
                boomerangStack.hurtAndBreak(1, player, (player1) -> player1.broadcastBreakEvent(livingEntity.getUsedItemHand()));
                Entity target = itemTag.hasUUID(targetUUID) ? serverLevel.getEntity(itemTag.getUUID(targetUUID)) : null;
                BoomerangEntity boomerangEntity = new BoomerangEntity(level, player, boomerangStack, power * 0.5F, target);

                float critChance = getCrit(boomerangStack);
                boolean crit = level.random.nextFloat(100F) <= critChance;
                if (power == 1.0F && critChance > 0F) boomerangEntity.setCritArrow(crit);

                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, boomerangStack) > 0) boomerangEntity.setSecondsOnFire(100);

                level.addFreshEntity(boomerangEntity);
                level.playSound(null, boomerangEntity, BrassArmorySounds.BOMB_THROW.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                player.getInventory().removeItem(boomerangStack);
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onUsingTick(ItemStack boomerangStack, LivingEntity player, int useDurationLeft) {
        float time = this.getUseDuration(boomerangStack) - useDurationLeft;
        float chargeTime = this.getChargeDuration(boomerangStack);
        float power = Math.min(time / chargeTime, 1F);

        Vec3 vec3 = player.getEyePosition(Minecraft.getInstance().getFrameTime());
        Vec3 vec31 = player.getViewVector(1.0F);
        double d0 = Objects.requireNonNull(player.getAttribute(ForgeMod.REACH_DISTANCE.get())).getValue() * power * 5D;
        Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
        AABB aabb = player.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
        double d1 = d0 * d0;
        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, vec3, vec32, aabb, (entity) -> !entity.isSpectator() && entity.isPickable(), d1);

        if (!player.level.isClientSide && entityhitresult != null && entityhitresult.getEntity() instanceof LivingEntity living) {
            /*living.setCustomName(boomerangStack.getHoverName());
            living.setCustomNameVisible(true);*/
            boomerangStack.getOrCreateTag().putUUID(BoomerangItem.targetUUID, living.getUUID());
            setTarget(boomerangStack, living);
        }
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
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || properEnchantments.contains(enchantment);
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public UseAnim getUseAnimation(ItemStack boomerangStack) {
        return UseAnim.SPEAR;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getUseDuration(ItemStack maceStack) {
        return 72000;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getCustomUseDuration(ItemStack maceStack, LocalPlayer localPlayer) {
        return this.getUseDuration(maceStack) - localPlayer.getUseItemRemainingTicks();
    }

    @Override
    public int getChargeDuration(ItemStack itemStack) {
        int quickCharge = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, itemStack);
        if (quickCharge > 3) quickCharge = 3;
        return quickCharge == 0 ? 40 : 40 - 10 * quickCharge;
    }

    public static void setTarget(ItemStack boomerangStack, Entity entity) {
        if (entity == null) boomerangStack.removeTagKey(BoomerangItem.targetID);
        else boomerangStack.getOrCreateTag().putInt(BoomerangItem.targetID, entity.getId());
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public static Entity getTargetEntity(ItemStack boomerangStack, Level level) {
        if (boomerangStack.getTag() != null && boomerangStack.getTag().contains(BoomerangItem.targetID)) {
            return level.getEntity(boomerangStack.getOrCreateTag().getInt(BoomerangItem.targetID));
        }
        return null;
    }

    @ParametersAreNonnullByDefault
    public static void setCrit(ItemStack boomerangStack, float crit) {
        boomerangStack.getOrCreateTag().putFloat(BoomerangItem.critTag, crit);
    }

    @ParametersAreNonnullByDefault
    public static float getCrit(ItemStack boomerangStack) {
        if (boomerangStack.getTag() != null && boomerangStack.getTag().contains(BoomerangItem.critTag)) {
            return boomerangStack.getOrCreateTag().getFloat(BoomerangItem.critTag);
        }
        return 0F;
    }

    @Override //Makes the bomb item not twitch in a player's hand while the fuse is burning
    @ParametersAreNonnullByDefault
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.getItem().equals(newStack.getItem()) || slotChanged;
    }
}
