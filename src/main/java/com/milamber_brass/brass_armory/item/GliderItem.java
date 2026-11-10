package com.milamber_brass.brass_armory.item;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
public class GliderItem extends ElytraItem {
    public GliderItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(ItemStack glider, ItemStack repairItem) {
        return repairItem.is(Tags.Items.LEATHERS);
    }

    @Nullable
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.CHEST;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }
}
