package com.milamber_brass.brass_armory.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.entity.projectile.FireRodEntity;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class FireRodItem extends AbstractThrownWeaponItem {
    protected final float attackDamage;
    protected final float attackSpeed;
    protected final float attackReachBonus;
    public final ParticleOptions flameParticle;

    public FireRodItem(Tier tier, int attackDamage, float attackSpeed, float attackReachBonus, ParticleOptions flameParticle, Properties properties) {
        super(tier, attackDamage, attackSpeed, 15F, 1.3F, properties);
        this.attackDamage = (float)attackDamage + tier.getAttackDamageBonus();
        this.attackSpeed = attackSpeed;
        this.attackReachBonus = attackReachBonus;
        this.flameParticle = flameParticle;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected ImmutableMultimap.Builder<Attribute, AttributeModifier> setDefaultModifiers(float attackDamage, float attackSpeed) {
        return null;
    }

    @Override
    @ParametersAreNonnullByDefault
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (this.defaultModifiers == null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon damage modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon speed modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
            builder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(BASE_ATTACK_RANGE_UUID, "Weapon range modifier", this.attackReachBonus, AttributeModifier.Operation.ADDITION));
            this.defaultModifiers = builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!entity.level.isClientSide && entity instanceof LivingEntity living) {
            living.setRemainingFireTicks(60);
        }
        return false;
    }

    @NotNull
    @Override
    protected AbstractThrownWeaponEntity getEntity(Level level, LivingEntity living, ItemStack weaponStack) {
        return new FireRodEntity(level, living, weaponStack);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }
}
