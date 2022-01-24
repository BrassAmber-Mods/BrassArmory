package com.milamber_brass.brass_armory.client;


import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.client.render.BAArrowRenderer;
import com.milamber_brass.brass_armory.client.render.BombEntityRenderer;
import com.milamber_brass.brass_armory.client.render.BoomerangEntityRenderer;
import com.milamber_brass.brass_armory.client.render.SpearEntityRenderer;
import com.milamber_brass.brass_armory.entity.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.bomb.BombType;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.BombItem;
import com.milamber_brass.brass_armory.item.HalberdItem;
import com.milamber_brass.brass_armory.item.SpearItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        BrassArmory.LOGGER.debug("Running client setup.");
        // Register spear and arrow entity rendering handlers
        EntityRenderers.register(BrassArmoryEntityTypes.SPEAR.get(), SpearEntityRenderer::new);
        EntityRenderers.register(BrassArmoryEntityTypes.BA_ARROW.get(), BAArrowRenderer::new);

        EntityRenderers.register(BrassArmoryEntityTypes.BOMB.get(), BombEntityRenderer::new);
        EntityRenderers.register(BrassArmoryEntityTypes.BOUNCY_BOMB.get(), BombEntityRenderer::new);
        EntityRenderers.register(BrassArmoryEntityTypes.STICKY_BOMB.get(), BombEntityRenderer::new);

        EntityRenderers.register(BrassArmoryEntityTypes.BOOMERANG.get(), BoomerangEntityRenderer::new);

        event.enqueueWork(() -> { //Sets up alternative item models for all possible fuse states
            for (BombType bombType : BombType.values()) {
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

            HalberdItem[] halberds = { BrassArmoryItems.WOOD_HALBERD.get(), BrassArmoryItems.STONE_HALBERD.get(),
                                BrassArmoryItems.IRON_HALBERD.get(), BrassArmoryItems.GOLD_HALBERD.get(),
                                BrassArmoryItems.DIAMOND_HALBERD.get(), BrassArmoryItems.NETHERITE_HALBERD.get() };

            for (HalberdItem halberdItem : halberds) {
                ItemProperties.register(halberdItem, new ResourceLocation("blocking"), (halberdStack, clientLevel, living, k) -> {
                    return living != null && living.isUsingItem() && living.getUseItem() == halberdStack ? 1.0F : 0.0F;
                });
            }

            SpearItem[] spears = { BrassArmoryItems.WOOD_SPEAR.get(), BrassArmoryItems.STONE_SPEAR.get(),
                    BrassArmoryItems.IRON_SPEAR.get(), BrassArmoryItems.GOLD_SPEAR.get(),
                    BrassArmoryItems.DIAMOND_SPEAR.get(), BrassArmoryItems.NETHERITE_SPEAR.get() };

            for (SpearItem spearItem : spears) {
                ItemProperties.register(spearItem, new ResourceLocation("throwing"), (halberdStack, clientLevel, living, k)
                        -> living != null && living.isUsingItem() && living.getUseItem() == halberdStack ? 1.0F : 0.0F);
            }
        });
    }
}