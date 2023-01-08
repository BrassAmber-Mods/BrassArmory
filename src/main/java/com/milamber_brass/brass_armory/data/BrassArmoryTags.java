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
import net.minecraftforge.common.Tags;
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
        public static final TagKey<Item> WARP_CRYSTAL_GENERATORS = TagKey.create(Registry.ITEM_REGISTRY, BrassArmory.locate("warp_crystal_generators"));

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

            tag(WARP_CRYSTAL_GENERATORS).addTags(
                    Tags.Items.GEMS_DIAMOND,
                    Tags.Items.GEMS_EMERALD
            ).add(
                    net.minecraft.world.item.Items.ENDER_PEARL
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
