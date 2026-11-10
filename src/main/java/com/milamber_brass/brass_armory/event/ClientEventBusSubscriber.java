package com.milamber_brass.brass_armory.event;


import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.client.gui.ClientQuiverTooltip;
import com.milamber_brass.brass_armory.client.gui.GunScreen;
import com.milamber_brass.brass_armory.client.particle.BloodParticle;
import com.milamber_brass.brass_armory.client.render.*;
import com.milamber_brass.brass_armory.entity.projectile.FireRodEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmoryMenus;
import com.milamber_brass.brass_armory.init.BrassArmoryModels;
import com.milamber_brass.brass_armory.init.BrassArmoryParticles;
import com.milamber_brass.brass_armory.inventory.GunContainer;
import com.milamber_brass.brass_armory.inventory.QuiverTooltip;
import com.milamber_brass.brass_armory.item.*;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import com.milamber_brass.brass_armory.util.ItemData;
import com.milamber_brass.brass_armory.util.PotionData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(BrassArmoryMenus.GUN_MENU.get(), GunScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BrassArmoryEntityTypes.DIRT_ARROW.get(), SpecialArrowRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.EXPLOSION_ARROW.get(), SpecialArrowRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.FROST_ARROW.get(), SpecialArrowRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.GRASS_ARROW.get(), SpecialArrowRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.LASER_ARROW.get(), SpecialArrowRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.ROPE_ARROW.get(), SpecialArrowRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.SLIME_ARROW.get(), SpecialArrowRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.WARP_ARROW.get(), SpecialArrowRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.FIRE_ARROW.get(), SpecialArrowRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.CONFUSION_ARROW.get(), SpecialArrowRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.TORCH_ARROW.get(), SpecialArrowRenderer::new);

        event.registerEntityRenderer(BrassArmoryEntityTypes.BULLET.get(), BulletRenderer::new);

        event.registerEntityRenderer(BrassArmoryEntityTypes.BOMB.get(), RollingItemEntityRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.BOUNCY_BOMB.get(), RollingItemEntityRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.STICKY_BOMB.get(), RollingItemEntityRenderer::new);

        event.registerEntityRenderer(BrassArmoryEntityTypes.SPIKY_BALL.get(), RollingItemEntityRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.FLAIL_HEAD.get(), FlailHeadEntityRenderer::new);

        event.registerEntityRenderer(BrassArmoryEntityTypes.BOOMERANG.get(), BoomerangEntityRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.SPEAR.get(), SpearEntityRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.FIRE_ROD.get(), SpearEntityRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.DAGGER.get(), ThrownWeaponEntityRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.BATTLEAXE.get(), ThrownWeaponEntityRenderer::new);

        event.registerEntityRenderer(BrassArmoryEntityTypes.CANNON.get(), CannonEntityRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.CANNON_BALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.CARCASS_ROUND.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(BrassArmoryEntityTypes.SIEGE_ROUND.get(), ThrownItemRenderer::new);

    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BrassArmoryParticles.BLOOD_FALL_PARTICLE.get(), BloodParticle.BloodFallProvider::new);
        event.registerSpriteSet(BrassArmoryParticles.BLOOD_LAND_PARTICLE.get(), BloodParticle.BloodLandProvider::new);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        BrassArmory.LOGGER.debug("Running client setup.");

        event.enqueueWork(() -> {


            //BOMBS
            for (BombItem bombItem : new BombItem[] { BrassArmoryItems.BOMB.get(), BrassArmoryItems.STICKY_BOMB.get(), BrassArmoryItems.BOUNCY_BOMB.get() }) {
                //Sets up alternative item models for all possible fuse states
                ItemProperties.register(bombItem, ResourceLocation.parse("bomb_fuse"), (bombStack, clientLevel, living, k) -> {
                    Entity entity = living != null ? living : bombStack.getEntityRepresentation();
                    if (ItemData.getTag(bombStack).getBoolean("GuiDisplay")) return 0.8F;
                    if (!BombItem.getFuseLit(bombStack) || entity == null) return 1.0F;
                    else {
                        if (clientLevel == null && entity.level() instanceof ClientLevel clientLevel1) clientLevel = clientLevel1;
                        if (clientLevel == null || !(bombStack.getItem() instanceof BombItem)) return 1.0F;
                        return ((float) BombItem.getFuseLength(bombStack) / 60F);
                    }
                });

                ItemProperties.register(bombItem, ResourceLocation.parse("defused"), (bombStack, clientLevel, living, k) -> {
                    Entity entity = living != null ? living : bombStack.getEntityRepresentation();
                    return (entity instanceof BombEntity bomb && bomb.getDefused()) ? 1.0F : 0.0F;
                });
            }

            //HALBERDS
            for (HalberdItem halberdItem : new HalberdItem[] {
                    BrassArmoryItems.WOODEN_HALBERD.get(), BrassArmoryItems.STONE_HALBERD.get(),
                    BrassArmoryItems.IRON_HALBERD.get(), BrassArmoryItems.GOLDEN_HALBERD.get(),
                    BrassArmoryItems.DIAMOND_HALBERD.get(), BrassArmoryItems.NETHERITE_HALBERD.get()}) {
                ItemProperties.register(halberdItem, ResourceLocation.parse("stance"), (halberdStack, clientLevel, living, k) ->
                        HalberdItem.getStance(halberdStack) ? 1.0F : 0.0F);
            }

            //SPEARS
            for (AbstractThrownWeaponItem thrownWeaponItem : new AbstractThrownWeaponItem[] {
                    BrassArmoryItems.WOODEN_SPEAR.get(), BrassArmoryItems.STONE_SPEAR.get(),
                    BrassArmoryItems.IRON_SPEAR.get(), BrassArmoryItems.GOLDEN_SPEAR.get(),
                    BrassArmoryItems.DIAMOND_SPEAR.get(), BrassArmoryItems.NETHERITE_SPEAR.get(),
                    BrassArmoryItems.FIRE_ROD.get(),
                    BrassArmoryItems.WOODEN_FLAIL.get(), BrassArmoryItems.STONE_FLAIL.get(),
                    BrassArmoryItems.IRON_FLAIL.get(), BrassArmoryItems.GOLDEN_FLAIL.get(),
                    BrassArmoryItems.DIAMOND_FLAIL.get(), BrassArmoryItems.NETHERITE_FLAIL.get()}) {
                ItemProperties.register(thrownWeaponItem, ResourceLocation.parse("throwing"), (spearStack, clientLevel, living, k) ->
                        living != null && living.isUsingItem() && living.getUseItem() == spearStack ? 1.0F : 0.0F);
            }

            //FIRE RODS
            ItemProperties.register(BrassArmoryItems.FIRE_ROD.get(), ResourceLocation.parse("extinguished"), (fireRodStack, clientLevel, living, k) ->
                    fireRodStack.getEntityRepresentation() instanceof FireRodEntity fireRodEntity && fireRodEntity.hasBeenExtinguished() ? 1.0F : 0.0F);

            ItemProperties.register(BrassArmoryItems.FIRE_ROD.get(), ResourceLocation.parse("gui"), (fireRodStack, clientLevel, living, k) ->
                    living == null || living.isHolding(BrassArmoryItems.FIRE_ROD.get()) ? 0.0F : 1.0F);

            //FLAILS
            for (FlailItem flailItem : new FlailItem[] {
                    BrassArmoryItems.WOODEN_FLAIL.get(), BrassArmoryItems.STONE_FLAIL.get(),
                    BrassArmoryItems.IRON_FLAIL.get(), BrassArmoryItems.GOLDEN_FLAIL.get(),
                    BrassArmoryItems.DIAMOND_FLAIL.get(), BrassArmoryItems.NETHERITE_FLAIL.get()}) {
                ItemProperties.register(flailItem, ResourceLocation.parse("no_head"), (stack, clientLevel, living, k) ->
                        living != null && flailItem.isExtended(living, stack) ? 1.0F : 0.0F);
            }

            //GUNS
            for (FlintlockItem gunItem : new FlintlockItem[] {
                    BrassArmoryItems.FLINTLOCK_PISTOL.get(), BrassArmoryItems.MUSKET.get(), BrassArmoryItems.BLUNDERBUSS.get()}) {
                ItemProperties.register(gunItem, ResourceLocation.parse("loading"), (stack, clientLevel, living, k) ->
                        living != null && living.isUsingItem() && FlintlockItem.getLoad(stack) == 1 ? 1.0F : 0.0F);

                ItemProperties.register(gunItem, ResourceLocation.parse("status"), (stack, clientLevel, living, k) ->
                        living != null && living.isUsingItem() ? FlintlockItem.getLoadProgress(stack) / 20F : 0.0F);

                ItemProperties.register(gunItem, ResourceLocation.parse("loaded"), (stack, clientLevel, living, k) ->
                        FlintlockItem.getLoad(stack) == 2 ? 1.0F : 0.0F);

                ItemProperties.register(gunItem, ResourceLocation.parse("menu"), (stack, clientLevel, living, k) ->
                        living instanceof Player player && player.containerMenu instanceof GunContainer<?> && !ItemData.getTag(stack).isEmpty() && ItemData.getTag(stack).contains(GunContainer.gunIcon) ? 1.0F : 0.0F);
            }

            //LONGBOW
            ItemProperties.register(BrassArmoryItems.LONGBOW.get(), ResourceLocation.parse("pull"), (stack, clientLevel, living, k) ->
                    living == null ? 0F : (living.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(living) - living.getUseItemRemainingTicks()) / 30.0F));

            ItemProperties.register(BrassArmoryItems.LONGBOW.get(), ResourceLocation.parse("pulling"), (stack, clientLevel, living, k) ->
                    living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);

            //KATANA
            ItemProperties.register(BrassArmoryItems.KATANA.get(), ResourceLocation.parse("withering"), (stack, clientLevel, living, k) ->
                    (float)KatanaItem.getWither(stack) / 100F);

            //CARCASS ROUNDS
            ItemProperties.register(BrassArmoryItems.CARCASS_ROUND.get(), ResourceLocation.parse("dragon"), (stack, clientLevel, living, k) ->
                    CarcassRoundItem.isADragonRound(stack) ? 1.0F : 0.0F);
        });
    }

    @SubscribeEvent
    public static void registerClientTooltipComponentFactoriesEvent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(QuiverTooltip.class, ClientQuiverTooltip::new);
    }

    @SubscribeEvent
    public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        BrassArmoryModels.register(event);
    }

    @SubscribeEvent
    public static void colorHandlerEvent(RegisterColorHandlersEvent.Item event) {
        event.register((stack, i) -> i > 0 ? -1 : DyedItemColor.getOrDefault(stack, 0xA06540), BrassArmoryItems.GLIDER.get());
        event.register((stack, i) -> i > 0 ? -1 : PotionData.getColor(stack), BrassArmoryItems.CARCASS_ROUND.get());
    }


    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        EntityModelSet entityModels = event.getEntityModels();

        event.getSkins().forEach(skin -> {
            if(event.getSkin(skin) instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new GliderLayer<>(playerRenderer, entityModels));
            }
        });

        if(event.getRenderer(EntityType.ARMOR_STAND) instanceof ArmorStandRenderer armorStandRenderer) {
            armorStandRenderer.addLayer(new GliderLayer<>(armorStandRenderer, entityModels));
        }
    }

    private static final ResourceLocation GLIDER_TEXTURE = ResourceLocation.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/glider.png");
    private static final ResourceLocation GLIDER_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(BrassArmory.MOD_ID,"textures/entity/glider_overlay.png");

    private static class GliderLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {
        private final EntityModel<T> gliderModel;

        public GliderLayer(RenderLayerParent<T, M> layerParent, EntityModelSet entityModelSet) {
            super(layerParent, entityModelSet);
            this.gliderModel = new ElytraModel<>(entityModelSet.bakeLayer(ModelLayers.ELYTRA));
        }

        @Override
        public boolean shouldRender(ItemStack stack, T entity) {
            return stack.is(BrassArmoryItems.GLIDER.get());
        }

        @Override
        public @NotNull ResourceLocation getElytraTexture(ItemStack stack, T entity) {
            return GLIDER_TEXTURE;
        }

        @Override
        public void render(PoseStack stack, MultiBufferSource bufferSource, int light, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack itemstack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
            if (shouldRender(itemstack, livingEntity)) {
                ResourceLocation resourcelocation;
                if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                    if (abstractclientplayer.getSkin().elytraTexture() != null) {
                        resourcelocation = abstractclientplayer.getSkin().elytraTexture();
                    } else if (abstractclientplayer.getSkin().capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                        resourcelocation = abstractclientplayer.getSkin().capeTexture();
                    } else {
                        resourcelocation = getElytraTexture(itemstack, livingEntity);
                    }
                } else resourcelocation = getElytraTexture(itemstack, livingEntity);

                stack.pushPose();
                stack.translate(0.0D, 0.0D, 0.125D);
                this.getParentModel().copyPropertiesTo(this.gliderModel);
                this.gliderModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(resourcelocation), itemstack.hasFoil());


                int i = DyedItemColor.getOrDefault(itemstack, 0xA06540);

                this.gliderModel.renderToBuffer(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 0xFF000000 | i);
                vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(GLIDER_OVERLAY_TEXTURE), itemstack.hasFoil());
                this.gliderModel.renderToBuffer(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, -1);
                stack.popPose();
            }
        }
    }
}
