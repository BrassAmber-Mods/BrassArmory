package com.milamber_brass.brass_armory.event;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.capabilities.IEffectCapability;
import com.milamber_brass.brass_armory.capabilities.IQuiverCapability;
import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.effect.BleedEffect;
import com.milamber_brass.brass_armory.entity.CannonEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryCapabilities;
import com.milamber_brass.brass_armory.init.BrassArmoryEffects;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.BombItem;
import com.milamber_brass.brass_armory.item.QuiverItem;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import java.util.Optional;
import java.util.Set;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = BrassArmory.MOD_ID)
public class UniversalEventSubscriber {
    @SubscribeEvent //Drop any lit bombs if opening a container, to prevent storing lit bombs
    public static void onPlayerContainerEventOpen(PlayerContainerEvent.Open event) {
        Player player = event.getEntity();
        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            if (stack.getItem() instanceof BombItem bombItem && BombItem.getFuseLit(stack)) {
                BombEntity bomb = bombItem.newBombFunction.apply(player.level(), player, player.getMainArm());
                bomb.setFuse(BombItem.getFuseLength(stack));
                bomb.setItem(stack);
                BombItem.setFuseLength(stack, 60);
                BombItem.setFuseLit(stack, false);
                if (!(player.getAbilities().instabuild)) stack.shrink(1);
                player.level().addFreshEntity(bomb);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurtEvent(LivingIncomingDamageEvent event) {
        if (event.getSource().getDirectEntity() instanceof ItemSupplier itemSupplier && itemSupplier.getItem().is(BrassArmoryTags.Items.BLEEDING_EDGE)) {
            BleedEffect.bleedHarder(event.getEntity(), 50, 0);
        }
    }

    @SubscribeEvent
    public static void onPotionApplicableEvent(MobEffectEvent.Applicable event) {
        LivingEntity living = event.getEntity();
        if (event.getEffectInstance().getEffect().equals(BrassArmoryEffects.BLEEDING) && living != null && (living.is(EntityTypeTags.UNDEAD) || living.isSensitiveToWater() || living instanceof AbstractGolem || living instanceof Slime)) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            return;
        }
    }

    @SubscribeEvent
    public static void onPotionAddedEvent(MobEffectEvent.Added event) {
        LivingEntity living = event.getEntity();

        if (!living.level().isClientSide()) {
            var effect = event.getEffectInstance().getEffect();
            if (effect.equals(BrassArmoryEffects.BLEEDING) || effect.equals(BrassArmoryEffects.CONFUSION)) {
                ((ServerLevel) living.level()).getChunkSource().sendToTrackingPlayersAndSelf(living, new ClientboundUpdateMobEffectPacket(living.getId(), event.getEffectInstance(), false));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDropsEvent(LivingDropsEvent event) {
        LivingEntity living = event.getEntity();
        Set<String> tags = living.entityTags();
        if (!tags.isEmpty() && tags.contains("no_loot")) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLivingUpdateEvent(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide() && !player.isDeadOrDying()) {
            Optional.of(player.getData(BrassArmoryCapabilities.QUIVER_CAPABILITY)).ifPresent(IQuiverCapability::tick);
            Optional.of(player.getData(BrassArmoryCapabilities.EFFECT_CAPABILITY)).ifPresent(IEffectCapability::tick);
        }
    }

    @SubscribeEvent
    public static void onLivingGetProjectileEvent(LivingGetProjectileEvent event) {
        if (event.getEntity() instanceof Player player) {
            for (ItemStack quiverStack : player.getInventory().getNonEquipmentItems()) {
                if (quiverStack.getItem() instanceof QuiverItem) {
                    Optional<ItemStack> optionalInQuiverStack = QuiverItem.getContents(quiverStack).findFirst();
                    if (optionalInQuiverStack.isPresent()) {
                        if (!player.level().isClientSide()) {
                            Optional.of(player.getData(BrassArmoryCapabilities.QUIVER_CAPABILITY)).ifPresent(capability -> {
                                capability.setAmmoStack(optionalInQuiverStack.get());
                                capability.setQuiverStack(quiverStack);
                                event.setProjectileItemStack(capability.getAmmoStack());
                            });
                        } else event.setProjectileItemStack(optionalInQuiverStack.get());
                        return;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (player.isPassenger() && player.getVehicle() instanceof CannonEntity cannon && cannon.getFuse() == 0 && ArmoryUtil.isFuseLighter(event.getItemStack()) && event.getHand().equals(InteractionHand.MAIN_HAND)) {
            event.setCanceled(true);
            event.setCancellationResult(cannon.interact(player, event.getHand(), net.minecraft.world.phys.Vec3.ZERO));
        }
    }
}
