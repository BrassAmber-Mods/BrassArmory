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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.List;
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
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(ResourceLocation.tryParse(BrassArmory.MOD_ID),() -> VERSION , versionCheck, versionCheck);

    public Spear(ItemTier tier, int attackDamageIn, Properties builderIn) {
        super(tier, builderIn);
        finalTier = tier;
        this.attackDamage = (float)attackDamageIn + tier.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.6D, AttributeModifier.Operation.ADDITION));
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

    public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
            Material material = state.getMaterial();
            return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.CORAL && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
    }

    public IItemTier getFinalTier(){
        return finalTier;
    }


    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.SPEAR;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) entityLiving;
            int i = this.getUseDuration(stack) - timeLeft;
            if (i >= 10) {
                if (!worldIn.isClientSide) {
                    stack.hurtAndBreak(1, playerentity, (player) -> {
                        player.broadcastBreakEvent(entityLiving.getUsedItemHand());
                    });
                    Spear_Entity spear_entity = new Spear_Entity(worldIn, playerentity, stack, finalTier);
                    spear_entity.shootFromRotation(playerentity, playerentity.xRot,
                            playerentity.yRot, 0.0F, 2.5F * 0.5F, 1.0F);
                    if (playerentity.abilities.instabuild) {
                        spear_entity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                    }

                    worldIn.addFreshEntity(spear_entity);
                    worldIn.playSound((PlayerEntity) null, spear_entity, SoundEvents.TRIDENT_THROW,
                            SoundCategory.PLAYERS, 1.0F, 1.0F);
                    if (!playerentity.abilities.instabuild) {
                        playerentity.inventory.removeItem(stack);
                    }

                }

                playerentity.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }



    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return ActionResult.fail(itemstack);
        } else {
            playerIn.startUsingItem(handIn);
            return ActionResult.consume(itemstack);
        }
    }



    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double)state.getDestroySpeed(worldIn, pos) != 0.0D) {
            stack.hurtAndBreak(2, entityLiving, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        // Why does this method exist?
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
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
            Entity entity = mc.getCameraEntity();
            float partialTicks = mc.getFrameTime();
            double blockReachDistance = mc.gameMode.getPickRange();
            RayTraceResult blockMouseOver = entity.pick(blockReachDistance, partialTicks,false);

            Vector3d eyeVec = entity.getEyePosition(partialTicks);
            double entityReachDistance = blockReachDistance - 1.5D;
            if (blockMouseOver.getType() != RayTraceResult.Type.MISS) {
                entityReachDistance = Math.min(eyeVec.distanceTo(blockMouseOver.getLocation()), entityReachDistance);
            }

            Vector3d lookVec = entity.getViewVector(partialTicks);
            AxisAlignedBB aabb = entity.getBoundingBox();
            aabb = aabb.expandTowards(lookVec.x * entityReachDistance, lookVec.y * entityReachDistance, lookVec.z * entityReachDistance);
            aabb = aabb.inflate(1.0D, 1.0D, 1.0D);
            List<Entity> list = mc.level.getEntities(entity, aabb, entity1 -> {
                if (!EntityPredicates.NO_SPECTATORS.test(entity1)) {
                    return false;
                }
                return entity1.canBeCollidedWith();
            });

            Entity pointedEntity = null;
            Vector3d endVec = eyeVec.add(lookVec.x * entityReachDistance, lookVec.y * entityReachDistance, lookVec.z * entityReachDistance);
            double minSqr = entityReachDistance * entityReachDistance;
            for (Entity entity2 : list) {
                if (entity.getRootVehicle() == entity2.getRootVehicle() && !entity2.canRiderInteract()) {
                    continue;
                }

                AxisAlignedBB aabb1 = entity2.getBoundingBox().inflate(entity2.getPickRadius());
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

            if (pointedEntity != null && (mc.hitResult == null || pointedEntity != null && (mc.hitResult.getType() == RayTraceResult.Type.ENTITY))) {
                METHOD_SYNC_CURRENT_PLAY_ITEM.invoke(mc.gameMode);
                //NETWORK.sendToServer(new PacketAttackEntity(pointedEntity));

                if (mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
                    mc.player.attack(pointedEntity);
                    mc.player.resetAttackStrengthTicker();
                }
            }
        }

    }
}
