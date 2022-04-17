package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractRollableItemProjectile;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.SpikyBallItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
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

public class SpikyBallEntity extends AbstractRollableItemProjectile {

    public SpikyBallEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public SpikyBallEntity(double x, double y, double z, Level level) {
        super(BrassArmoryEntityTypes.SPIKY_BALL.get(), x, y, z, level);
    }

    public SpikyBallEntity(LivingEntity living, Level level, @Nullable HumanoidArm arm) {
        super(BrassArmoryEntityTypes.SPIKY_BALL.get(), living, level, arm);
    }

    @Override
    public void tick() {
        super.tick();
        Item item = this.getItem().getItem();
        if (this.tickCount > 10 && item instanceof SpikyBallItem spikyBallItem) {
            for (Entity entity : this.level.getEntities(this, this.getBoundingBox(), Entity::isAlive)) {//TODO DamageSources everywhere
                if (entity instanceof LivingEntity living && living.hurt(new DamageSource("spiky_bastard_lol_this_is_a_placeholder"), spikyBallItem.getTier().getAttackDamageBonus() / 2F) && this.random.nextInt(99) < spikyBallItem.breakChance) {
                    this.discard();
                    return;
                }
            }
        }
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
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
        return BrassArmorySounds.BOMB_HIT.get();
    }

    @Override
    protected float getVolumeMultiplier() {
        return 0.1F;
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
