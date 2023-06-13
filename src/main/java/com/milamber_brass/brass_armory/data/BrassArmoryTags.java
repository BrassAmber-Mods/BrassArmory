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
        public static final TagKey<Item> BOMB = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("bomb"));
        public static final TagKey<Item> FLINTLOCK = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("flintlock"));
        public static final TagKey<Item> FLINTLOCK_AMMO = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("flintlock_ammo"));
        public static final TagKey<Item> FLINTLOCK_POWDER = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("flintlock_powder"));
        public static final TagKey<Item> FUSE_LIGHTER = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("fuse_lighter"));
        public static final TagKey<Item> CANNON_AMMO = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("cannon_ammo"));
        public static final TagKey<Item> BLEEDING_EDGE = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("bleeding_edge"));

        public static final TagKey<Item> BUILDING_BLOCKS = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("building_blocks"));
        public static final TagKey<Item> REDSTONE = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("redstone"));
        public static final TagKey<Item> MISC = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("misc"));
        public static final TagKey<Item> EQUIPMENT = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("equipment"));

        public Items(DataGenerator generator, BlockTagsProvider blockTagsProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(generator, blockTagsProvider, modId, existingFileHelper);
        }

        @Override
        public @NotNull String getName() {
            return "Brass's Armory Item Tags";
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void addTags() {
            tag(BOMB).add(
                    BrassArmoryItems.BOMB.get(),
                    BrassArmoryItems.BOUNCY_BOMB.get(),
                    BrassArmoryItems.STICKY_BOMB.get()
            );

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
                    BrassArmoryItems.CONFUSION_ARROW.get(),
                    BrassArmoryItems.TORCH_ARROW.get()
            );

            tag(FLINTLOCK).add(
                    BrassArmoryItems.FLINTLOCK_PISTOL.get(),
                    BrassArmoryItems.MUSKET.get(),
                    BrassArmoryItems.BLUNDERBUSS.get()
            );

            tag(FLINTLOCK_AMMO).add(
                    BrassArmoryItems.MUSKET_BALL.get(),
                    BrassArmoryItems.BUNDLE_SHOT.get(),
                    net.minecraft.world.item.Items.GRAVEL
            );

            tag(FLINTLOCK_POWDER).add(
                    net.minecraft.world.item.Items.GUNPOWDER,
                    net.minecraft.world.item.Items.BLAZE_POWDER
            );

            tag(FUSE_LIGHTER).add(
                    net.minecraft.world.item.Items.FLINT_AND_STEEL,
                    net.minecraft.world.item.Items.FIRE_CHARGE,
                    net.minecraft.world.item.Items.TORCH,
                    BrassArmoryItems.FIRE_ARROW.get(),
                    BrassArmoryItems.FIRE_ROD.get()
            );

            tag(CANNON_AMMO).add(
                    BrassArmoryItems.CANNON_BALL.get(),
                    BrassArmoryItems.CARCASS_ROUND.get(),
                    BrassArmoryItems.SIEGE_ROUND.get()
            ).addTags(
                    BOMB
            );

            tag(ItemTags.PIGLIN_LOVED).add(
                    BrassArmoryItems.GOLDEN_DAGGER.get(),
                    BrassArmoryItems.GOLDEN_SPIKY_BALL.get(),
                    BrassArmoryItems.GOLDEN_BATTLEAXE.get(),
                    BrassArmoryItems.GOLDEN_BOOMERANG.get(),
                    BrassArmoryItems.GOLDEN_FLAIL.get(),
                    BrassArmoryItems.GOLDEN_HALBERD.get(),
                    BrassArmoryItems.GOLDEN_MACE.get(),
                    BrassArmoryItems.GOLDEN_SPEAR.get()
            );

            tag(MISC).add(
                    BrassArmoryBlocks.EXPLORERS_ROPE_BLOCK.get().asItem(),
                    BrassArmoryItems.PETTY_WARP_CRYSTAL.get(),
                    BrassArmoryItems.LESSER_WARP_CRYSTAL.get(),
                    BrassArmoryItems.COMMON_WARP_CRYSTAL.get(),
                    BrassArmoryItems.GREATER_WARP_CRYSTAL.get(),
                    BrassArmoryItems.GRAND_WARP_CRYSTAL.get(),
                    BrassArmoryItems.GUN_STOCK.get(),
                    BrassArmoryItems.MUSKET_PARTS.get(),
                    BrassArmoryItems.BLUNDERBUSS_PARTS.get()
            );

            tag(EQUIPMENT).add(
                    BrassArmoryItems.DIRT_ARROW.get(),
                    BrassArmoryItems.EX_ARROW.get(),
                    BrassArmoryItems.FROST_ARROW.get(),
                    BrassArmoryItems.GRASS_ARROW.get(),
                    BrassArmoryItems.LASER_ARROW.get(),
                    BrassArmoryItems.ROPE_ARROW.get(),
                    BrassArmoryItems.SLIME_ARROW.get(),
                    BrassArmoryItems.WARP_ARROW.get(),
                    BrassArmoryItems.FIRE_ARROW.get(),
                    BrassArmoryItems.CONFUSION_ARROW.get(),
                    BrassArmoryItems.TORCH_ARROW.get(),

                    BrassArmoryItems.WOODEN_BOOMERANG.get(),
                    BrassArmoryItems.GOLDEN_BOOMERANG.get(),
                    BrassArmoryItems.STONE_BOOMERANG.get(),
                    BrassArmoryItems.IRON_BOOMERANG.get(),
                    BrassArmoryItems.DIAMOND_BOOMERANG.get(),
                    BrassArmoryItems.NETHERITE_BOOMERANG.get(),

                    BrassArmoryItems.BOMB.get(),
                    BrassArmoryItems.STICKY_BOMB.get(),
                    BrassArmoryItems.BOUNCY_BOMB.get(),

                    BrassArmoryItems.WOODEN_SPEAR.get(),
                    BrassArmoryItems.GOLDEN_SPEAR.get(),
                    BrassArmoryItems.STONE_SPEAR.get(),
                    BrassArmoryItems.IRON_SPEAR.get(),
                    BrassArmoryItems.DIAMOND_SPEAR.get(),
                    BrassArmoryItems.NETHERITE_SPEAR.get(),

                    BrassArmoryItems.FIRE_ROD.get(),

                    BrassArmoryItems.WOODEN_DAGGER.get(),
                    BrassArmoryItems.GOLDEN_DAGGER.get(),
                    BrassArmoryItems.STONE_DAGGER.get(),
                    BrassArmoryItems.IRON_DAGGER.get(),
                    BrassArmoryItems.DIAMOND_DAGGER.get(),
                    BrassArmoryItems.NETHERITE_DAGGER.get(),

                    BrassArmoryItems.WOODEN_BATTLEAXE.get(),
                    BrassArmoryItems.GOLDEN_BATTLEAXE.get(),
                    BrassArmoryItems.STONE_BATTLEAXE.get(),
                    BrassArmoryItems.IRON_BATTLEAXE.get(),
                    BrassArmoryItems.DIAMOND_BATTLEAXE.get(),
                    BrassArmoryItems.NETHERITE_BATTLEAXE.get(),

                    BrassArmoryItems.WOODEN_HALBERD.get(),
                    BrassArmoryItems.GOLDEN_HALBERD.get(),
                    BrassArmoryItems.STONE_HALBERD.get(),
                    BrassArmoryItems.IRON_HALBERD.get(),
                    BrassArmoryItems.DIAMOND_HALBERD.get(),
                    BrassArmoryItems.NETHERITE_HALBERD.get(),

                    BrassArmoryItems.WOODEN_MACE.get(),
                    BrassArmoryItems.GOLDEN_MACE.get(),
                    BrassArmoryItems.STONE_MACE.get(),
                    BrassArmoryItems.IRON_MACE.get(),
                    BrassArmoryItems.DIAMOND_MACE.get(),
                    BrassArmoryItems.NETHERITE_MACE.get(),

                    BrassArmoryItems.WOODEN_SPIKY_BALL.get(),
                    BrassArmoryItems.GOLDEN_SPIKY_BALL.get(),
                    BrassArmoryItems.STONE_SPIKY_BALL.get(),
                    BrassArmoryItems.IRON_SPIKY_BALL.get(),
                    BrassArmoryItems.DIAMOND_SPIKY_BALL.get(),
                    BrassArmoryItems.NETHERITE_SPIKY_BALL.get(),

                    BrassArmoryItems.WOODEN_FLAIL.get(),
                    BrassArmoryItems.GOLDEN_FLAIL.get(),
                    BrassArmoryItems.STONE_FLAIL.get(),
                    BrassArmoryItems.IRON_FLAIL.get(),
                    BrassArmoryItems.DIAMOND_FLAIL.get(),
                    BrassArmoryItems.NETHERITE_FLAIL.get(),

                    BrassArmoryItems.FLINTLOCK_PISTOL.get(),
                    BrassArmoryItems.MUSKET.get(),
                    BrassArmoryItems.BLUNDERBUSS.get(),
                    BrassArmoryItems.MUSKET_BALL.get(),
                    BrassArmoryItems.BUNDLE_SHOT.get(),
                    BrassArmoryItems.CANNON.get(),
                    BrassArmoryItems.CANNON_BALL.get(),
                    BrassArmoryItems.CARCASS_ROUND.get(),
                    BrassArmoryItems.SIEGE_ROUND.get(),

                    BrassArmoryItems.LONGBOW.get(),
                    BrassArmoryItems.KATANA.get(),
                    BrassArmoryItems.GLIDER.get(),
                    BrassArmoryItems.QUIVER.get()
            );
        }
    }

    public static class Entities extends EntityTypeTagsProvider {
        public Entities(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(generator, modId, existingFileHelper);
        }

        public static final TagKey<EntityType<?>> WITHER = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, BrassArmory.locate("wither"));
        public static final TagKey<EntityType<?>> FOCUSED = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, BrassArmory.locate("focused"));

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

            tag(FOCUSED).add(
                    EntityType.WITHER,
                    EntityType.ENDER_DRAGON
            );
        }
    }
}
