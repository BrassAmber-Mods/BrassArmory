package com.milamber_brass.brass_armory.entity.bomb;

import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public enum BombType {
    NORMAL(0.3D, 0.2F),
    BOUNCY(1D,  1.1F),
    STICKY(0D, 0.9F);

    private final double bounceMultiplier;
    private final float volumeMultiplier;

    BombType(double bounceMultiplier, float volumeMultiplier) {
        this.bounceMultiplier = bounceMultiplier;
        this.volumeMultiplier = volumeMultiplier;
    }

    public double getBounceMultiplier() {
        return this.bounceMultiplier;
    }

    public float getVolumeMultiplier() {
        return this.volumeMultiplier;
    }

    public static Item getBombItem(BombType bombType) {
        return switch (bombType) {
            case NORMAL -> BrassArmoryItems.BOMB.get();
            case BOUNCY -> BrassArmoryItems.BOUNCY_BOMB.get();
            default -> BrassArmoryItems.STICKY_BOMB.get();
        };
    }

    public static BombEntity playerBombEntityFromType(BombType bombType, Level level, Player player, HumanoidArm humanoidarm) {
        return switch (bombType) {
            case NORMAL -> new BombEntity(level, player, humanoidarm);
            case BOUNCY -> new BouncyBombEntity(level, player, humanoidarm);
            default -> new StickyBombEntity(level, player, humanoidarm);
        };
    }

    public static BombEntity vec3BombEntityFromType(BombType bombType, Level level, double x, double y, double z) {
        return switch (bombType) {
            case NORMAL -> new BombEntity(level, x, y, z);
            case BOUNCY -> new BouncyBombEntity(level, x, y, z);
            default -> new StickyBombEntity(level, x, y, z);
        };
    }
}