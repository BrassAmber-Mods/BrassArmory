package com.milamber_brass.brass_armory.item.abstracts;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ArmoryTieredItem extends Item {
    private final ToolMaterial material;
    public ArmoryTieredItem(ToolMaterial material, Properties properties) {
        super(properties.durability(material.durability()).repairable(material.repairItems()).enchantable(material.enchantmentValue()));
        this.material = material;
    }
    public ToolMaterial getTier() { return this.material; }
}
