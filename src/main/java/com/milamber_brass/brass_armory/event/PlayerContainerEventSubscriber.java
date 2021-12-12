package com.milamber_brass.brass_armory.event;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.bomb.BombType;
import com.milamber_brass.brass_armory.item.BombItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID)
public class PlayerContainerEventSubscriber {
    @SubscribeEvent //Drop any lit bombs if opening a container, to prevent storing lit bombs
    public static void PlayerContainerEvent(PlayerContainerEvent.Open event) {
        Player player = event.getPlayer();
        if (player != null) {
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
    }
}
