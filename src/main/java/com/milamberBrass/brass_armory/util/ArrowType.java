package com.milamberBrass.brass_armory.util;

import com.milamberBrass.brass_armory.items.ModItems;
import com.milamberBrass.brass_armory.items.custom.BABaseArrowItem;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;

public enum ArrowType implements IStringSerializable {
    EMPTY(0D, "empty"),
    DIRT(1D, "dirt"),
    EXPLOSION(0D, "explosion"),
    FROST(4D, "frost"),
    GRASS(2D, "grass"),
    LASER(4D, "laser"),
    ROPE(2D, "rope"),
    SLIME(4D, "slime"),
    WARP(1D, "warp"),
    FIRE(4D, "fire"),
    CONCUSSION(4D, "concuss");

    private final double damage;
    private String name;

    private ArrowType(double damageIn, String nameIn) {
        this.damage = damageIn;
        this.name = nameIn;
    }


    public double getDamage() {return this.damage;}


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
            case FIRE:
            	return ModItems.FIRE_ARROW.get();
            case CONCUSSION:
            	return ModItems.CONCUSSION_ARROW.get();
        }
    }
    
    public static ArrowType byName(String name) {
        for(ArrowType arrowType : values()) {
           if (arrowType.name.equals(name)) {
              return arrowType;
           }
        }

        return EMPTY;
     }

    @Override
    public String getSerializedName() {
        return this.name;
    }

}
