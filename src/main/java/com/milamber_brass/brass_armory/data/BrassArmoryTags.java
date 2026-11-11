package com.milamber_brass.brass_armory.data;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryBlocks;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.FireRodItem;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import com.milamber_brass.brass_armory.item.abstracts.ArmoryTieredItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BrassArmoryTags {
    public static class Blocks extends BlockTagsProvider {
        public Blocks(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider, BrassArmory.MOD_ID);
        }

        @Override
        public @NotNull String getName() {
            return "Brass's Armory Block Tags";
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(BlockTags.CLIMBABLE).add(BrassArmoryBlocks.EXPLORERS_ROPE_BLOCK.key());
        }
    }

    public static class Items extends TagsProvider<Item> {
        public static final TagKey<Item> BOMB = ItemTags.create(BrassArmory.locate("bomb"));
        public static final TagKey<Item> FLINTLOCK = ItemTags.create(BrassArmory.locate("flintlock"));
        public static final TagKey<Item> FLINTLOCK_AMMO = ItemTags.create(BrassArmory.locate("flintlock_ammo"));
        public static final TagKey<Item> FLINTLOCK_POWDER = ItemTags.create(BrassArmory.locate("flintlock_powder"));
        public static final TagKey<Item> FUSE_LIGHTER = ItemTags.create(BrassArmory.locate("fuse_lighter"));
        public static final TagKey<Item> CANNON_AMMO = ItemTags.create(BrassArmory.locate("cannon_ammo"));
        public static final TagKey<Item> BLEEDING_EDGE = ItemTags.create(BrassArmory.locate("bleeding_edge"));

        public static final TagKey<Item> BUILDING_BLOCKS = ItemTags.create(BrassArmory.locate("building_blocks"));
        public static final TagKey<Item> REDSTONE = ItemTags.create(BrassArmory.locate("redstone"));
        public static final TagKey<Item> MISC = ItemTags.create(BrassArmory.locate("misc"));
        public static final TagKey<Item> EQUIPMENT = ItemTags.create(BrassArmory.locate("equipment"));

        public Items(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, Registries.ITEM, lookupProvider, BrassArmory.MOD_ID);
        }

        @Override
        public @NotNull String getName() {
            return "Brass's Armory Item Tags";
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            this.tag(ItemTags.CAULDRON_CAN_REMOVE_DYE).add(BrassArmoryItems.GLIDER.key());

            for (var entry : BrassArmoryItems.REGISTRY.getEntries()) {
                Item item = entry.get();
                boolean weapon = item instanceof ArmoryTieredItem
                        && !(item instanceof FireRodItem);
                if (weapon) {
                    this.tag(ItemTags.WEAPON_ENCHANTABLE).add(entry.key());
                    this.tag(ItemTags.SHARP_WEAPON_ENCHANTABLE).add(entry.key());
                    this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(entry.key());
                    this.tag(ItemTags.VANISHING_ENCHANTABLE).add(entry.key());
                }
            }
            this.tag(ItemTags.MELEE_WEAPON_ENCHANTABLE).add(BrassArmoryItems.KATANA.key());
            this.tag(ItemTags.SWEEPING_ENCHANTABLE).add(BrassArmoryItems.KATANA.key());
            this.tag(ItemTags.BOW_ENCHANTABLE).add(BrassArmoryItems.LONGBOW.key());
            this.tag(ItemTags.DURABILITY_ENCHANTABLE).addAll(List.of(BrassArmoryItems.LONGBOW.key(), BrassArmoryItems.GLIDER.key()));
            this.tag(ItemTags.VANISHING_ENCHANTABLE).addAll(List.of(BrassArmoryItems.LONGBOW.key(), BrassArmoryItems.GLIDER.key(),
                    BrassArmoryItems.FLINTLOCK_PISTOL.key(), BrassArmoryItems.MUSKET.key(), BrassArmoryItems.BLUNDERBUSS.key()));

            this.tag(BOMB).addAll(List.of(
                    BrassArmoryItems.BOMB.key(),
                    BrassArmoryItems.BOUNCY_BOMB.key(),
                    BrassArmoryItems.STICKY_BOMB.key()
            ));

            this.tag(BLEEDING_EDGE).addAll(List.of(
                    BrassArmoryItems.KATANA.key(),
                    BrassArmoryItems.GOLDEN_DAGGER.key(),
                    BrassArmoryItems.WOODEN_DAGGER.key(),
                    BrassArmoryItems.STONE_DAGGER.key(),
                    BrassArmoryItems.IRON_DAGGER.key(),
                    BrassArmoryItems.DIAMOND_DAGGER.key(),
                    BrassArmoryItems.NETHERITE_DAGGER.key()
            ));

            this.tag(ItemTags.ARROWS).addAll(List.of(
                    BrassArmoryItems.DIRT_ARROW.key(),
                    BrassArmoryItems.EX_ARROW.key(),
                    BrassArmoryItems.FROST_ARROW.key(),
                    BrassArmoryItems.GRASS_ARROW.key(),
                    BrassArmoryItems.LASER_ARROW.key(),
                    BrassArmoryItems.ROPE_ARROW.key(),
                    BrassArmoryItems.SLIME_ARROW.key(),
                    BrassArmoryItems.WARP_ARROW.key(),
                    BrassArmoryItems.FIRE_ARROW.key(),
                    BrassArmoryItems.CONFUSION_ARROW.key(),
                    BrassArmoryItems.TORCH_ARROW.key()
            ));

            this.tag(FLINTLOCK).addAll(List.of(
                    BrassArmoryItems.FLINTLOCK_PISTOL.key(),
                    BrassArmoryItems.MUSKET.key(),
                    BrassArmoryItems.BLUNDERBUSS.key()
            ));

            this.tag(FLINTLOCK_AMMO).addAll(List.of(
                    BrassArmoryItems.MUSKET_BALL.key(),
                    BrassArmoryItems.BUNDLE_SHOT.key(),
                    net.minecraft.references.BlockItemIds.GRAVEL.item()
            ));

            this.tag(FLINTLOCK_POWDER).addAll(List.of(
                    net.minecraft.references.ItemIds.GUNPOWDER,
                    net.minecraft.references.ItemIds.BLAZE_POWDER
            ));

            this.tag(FUSE_LIGHTER).addAll(List.of(
                    net.minecraft.references.ItemIds.FLINT_AND_STEEL,
                    net.minecraft.references.ItemIds.FIRE_CHARGE,
                    net.minecraft.references.BlockItemIds.TORCH.item(),
                    BrassArmoryItems.FIRE_ARROW.key(),
                    BrassArmoryItems.FIRE_ROD.key()
            ));

            this.tag(CANNON_AMMO).addAll(List.of(
                    BrassArmoryItems.CANNON_BALL.key(),
                    BrassArmoryItems.CARCASS_ROUND.key(),
                    BrassArmoryItems.SIEGE_ROUND.key()
            )).addTag(
                    BOMB
            );

            this.tag(ItemTags.PIGLIN_LOVED).addAll(List.of(
                    BrassArmoryItems.GOLDEN_DAGGER.key(),
                    BrassArmoryItems.GOLDEN_SPIKY_BALL.key(),
                    BrassArmoryItems.GOLDEN_BATTLEAXE.key(),
                    BrassArmoryItems.GOLDEN_BOOMERANG.key(),
                    BrassArmoryItems.GOLDEN_FLAIL.key(),
                    BrassArmoryItems.GOLDEN_HALBERD.key(),
                    BrassArmoryItems.GOLDEN_MACE.key(),
                    BrassArmoryItems.GOLDEN_SPEAR.key()
            ));

            this.tag(MISC).addAll(List.of(
                    BrassArmoryItems.EXPLORERS_ROPE.key(),
                    BrassArmoryItems.PETTY_WARP_CRYSTAL.key(),
                    BrassArmoryItems.LESSER_WARP_CRYSTAL.key(),
                    BrassArmoryItems.COMMON_WARP_CRYSTAL.key(),
                    BrassArmoryItems.GREATER_WARP_CRYSTAL.key(),
                    BrassArmoryItems.GRAND_WARP_CRYSTAL.key(),
                    BrassArmoryItems.GUN_STOCK.key(),
                    BrassArmoryItems.MUSKET_PARTS.key(),
                    BrassArmoryItems.BLUNDERBUSS_PARTS.key()
            ));

            this.tag(EQUIPMENT).addAll(List.of(
                    BrassArmoryItems.DIRT_ARROW.key(),
                    BrassArmoryItems.EX_ARROW.key(),
                    BrassArmoryItems.FROST_ARROW.key(),
                    BrassArmoryItems.GRASS_ARROW.key(),
                    BrassArmoryItems.LASER_ARROW.key(),
                    BrassArmoryItems.ROPE_ARROW.key(),
                    BrassArmoryItems.SLIME_ARROW.key(),
                    BrassArmoryItems.WARP_ARROW.key(),
                    BrassArmoryItems.FIRE_ARROW.key(),
                    BrassArmoryItems.CONFUSION_ARROW.key(),
                    BrassArmoryItems.TORCH_ARROW.key(),

                    BrassArmoryItems.WOODEN_BOOMERANG.key(),
                    BrassArmoryItems.GOLDEN_BOOMERANG.key(),
                    BrassArmoryItems.STONE_BOOMERANG.key(),
                    BrassArmoryItems.IRON_BOOMERANG.key(),
                    BrassArmoryItems.DIAMOND_BOOMERANG.key(),
                    BrassArmoryItems.NETHERITE_BOOMERANG.key(),

                    BrassArmoryItems.BOMB.key(),
                    BrassArmoryItems.STICKY_BOMB.key(),
                    BrassArmoryItems.BOUNCY_BOMB.key(),

                    BrassArmoryItems.WOODEN_SPEAR.key(),
                    BrassArmoryItems.GOLDEN_SPEAR.key(),
                    BrassArmoryItems.STONE_SPEAR.key(),
                    BrassArmoryItems.IRON_SPEAR.key(),
                    BrassArmoryItems.DIAMOND_SPEAR.key(),
                    BrassArmoryItems.NETHERITE_SPEAR.key(),

                    BrassArmoryItems.FIRE_ROD.key(),

                    BrassArmoryItems.WOODEN_DAGGER.key(),
                    BrassArmoryItems.GOLDEN_DAGGER.key(),
                    BrassArmoryItems.STONE_DAGGER.key(),
                    BrassArmoryItems.IRON_DAGGER.key(),
                    BrassArmoryItems.DIAMOND_DAGGER.key(),
                    BrassArmoryItems.NETHERITE_DAGGER.key(),

                    BrassArmoryItems.WOODEN_BATTLEAXE.key(),
                    BrassArmoryItems.GOLDEN_BATTLEAXE.key(),
                    BrassArmoryItems.STONE_BATTLEAXE.key(),
                    BrassArmoryItems.IRON_BATTLEAXE.key(),
                    BrassArmoryItems.DIAMOND_BATTLEAXE.key(),
                    BrassArmoryItems.NETHERITE_BATTLEAXE.key(),

                    BrassArmoryItems.WOODEN_HALBERD.key(),
                    BrassArmoryItems.GOLDEN_HALBERD.key(),
                    BrassArmoryItems.STONE_HALBERD.key(),
                    BrassArmoryItems.IRON_HALBERD.key(),
                    BrassArmoryItems.DIAMOND_HALBERD.key(),
                    BrassArmoryItems.NETHERITE_HALBERD.key(),

                    BrassArmoryItems.WOODEN_MACE.key(),
                    BrassArmoryItems.GOLDEN_MACE.key(),
                    BrassArmoryItems.STONE_MACE.key(),
                    BrassArmoryItems.IRON_MACE.key(),
                    BrassArmoryItems.DIAMOND_MACE.key(),
                    BrassArmoryItems.NETHERITE_MACE.key(),

                    BrassArmoryItems.WOODEN_SPIKY_BALL.key(),
                    BrassArmoryItems.GOLDEN_SPIKY_BALL.key(),
                    BrassArmoryItems.STONE_SPIKY_BALL.key(),
                    BrassArmoryItems.IRON_SPIKY_BALL.key(),
                    BrassArmoryItems.DIAMOND_SPIKY_BALL.key(),
                    BrassArmoryItems.NETHERITE_SPIKY_BALL.key(),

                    BrassArmoryItems.WOODEN_FLAIL.key(),
                    BrassArmoryItems.GOLDEN_FLAIL.key(),
                    BrassArmoryItems.STONE_FLAIL.key(),
                    BrassArmoryItems.IRON_FLAIL.key(),
                    BrassArmoryItems.DIAMOND_FLAIL.key(),
                    BrassArmoryItems.NETHERITE_FLAIL.key(),

                    BrassArmoryItems.FLINTLOCK_PISTOL.key(),
                    BrassArmoryItems.MUSKET.key(),
                    BrassArmoryItems.BLUNDERBUSS.key(),
                    BrassArmoryItems.MUSKET_BALL.key(),
                    BrassArmoryItems.BUNDLE_SHOT.key(),
                    BrassArmoryItems.CANNON.key(),
                    BrassArmoryItems.CANNON_BALL.key(),
                    BrassArmoryItems.CARCASS_ROUND.key(),
                    BrassArmoryItems.SIEGE_ROUND.key(),

                    BrassArmoryItems.LONGBOW.key(),
                    BrassArmoryItems.KATANA.key(),
                    BrassArmoryItems.GLIDER.key(),
                    BrassArmoryItems.QUIVER.key()
            ));
        }
    }

    public static class Entities extends EntityTypeTagsProvider {
        public static final TagKey<EntityType<?>> WITHER = TagKey.create(Registries.ENTITY_TYPE, BrassArmory.locate("wither"));
        public static final TagKey<EntityType<?>> FOCUSED = TagKey.create(Registries.ENTITY_TYPE, BrassArmory.locate("focused"));

        public Entities(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
            super(output, future, BrassArmory.MOD_ID);
        }

        @Override
        public @NotNull String getName() {
            return "Brass's Armory Entity Tags";
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            this.tag(WITHER).addAll(List.of(
                    net.minecraft.world.entity.EntityTypeIds.WITHER,
                    net.minecraft.world.entity.EntityTypeIds.WITHER_SKELETON
            ));

            this.tag(FOCUSED).addAll(List.of(
                    net.minecraft.world.entity.EntityTypeIds.WITHER,
                    net.minecraft.world.entity.EntityTypeIds.ENDER_DRAGON
            ));
        }
    }

    public static class DamageTypes extends TagsProvider<DamageType> {
        public DamageTypes(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
            super(output, Registries.DAMAGE_TYPE, future, BrassArmory.MOD_ID);
        }

        @Override
        public @NotNull String getName() {
            return "Brass's Armory Damage Type Tags";
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            this.tag(DamageTypeTags.IS_PROJECTILE).addAll(List.of(
                    BrassArmoryDamageTypes.BATTLE_AXE,
                    BrassArmoryDamageTypes.BOOMERANG,
                    BrassArmoryDamageTypes.DAGGER,
                    BrassArmoryDamageTypes.FIRE_ROD,
                    BrassArmoryDamageTypes.FLAIL,
                    BrassArmoryDamageTypes.SPEAR
            ));

            this.tag(DamageTypeTags.BYPASSES_ARMOR).addAll(List.of(
                    BrassArmoryDamageTypes.IMPALE,
                    BrassArmoryDamageTypes.BLEED
            ));
        }
    }
}
