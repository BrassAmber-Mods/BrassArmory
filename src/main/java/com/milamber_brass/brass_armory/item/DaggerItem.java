package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.DaggerEntity;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import com.milamber_brass.brass_armory.item.interfaces.ICustomAnimationItem;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

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
    protected ItemAttributeModifiers.Builder setDefaultModifiers(float attackDamage, float attackSpeed) {
        return null;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        if (this.defaultModifiers == null) {
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, this.attackDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            builder.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, this.attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            builder.add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(BrassArmory.locate("attack_range"), this.attackReachBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            this.defaultModifiers = builder.build();
        }
        return super.getDefaultAttributeModifiers(stack);
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
