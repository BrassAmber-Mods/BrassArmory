package com.milamber_brass.brass_armory.item;

import net.minecraft.util.Unit;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.neoforge.common.Tags;

public class GliderItem extends Item {
    public GliderItem(Properties properties) {
        super(properties.component(DataComponents.GLIDER, Unit.INSTANCE)
                .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.CHEST)
                        .setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA).setAsset(net.minecraft.resources.ResourceKey.create(EquipmentAssets.ROOT_ID, com.milamber_brass.brass_armory.BrassArmory.locate("glider")))
                        .setDamageOnHurt(false).build()).repairable(Tags.Items.LEATHERS));
    }
}
