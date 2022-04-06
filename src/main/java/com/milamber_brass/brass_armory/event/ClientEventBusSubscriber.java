package com.milamber_brass.brass_armory.event;


import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.client.gui.GunScreen;
import com.milamber_brass.brass_armory.client.render.*;
import com.milamber_brass.brass_armory.container.GunContainer;
import com.milamber_brass.brass_armory.entity.projectile.FireRodEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombType;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmoryMenus;
import com.milamber_brass.brass_armory.init.BrassArmoryModels;
import com.milamber_brass.brass_armory.item.BombItem;
import com.milamber_brass.brass_armory.item.FireRodItem;
import com.milamber_brass.brass_armory.item.HalberdItem;
import com.milamber_brass.brass_armory.item.abstracts.AbstractGunItem;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {
    @SubscribeEvent
    @ParametersAreNonnullByDefault
    public static void clientSetup(FMLClientSetupEvent event) {
        BrassArmory.LOGGER.debug("Running client setup.");
        // Register spear and arrow entity rendering handlers
        EntityRenderers.register(BrassArmoryEntityTypes.BA_ARROW.get(), BAArrowRenderer::new);
        EntityRenderers.register(BrassArmoryEntityTypes.BULLET.get(), BulletRenderer::new);

        EntityRenderers.register(BrassArmoryEntityTypes.BOMB.get(), BombEntityRenderer::new);
        EntityRenderers.register(BrassArmoryEntityTypes.BOUNCY_BOMB.get(), BombEntityRenderer::new);
        EntityRenderers.register(BrassArmoryEntityTypes.STICKY_BOMB.get(), BombEntityRenderer::new);

        EntityRenderers.register(BrassArmoryEntityTypes.BOOMERANG.get(), BoomerangEntityRenderer::new);
        EntityRenderers.register(BrassArmoryEntityTypes.SPEAR.get(), SpearEntityRenderer::new);
        EntityRenderers.register(BrassArmoryEntityTypes.FIRE_ROD.get(), SpearEntityRenderer::new);
        EntityRenderers.register(BrassArmoryEntityTypes.DAGGER.get(), ThrownWeaponEntityRenderer::new);
        EntityRenderers.register(BrassArmoryEntityTypes.BATTLEAXE.get(), ThrownWeaponEntityRenderer::new);


        event.enqueueWork(() -> {
            MenuScreens.register(BrassArmoryMenus.GUN_MENU.get(), GunScreen::new);

            //BOMBS
            for (BombType bombType : BombType.values()) {
                //Sets up alternative item models for all possible fuse states
                ItemProperties.register(BombType.getBombItem(bombType), new ResourceLocation("bomb_fuse"), (bombStack, clientLevel, living, k) -> {
                    Entity entity = living != null ? living : bombStack.getEntityRepresentation();
                    if (!BombItem.getFuseLit(bombStack) || entity == null) return 1.0F;
                    else {
                        if (clientLevel == null && entity.level instanceof ClientLevel) clientLevel = (ClientLevel)entity.level;
                        if (clientLevel == null || !(bombStack.getItem() instanceof BombItem)) return 1.0F;
                        return ((float)BombItem.getFuseLength(bombStack) / 60F);
                    }
                });
                ItemProperties.register(BombType.getBombItem(bombType), new ResourceLocation("defused"), (bombStack, clientLevel, living, k) -> {
                    Entity entity = living != null ? living : bombStack.getEntityRepresentation();
                    return (entity instanceof BombEntity bomb && bomb.getDefused()) ? 1.0F : 0.0F;
                });
            }

            //HALBERDS
            for (HalberdItem halberdItem : new HalberdItem[] {
                    BrassArmoryItems.WOODEN_HALBERD.get(), BrassArmoryItems.STONE_HALBERD.get(),
                    BrassArmoryItems.IRON_HALBERD.get(), BrassArmoryItems.GOLDEN_HALBERD.get(),
                    BrassArmoryItems.DIAMOND_HALBERD.get(), BrassArmoryItems.NETHERITE_HALBERD.get() }) {
                ItemProperties.register(halberdItem, new ResourceLocation("stance"), (halberdStack, clientLevel, living, k) ->
                        HalberdItem.getStance(halberdStack) ? 1.0F : 0.0F);
            }

            //SPEARS
            for (AbstractThrownWeaponItem spearItem : new AbstractThrownWeaponItem[] {
                    BrassArmoryItems.WOODEN_SPEAR.get(), BrassArmoryItems.STONE_SPEAR.get(),
                    BrassArmoryItems.IRON_SPEAR.get(), BrassArmoryItems.GOLDEN_SPEAR.get(),
                    BrassArmoryItems.DIAMOND_SPEAR.get(), BrassArmoryItems.NETHERITE_SPEAR.get(),
                    BrassArmoryItems.FIRE_ROD.get() }) {
                ItemProperties.register(spearItem, new ResourceLocation("throwing"), (spearStack, clientLevel, living, k) ->
                        living != null && living.isUsingItem() && living.getUseItem() == spearStack ? 1.0F : 0.0F);
            }

            //FIRE RODS
            for (FireRodItem fireRod : new FireRodItem[] {
                    BrassArmoryItems.FIRE_ROD.get()}) {
                ItemProperties.register(fireRod, new ResourceLocation("extinguished"), (fireRodStack, clientLevel, living, k) ->
                        fireRodStack.getEntityRepresentation() instanceof FireRodEntity fireRodEntity && fireRodEntity.hasBeenExtinguished() ? 1.0F : 0.0F);

                ItemProperties.register(BrassArmoryItems.FIRE_ROD.get(), new ResourceLocation("gui"), (fireRodStack, clientLevel, living, k) ->
                        living == null || living.isHolding(BrassArmoryItems.FIRE_ROD.get()) ? 0.0F : 1.0F);

            }

            //GUNS
            for (AbstractGunItem gunItem : new AbstractGunItem[] {
                    BrassArmoryItems.FLINTLOCK_PISTOL.get(), BrassArmoryItems.MUSKET.get(), BrassArmoryItems.BLUNDERBUSS.get() }) {
                ItemProperties.register(gunItem, new ResourceLocation("loading"), (stack, clientLevel, living, k) ->
                        living != null && living.isUsingItem() ? 1.0F : 0.0F);

                ItemProperties.register(gunItem, new ResourceLocation("status"), (stack, clientLevel, living, k) ->
                        living != null && living.isUsingItem() ? AbstractGunItem.getLoadProgress(stack) / 20F : 0.0F);

                ItemProperties.register(gunItem, new ResourceLocation("loaded"), (stack, clientLevel, living, k) ->
                        AbstractGunItem.getLoad(stack) == 2 ? 1.0F : 0.0F);

                ItemProperties.register(gunItem, new ResourceLocation("menu"), (stack, clientLevel, living, k) ->
                        living instanceof Player player && player.containerMenu instanceof GunContainer ? 1.0F : 0.0F);
            }

            //LONGBOW
            ItemProperties.register(BrassArmoryItems.LONGBOW.get(), new ResourceLocation("pull"), (stack, clientLevel, living, k) ->
                    living == null ? 0F : (living.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - living.getUseItemRemainingTicks()) / 30.0F));

            ItemProperties.register(BrassArmoryItems.LONGBOW.get(), new ResourceLocation("pulling"), (stack, clientLevel, living, k) ->
                    living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);
        });
    }

    @SubscribeEvent
    public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        BrassArmoryModels.register(event);
    }

    @SubscribeEvent
    @ParametersAreNonnullByDefault
    public static void TextureStitchEvent(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
            event.addSprite(GunContainer.EMPTY_POWDER_SLOT);
            event.addSprite(GunContainer.EMPTY_AMMO_SLOT);
        }
    }
}