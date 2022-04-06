package com.milamber_brass.brass_armory.event;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombType;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.BombItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID)
public class ServerEventSubscriber {
    @SubscribeEvent //Drop any lit bombs if opening a container, to prevent storing lit bombs
    @ParametersAreNonnullByDefault
    public static void PlayerContainerEvent(PlayerContainerEvent.Open event) {
        Player player = event.getPlayer();
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof BombItem bombItem && BombItem.getFuseLit(stack)) {
                BombEntity bomb = BombType.playerBombEntityFromType(bombItem.getBombType(), player.level, player, null);
                bomb.setFuse(BombItem.getFuseLength(stack));
                bomb.setItem(stack);
                BombItem.setFuseLength(stack, 60);
                BombItem.setFuseLit(stack, false);
                if (!(player.getAbilities().instabuild)) stack.shrink(1);
                player.level.addFreshEntity(bomb);
            }
        }
    }

    @SubscribeEvent
    @ParametersAreNonnullByDefault
    public static void WandererTradesEvent(WandererTradesEvent event) {
        event.getRareTrades().add(new BasicItemListing(10, BrassArmoryItems.BOMB.get().getDefaultInstance(), 8, 4, 1));
        event.getRareTrades().add(new BasicItemListing(20, BrassArmoryItems.LONGBOW.get().getDefaultInstance(), 3, 6, 1));
    }
}
