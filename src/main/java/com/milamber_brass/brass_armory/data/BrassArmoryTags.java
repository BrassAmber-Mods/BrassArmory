package com.milamber_brass.brass_armory.data;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryBlocks;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BrassArmoryTags {
    public static class Blocks extends BlockTagsProvider {
        public Blocks(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(generator, modId, existingFileHelper);
        }

        @Override
        public @NotNull String getName() {
            return "Brass's Armory Block Tags";
        }

        @Override
        protected void addTags() {
            tag(BlockTags.CLIMBABLE).add(BrassArmoryBlocks.EXPLORERS_ROPE_BLOCK.get());
        }
    }

    public static class Items extends ItemTagsProvider {
        public static final TagKey<Item> FLINTLOCK_AMMO = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("flintlock_ammo"));
        public static final TagKey<Item> BLUNDERBUSS_AMMO = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("blunderbuss_ammo"));
        public static final TagKey<Item> BLEEDING_EDGE = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("bleeding_edge"));

        public Items(DataGenerator generator, BlockTagsProvider blockTagsProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(generator, blockTagsProvider, modId, existingFileHelper);
        }

        @Override
        public @NotNull String getName() {
            return "Brass's Armory Item Tags";
        }

        @Override
        protected void addTags() {
            tag(FLINTLOCK_AMMO).add(BrassArmoryItems.MUSKET_BALL.get());
            tag(BLUNDERBUSS_AMMO).add(BrassArmoryItems.BUNDLE_SHOT.get());

            tag(BLEEDING_EDGE).add(
                    BrassArmoryItems.KATANA.get(),
                    BrassArmoryItems.GOLDEN_DAGGER.get(),
                    BrassArmoryItems.WOODEN_DAGGER.get(),
                    BrassArmoryItems.STONE_DAGGER.get(),
                    BrassArmoryItems.IRON_DAGGER.get(),
                    BrassArmoryItems.DIAMOND_DAGGER.get(),
                    BrassArmoryItems.NETHERITE_DAGGER.get()
            );

            tag(ItemTags.ARROWS).add(
                    BrassArmoryItems.DIRT_ARROW.get(),
                    BrassArmoryItems.EX_ARROW.get(),
                    BrassArmoryItems.FROST_ARROW.get(),
                    BrassArmoryItems.GRASS_ARROW.get(),
                    BrassArmoryItems.LASER_ARROW.get(),
                    BrassArmoryItems.ROPE_ARROW.get(),
                    BrassArmoryItems.SLIME_ARROW.get(),
                    BrassArmoryItems.WARP_ARROW.get(),
                    BrassArmoryItems.FIRE_ARROW.get(),
                    BrassArmoryItems.CONCUSSION_ARROW.get()
            );
        }
    }

    public static class Entities extends EntityTypeTagsProvider {
        public Entities(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(generator, modId, existingFileHelper);
        }

        public static final TagKey<EntityType<?>> WITHER = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, BrassArmory.locate("wither"));
        @Override

        public @NotNull String getName() {
            return "Brass's Armory Entity Tags";
        }

        @Override
        protected void addTags() {
            tag(WITHER).add(
                    EntityType.WITHER,
                    EntityType.WITHER_SKELETON
            );
        }
    }
}
