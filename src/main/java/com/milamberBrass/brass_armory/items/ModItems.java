package com.milamberBrass.brass_armory.items;

import com.milamberBrass.brass_armory.BrassArmory;
import com.milamberBrass.brass_armory.items.custom.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BrassArmory.MOD_ID);

    public static final RegistryObject<Spear> GOLD_SPEAR =
            ITEMS.register("gold_spear", () -> new Spear(ItemTier.GOLD, 2,
                    new Item.Properties().rarity(Rarity.RARE)
                            .group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Spear> WOOD_SPEAR =
            ITEMS.register("wood_spear", () -> new Spear(ItemTier.WOOD, 2,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Spear> STONE_SPEAR =
            ITEMS.register("stone_spear", () -> new Spear(ItemTier.STONE, 3,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Spear> IRON_SPEAR =
            ITEMS.register("iron_spear", () -> new Spear(ItemTier.IRON, 4,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .maxStackSize(1).maxDamage(250).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Spear> DIAMOND_SPEAR =
            ITEMS.register("diamond_spear", () -> new Spear(ItemTier.DIAMOND, 5,
                    new Item.Properties().rarity(Rarity.RARE)
                            .maxStackSize(1).maxDamage(1562).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Spear> NETHERITE_SPEAR =
            ITEMS.register("netherite_spear", () -> new Spear(ItemTier.NETHERITE, 5,
                    new Item.Properties().rarity(Rarity.EPIC)
                            .maxStackSize(1).maxDamage(2031).group(ModItemGroup.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<Dagger> GOLD_DAGGER =
            ITEMS.register( "gold_dagger", () -> new Dagger(ItemTier.GOLD, 2,
                    new Item.Properties().rarity(Rarity.RARE)
                            .maxStackSize(1).maxDamage(32).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Dagger> WOOD_DAGGER =
            ITEMS.register( "wood_dagger", () -> new Dagger(ItemTier.WOOD, 2,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .maxStackSize(1).maxDamage(59).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Dagger> STONE_DAGGER =
            ITEMS.register( "stone_dagger", () -> new Dagger(ItemTier.STONE, 3,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .maxStackSize(1).maxDamage(131).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Dagger> IRON_DAGGER =
            ITEMS.register( "iron_dagger", () -> new Dagger(ItemTier.IRON, 4,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .maxStackSize(1).maxDamage(250).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Dagger> DIAMOND_DAGGER =
            ITEMS.register( "diamond_dagger", () -> new Dagger(ItemTier.DIAMOND, 5,
                    new Item.Properties().rarity(Rarity.RARE)
                            .maxStackSize(1).maxDamage(1562).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Dagger> NETHERITE_DAGGER =
            ITEMS.register( "netherite_dagger", () -> new Dagger(ItemTier.NETHERITE, 5,
                    new Item.Properties().rarity(Rarity.EPIC)
                            .maxStackSize(1).defaultMaxDamage(2031).group(ModItemGroup.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<Battleaxe> GOLD_BATTLEAXE =
            ITEMS.register( "gold_battleaxe", () -> new Battleaxe(ItemTier.GOLD, 6,
                    new Item.Properties().rarity(Rarity.RARE)
                            .maxStackSize(1).maxDamage(32).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Battleaxe> WOOD_BATTLEAXE =
            ITEMS.register( "wood_battleaxe", () -> new Battleaxe(ItemTier.WOOD, 6,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .maxStackSize(1).maxDamage(59).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Battleaxe> STONE_BATTLEAXE =
            ITEMS.register( "stone_battleaxe", () -> new Battleaxe(ItemTier.STONE, 6,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .maxStackSize(1).maxDamage(131).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Battleaxe> IRON_BATTLEAXE =
            ITEMS.register( "iron_battleaxe", () -> new Battleaxe(ItemTier.IRON, 6,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .maxStackSize(1).maxDamage(250).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Battleaxe> DIAMOND_BATTLEAXE =
            ITEMS.register( "diamond_battleaxe", () -> new Battleaxe(ItemTier.DIAMOND, 6,
                    new Item.Properties().rarity(Rarity.RARE)
                            .maxStackSize(1).maxDamage(1562).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Battleaxe> NETHERITE_BATTLEAXE =
            ITEMS.register( "netherite_battleaxe", () -> new Battleaxe(ItemTier.NETHERITE, 6,
                    new Item.Properties().rarity(Rarity.EPIC)
                            .maxStackSize(1).defaultMaxDamage(2031).group(ModItemGroup.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<Halberd> GOLD_HALBERD =
            ITEMS.register( "gold_halberd", () -> new Halberd(ItemTier.GOLD, 4,
                    new Item.Properties().rarity(Rarity.RARE)
                            .maxStackSize(1).maxDamage(32).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Halberd> WOOD_HALBERD =
            ITEMS.register( "wood_halberd", () -> new Halberd(ItemTier.WOOD, 4,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .maxStackSize(1).maxDamage(59).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Halberd> STONE_HALBERD =
            ITEMS.register( "stone_halberd", () -> new Halberd(ItemTier.STONE, 5,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .maxStackSize(1).maxDamage(131).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Halberd> IRON_HALBERD =
            ITEMS.register( "iron_halberd", () -> new Halberd(ItemTier.IRON, 6,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .maxStackSize(1).maxDamage(250).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Halberd> DIAMOND_HALBERD =
            ITEMS.register( "diamond_halberd", () -> new Halberd(ItemTier.DIAMOND, 7,
                    new Item.Properties().rarity(Rarity.RARE)
                            .maxStackSize(1).maxDamage(1562).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Halberd> NETHERITE_HALBERD =
            ITEMS.register( "netherite_halberd", () -> new Halberd(ItemTier.NETHERITE, 7,
                    new Item.Properties().rarity(Rarity.EPIC)
                            .maxStackSize(1).defaultMaxDamage(2031).group(ModItemGroup.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<Mace> GOLD_MACE =
            ITEMS.register( "gold_mace", () -> new Mace(ItemTier.GOLD, 3,
                    new Item.Properties().rarity(Rarity.RARE)
                            .maxStackSize(1).maxDamage(32).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Mace> WOOD_MACE =
            ITEMS.register( "wood_mace", () -> new Mace(ItemTier.WOOD, 3,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .maxStackSize(1).maxDamage(59).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Mace> STONE_MACE =
            ITEMS.register( "stone_mace", () -> new Mace(ItemTier.STONE, 4,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .maxStackSize(1).maxDamage(131).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Mace> IRON_MACE =
            ITEMS.register( "iron_mace", () -> new Mace(ItemTier.IRON, 5,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .maxStackSize(1).maxDamage(250).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Mace> DIAMOND_MACE =
            ITEMS.register( "diamond_mace", () -> new Mace(ItemTier.DIAMOND, 6,
                    new Item.Properties().rarity(Rarity.RARE)
                            .maxStackSize(1).maxDamage(1562).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Mace> NETHERITE_MACE =
            ITEMS.register( "netherite_mace", () -> new Mace(ItemTier.NETHERITE, 6,
                    new Item.Properties().rarity(Rarity.EPIC)
                            .maxStackSize(1).defaultMaxDamage(2031).group(ModItemGroup.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<Flail> GOLD_FLAIL =
            ITEMS.register( "gold_flail", () -> new Flail(ItemTier.GOLD, 3,
                    new Item.Properties().rarity(Rarity.RARE)
                            .maxStackSize(1).maxDamage(32).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Flail> WOOD_FLAIL =
            ITEMS.register( "wood_flail", () -> new Flail(ItemTier.WOOD, 3,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .maxStackSize(1).maxDamage(59).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Flail> STONE_FLAIL =
            ITEMS.register( "stone_flail", () -> new Flail(ItemTier.STONE, 4,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .maxStackSize(1).maxDamage(131).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Flail> IRON_FLAIL =
            ITEMS.register( "iron_flail", () -> new Flail(ItemTier.IRON, 5,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .maxStackSize(1).maxDamage(250).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Flail> DIAMOND_FLAIL =
            ITEMS.register( "diamond_flail", () -> new Flail(ItemTier.DIAMOND, 6,
                    new Item.Properties().rarity(Rarity.RARE)
                            .maxStackSize(1).maxDamage(1562).group(ModItemGroup.BRASS_ARMORY)));

    public static final RegistryObject<Flail> NETHERITE_FLAIL =
            ITEMS.register( "netherite_flail", () -> new Flail(ItemTier.NETHERITE, 6,
                    new Item.Properties().rarity(Rarity.EPIC)
                            .maxStackSize(1).defaultMaxDamage(2031).group(ModItemGroup.BRASS_ARMORY)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
