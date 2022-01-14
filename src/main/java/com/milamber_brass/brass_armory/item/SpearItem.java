package com.milamber_brass.brass_armory.item;


import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.SpearEntity;

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
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.TridentItem;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;


public class SpearItem extends TridentItem implements IVanishable, ICustomReachItem, ITieredItem {

    private static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("99f7541c-a163-437c-8c25-bd685549b305");
    private static final float SPECIAL_REACH_MULTIPLIER = 1.5F;
    private final double reachDistanceBonus = 1.0D;
    private final float attackDamage;
    private final IItemTier tier;

    /**
     * Modifiers applied when the item is in the mainhand of a user.
     */
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private Multimap<Attribute, AttributeModifier> customAttributes;

    public SpearItem(ItemTier tier, int attackDamageIn, Properties builderIn) {
        super(builderIn.defaultDurability(tier.getUses()));
        this.attackDamage = (float) attackDamageIn + tier.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.6D, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
        this.tier = tier;
    }

    public double getReach() {
        return this.reachDistanceBonus;
    }

    public double getReachExtended() {
        return this.reachDistanceBonus * SPECIAL_REACH_MULTIPLIER;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    @ParametersAreNonnullByDefault
    public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    @Override
    @ParametersAreNonnullByDefault
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.CORAL && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
    }

    public IItemTier getFinalTier() {
        return this.getTier();
    }

    /**
     * @return The action that specifies what animation to play when the item is being used.
     */
    @ParametersAreNonnullByDefault
    @Nonnull
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.SPEAR;
    }

    /**
     * How long it takes to use or consume an item.
     */
    @ParametersAreNonnullByDefault
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @ParametersAreNonnullByDefault
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) entityLiving;
            int i = this.getUseDuration(stack) - timeLeft;
            if (i >= 10) {
                if (!worldIn.isClientSide) {
                    stack.hurtAndBreak(1, playerentity, (player) -> {
                        player.broadcastBreakEvent(entityLiving.getUsedItemHand());
                    });
                    SpearEntity spear_entity = new SpearEntity(worldIn, playerentity, stack, this.getTier());
                    spear_entity.shootFromRotation(playerentity, playerentity.xRot,
                            playerentity.yRot, 0.0F, 2.5F * 0.5F, 1.0F);
                    if (playerentity.abilities.instabuild) {
                        spear_entity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                    }

                    worldIn.addFreshEntity(spear_entity);
                    worldIn.playSound(null, spear_entity, SoundEvents.TRIDENT_THROW,
                            SoundCategory.PLAYERS, 1.0F, 1.0F);
                    if (!playerentity.abilities.instabuild) {
                        playerentity.inventory.removeItem(stack);
                    }

                }

                playerentity.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @ParametersAreNonnullByDefault
    @Nonnull
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
    @ParametersAreNonnullByDefault
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    @ParametersAreNonnullByDefault
    public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double) state.getDestroySpeed(worldIn, pos) != 0.0D) {
            stack.hurtAndBreak(2, entityLiving, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }

    @ParametersAreNonnullByDefault
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        // Why does this method exist?
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    // Reach stuff...
    @Override
    public Multimap<Attribute, AttributeModifier> getCustomAttributesField() {
        return this.customAttributes;
    }

    @Override
    public void setCustomAttributesField(Multimap<Attribute, AttributeModifier> value) {
        this.customAttributes = value;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> execSuperGetAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        //This smells like cqr code...
   		/*
        ClientPlayerEntity player = (ClientPlayerEntity) stack.getAttachedEntity();
        if (slot == EquipmentSlotType.MAINHAND) {
            multimap.put(player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getAttribute(),
                new AttributeModifier(REACH_DISTANCE_MODIFIER, "Weapon Modifier", this.reachDistanceBonus,
                AttributeModifier.Operation.ADDITION));
        }
        */
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return ICustomReachItem.super.getAttributeModifiers(slot, stack);
    }

    @Override
    public double getReachDistanceBonus() {
        return 1;
    }

    @Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, value = Dist.CLIENT)
    private static class EventHandler {

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
            RayTraceResult blockMouseOver = entity.pick(blockReachDistance, partialTicks, false);

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

                /*
                RayTraceResult rayTraceResult = aabb1.rayTrace(eyeVec, endVec);
                if (rayTraceResult == null) {
                    continue;
                }

                double dist = eyeVec.squareDistanceTo(rayTraceResult.getHitVec());
                if (dist < minSqr) {
                    pointedEntity = entity2;
                    minSqr = dist;
                }
                */
            }

            if (pointedEntity != null && (mc.hitResult == null || pointedEntity != null && (mc.hitResult.getType() == RayTraceResult.Type.ENTITY))) {
                Method ensureHasSentCarriedItem = ObfuscationReflectionHelper.findMethod(PlayerController.class, "ensureHasSentCarriedItem");
                ensureHasSentCarriedItem.setAccessible(true);
                try {
                    ensureHasSentCarriedItem.invoke(mc.gameMode);
                } catch (Exception e) {
                    // I've failed.
                    e.printStackTrace();
                }
                //NETWORK.sendToServer(new PacketAttackEntity(pointedEntity));

                if (mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
                    mc.player.attack(pointedEntity);
                    mc.player.resetAttackStrengthTicker();
                }
            }
        }

    }
    
    @Override
    public int getEnchantmentValue() {
    	return ITieredItem.super.getEnchantmentValue();
    }
    
    @Override
    public boolean isValidRepairItem(ItemStack p_82789_1_, ItemStack p_82789_2_) {
    	return ITieredItem.super.isValidRepairItem(p_82789_1_, p_82789_2_) || super.isValidRepairItem(p_82789_1_, p_82789_2_);
    }

	@Override
	public IItemTier getTier() {
		return this.tier;
	}

}
