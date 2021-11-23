package com.milamber_brass.brass_armory.util;

import com.milamber_brass.brass_armory.BrassArmoryItems;
import com.milamber_brass.brass_armory.items.BABaseArrowItem;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
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
    private final String name;

    ArrowType(double damageIn, String nameIn) {
        this.damage = damageIn;
        this.name = nameIn;
    }

    @Nullable
    public static BABaseArrowItem getModItemFor(ArrowType arrowType) {
        switch (arrowType) {
            case DIRT:
                return BrassArmoryItems.DIRT_ARROW.get();
            case EXPLOSION:
                return BrassArmoryItems.EX_ARROW.get();
            case FROST:
                return BrassArmoryItems.FROST_ARROW.get();
            case GRASS:
                return BrassArmoryItems.GRASS_ARROW.get();
            case LASER:
                return BrassArmoryItems.LASER_ARROW.get();
            case ROPE:
                return BrassArmoryItems.ROPE_ARROW.get();
            case SLIME:
                return BrassArmoryItems.SLIME_ARROW.get();
            case WARP:
                return BrassArmoryItems.WARP_ARROW.get();
            case FIRE:
                return BrassArmoryItems.FIRE_ARROW.get();
            case CONCUSSION:
                return BrassArmoryItems.CONCUSSION_ARROW.get();
            default:
                return null;
        }
    }

    public static ArrowType byName(String name) {
        for (ArrowType arrowType : values()) {
            if (arrowType.name.equals(name)) {
                return arrowType;
            }
        }

        return EMPTY;
    }

    public double getDamage() {
        return this.damage;
    }

    @Nonnull
    @Override
    public String getSerializedName() {
        return this.name;
    }

}
