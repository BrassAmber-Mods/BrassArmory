package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.entity.projectile.BombEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public class BombItem extends Item {
    private final int bombType;

    public BombItem(Properties properties, int bombType) {
        super(properties);
        this.bombType = bombType;
    }

    @Override
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
            if (!level.isClientSide) {
                boolean mainHandFlag = interactionHand == InteractionHand.MAIN_HAND;
                HumanoidArm humanoidarm = mainHandFlag ? player.getMainArm() : player.getMainArm().getOpposite();
                BombEntity bomb = new BombEntity(level, player, humanoidarm);
                bomb.setItem(bombStack);
                bomb.setFuse(getFuseLength(bombStack));
                bomb.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.5F, 0.25F);
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
        return InteractionResultHolder.sidedSuccess(bombStack, level.isClientSide());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack bombStack, Level level, Entity entity, int i, boolean b) {
        if (getFuseLit(bombStack)) {
            int fuse = getFuseLength(bombStack) - 1;
            setFuseLength(bombStack, fuse);
            if (!level.isClientSide) {
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

    @Override
    public boolean hasCustomEntity(ItemStack bombStack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(Level level, Entity itemEntity, ItemStack bombStack) {
        if (!getFuseLit(bombStack)) return null;
        Vec3 itemVec = itemEntity.position();
        BombEntity bomb = new BombEntity(level, itemVec.x, itemVec.y, itemVec.z);
        bomb.setDeltaMovement(itemEntity.getDeltaMovement());
        bomb.setItem(bombStack);
        bomb.setFuse(getFuseLength(bombStack));
        UUID playerID = ((ItemEntity)itemEntity).getOwner();
        if (playerID != null) bomb.setOwner(level.getPlayerByUUID(playerID));
        if (bombStack.getCount() > 1) {
            bombStack.shrink(1);
            BombItem.setFuseLit(bombStack, false);
            BombItem.setFuseLength(bombStack, 60);
            ItemEntity newItemEntity = new ItemEntity(level, itemVec.x, itemVec.y, itemVec.z, bombStack);
            if (playerID != null) newItemEntity.setThrower(playerID);
            newItemEntity.setPickUpDelay(40);
            newItemEntity.setDeltaMovement(itemEntity.getDeltaMovement());
            var executor = LogicalSidedProvider.WORKQUEUE.get(level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
            executor.tell(new TickTask(0, () -> level.addFreshEntity(newItemEntity)));
        }
        return bomb;
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        MinecraftServer server = player.getLevel().getServer();
        if (server != null && BombItem.getFuseLength(item) > 0) {
            server.tell(new TickTask(server.getTickCount(), () -> {
                BombItem.setFuseLength(item, 60);
                BombItem.setFuseLit(item, false);
            }));
        } else return false;
        return true;
    }



    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.getItem().equals(newStack.getItem());
    }

    public int getBombType() {
        return this.bombType;
    }

    public static void setFuseLength(ItemStack bombStack, int fuse) {
        bombStack.getOrCreateTag().putInt("BrassArmoryFuseLength", Math.min(60, fuse));
    }

    public static int getFuseLength(ItemStack bombStack) {
        return bombStack.getOrCreateTag().getInt("BrassArmoryFuseLength");
    }

    public static void setFuseLit(ItemStack bombStack, boolean fuseLit) {
        bombStack.getOrCreateTag().putBoolean("BrassArmoryFuseLit", fuseLit);
    }

    public static boolean getFuseLit(ItemStack bombStack) {
        return bombStack.getOrCreateTag().getBoolean("BrassArmoryFuseLit");
    }

    public static Item getBomb(int bombID) {
        return switch (bombID) {
            case 0 -> BrassArmoryItems.BOMB.get();
            case 1 -> BrassArmoryItems.BOUNCY_BOMB.get();
            default -> BrassArmoryItems.STICKY_BOMB.get();
        };
    }
}
