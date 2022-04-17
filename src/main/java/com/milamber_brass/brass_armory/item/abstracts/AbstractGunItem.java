package com.milamber_brass.brass_armory.item.abstracts;

import com.google.common.collect.Lists;
import com.milamber_brass.brass_armory.container.GunContainer;
import com.milamber_brass.brass_armory.item.ammo_behaviour.AbstractAmmoBehaviour;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractGunItem extends ProjectileWeaponItem implements Vanishable {
    protected final boolean oneHanded;
    protected final int maxAmmoItems;
    public final double damageMultiplier;
    protected final float loadSpeed;
    protected final float accuracy;
    protected final double recoil;

    public AbstractGunItem(Properties properties, boolean oneHanded, int maxAmmo, double damageMultiplier, float loadSpeed, float accuracy, double recoil) {
        super(properties);
        this.oneHanded = oneHanded;
        this.maxAmmoItems = maxAmmo;
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
    @ParametersAreNonnullByDefault
    public UseAnim getUseAnimation(ItemStack gunStack) {
        return UseAnim.NONE;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        switch (getLoad(stack)) {
            case 0 -> {
                if (!level.isClientSide) {
                    MenuProvider container = new SimpleMenuProvider(GunContainer.getServerContainer(stack), TextComponent.EMPTY);
                    NetworkHooks.openGui((ServerPlayer) player, container);
                }
            }
            case 1 -> player.startUsingItem(interactionHand);
            default -> {
                Vec3 view = player.getViewVector(level.isClientSide ? Minecraft.getInstance().getFrameTime() : 0F).scale(-this.recoil);
                player.push(view.x, view.y, view.z);
                if (!level.isClientSide) {
                    for (ItemStack ammoStack : loadStack(stack, "BAAmmo")) {
                        AbstractAmmoBehaviour.getStackBehaviour(ammoStack).ifPresent(ammoBehaviour -> {
                            if (ammoBehaviour.onShoot(level, player, stack, ammoStack, this.damageMultiplier, this.accuracy, 5F)) {
                                stack.hurtAndBreak(1, player, (living) -> player.broadcastBreakEvent(player.getUsedItemHand()));
                            }
                        });
                    }

                    level.playSound(null, player, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    setLoad(stack, 0);
                    clearStack(stack, "BAAmmo");
                    clearStack(stack, "BAPowder");
                    setLoadProgress(stack, 0F);
                    player.getCooldowns().addCooldown(stack.getItem(), 10);
                    player.awardStat(Stats.ITEM_USED.get(this));
                } else {
                    float partialTicks = Minecraft.getInstance().getFrameTime();
                    for (double d = 0; d < (this.oneHanded ? 3 : 5); d++) {
                        for (int i = 0; i < (this.oneHanded ? 5 : 8); i++) {
                            for (double o = 0; o < 1; o += 0.1) {
                                Vec3 vec3 = player.getEyePosition(partialTicks);
                                Vec3 vec31 = player.getViewVector(partialTicks);
                                Vec3 vec32 = vec3.add(vec31.x * d + level.random.nextGaussian() * o, vec31.y * d + level.random.nextGaussian() * o, vec31.z * d + level.random.nextGaussian() * o);
                                Vec3 particlePos = level.clip(new ClipContext(vec3, vec32, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getLocation();
                                level.addParticle(new DustParticleOptions(new Vector3f(new Vec3(0.98D, 0.94D, 0.9D)), 0.8F), particlePos.x, particlePos.y, particlePos.z, 0.0D, 0.0D, 0.0D);
                            }
                        }
                    }
                }
            }
        }
        return InteractionResultHolder.consume(player.getItemInHand(interactionHand));
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity entityLiving, int count) {
        setLoadProgress(stack, getLoadProgress(stack) + this.loadSpeed);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            if (getLoadProgress(stack) < 20F) {
                setLoadProgress(stack, 0F);
            } else {
                setLoad(stack, 2);
                player.getCooldowns().addCooldown(stack.getItem(), 5);
                player.level.playSound(player, player, SoundEvents.DISPENSER_DISPENSE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public boolean isOneHanded() {
        return this.oneHanded;
    }

    public int getMaxAmmoItems() {
        return this.maxAmmoItems;
    }


    @Override
    @ParametersAreNonnullByDefault
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.getItem().equals(newStack.getItem()) || slotChanged;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }


    @ParametersAreNonnullByDefault
    public static void setLoadProgress(ItemStack gunStack, float progress) {
        if (gunStack.getTag() != null) {
            CompoundTag compoundtag = gunStack.getTag().copy();
            compoundtag.putFloat("LoadProgress", Math.min(progress, 20F));
            gunStack.setTag(compoundtag);
        }
    }

    @ParametersAreNonnullByDefault
    public static float getLoadProgress(ItemStack gunStack) {
        if (gunStack.hasTag() && gunStack.getTag() != null) {
            return gunStack.getTag().getFloat("LoadProgress");
        } else {
            CompoundTag compoundtag = new CompoundTag();
            compoundtag.putFloat("LoadProgress", 0F);
            gunStack.setTag(new CompoundTag());
            return 0F;
        }
    }

    @ParametersAreNonnullByDefault
    public static void setLoad(ItemStack gunStack, int load) {
        CompoundTag tag = gunStack.getTag() != null ? gunStack.getTag().copy() : new CompoundTag();
        tag.putInt("BAGunLoaded", load);
        gunStack.setTag(tag);
    }

    @ParametersAreNonnullByDefault
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

    @ParametersAreNonnullByDefault
    public static void addStack(ItemStack weaponStack, ItemStack itemStack, String name) {
        CompoundTag weaponCompoundTag = weaponStack.getOrCreateTag();
        ListTag listtag;
        if (weaponCompoundTag.contains(name, 9)) {
            listtag = weaponCompoundTag.getList(name, 10);
        } else listtag = new ListTag();

        CompoundTag ammoCompoundTag = new CompoundTag();
        itemStack.save(ammoCompoundTag);
        listtag.add(ammoCompoundTag);
        weaponCompoundTag.put(name, listtag);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static List<ItemStack> loadStack(ItemStack weaponStack, String name) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundTag compoundtag = weaponStack.getTag();
        if (compoundtag != null && compoundtag.contains(name, 9)) {
            ListTag listtag = compoundtag.getList(name, 10);
            for(int i = 0; i < listtag.size(); ++i) {
                CompoundTag ammoCompoundTag = listtag.getCompound(i);
                list.add(ItemStack.of(ammoCompoundTag));
            }
        }
        return list;
    }

    @ParametersAreNonnullByDefault
    private static void clearStack(ItemStack weaponStack, String name) {
        CompoundTag compoundtag = weaponStack.getTag();
        if (compoundtag != null) {
            ListTag listtag = compoundtag.getList(name, 9);
            listtag.clear();
            compoundtag.put(name, listtag);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
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
    protected abstract TagKey<Item> getAmmoType();

    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }
}
