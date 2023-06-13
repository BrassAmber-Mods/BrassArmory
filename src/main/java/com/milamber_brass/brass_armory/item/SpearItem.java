package com.milamber_brass.brass_armory.item;


import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.entity.projectile.SpearEntity;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpearItem extends AbstractThrownWeaponItem {
    protected final float attackDamage;
    protected final float attackSpeed;
    protected final float attackReachBonus;

    public SpearItem(Tier tier, int attackDamage, float attackSpeed, float attackReachBonus, Properties properties) {
        super(tier, attackDamage, attackSpeed, 16F, 1.25F, properties);
        this.attackDamage = (float)attackDamage + tier.getAttackDamageBonus();
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

    @Override //Deal bonus damage when attacking
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!entity.level.isClientSide && entity instanceof LivingEntity living) {
            ArmoryUtil.impaleLivingEntity(living, (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE), player.level.random);
        }
        return false;
    }

    @Override
    public @NotNull SoundEvent throwSound() {
        return BrassArmorySounds.SPEAR_THROW.get();
    }

    @NotNull
    @Override
    protected AbstractThrownWeaponEntity getEntity(Level level, LivingEntity living, ItemStack weaponStack) {
        return new SpearEntity(level, living, weaponStack);
    }
}
