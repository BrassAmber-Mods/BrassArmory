package com.milamber_brass.brass_armory.util;

import com.milamber_brass.brass_armory.item.MaceItem;
import com.milamber_brass.brass_armory.item.WarpCrystalItem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ArmoryCooldownCache {
    public static List<Item> BrassArmoryMaces = new ArrayList<>();
    public static List<Item> BrassArmoryWarpCrystals = new ArrayList<>();

    public static void init() {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof MaceItem) BrassArmoryMaces.add(item);
            else if (item instanceof WarpCrystalItem) BrassArmoryWarpCrystals.add(item);
        }
    }
}
