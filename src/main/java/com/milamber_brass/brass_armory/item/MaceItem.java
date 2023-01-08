package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.capabilities.EffectCapabilityHandler;
import com.milamber_brass.brass_armory.init.BrassArmoryAdvancements;
import com.milamber_brass.brass_armory.init.BrassArmoryPackets;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.abstracts.AbstractTieredWeaponItem;
import com.milamber_brass.brass_armory.item.interfaces.ICustomAnimationItem;
import com.milamber_brass.brass_armory.packets.ParticlePacket;
import com.milamber_brass.brass_armory.util.ArmoryCooldownCache;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class MaceItem extends AbstractTieredWeaponItem implements ICustomAnimationItem {
    public static final DustParticleOptions DUST = new DustParticleOptions(new Vector3f(new Vec3(0.98D,0.94D,0.9D)), 0.8F);
    protected float DAMAGE_MULTIPLIER = 0.12F; //Make this smaller to nerf, make bigger to buff

    public MaceItem(Tier tier, float attackDamage, float attackSpeed, Properties builderIn) {
        super(tier, attackDamage, attackSpeed, builderIn);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack maceStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(maceStack);
    }

    @Override
    public void releaseUsing(ItemStack maceStack, Level level, LivingEntity livingEntity, int useDurationLeft) {
        if (livingEntity instanceof Player player) {
            if (this.getUseDuration(maceStack) - livingEntity.getUseItemRemainingTicks() >= this.getChargeDuration(maceStack)) {
                double pRange = Objects.requireNonNull(player.getAttribute(ForgeMod.REACH_DISTANCE.get())).getValue() * 1.1D;
                Vec3 vec3 = player.getEyePosition(ArmoryUtil.frameTime(level));
                Vec3 vec31 = player.getViewVector(ArmoryUtil.frameTime(level));
                Vec3 vec32 = vec3.add(vec31.x * pRange, vec31.y * pRange, vec31.z * pRange);
                BlockHitResult blockHitResult = level.clip(new ClipContext(vec3, vec32, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

                double trueDistance = blockHitResult.getLocation().distanceTo(player.getEyePosition());
                double power = Mth.clamp((3.5D - trueDistance) * 2.0D, 0.5D, 5.0D);
                if (player.fallDistance > 0.0F && !player.isOnGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger())
                    power *= 1.2D;

                BlockPos blockPos = blockHitResult.getBlockPos();
                Vec3 vec = blockHitResult.getLocation();

                if (blockHitResult.getType() == HitResult.Type.MISS) return;
                boolean mainHand = player.getUsedItemHand() == InteractionHand.MAIN_HAND;
                ItemStack otherItemStack = player.getItemInHand(mainHand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

                if (!level.isClientSide) {
                    if (otherItemStack.getItem() instanceof MaceItem otherMace) {
                        power *= 1.2D;
                        otherMace.smash((ServerPlayer) player, level, vec, maceStack, otherItemStack, power);
                        maceStack.hurtAndBreak(10, player, (player1) -> player1.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                        otherItemStack.hurtAndBreak(10, player, (player1) -> player1.broadcastBreakEvent(InteractionHand.OFF_HAND));
                    } else {
                        smash((ServerPlayer) player, level, vec, maceStack, null, power);
                        maceStack.hurtAndBreak(10, player, (player1) -> player1.broadcastBreakEvent(player.getUsedItemHand()));
                    }
                    smashParticles(livingEntity, vec, level, blockPos, power, blockHitResult.getDirection());
                    level.playSound(null, vec.x, vec.y, vec.z, BrassArmorySounds.MACE_SMASH.get(), SoundSource.PLAYERS, (float) power * 0.2F, 0.5F - (float) power * 0.1F);
                    EffectCapabilityHandler.setShakePower(player, power * 0.35D);
                }
            }
        }
    }

    private void smash(ServerPlayer player, Level level, Vec3 vec, ItemStack maceStack, @Nullable ItemStack otherMaceStack, double power) {
        double x = vec.x; double y = vec.y; double z = vec.z;
        AABB aabb = (new AABB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D)).inflate(8.0D + power);
        List<Entity> entitiesInRange = level.getEntitiesOfClass(Entity.class, aabb, Entity::isAlive);

        boolean otherMaceFlag = otherMaceStack != null;

        float maceDamage = ((MaceItem)maceStack.getItem()).getAttackDamage() * 1.5F;
        float otherMaceDamage = otherMaceFlag ? ((MaceItem)otherMaceStack.getItem()).getAttackDamage() * 1.5F : 0F;

        boolean hitAtLeastOne = false;

        for (Entity entity : entitiesInRange) {
            if (entity != player && !(entity instanceof ItemEntity) && !(entity instanceof TamableAnimal pet && pet.getOwner() == player)) {
                double entityDistance = entity.position().distanceTo(vec) * 0.8D;
                if (entityDistance <= power) {
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
                    float bonusDMG = Math.min(maceDamage + maceEnchantment, otherMaceDamage + otherMaceEnchantment) * 0.5F;
                    float finalDMG = (totalDMG + bonusDMG) * seenPercent * ((float)power * this.DAMAGE_MULTIPLIER) * 3.0F;

                    if (finalDMG > 0) {
                        if (entity instanceof Player p) EffectCapabilityHandler.setShakePower(p, power);
                        if (entity.hurt(new EntityDamageSource("brass_armory.mace_smash", player), entity instanceof LivingEntity ? finalDMG : finalDMG * 0.25F)) {
                            if (entity instanceof LivingEntity) player.crit(entity);
                            hitAtLeastOne = true;
                        }
                    }
                }
                if (entityDistance <= power * 1.2D && entity instanceof LivingEntity living) {
                    living.knockback(0.1F * Math.abs(entityDistance - 5) + entityDistance > 4 ? 0.4F : 0, vec.x - entity.getX(), vec.z - entity.getZ());
                }
            }
        }

        if (otherMaceFlag && hitAtLeastOne) BrassArmoryAdvancements.MACE_SMASH_TRIGGER.trigger(player);

        ArmoryUtil.addCooldownToList(player, ArmoryCooldownCache.BrassArmoryMaces, 600);
    }

    private void smashParticles(LivingEntity player, Vec3 vec, Level level, BlockPos blockPos, double power, Direction direction) {
        Vec3i dirNormal = direction.getNormal();

        for (BlockPos pos : BlockPos.betweenClosed(blockPos.offset(-6, -6, -6), blockPos.offset(6, 6, 6))) {
            BlockState state = level.getBlockState(pos);
            if (state.isFaceSturdy(level, pos, direction)) {
                BlockPos relativePos = pos.relative(direction);
                BlockState relativeState = level.getBlockState(relativePos);
                if (relativeState.getMaterial().isReplaceable() || !relativeState.isFaceSturdy(level, relativePos, direction.getOpposite())) {
                    ParticlePacket particlePacket = new ParticlePacket();
                    for (int i = 0; i < Math.max((int)power, 1); i++) {
                        Vec3 offset = new Vec3(dirNormal.getX() * i, dirNormal.getY() * i, dirNormal.getZ() * i);
                        Vec3 particlePos = Vec3.atLowerCornerOf(relativePos).add(offset);
                        for (int j = 0; j < level.random.nextInt((int)Math.max(power - Math.sqrt(pos.distSqr(blockPos)), 8.0D)) * 2; j++) {
                            Vec3 finalPos = particlePos.add(level.random.nextGaussian(), level.random.nextGaussian(), level.random.nextGaussian());
                            if (finalPos.distanceTo(vec) < power * 1.5D) particlePacket.queueParticle(DUST, false, finalPos.x, finalPos.y, finalPos.z, 0.0D, 0.0D, 0.0D);
                        }
                    }
                    Vec3 off = new Vec3(dirNormal.getX() == 0 ? 1 : 0, dirNormal.getY() == 0 ? 1 : 0, dirNormal.getZ() == 0 ? 1 : 0);

                    for (int i = 0; i < Math.max((int)power, 1) * 5; i++) {
                        Vec3 finalPos = Vec3.atCenterOf(pos).add((double)dirNormal.getX() * 0.5D, (double)dirNormal.getY() * 0.5D, (double)dirNormal.getZ() * 0.5D).add(level.random.nextGaussian() * off.x, level.random.nextGaussian() * off.y, level.random.nextGaussian() * off.z);
                        if (finalPos.distanceTo(vec) < power * 1.5D) particlePacket.queueParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), false, finalPos.x, finalPos.y, finalPos.z, 0.0D, 0.0D, 0.0D);
                    }
                    BrassArmoryPackets.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), particlePacket);
                }
            }
        }
    }

    @Nonnull
    @Override
    public UseAnim getUseAnimation(ItemStack maceStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return false;
    }

    @Override
    public int getUseDuration(ItemStack maceStack) {
        return 72000;
    }

    @Override
    public float getChargeDuration(ItemStack itemStack) {
        return 20;
    }
}