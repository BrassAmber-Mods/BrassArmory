package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BrassArmoryTags {
    public static class Items {
        public static final TagKey<Item> FLINTLOCK_AMMO = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("flintlock_ammo"));
        public static final TagKey<Item> BLUNDERBUSS_AMMO = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("blunderbuss_ammo"));
    }
}
