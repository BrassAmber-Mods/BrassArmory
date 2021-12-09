package com.milamber_brass.brass_armory.client;


import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.client.render.BAArrowRenderer;
import com.milamber_brass.brass_armory.client.render.BombEntityRenderer;
import com.milamber_brass.brass_armory.client.render.SpearEntityRenderer;
import com.milamber_brass.brass_armory.entity.projectile.BombEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.BombItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        BrassArmory.LOGGER.debug("Running client setup.");
        // Register spear and arrow entity rendering handlers
        EntityRenderers.register(BrassArmoryEntityTypes.SPEAR.get(), SpearEntityRenderer::new);
        EntityRenderers.register(BrassArmoryEntityTypes.BA_ARROW.get(), BAArrowRenderer::new);
        EntityRenderers.register(BrassArmoryEntityTypes.BOMB.get(), BombEntityRenderer::new);

        event.enqueueWork(() -> {
            for (int i = 0; i < 3; i++) {
                ItemProperties.register(BombItem.getBomb(i), new ResourceLocation("bomb_fuse"), (bombStack, clientLevel, living, k) -> {
                    Entity entity = living != null ? living : bombStack.getEntityRepresentation();
                    if (!BombItem.getFuseLit(bombStack) || entity == null) return 1.0F;
                    else {
                        if (clientLevel == null && entity.level instanceof ClientLevel) clientLevel = (ClientLevel)entity.level;
                        if (clientLevel == null || !(bombStack.getItem() instanceof BombItem)) return 1.0F;
                        return ((float)BombItem.getFuseLength(bombStack) / 60F);
                    }
                });
                ItemProperties.register(BombItem.getBomb(i), new ResourceLocation("defused"), (bombStack, clientLevel, living, k) -> {
                    Entity entity = living != null ? living : bombStack.getEntityRepresentation();
                    return (entity instanceof BombEntity bomb && bomb.getDefused()) ? 1.0F : 0.0F;
                });
            }
        });
    }
}
