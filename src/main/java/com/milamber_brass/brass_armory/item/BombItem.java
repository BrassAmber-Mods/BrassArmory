package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombType;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class BombItem extends Item {
    private final BombType bombType;

    public BombItem(Properties properties, BombType bombType) {
        super(properties);
        this.bombType = bombType;
    }

    @Override //Light bomb if it's not lit, if it is, throw it
    @ParametersAreNonnullByDefault
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack bombStack = player.getItemInHand(interactionHand);
        if (!getFuseLit(bombStack)) {
            if (!player.isUnderWater()) {
                setFuseLit(bombStack, true);
                setFuseLength(bombStack, 60);
                player.getCooldowns().addCooldown(bombStack.getItem(), 5);
                player.playSound(BrassArmorySounds.BOMB_LIT.get(), 0.9F, level.getRandom().nextFloat() * 0.4F + 0.7F);
                player.gameEvent(GameEvent.PRIME_FUSE);
            }
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), BrassArmorySounds.BOMB_THROW.get(), SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            this.throwBomb(level, player, interactionHand, bombStack, 0.75F);
        }
        return InteractionResultHolder.sidedSuccess(bombStack, level.isClientSide());
    }

    @ParametersAreNonnullByDefault
    private void throwBomb(Level level, Player player, InteractionHand interactionHand, ItemStack bombStack, float power) {
        if (!level.isClientSide) {
            boolean mainHandFlag = interactionHand == InteractionHand.MAIN_HAND;
            HumanoidArm humanoidarm = mainHandFlag ? player.getMainArm() : player.getMainArm().getOpposite();
            BombEntity bomb = BombType.playerBombEntityFromType(this.bombType, level, player, humanoidarm);
            bomb.setItem(bombStack);
            bomb.setFuse(getFuseLength(bombStack));
            bomb.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, power, 0.25F);
            level.addFreshEntity(bomb);
            if (bombStack.getCount() > 1 || player.getAbilities().instabuild) {
                setFuseLit(bombStack, false);
                setFuseLength(bombStack, 60);
            }
        }
        if (!player.getAbilities().instabuild) {
            bombStack.shrink(1);
            player.getCooldowns().addCooldown(bombStack.getItem(), 20);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack bombStack, Level level, Entity entity, int i, boolean b) {
        if (getFuseLit(bombStack)) {
            if (!level.isClientSide) {
                int fuse = getFuseLength(bombStack) - 1;
                setFuseLength(bombStack, fuse);
                if (fuse <= 0) {
                    BombEntity.explode(level, entity);
                    setFuseLit(bombStack, false);
                    setFuseLength(bombStack, 60);
                    if (!(entity instanceof Player player && player.getAbilities().instabuild)) bombStack.shrink(1);
                }
            }
            entity.playSound(BrassArmorySounds.BOMB_FUSE.get(), 0.03F, level.getRandom().nextFloat() * 0.6F + 1F);
        } else setFuseLength(bombStack, 60);
    }

    @Override //Summons a lit bomb instead of an ItemEntity if a player tries to drop a lit bomb
    public boolean onDroppedByPlayer(ItemStack bombStack, Player player) {
        if (BombItem.getFuseLit(bombStack)) {
            this.throwBomb(player.getLevel(), player, InteractionHand.MAIN_HAND, bombStack, 0.24F);
            return false;
        }
        return true;
    }

    @Override //Makes the bomb item not twitch in a player's hand while the fuse is burning
    @ParametersAreNonnullByDefault
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.getItem().equals(newStack.getItem()) || slotChanged;
    }

    public BombType getBombType() {
        return this.bombType;
    }

    @ParametersAreNonnullByDefault
    public static void setFuseLength(ItemStack bombStack, int fuse) {
        bombStack.getOrCreateTag().putInt("BrassArmoryFuseLength", Math.min(60, fuse));
    }

    @ParametersAreNonnullByDefault
    public static int getFuseLength(ItemStack bombStack) {
        return bombStack.getOrCreateTag().getInt("BrassArmoryFuseLength");
    }

    @ParametersAreNonnullByDefault
    public static void setFuseLit(ItemStack bombStack, boolean fuseLit) {
        bombStack.getOrCreateTag().putBoolean("BrassArmoryFuseLit", fuseLit);
    }

    @ParametersAreNonnullByDefault
    public static boolean getFuseLit(ItemStack bombStack) {
        return bombStack.getOrCreateTag().getBoolean("BrassArmoryFuseLit");
    }
}
