package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.ArrowType;
import com.milamber_brass.brass_armory.item.*;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Bus.MOD)
public class BrassArmoryItems {

    // Item Registry
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BrassArmory.MOD_ID);

    //------------------------------------RANGED----------------------------------------------------------

    public static final RegistryObject<BABaseArrowItem> DIRT_ARROW =
            REGISTRY.register("dirt_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().rarity(Rarity.COMMON).tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.DIRT));

    public static final RegistryObject<BABaseArrowItem> EX_ARROW =
            REGISTRY.register("ex_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().rarity(Rarity.RARE).tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.EXPLOSION));

    public static final RegistryObject<BABaseArrowItem> FROST_ARROW =
            REGISTRY.register("frost_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().rarity(Rarity.UNCOMMON).tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.FROST));

    public static final RegistryObject<BABaseArrowItem> GRASS_ARROW =
            REGISTRY.register("grass_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().rarity(Rarity.COMMON).tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.GRASS));

    public static final RegistryObject<BABaseArrowItem> LASER_ARROW =
            REGISTRY.register("laser_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().rarity(Rarity.RARE).tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.LASER));

    public static final RegistryObject<BABaseArrowItem> ROPE_ARROW =
            REGISTRY.register("rope_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().rarity(Rarity.UNCOMMON).tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.ROPE));

    public static final RegistryObject<BABaseArrowItem> SLIME_ARROW =
            REGISTRY.register("slime_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().rarity(Rarity.UNCOMMON).tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.SLIME));

    public static final RegistryObject<BABaseArrowItem> WARP_ARROW =
            REGISTRY.register("warp_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().rarity(Rarity.EPIC).tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.WARP));

    public static final RegistryObject<BABaseArrowItem> FIRE_ARROW =
            REGISTRY.register("fire_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().rarity(Rarity.UNCOMMON).tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.FIRE));

    public static final RegistryObject<BABaseArrowItem> CONCUSSION_ARROW =
            REGISTRY.register("concussion_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().rarity(Rarity.UNCOMMON).tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.CONCUSSION));

    //               --------------------------BOOMERANG-------------------------------

    public static final RegistryObject<BowItem> WOOD_BOOMERANG =
            REGISTRY.register("wood_boomerang", () ->
                    new BowItem(new Item.Properties().rarity(Rarity.COMMON).durability(384).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BowItem> GOLD_BOOMERANG =
            REGISTRY.register("gold_boomerang", () ->
                    new BowItem(new Item.Properties().rarity(Rarity.COMMON).durability(384).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BowItem> STONE_BOOMERANG =
            REGISTRY.register("stone_boomerang", () ->
                    new BowItem(new Item.Properties().rarity(Rarity.COMMON).durability(384).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BowItem> IRON_BOOMERANG =
            REGISTRY.register("iron_boomerang", () ->
                    new BowItem(new Item.Properties().rarity(Rarity.COMMON).durability(384).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BowItem> DIAMOND_BOOMERANG =
            REGISTRY.register("diamond_boomerang", () ->
                    new BowItem(new Item.Properties().rarity(Rarity.COMMON).durability(384).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BowItem> NETHERITE_BOOMERANG =
            REGISTRY.register("netherite_boomerang", () ->
                    new BowItem(new Item.Properties().rarity(Rarity.COMMON).durability(384).tab(BrassArmoryItemGroups.BRASS_ARMORY)));


    //------------------------------------BOMB-----------------------------------------------------------

    public static final RegistryObject<BombItem> BOMB =
            REGISTRY.register("bomb", () ->
                    new BombItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(16).tab(BrassArmoryItemGroups.BRASS_ARMORY), 0));

    public static final RegistryObject<BombItem> BOUNCY_BOMB =
            REGISTRY.register("bouncy_bomb", () ->
                    new BombItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(16).tab(BrassArmoryItemGroups.BRASS_ARMORY), 1));

    public static final RegistryObject<BombItem> STICKY_BOMB =
            REGISTRY.register("sticky_bomb", () ->
                    new BombItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(16).tab(BrassArmoryItemGroups.BRASS_ARMORY), 2));


    //------------------------------------MELEE-----------------------------------------------------------

    public static final RegistryObject<SpearItem> GOLD_SPEAR =
            REGISTRY.register("gold_spear", () -> new SpearItem(Tiers.GOLD, 2,
                    new Item.Properties().rarity(Rarity.RARE)
                            .tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<SpearItem> WOOD_SPEAR =
            REGISTRY.register("wood_spear", () -> new SpearItem(Tiers.WOOD, 2,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<SpearItem> STONE_SPEAR =
            REGISTRY.register("stone_spear", () -> new SpearItem(Tiers.STONE, 3,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<SpearItem> IRON_SPEAR =
            REGISTRY.register("iron_spear", () -> new SpearItem(Tiers.IRON, 4,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(250).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<SpearItem> DIAMOND_SPEAR =
            REGISTRY.register("diamond_spear", () -> new SpearItem(Tiers.DIAMOND, 5,
                    new Item.Properties().rarity(Rarity.RARE)
                            .durability(1562).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<SpearItem> NETHERITE_SPEAR =
            REGISTRY.register("netherite_spear", () -> new SpearItem(Tiers.NETHERITE, 5,
                    new Item.Properties().rarity(Rarity.EPIC)
                            .durability(2031).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<DaggerItem> GOLD_DAGGER =
            REGISTRY.register("gold_dagger", () -> new DaggerItem(Tiers.GOLD, 2,
                    new Item.Properties().rarity(Rarity.RARE)
                            .durability(32).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<DaggerItem> WOOD_DAGGER =
            REGISTRY.register("wood_dagger", () -> new DaggerItem(Tiers.WOOD, 2,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .durability(59).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<DaggerItem> STONE_DAGGER =
            REGISTRY.register("stone_dagger", () -> new DaggerItem(Tiers.STONE, 3,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .durability(131).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<DaggerItem> IRON_DAGGER =
            REGISTRY.register("iron_dagger", () -> new DaggerItem(Tiers.IRON, 4,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(250).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<DaggerItem> DIAMOND_DAGGER =
            REGISTRY.register("diamond_dagger", () -> new DaggerItem(Tiers.DIAMOND, 5,
                    new Item.Properties().rarity(Rarity.RARE)
                            .durability(1562).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<DaggerItem> NETHERITE_DAGGER =
            REGISTRY.register("netherite_dagger", () -> new DaggerItem(Tiers.NETHERITE, 5,
                    new Item.Properties().rarity(Rarity.EPIC)
                            .defaultDurability(2031).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<BattleaxeItem> GOLD_BATTLEAXE =
            REGISTRY.register("gold_battleaxe", () -> new BattleaxeItem(Tiers.GOLD, 6,
                    new Item.Properties().rarity(Rarity.RARE)
                            .durability(32).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BattleaxeItem> WOOD_BATTLEAXE =
            REGISTRY.register("wood_battleaxe", () -> new BattleaxeItem(Tiers.WOOD, 6,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .durability(59).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BattleaxeItem> STONE_BATTLEAXE =
            REGISTRY.register("stone_battleaxe", () -> new BattleaxeItem(Tiers.STONE, 6,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .durability(131).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BattleaxeItem> IRON_BATTLEAXE =
            REGISTRY.register("iron_battleaxe", () -> new BattleaxeItem(Tiers.IRON, 6,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(250).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BattleaxeItem> DIAMOND_BATTLEAXE =
            REGISTRY.register("diamond_battleaxe", () -> new BattleaxeItem(Tiers.DIAMOND, 6,
                    new Item.Properties().rarity(Rarity.RARE)
                            .durability(1562).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BattleaxeItem> NETHERITE_BATTLEAXE =
            REGISTRY.register("netherite_battleaxe", () -> new BattleaxeItem(Tiers.NETHERITE, 6,
                    new Item.Properties().rarity(Rarity.EPIC)
                            .defaultDurability(2031).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<HalberdItem> GOLD_HALBERD =
            REGISTRY.register("gold_halberd", () -> new HalberdItem(Tiers.GOLD, 4,
                    new Item.Properties().rarity(Rarity.RARE)
                            .durability(32).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<HalberdItem> WOOD_HALBERD =
            REGISTRY.register("wood_halberd", () -> new HalberdItem(Tiers.WOOD, 4,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .durability(59).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<HalberdItem> STONE_HALBERD =
            REGISTRY.register("stone_halberd", () -> new HalberdItem(Tiers.STONE, 5,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .durability(131).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<HalberdItem> IRON_HALBERD =
            REGISTRY.register("iron_halberd", () -> new HalberdItem(Tiers.IRON, 6,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(250).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<HalberdItem> DIAMOND_HALBERD =
            REGISTRY.register("diamond_halberd", () -> new HalberdItem(Tiers.DIAMOND, 7,
                    new Item.Properties().rarity(Rarity.RARE)
                            .durability(1562).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<HalberdItem> NETHERITE_HALBERD =
            REGISTRY.register("netherite_halberd", () -> new HalberdItem(Tiers.NETHERITE, 7,
                    new Item.Properties().rarity(Rarity.EPIC)
                            .defaultDurability(2031).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<MaceItem> GOLD_MACE =
            REGISTRY.register("gold_mace", () -> new MaceItem(Tiers.GOLD, 3,
                    new Item.Properties().rarity(Rarity.RARE)
                            .durability(32).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<MaceItem> WOOD_MACE =
            REGISTRY.register("wood_mace", () -> new MaceItem(Tiers.WOOD, 3,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .durability(59).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<MaceItem> STONE_MACE =
            REGISTRY.register("stone_mace", () -> new MaceItem(Tiers.STONE, 4,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .durability(131).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<MaceItem> IRON_MACE =
            REGISTRY.register("iron_mace", () -> new MaceItem(Tiers.IRON, 5,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(250).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<MaceItem> DIAMOND_MACE =
            REGISTRY.register("diamond_mace", () -> new MaceItem(Tiers.DIAMOND, 6,
                    new Item.Properties().rarity(Rarity.RARE)
                            .durability(1562).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<MaceItem> NETHERITE_MACE =
            REGISTRY.register("netherite_mace", () -> new MaceItem(Tiers.NETHERITE, 6,
                    new Item.Properties().rarity(Rarity.EPIC)
                            .defaultDurability(2031).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<FlailItem> GOLD_FLAIL =
            REGISTRY.register("gold_flail", () -> new FlailItem(Tiers.GOLD, 3,
                    new Item.Properties().rarity(Rarity.RARE)
                            .durability(32).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<FlailItem> WOOD_FLAIL =
            REGISTRY.register("wood_flail", () -> new FlailItem(Tiers.WOOD, 3,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .durability(59).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<FlailItem> STONE_FLAIL =
            REGISTRY.register("stone_flail", () -> new FlailItem(Tiers.STONE, 4,
                    new Item.Properties().rarity(Rarity.COMMON)
                            .durability(131).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<FlailItem> IRON_FLAIL =
            REGISTRY.register("iron_flail", () -> new FlailItem(Tiers.IRON, 5,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(250).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<FlailItem> DIAMOND_FLAIL =
            REGISTRY.register("diamond_flail", () -> new FlailItem(Tiers.DIAMOND, 6,
                    new Item.Properties().rarity(Rarity.RARE)
                            .durability(1562).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<FlailItem> NETHERITE_FLAIL =
            REGISTRY.register("netherite_flail", () -> new FlailItem(Tiers.NETHERITE, 6,
                    new Item.Properties().rarity(Rarity.EPIC)
                            .defaultDurability(2031).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    // Attach event handler for registry.
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

}
