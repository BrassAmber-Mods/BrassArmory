package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.item.abstracts.AbstractTieredWeaponItem;
import com.milamber_brass.brass_armory.item.interfaces.ICustomAnimationItem;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MaceItem extends AbstractTieredWeaponItem implements ICustomAnimationItem {
    public MaceItem(Tiers tier, int attackDamageIn, Properties builderIn) {
        super(tier, attackDamageIn, -3.2F, builderIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack maceStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(maceStack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void releaseUsing(ItemStack maceStack, Level level, LivingEntity livingEntity, int useDurationLeft) {
        if (livingEntity instanceof Player player) {
            if (this.getUseDuration(maceStack) - livingEntity.getUseItemRemainingTicks() >= this.getChargeDuration(maceStack)) {
                double pRange = Objects.requireNonNull(player.getAttribute(ForgeMod.REACH_DISTANCE.get())).getValue();
                float partialTicks = Minecraft.getInstance().getFrameTime();
                Vec3 vec3 = player.getEyePosition(partialTicks);
                Vec3 vec31 = player.getViewVector(partialTicks);
                Vec3 vec32 = vec3.add(vec31.x * pRange, vec31.y * pRange, vec31.z * pRange);
                BlockHitResult blockHitResult = level.clip(new ClipContext(vec3, vec32, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
                double distance = blockHitResult.distanceTo(player);
                BlockPos blockPos = blockHitResult.getBlockPos();
                Vec3 vec = blockHitResult.getLocation();

                if (blockHitResult.getType() == HitResult.Type.MISS || distance > 6.904D || blockHitResult.getDirection() != Direction.UP) return;
                if (!level.isClientSide) {
                    boolean mainHand = player.getUsedItemHand() == InteractionHand.MAIN_HAND;
                    ItemStack otherItemStack = player.getItemInHand(mainHand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
                    if (otherItemStack.getItem() instanceof MaceItem otherMace) {
                        otherMace.smash(player, level, vec, maceStack, otherItemStack);
                        maceStack.hurtAndBreak(10, player, (player1) -> player1.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                        otherItemStack.hurtAndBreak(10, player, (player1) -> player1.broadcastBreakEvent(InteractionHand.OFF_HAND));
                    } else {
                        smash(player, level, vec, maceStack, null);
                        maceStack.hurtAndBreak(10, player, (player1) -> player1.broadcastBreakEvent(player.getUsedItemHand()));
                    }
                } else {
                    smashParticles(vec, level, blockPos);
                    level.playSound(player, vec.x, vec.y, vec.z, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void smash(Player player, Level level, Vec3 vec, ItemStack maceStack, @Nullable ItemStack otherMaceStack) {
        double x = vec.x; double y = vec.y; double z = vec.z;
        AABB aabb = (new AABB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D)).inflate(8.0D);
        List<Entity> entitiesInRange = level.getEntitiesOfClass(Entity.class, aabb, Entity::isAlive);

        boolean otherMaceFlag = otherMaceStack != null;

        float maceDamage = ((MaceItem)maceStack.getItem()).getAttackDamage() * 1.5F;
        float otherMaceDamage = otherMaceFlag ? ((MaceItem)otherMaceStack.getItem()).getAttackDamage() * 1.5F : 0F;
        for (Entity entity : entitiesInRange) {
            if (entity != player && !(entity instanceof ItemEntity) && !(entity instanceof TamableAnimal pet && pet.getOwner() == player)) {
                double entityDistance = Math.sqrt(entity.distanceToSqr(vec));
                if (entityDistance <= 4.0D) {
                    float seenPercent = Explosion.getSeenPercent(vec, entity);

                    float maceEnchantment;
                    float otherMaceEnchantment;

                    if (entity instanceof LivingEntity living) {
                        maceEnchantment = EnchantmentHelper.getDamageBonus(maceStack, living.getMobType());
                        otherMaceEnchantment = otherMaceFlag ? EnchantmentHelper.getDamageBonus(otherMaceStack, living.getMobType()) : 0F;
                    } else {
                        maceEnchantment = EnchantmentHelper.getDamageBonus(maceStack, MobType.UNDEFINED);
                        otherMaceEnchantment = otherMaceFlag ? EnchantmentHelper.getDamageBonus(otherMaceStack, MobType.UNDEFINED) : 0F;
                    }

                    float totalDMG = Math.max(maceDamage + maceEnchantment, otherMaceDamage + otherMaceEnchantment);
                    float bonusDMG = Math.min(maceDamage + maceEnchantment, otherMaceDamage + otherMaceEnchantment) / 2F;
                    float finalDMG = (totalDMG + bonusDMG) * seenPercent;

                    if (finalDMG > 0) {
                        entity.hurt(DamageSource.playerAttack(player), (totalDMG + bonusDMG) * seenPercent);
                        if (entity instanceof LivingEntity) player.crit(entity);
                    }
                }
                if (entityDistance <= 5.0D && entity instanceof LivingEntity living) {
                    living.knockback(0.1F * Math.abs(entityDistance - 5) + entityDistance > 4 ? 0.4F : 0, vec.x - entity.getX(), vec.z - entity.getZ());
                }
            }
        }
        if (!player.getAbilities().instabuild) {
            player.getCooldowns().addCooldown(maceStack.getItem(), 600);
            if (otherMaceFlag) player.getCooldowns().addCooldown(otherMaceStack.getItem(), 600);
        }
    }

    private void smashParticles(Vec3 vec, Level level, BlockPos blockPos) {
        for (int x = -6; x <= 6; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -6; z <= 6; z++) {
                    BlockPos newPos = new BlockPos(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);
                    Random r = level.getRandom();
                    Vec3 newVec = new Vec3(newPos.getX() + r.nextDouble(), newPos.getY() + 1D + r.nextDouble() * 2D, newPos.getZ() + r.nextDouble());
                    double newVecToVec = newVec.distanceTo(vec);
                    if (!level.getBlockState(newPos).isAir() && newVecToVec <= 5.0D) {
                        for (int i = 0; i < r.nextInt(Math.max((int)Math.abs(newVecToVec - 5.0D), 1) * 32); i++) {
                            level.addParticle(new DustParticleOptions(new Vector3f(new Vec3(0.98D,0.94D,0.9D)), 0.8F), newVec.x, newVec.y, newVec.z, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public UseAnim getUseAnimation(ItemStack maceStack) {
        return UseAnim.SPEAR;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getUseDuration(ItemStack maceStack) {
        return 72000;
    }

    @Override
    public float getChargeDuration(ItemStack itemStack) {
        return 20;
    }
}