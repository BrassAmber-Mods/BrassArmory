package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.behaviour.GunBehaviours;
import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.init.BrassArmoryAdvancements;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.inventory.GunContainer;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class FlintlockItem extends ProjectileWeaponItem implements Vanishable, iGun {
    protected final boolean oneHanded;
    public final double damageMultiplier;
    protected final float loadSpeed;
    protected final float accuracy;
    protected final double recoil;

    public FlintlockItem(Properties properties, boolean oneHanded, double damageMultiplier, float loadSpeed, float accuracy, double recoil) {
        super(properties);
        this.oneHanded = oneHanded;
        this.damageMultiplier = damageMultiplier;
        this.loadSpeed = loadSpeed;
        this.accuracy = accuracy;
        this.recoil = recoil;
    }

    public double getDamageMultiplier() {
        return this.damageMultiplier;
    }

    @Nonnull
    @Override
    public UseAnim getUseAnimation(ItemStack gunStack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);

        int load = getLoad(stack);
        if (load == 0 && !ArmoryUtil.loadStack(stack.getOrCreateTag(), GunContainer.gunAmmo, ItemStack.EMPTY).isEmpty() && !ArmoryUtil.loadStack(stack.getOrCreateTag(), GunContainer.gunPowder, ItemStack.EMPTY).isEmpty()) {
            load = 1;
            setLoad(stack, 1);
        }

        switch (load) {
            case 0 -> {
                if (!level.isClientSide) {
                    ArmoryUtil.addStack(stack.getOrCreateTag(), this.getDefaultInstance(), GunContainer.gunIcon);
                    MenuProvider container = new SimpleMenuProvider(GunContainer.getServerContainer(this, stack.getOrCreateTag()), Component.empty());
                    player.openMenu(container);
                }
            }
            case 1 -> {
                ArmoryUtil.clearStack(stack.getOrCreateTag(), GunContainer.gunIcon);
                player.startUsingItem(interactionHand);
            }
            default -> GunBehaviours.getPowderBehaviour(ArmoryUtil.loadStack(stack.getOrCreateTag(), GunContainer.gunPowder, ItemStack.EMPTY)).ifPresent(powderBehaviour -> {
                Vec3 view = player.getViewVector(ArmoryUtil.frameTime(level)).scale(-this.recoil);
                player.push(view.x, view.y, view.z);
                ItemStack ammo = ArmoryUtil.loadStack(stack.getOrCreateTag(), GunContainer.gunAmmo, ItemStack.EMPTY);
                GunBehaviours.getAmmoBehavior(ammo).ifPresent(ammoBehaviour -> {
                    if (ammoBehaviour.onShoot(level, player, player, ammo, powderBehaviour, this)) {
                        stack.hurtAndBreak(1, player, (living) -> player.broadcastBreakEvent(player.getUsedItemHand()));
                    }
                });

                if (!level.isClientSide) {
                    level.playSound(null, player, this.getGunShootSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    setLoad(stack, 0);
                    ArmoryUtil.clearStack(stack.getOrCreateTag(), GunContainer.gunAmmo);
                    ArmoryUtil.clearStack(stack.getOrCreateTag(), GunContainer.gunPowder);
                    setLoadProgress(stack, 0F);
                    player.getCooldowns().addCooldown(stack.getItem(), 10);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            });
        }
        return InteractionResultHolder.consume(player.getItemInHand(interactionHand));
    }

    protected SoundEvent getGunShootSound() {
        return BrassArmorySounds.GUN_SHOOT.get();
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
        setLoadProgress(stack, getLoadProgress(stack) + this.loadSpeed);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            if (getLoadProgress(stack) < 20F) {
                setLoadProgress(stack, 0F);
            } else {
                setLoad(stack, 2);
                player.getCooldowns().addCooldown(stack.getItem(), 5);
                player.level().playSound(player, player, this.getGunLoadSound(), SoundSource.PLAYERS, 1.0F, level.getRandom().nextFloat() * 0.25F + 0.75F);
                if (player instanceof ServerPlayer serverPlayer) BrassArmoryAdvancements.LOAD_GUN.trigger(serverPlayer);
            }
        }
    }

    public SoundEvent getGunLoadSound() {
        return BrassArmorySounds.GUN_LOAD.get();
    }

    public boolean isOneHanded() {
        return this.oneHanded;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.getItem().equals(newStack.getItem()) || slotChanged;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }


    public static void setLoadProgress(ItemStack gunStack, float progress) {
        if (gunStack.getTag() != null) gunStack.setTag(Util.make(gunStack.getOrCreateTag().copy(), tag -> tag.putFloat("LoadProgress", Math.min(progress, 20F))));
    }

    public static float getLoadProgress(ItemStack gunStack) {
        if (gunStack.hasTag() && gunStack.getTag() != null) return gunStack.getTag().getFloat("LoadProgress");
        else {
            gunStack.setTag(Util.make(new CompoundTag(), tag -> tag.putFloat("LoadProgress", 0F)));
            return 0F;
        }
    }

    public static void setLoad(ItemStack gunStack, int load) {
        CompoundTag tag = gunStack.getTag() != null ? gunStack.getTag().copy() : new CompoundTag();
        tag.putInt("BAGunLoaded", load);
        gunStack.setTag(tag);
    }

    public static int getLoad(ItemStack gunStack) {
        if (gunStack.hasTag() && gunStack.getTag() != null) {
            return gunStack.getTag().getInt("BAGunLoaded");
        } else {
            CompoundTag compoundtag = new CompoundTag();
            compoundtag.putInt("BAGunLoaded", 0);
            gunStack.setTag(new CompoundTag());
            return 0;
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double) state.getDestroySpeed(worldIn, pos) != 0.0D) {
            stack.hurtAndBreak(2, entityLiving, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Nonnull
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return Optional.ofNullable(this.getAmmoType()).<Predicate<ItemStack>>map(itemTag -> (stack -> stack.is(itemTag))).orElseGet(() -> stack -> false);
    }

    @Nullable
    protected TagKey<Item> getAmmoType() {
        return BrassArmoryTags.Items.FLINTLOCK_AMMO;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if (getLoad(stack) == 2) {
            for (ItemStack ammoStack : ArmoryUtil.loadStackList(stack.getOrCreateTag(), GunContainer.gunAmmo)) {
                components.add((Component.translatable("item.minecraft.crossbow.projectile")).append(" ").append(ammoStack.getDisplayName()));
            }
        }
    }

    @Override
    public void onOpen(Player owner, CompoundTag tag) {
        owner.level().playSound(null, owner, BrassArmorySounds.GUN_OPEN.get(), SoundSource.PLAYERS, 0.3F, owner.level().getRandom().nextFloat() * 0.05F + 0.6F);
    }

    @Override
    public void onLoad(Player owner, CompoundTag tag) {
        owner.level().playSound(null, owner, BrassArmorySounds.GUN_CLOSE.get(), SoundSource.PLAYERS, 0.5F, owner.level().getRandom().nextFloat() * 0.06F + 0.2F);
    }

    @Override
    public Predicate<ItemStack> ammoPredicate() {
        return this.getAllSupportedProjectiles();
    }

    @Override
    public Predicate<ItemStack> powderPredicate() {
        return itemStack -> itemStack.is(BrassArmoryTags.Items.FLINTLOCK_POWDER);
    }

    @Override
    public double damage() {
        return this.damageMultiplier;
    }

    @Override
    public float accuracy() {
        return this.accuracy;
    }

    @Override
    public float speed() {
        return 5F;
    }

    @Override
    public float particleMultiplier() {
        return this.isOneHanded() ? 0.5F : 1.0F;
    }

    @Override
    public double particleOffset() {
        return 0.05D;
    }
}
