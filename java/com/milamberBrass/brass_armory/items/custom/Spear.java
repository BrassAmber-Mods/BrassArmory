package com.milamberBrass.brass_armory.items.custom;


import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.milamberBrass.brass_armory.BrassArmory;
import com.milamberBrass.brass_armory.entities.custom.Spear_Entity;
import com.milamberBrass.brass_armory.util.ReflectionMethod;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.network.NetworkRegistry;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;


public class Spear extends TieredItem implements IVanishable {
    private static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("99f7541c-a163-437c-8c25-bd685549b305");
    private static final float SPECIAL_REACH_MULTIPLIER = 1.5F;
    private final double reachDistanceBonus = 1.0D;

    private final float attackDamage;
    /** Modifiers applied when the item is in the mainhand of a user. */
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    protected ItemTier finalTier;
    public static final String VERSION = "1.0";
    public static Predicate<String> versionCheck = NetworkRegistry.acceptMissingOr(VERSION);
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(ResourceLocation.tryCreate(BrassArmory.MOD_ID),() -> VERSION , versionCheck, versionCheck);

    public Spear(ItemTier tier, int attackDamageIn, Properties builderIn) {
        super(tier, builderIn);
        finalTier = tier;
        this.attackDamage = (float)attackDamageIn + tier.getAttackDamage();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.6D, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    public double getReach() {
        return this.reachDistanceBonus;
    }

    public double getReachExtended() {
        return this.reachDistanceBonus * SPECIAL_REACH_MULTIPLIER;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        /** ClientPlayerEntity player = (ClientPlayerEntity) stack.getAttachedEntity();
        if (slot == EquipmentSlotType.MAINHAND) {
            multimap.put(player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getAttribute(),
                    new AttributeModifier(REACH_DISTANCE_MODIFIER, "Weapon Modifier", this.reachDistanceBonus,
                            AttributeModifier.Operation.ADDITION));
        }*/
        return multimap;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
            Material material = state.getMaterial();
            return material != Material.PLANTS && material != Material.TALL_PLANTS && material != Material.CORAL && !state.isIn(BlockTags.LEAVES) && material != Material.GOURD ? 1.0F : 1.5F;
    }

    public IItemTier getFinalTier(){
        return finalTier;
    }


    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) entityLiving;
            int i = this.getUseDuration(stack) - timeLeft;
            if (i >= 10) {
                if (!worldIn.isRemote) {
                    stack.damageItem(1, playerentity, (player) -> {
                        player.sendBreakAnimation(entityLiving.getActiveHand());
                    });
                    Spear_Entity spear_entity = new Spear_Entity(worldIn, playerentity, stack, finalTier);
                    spear_entity.setDirectionAndMovement(playerentity, playerentity.rotationPitch,
                            playerentity.rotationYaw, 0.0F, 2.5F * 0.5F, 1.0F);
                    if (playerentity.abilities.isCreativeMode) {
                        spear_entity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                    }

                    worldIn.addEntity(spear_entity);
                    worldIn.playMovingSound((PlayerEntity) null, spear_entity, SoundEvents.ITEM_TRIDENT_THROW,
                            SoundCategory.PLAYERS, 1.0F, 1.0F);
                    if (!playerentity.abilities.isCreativeMode) {
                        playerentity.inventory.deleteStack(stack);
                    }

                }

                playerentity.addStat(Stats.ITEM_USED.get(this));
            }
        }
    }



    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (itemstack.getDamage() >= itemstack.getMaxDamage() - 1) {
            return ActionResult.resultFail(itemstack);
        } else {
            playerIn.setActiveHand(handIn);
            return ActionResult.resultConsume(itemstack);
        }
    }



    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(1, attacker, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double)state.getBlockHardness(worldIn, pos) != 0.0D) {
            stack.damageItem(2, entityLiving, (entity) -> {
                entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(equipmentSlot);
    }

    @Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, value = Dist.CLIENT)
    private static class EventHandler {

        private static final ReflectionMethod<Object> METHOD_SYNC_CURRENT_PLAY_ITEM = new ReflectionMethod<>(PlayerController.class, "func_78750_j", "syncCurrentPlayItem");

        @SubscribeEvent
        public static void onMouseEvent(MouseInputEvent event) {
            if (event.getButton() != 0) {
                return;
            }
            if (!event.isCanceled()) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            Entity entity = mc.getRenderViewEntity();
            float partialTicks = mc.getRenderPartialTicks();
            double blockReachDistance = mc.playerController.getBlockReachDistance();
            RayTraceResult blockMouseOver = entity.pick(blockReachDistance, partialTicks,false);

            Vector3d eyeVec = entity.getEyePosition(partialTicks);
            double entityReachDistance = blockReachDistance - 1.5D;
            if (blockMouseOver.getType() != RayTraceResult.Type.MISS) {
                entityReachDistance = Math.min(eyeVec.distanceTo(blockMouseOver.getHitVec()), entityReachDistance);
            }

            Vector3d lookVec = entity.getLook(partialTicks);
            AxisAlignedBB aabb = entity.getBoundingBox();
            aabb = aabb.expand(lookVec.x * entityReachDistance, lookVec.y * entityReachDistance, lookVec.z * entityReachDistance);
            aabb = aabb.grow(1.0D, 1.0D, 1.0D);
            List<Entity> list = mc.world.getEntitiesInAABBexcluding(entity, aabb, entity1 -> {
                if (!EntityPredicates.NOT_SPECTATING.test(entity1)) {
                    return false;
                }
                return entity1.canBeCollidedWith();
            });

            Entity pointedEntity = null;
            Vector3d endVec = eyeVec.add(lookVec.x * entityReachDistance, lookVec.y * entityReachDistance, lookVec.z * entityReachDistance);
            double minSqr = entityReachDistance * entityReachDistance;
            for (Entity entity2 : list) {
                if (entity.getLowestRidingEntity() == entity2.getLowestRidingEntity() && !entity2.canRiderInteract()) {
                    continue;
                }

                AxisAlignedBB aabb1 = entity2.getBoundingBox().grow(entity2.getCollisionBorderSize());
                if (aabb1.contains(eyeVec)) {
                    pointedEntity = entity2;
                    minSqr = 0.0D;
                    break;
                }

                /*RayTraceResult rayTraceResult = aabb1.rayTrace(eyeVec, endVec);
                if (rayTraceResult == null) {
                    continue;
                }

                double dist = eyeVec.squareDistanceTo(rayTraceResult.getHitVec());
                if (dist < minSqr) {
                    pointedEntity = entity2;
                    minSqr = dist;
                }*/
            }

            if (pointedEntity != null && (mc.objectMouseOver == null || pointedEntity != null && (mc.objectMouseOver.getType() == RayTraceResult.Type.ENTITY))) {
                METHOD_SYNC_CURRENT_PLAY_ITEM.invoke(mc.playerController);
                //NETWORK.sendToServer(new PacketAttackEntity(pointedEntity));

                if (mc.playerController.getCurrentGameType() != GameType.SPECTATOR) {
                    mc.player.attackTargetEntityWithCurrentItem(pointedEntity);
                    mc.player.resetCooldown();
                }
            }
        }

    }
}
