package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.SpearItem;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;

public class SpearEntity extends ThrownTrident implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(SpearEntity.class, EntityDataSerializers.ITEM_STACK);

    public SpearEntity(EntityType<? extends ThrownTrident> entityType, Level level) {
        super(entityType, level);
    }

    @SuppressWarnings("unchecked")
    public SpearEntity(Level level, LivingEntity livingEntity, ItemStack spearStack) throws Exception {
        super(BrassArmoryEntityTypes.SPEAR.get(), level);
        this.setOwner(livingEntity);
        if(livingEntity instanceof Player) this.pickup = Pickup.ALLOWED;
        this.setItem(spearStack);
        this.setPos(livingEntity.getX(), livingEntity.getEyeY() - (double)0.1F, livingEntity.getZ());

        final Field loyalty = ObfuscationReflectionHelper.findField(ThrownTrident.class, "ID_LOYALTY");
        loyalty.setAccessible(true);
        this.entityData.set((EntityDataAccessor<Byte>)loyalty.get(this), (byte)EnchantmentHelper.getLoyalty(spearStack));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    @Override//Didnt think I would have to, but then I realized that default trident on hit code always deals the same amount of damage
    @ParametersAreNonnullByDefault
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        ItemStack spearStack = this.getItem();
        SpearItem spear = (SpearItem)spearStack.getItem();

        float damage = spear.getAttackDamage();
        if (entity instanceof LivingEntity livingentity) {
            damage += EnchantmentHelper.getDamageBonus(spearStack, livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = DamageSource.trident(this, entity1 == null ? this : entity1);


        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;//TODO: CUSTOM SOUNDS
        if (entity.hurt(damagesource, damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity living) {
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(living, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, living);
                }
                this.doPostHurtEffects(living);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        this.playSound(soundevent, 1.0F, 1.0F);

        final Field dealtDamage = ObfuscationReflectionHelper.findField(ThrownTrident.class, "dealtDamage");
        dealtDamage.setAccessible(true);
        try {
            dealtDamage.set(this, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Nonnull
    protected ItemStack getPickupItem() {
        return this.getItem();
    }

    protected Item getDefaultItem() {
        return BrassArmoryItems.STONE_SPEAR.get();
    }

    protected ItemStack getItemRaw() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    @ParametersAreNonnullByDefault
    public void setItem(ItemStack spearStack) {
        if (!spearStack.is(this.getDefaultItem()) || spearStack.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, Util.make(spearStack.copy(), (itemStack) -> itemStack.setCount(1)));
        }
    }

    @Override
    @Nonnull
    public ItemStack getItem() {
        ItemStack spearStack = this.getItemRaw();
        return spearStack.isEmpty() ? new ItemStack(this.getDefaultItem()) : spearStack;
    }
}