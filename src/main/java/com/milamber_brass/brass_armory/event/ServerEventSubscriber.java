package com.milamber_brass.brass_armory.event;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.effect.BleedEffect;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombType;
import com.milamber_brass.brass_armory.init.BrassArmoryEffects;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.BombItem;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID)
public class ServerEventSubscriber {
    @SubscribeEvent //Drop any lit bombs if opening a container, to prevent storing lit bombs
    public static void PlayerContainerEventOpen(PlayerContainerEvent.Open event) {
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
    public static void WandererTradesEvent(WandererTradesEvent event) {
        event.getRareTrades().add(new BasicItemListing(8, BrassArmoryItems.BOMB.get().getDefaultInstance(), 8, 4, 4));
        event.getRareTrades().add(new BasicItemListing(10, BrassArmoryItems.LONGBOW.get().getDefaultInstance(), 3, 6, 1));
        event.getRareTrades().add(new BasicItemListing(Items.WITHER_SKELETON_SKULL.getDefaultInstance(), BrassArmoryItems.KATANA.get().getDefaultInstance(), 1, 32, 1));
    }

    @SubscribeEvent
    public static void LivingHurtEvent(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof ItemSupplier itemSupplier && itemSupplier.getItem().is(BrassArmoryTags.Items.BLEEDING_EDGE)) {
            BleedEffect.bleedHarder(event.getEntityLiving(), 50, 0);
        }
    }

    @SubscribeEvent
    public static void PotionApplicableEvent(PotionEvent.PotionApplicableEvent event) {
        if (event.getEntity() instanceof LivingEntity living && (living.getMobType() == MobType.UNDEAD || living.isSensitiveToWater() || living instanceof AbstractGolem || living instanceof Slime)) {
            event.setResult(Event.Result.DENY);
            return;
        }
        event.setResult(Event.Result.DEFAULT);
    }

    @SubscribeEvent
    public static void PotionAddedEvent(PotionEvent.PotionAddedEvent event) {
        LivingEntity living = event.getEntityLiving();

        if (!living.level.isClientSide && event.getPotionEffect().getEffect().equals(BrassArmoryEffects.BLEEDING.get())) {
            ((ServerLevel)living.level).getChunkSource().chunkMap.broadcast(living, new ClientboundUpdateMobEffectPacket(living.getId(), event.getPotionEffect()));
        }
    }
}
