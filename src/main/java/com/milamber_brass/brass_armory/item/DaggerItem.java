package com.milamber_brass.brass_armory.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.entity.projectile.DaggerEntity;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import com.milamber_brass.brass_armory.item.interfaces.ICustomAnimationItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DaggerItem extends AbstractThrownWeaponItem implements ICustomAnimationItem {
    protected final float attackDamage;
    protected final float attackSpeed;
    protected final float attackReachBonus;

    public DaggerItem(Tier tier, float attackDamage, float attackSpeed, float attackReachBonus, Properties properties) {
        super(tier, attackDamage, attackSpeed, 10F, 1.05F, properties);
        this.attackDamage = attackDamage + tier.getAttackDamageBonus();
        this.attackSpeed = attackSpeed;
        this.attackReachBonus = attackReachBonus;
    }

    @Override
    protected ImmutableMultimap.Builder<Attribute, AttributeModifier> setDefaultModifiers(float attackDamage, float attackSpeed) {
        return null;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (this.defaultModifiers == null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon damage modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon speed modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
            builder.put(ForgeMod.ATTACK_RANGE.get(), new AttributeModifier(BASE_ATTACK_RANGE_UUID, "Weapon range modifier", this.attackReachBonus, AttributeModifier.Operation.ADDITION));
            this.defaultModifiers = builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public @NotNull SoundEvent throwSound() {
        return BrassArmorySounds.DAGGER_THROW.get();
    }

    @NotNull
    @Override
    protected AbstractThrownWeaponEntity getEntity(Level level, LivingEntity living, ItemStack weaponStack) {
        return new DaggerEntity(living, level, weaponStack);
    }

    @Override
    public float getChargeDuration(ItemStack itemStack) {
        return this.chargeDuration;
    }
}
