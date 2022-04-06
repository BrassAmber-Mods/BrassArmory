package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.ArrowType;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombType;
import com.milamber_brass.brass_armory.item.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Bus.MOD)
public class BrassArmoryItems {

    // Item Registry
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BrassArmory.MOD_ID);

    //------------------------------------RANGED----------------------------------------------------------

    public static final RegistryObject<BABaseArrowItem> DIRT_ARROW =
            REGISTRY.register("dirt_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.DIRT));

    public static final RegistryObject<BABaseArrowItem> EX_ARROW =
            REGISTRY.register("ex_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.EXPLOSION));

    public static final RegistryObject<BABaseArrowItem> FROST_ARROW =
            REGISTRY.register("frost_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.FROST));

    public static final RegistryObject<BABaseArrowItem> GRASS_ARROW =
            REGISTRY.register("grass_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.GRASS));

    public static final RegistryObject<BABaseArrowItem> LASER_ARROW =
            REGISTRY.register("laser_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.LASER));

    public static final RegistryObject<BABaseArrowItem> ROPE_ARROW =
            REGISTRY.register("rope_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.ROPE));

    public static final RegistryObject<BABaseArrowItem> SLIME_ARROW =
            REGISTRY.register("slime_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.SLIME));

    public static final RegistryObject<BABaseArrowItem> WARP_ARROW =
            REGISTRY.register("warp_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.WARP));

    public static final RegistryObject<BABaseArrowItem> FIRE_ARROW =
            REGISTRY.register("fire_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.FIRE));

    public static final RegistryObject<BABaseArrowItem> CONCUSSION_ARROW =
            REGISTRY.register("concussion_arrow", () ->
                    new BABaseArrowItem(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY), ArrowType.CONCUSSION));

    //               --------------------------BOOMERANG-------------------------------

    public static final RegistryObject<BoomerangItem> WOODEN_BOOMERANG =
            REGISTRY.register("wooden_boomerang", () ->
                    new BoomerangItem(Tiers.WOOD, 2, -2.9F, new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BoomerangItem> GOLDEN_BOOMERANG =
            REGISTRY.register("golden_boomerang", () ->
                    new BoomerangItem(Tiers.GOLD, 2, -2.9F, new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BoomerangItem> STONE_BOOMERANG =
            REGISTRY.register("stone_boomerang", () ->
                    new BoomerangItem(Tiers.STONE, 2, -2.9F, new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BoomerangItem> IRON_BOOMERANG =
            REGISTRY.register("iron_boomerang", () ->
                    new BoomerangItem(Tiers.IRON, 2, -2.9F, new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BoomerangItem> DIAMOND_BOOMERANG =
            REGISTRY.register("diamond_boomerang", () ->
                    new BoomerangItem(Tiers.DIAMOND, 2, -2.9F, new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BoomerangItem> NETHERITE_BOOMERANG =
            REGISTRY.register("netherite_boomerang", () ->
                    new BoomerangItem(Tiers.NETHERITE, 2, -2.9F, new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));


    //------------------------------------BOMB-----------------------------------------------------------

    public static final RegistryObject<BombItem> BOMB =
            REGISTRY.register("bomb", () ->
                    new BombItem(new Item.Properties().stacksTo(16).tab(BrassArmoryItemGroups.BRASS_ARMORY), BombType.NORMAL));

    public static final RegistryObject<BombItem> BOUNCY_BOMB =
            REGISTRY.register("bouncy_bomb", () ->
                    new BombItem(new Item.Properties().stacksTo(16).tab(BrassArmoryItemGroups.BRASS_ARMORY), BombType.BOUNCY));

    public static final RegistryObject<BombItem> STICKY_BOMB =
            REGISTRY.register("sticky_bomb", () ->
                    new BombItem(new Item.Properties().stacksTo(16).tab(BrassArmoryItemGroups.BRASS_ARMORY), BombType.STICKY));


    //------------------------------------MELEE-----------------------------------------------------------

    public static final RegistryObject<SpearItem> GOLDEN_SPEAR =
            REGISTRY.register("golden_spear", () -> new SpearItem(Tiers.GOLD, 3, -2.6F, 1F,
                    new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<SpearItem> WOODEN_SPEAR =
            REGISTRY.register("wooden_spear", () -> new SpearItem(Tiers.WOOD, 3, -2.6F, 1F,
                    new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<SpearItem> STONE_SPEAR =
            REGISTRY.register("stone_spear", () -> new SpearItem(Tiers.STONE, 3, -2.6F, 1F,
                    new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<SpearItem> IRON_SPEAR =
            REGISTRY.register("iron_spear", () -> new SpearItem(Tiers.IRON, 3, -2.6F, 1F,
                    new Item.Properties().durability(250).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<SpearItem> DIAMOND_SPEAR =
            REGISTRY.register("diamond_spear", () -> new SpearItem(Tiers.DIAMOND, 3, -2.6F, 1F,
                    new Item.Properties().durability(1562).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<SpearItem> NETHERITE_SPEAR =
            REGISTRY.register("netherite_spear", () -> new SpearItem(Tiers.NETHERITE, 3, -2.6F, 1F,
                    new Item.Properties().durability(2031).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<FireRodItem> FIRE_ROD =
            REGISTRY.register("fire_rod", () -> new FireRodItem(Tiers.WOOD, 1, -2.6F, 1F, ParticleTypes.FLAME,
                    new Item.Properties().durability(8).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<DaggerItem> GOLDEN_DAGGER =
            REGISTRY.register("golden_dagger", () -> new DaggerItem(Tiers.GOLD, 2, -2.0F, -1F,
                    new Item.Properties().durability(32).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<DaggerItem> WOODEN_DAGGER =
            REGISTRY.register("wooden_dagger", () -> new DaggerItem(Tiers.WOOD, 2, -2.0F, -1F,
                    new Item.Properties().durability(59).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<DaggerItem> STONE_DAGGER =
            REGISTRY.register("stone_dagger", () -> new DaggerItem(Tiers.STONE, 2, -2.0F, -1F,
                    new Item.Properties().durability(131).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<DaggerItem> IRON_DAGGER =
            REGISTRY.register("iron_dagger", () -> new DaggerItem(Tiers.IRON, 2, -2.0F, -1F,
                    new Item.Properties().durability(250).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<DaggerItem> DIAMOND_DAGGER =
            REGISTRY.register("diamond_dagger", () -> new DaggerItem(Tiers.DIAMOND, 2, -2.0F, -1F,
                    new Item.Properties().durability(1562).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<DaggerItem> NETHERITE_DAGGER =
            REGISTRY.register("netherite_dagger", () -> new DaggerItem(Tiers.NETHERITE, 2, -2.0F, -1F,
                    new Item.Properties().defaultDurability(2031).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<BattleaxeItem> GOLDEN_BATTLEAXE =
            REGISTRY.register("golden_battleaxe", () -> new BattleaxeItem(Tiers.GOLD, 7,
                    new Item.Properties().durability(32).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BattleaxeItem> WOODEN_BATTLEAXE =
            REGISTRY.register("wooden_battleaxe", () -> new BattleaxeItem(Tiers.WOOD, 7,
                    new Item.Properties().durability(59).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BattleaxeItem> STONE_BATTLEAXE =
            REGISTRY.register("stone_battleaxe", () -> new BattleaxeItem(Tiers.STONE, 8,
                    new Item.Properties().durability(131).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BattleaxeItem> IRON_BATTLEAXE =
            REGISTRY.register("iron_battleaxe", () -> new BattleaxeItem(Tiers.IRON, 7,
                    new Item.Properties().durability(250).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BattleaxeItem> DIAMOND_BATTLEAXE =
            REGISTRY.register("diamond_battleaxe", () -> new BattleaxeItem(Tiers.DIAMOND, 6,
                    new Item.Properties().durability(1562).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BattleaxeItem> NETHERITE_BATTLEAXE =
            REGISTRY.register("netherite_battleaxe", () -> new BattleaxeItem(Tiers.NETHERITE, 6,
                    new Item.Properties().defaultDurability(2031).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<HalberdItem> GOLDEN_HALBERD =
            REGISTRY.register("golden_halberd", () -> new HalberdItem(Tiers.GOLD,
                    5,-3.4F, 0.5F,
                    4, -2.9F, 1F,
                    new Item.Properties().durability(32).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<HalberdItem> WOODEN_HALBERD =
            REGISTRY.register("wooden_halberd", () -> new HalberdItem(Tiers.WOOD,
                    5,-3.4F, 0.5F,
                    4, -2.9F, 1F,
                    new Item.Properties().durability(59).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<HalberdItem> STONE_HALBERD =
            REGISTRY.register("stone_halberd", () -> new HalberdItem(Tiers.STONE,
                    5,-3.4F, 0.5F,
                    4, -2.9F, 1F,
                    new Item.Properties().durability(131).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<HalberdItem> IRON_HALBERD =
            REGISTRY.register("iron_halberd", () -> new HalberdItem(Tiers.IRON,
                    5,-3.4F, 0.5F,
                    4, -2.9F, 1F,
                    new Item.Properties().durability(250).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<HalberdItem> DIAMOND_HALBERD =
            REGISTRY.register("diamond_halberd", () -> new HalberdItem(Tiers.DIAMOND,
                    5,-3.4F, 0.5F,
                    4, -2.9F, 1F,
                    new Item.Properties().durability(1562).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<HalberdItem> NETHERITE_HALBERD =
            REGISTRY.register("netherite_halberd", () -> new HalberdItem(Tiers.NETHERITE,
                    5,-3.4F, 0.5F,
                    4, -2.9F, 1F,
                    new Item.Properties().defaultDurability(2031).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<MaceItem> GOLDEN_MACE =
            REGISTRY.register("golden_mace", () -> new MaceItem(Tiers.GOLD, 4,
                    new Item.Properties().durability(32).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<MaceItem> WOODEN_MACE =
            REGISTRY.register("wooden_mace", () -> new MaceItem(Tiers.WOOD, 4,
                    new Item.Properties().durability(59).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<MaceItem> STONE_MACE =
            REGISTRY.register("stone_mace", () -> new MaceItem(Tiers.STONE, 4,
                    new Item.Properties().durability(131).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<MaceItem> IRON_MACE =
            REGISTRY.register("iron_mace", () -> new MaceItem(Tiers.IRON, 4,
                    new Item.Properties().durability(250).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<MaceItem> DIAMOND_MACE =
            REGISTRY.register("diamond_mace", () -> new MaceItem(Tiers.DIAMOND, 4,
                    new Item.Properties().durability(1562).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<MaceItem> NETHERITE_MACE =
            REGISTRY.register("netherite_mace", () -> new MaceItem(Tiers.NETHERITE, 4,
                    new Item.Properties().defaultDurability(2031).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<FlailItem> GOLDEN_FLAIL =
            REGISTRY.register("golden_flail", () -> new FlailItem(Tiers.GOLD, 3,
                    new Item.Properties().durability(32).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<FlailItem> WOODEN_FLAIL =
            REGISTRY.register("wooden_flail", () -> new FlailItem(Tiers.WOOD, 3,
                    new Item.Properties().durability(59).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<FlailItem> STONE_FLAIL =
            REGISTRY.register("stone_flail", () -> new FlailItem(Tiers.STONE, 3,
                    new Item.Properties().durability(131).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<FlailItem> IRON_FLAIL =
            REGISTRY.register("iron_flail", () -> new FlailItem(Tiers.IRON, 3,
                    new Item.Properties().durability(250).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<FlailItem> DIAMOND_FLAIL =
            REGISTRY.register("diamond_flail", () -> new FlailItem(Tiers.DIAMOND, 3,
                    new Item.Properties().durability(1562).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<FlailItem> NETHERITE_FLAIL =
            REGISTRY.register("netherite_flail", () -> new FlailItem(Tiers.NETHERITE, 3,
                    new Item.Properties().defaultDurability(2031).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<FlintlockItem> FLINTLOCK_PISTOL =
            REGISTRY.register("flintlock_pistol", () -> new FlintlockItem(new Item.Properties().defaultDurability(32).tab(BrassArmoryItemGroups.BRASS_ARMORY),
                    true, 5.5D, 2F, 0.05D));

    public static final RegistryObject<FlintlockItem> MUSKET =
            REGISTRY.register("musket", () -> new FlintlockItem(new Item.Properties().defaultDurability(64).tab(BrassArmoryItemGroups.BRASS_ARMORY),
                    false, 10D, 1.5F, 0.2D));

    public static final RegistryObject<FlintlockSpreadItem> BLUNDERBUSS =
            REGISTRY.register("blunderbuss", () -> new FlintlockSpreadItem(new Item.Properties().defaultDurability(80).tab(BrassArmoryItemGroups.BRASS_ARMORY),
                    false, 12D, 1.2F, 0.4D));

    public static final RegistryObject<Item> GUN_STOCK =
            REGISTRY.register("gun_stock", () -> new Item(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<Item> MUSKET_BARREL =
            REGISTRY.register("musket_barrel", () -> new Item(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<Item> BLUNDERBUSS_BARREL =
            REGISTRY.register("blunderbuss_barrel", () -> new Item(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<Item> MUSKET_BALL =
            REGISTRY.register("musket_ball", () -> new Item(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY).stacksTo(16)));

    public static final RegistryObject<Item> BUNDLE_SHOT =
            REGISTRY.register("bundle_shot", () -> new Item(new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY).stacksTo(16)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<LongBowItem> LONGBOW =
            REGISTRY.register("longbow", () -> new LongBowItem(new Item.Properties().defaultDurability(512).tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    public static final RegistryObject<BlockItem> ROPE_ITEM =
            REGISTRY.register("rope", () -> new BlockItem(BrassArmoryBlocks.ROPE.get(), new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY)));

    // Attach event handler for registry.
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
