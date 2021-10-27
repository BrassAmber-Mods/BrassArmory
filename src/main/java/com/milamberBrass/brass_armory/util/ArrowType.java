package com.milamberBrass.brass_armory.util;

import com.milamberBrass.brass_armory.items.ModItems;
import com.milamberBrass.brass_armory.items.custom.BABaseArrowItem;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;

public enum ArrowType implements IStringSerializable {
    EMPTY(0, "empty"),
    DIRT(0, "dirt"),
    EXPLOSION(0, "explosion"),
    FROST(2, "frost"),
    GRASS(3, "grass"),
    LASER(0, "laser"),
    ROPE(4, "rope"),
    SLIME(4, "slime"),
    WARP(4, "warp");

    private final int damage;
    private String name;

    private ArrowType(int damageIn, String nameIn) {
        this.damage = damageIn;
        this.name = nameIn;
    }

    public int getDamage() {return this.damage;}


    @Nullable
    public static BABaseArrowItem getModItemFor(ArrowType arrowType) {
        switch (arrowType) {
            case EMPTY:
            default:
            case DIRT:
                return ModItems.DIRT_ARROW.get();
            case EXPLOSION:
                return ModItems.EX_ARROW.get();
            case FROST:
                return ModItems.FROST_ARROW.get();
            case GRASS:
                return ModItems.GRASS_ARROW.get();
            case LASER:
                return ModItems.LASER_ARROW.get();
            case ROPE:
                return ModItems.ROPE_ARROW.get();
            case SLIME:
                return ModItems.SLIME_ARROW.get();
            case WARP:
                return ModItems.WARP_ARROW.get();
        }
    }

    @Override
    public String getString() {
        return this.name;
    }
}
