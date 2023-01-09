package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.init.BrassArmoryAdvancements;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class KatanaItem extends SwordItem {
    private final boolean canWither;

    public KatanaItem(Tier tier, int attackDamage, float attackSpeed, boolean canWither, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
        this.canWither = canWither;
    }

    public boolean canWither() {
        return this.canWither;
    }

    @Override
    public @NotNull AABB  getSweepHitBox(ItemStack stack, Player player, Entity target) {
        return target.getBoundingBox().inflate(3.0D, 0.25D, 3.0D);
    }

    public static void addWither(ItemStack stack, LivingEntity living, ServerLevel serverLevel, int amount) {
        int newWither = getWither(stack);

        boolean ten = false;
        boolean fifty = false;
        for (int i = 0; i < amount; i++) {
            newWither++;
            if (newWither % 10 == 0) ten = true;
            if (newWither % 50 == 0) fifty = true;
        }

        newWither = Math.min(newWither, 100);
        setWither(stack, newWither);

        if (ten) {
            SoundSource source = living instanceof Player ? SoundSource.PLAYERS : living instanceof Enemy ? SoundSource.HOSTILE : SoundSource.NEUTRAL;
            serverLevel.playSound(null, living.getX(), living.getY(), living.getZ(), BrassArmorySounds.KATANA_SMALL_UPGRADE.get(), source, (float)newWither / 100F, 1.2F - (float)newWither / 100F);
            if (fifty) {
                serverLevel.playSound(null, living.getX(), living.getY(), living.getZ(), BrassArmorySounds.KATANA_LARGE_UPGRADE.get(), source, (float)newWither / 100F, 0.7F);
                if (living instanceof ServerPlayer serverPlayer && newWither == 100) {
                    BrassArmoryAdvancements.WITHER_KATANA_TRIGGER.trigger(serverPlayer);
                }
            }
        }
    }

    @ParametersAreNonnullByDefault
    public static void setWither(ItemStack stack, int wither) {
        stack.getOrCreateTag().putInt("BrassArmoryWithering", wither);
    }

    @ParametersAreNonnullByDefault
    public static int getWither(ItemStack stack) {
        return stack.getOrCreateTag().getInt("BrassArmoryWithering");
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        super.fillItemCategory(tab, items);
        if (this.allowedIn(tab)) items.add(Util.make(this.getDefaultInstance(), stack -> setWither(stack, 100)));
    }

    private static final MutableComponent wilted = Component.translatable("item.brass_armory.katana_wilted");
    private static final MutableComponent withered = Component.translatable("item.brass_armory.katana_withered");

    @Override
    public @NotNull Component getName(ItemStack stack) {
        int wither = getWither(stack);
        if (wither < 50) return super.getName(stack);
        else if (wither < 100) return wilted;
        else return withered;
    }
}
