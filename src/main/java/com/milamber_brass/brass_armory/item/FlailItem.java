package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.entity.projectile.FlailHeadEntity;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import com.milamber_brass.brass_armory.item.interfaces.ICustomAnimationItem;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class FlailItem extends AbstractThrownWeaponItem implements ICustomAnimationItem {
    protected final SpikyBallItem head;
    protected final float attackDamage;

    public FlailItem(Tier tier, float attackDamageIn, SpikyBallItem head, Properties properties) {
        super(tier, attackDamageIn, -2.6F, 10F, 1F, properties);
        this.head = head;
        this.attackDamage = attackDamageIn + tier.getAttackDamageBonus();
    }

    public SpikyBallItem getHead() {
        return this.head;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    protected ItemAttributeModifiers.Builder setDefaultModifiers(float attackDamage, float attackSpeed) {
        return null;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return this.isExtended(player, player.getItemInHand(hand)) ? InteractionResultHolder.fail(player.getItemInHand(hand)) : super.use(level, player, hand);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        stack.hurtAndBreak(1, entityLiving, EquipmentSlot.MAINHAND);
        super.releaseUsing(stack, level, entityLiving, timeLeft);
    }

    @Override
    public @NotNull SoundEvent throwSound() {
        return BrassArmorySounds.FLAIL_THROW.get();
    }

    public boolean isExtended(LivingEntity living, ItemStack stack) {
        List<Entity> flails = living.level().getEntities(living, living.getBoundingBox().inflate(20), entity -> entity instanceof FlailHeadEntity);
        for (Entity entity : flails) {
            if (entity instanceof FlailHeadEntity flailHead && flailHead.getOwner() == living && ItemStack.isSameItemSameComponents(flailHead.getItem(), stack)) return true;
        }
        return false;
    }

    @Override
    protected boolean shrinkItem() {
        return false;
    }

    @NotNull
    @Override
    protected AbstractThrownWeaponEntity getEntity(Level level, LivingEntity living, ItemStack weaponStack) {
        return new FlailHeadEntity(living, level, weaponStack);
    }

    @Override
    public float getChargeDuration(ItemStack itemStack) {
        return this.chargeDuration;
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return super.supportsEnchantment(stack, enchantment) && !enchantment.is(Enchantments.LOYALTY);
    }
}