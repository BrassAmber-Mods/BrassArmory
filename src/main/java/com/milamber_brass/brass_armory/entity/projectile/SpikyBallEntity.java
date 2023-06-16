package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.data.BrassArmoryDamageTypes;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractRollableItemProjectileEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.SpikyBallItem;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpikyBallEntity extends AbstractRollableItemProjectileEntity {
    public static final long INTERVAL = 600L;

    public SpikyBallEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public SpikyBallEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.SPIKY_BALL.get(), x, y, z, level);
    }

    public SpikyBallEntity(Level level, LivingEntity living, @Nullable HumanoidArm arm) {
        super(BrassArmoryEntityTypes.SPIKY_BALL.get(), living, level, arm);
    }

    public SpikyBallEntity(Level level, LivingEntity living) {
        this(level, living, null);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 10 && this.getItem().getItem() instanceof SpikyBallItem spikyBallItem) {
            for (Entity entity : this.level().getEntities(this, this.getBoundingBox(), Entity::isAlive)) {
                if (entity instanceof LivingEntity living && living.hurt(ArmoryUtil.getDamageSource(this.level(), BrassArmoryDamageTypes.SPIKY_BALL), spikyBallItem.getTier().getAttackDamageBonus() + 1F) && this.hurt()) return;
            }
        }
    }

    @Override
    protected void onGroundTick() {
        if (!this.level().isClientSide && this.level().getGameTime() % INTERVAL == 0L && this.hurt()) return;
        super.onGroundTick();
    }

    protected boolean hurt() {
        ItemStack stack = this.getItem().copy();
        if (stack.hurt(1, this.level().random, this.getOwner() instanceof ServerPlayer serverPlayer ? serverPlayer : null)) {
            if (this.getOwner() instanceof Player player) player.awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
            this.discard();
            return true;
        }
        this.setItem(stack);
        return false;
    }

    @NotNull
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack handStack = player.getItemInHand(hand);
        if (handStack.isEmpty()) {
            player.setItemInHand(hand, this.getItem());
            this.discard();
            return InteractionResult.SUCCESS;
        } else if (handStack.is(this.getItem().getItem()) && handStack.getCount() < handStack.getMaxStackSize()) {
            handStack.grow(1);
            this.discard();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected SoundEvent getSoundEvent() {
        return BrassArmorySounds.SPIKY_BALL_HIT.get();
    }

    @Override
    protected float getVolumeMultiplier() {
        return 0.5F;
    }

    @Override
    protected double getBounceMultiplier() {
        return 0.2D;
    }

    @Nonnull
    @Override
    protected Item getDefaultItem() {
        return BrassArmoryItems.IRON_SPIKY_BALL.get();
    }
}
