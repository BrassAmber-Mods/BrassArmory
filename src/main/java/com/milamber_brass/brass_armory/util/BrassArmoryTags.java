package com.milamber_brass.brass_armory.util;

import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class BrassArmoryTags {

    public static class Items {

        public static final Tags.IOptionalNamedTag<Item> DIRT_ARROW_ITEM =
                createTag("dirt_arrow");

        public static final Tags.IOptionalNamedTag<Item> EXPLOSION_ARROW_ITEM =
                createTag("ex_arrow");

        public static final Tags.IOptionalNamedTag<Item> FROST_ARROW_ITEM =
                createTag("frost_arrow");

        public static final Tags.IOptionalNamedTag<Item> GRASS_ARROW_ITEM =
                createTag("grass_arrow");

        public static final Tags.IOptionalNamedTag<Item> LASER_ARROW_ITEM =
                createTag("laser_arrow");

        public static final Tags.IOptionalNamedTag<Item> ROPE_ARROW_ITEM =
                createTag("rope_arrow");

        public static final Tags.IOptionalNamedTag<Item> SLIME_ARROW_ITEM =
                createTag("slime_arrow");

        public static final Tags.IOptionalNamedTag<Item> WARP_ARROW_ITEM =
                createTag("warp_arrow");

        public static final Tags.IOptionalNamedTag<Item> FIRE_ARROW_ITEM =
                createTag("fire_arrow");

        public static final Tags.IOptionalNamedTag<Item> CONCUSSION_ARROW_ITEM =
                createTag("concussion_arrow");

        private static Tags.IOptionalNamedTag<Item> createTag(String name) {
            return ItemTags.createOptional(new ResourceLocation(BrassArmory.MOD_ID, name));
        }

    }

}
