package com.milamber_brass.brass_armory.item;


import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.SpearEntity;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.abstracts.AbstractThrownWeaponItem;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

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

    @Override //Deal bonus damage when attacking
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!entity.level().isClientSide && entity instanceof LivingEntity living) {
            ArmoryUtil.impaleLivingEntity(living, (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE), player.level().random);
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
