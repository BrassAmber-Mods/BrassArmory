package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.arrow.*;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BouncyBombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.StickyBombEntity;
import com.milamber_brass.brass_armory.item.*;
import com.milamber_brass.brass_armory.item.MaceItem;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.util.ItemData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class BrassArmoryItems {

    // Item Registry
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(BuiltInRegistries.ITEM, BrassArmory.MOD_ID);

    //------------------------------------RANGED----------------------------------------------------------

    public static final DeferredHolder<Item, SpecialArrowItem> DIRT_ARROW =
            REGISTRY.register("dirt_arrow", () ->
                    new SpecialArrowItem(defaultBuilder("dirt_arrow"), DirtArrowEntity::new, DirtArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> EX_ARROW =
            REGISTRY.register("ex_arrow", () ->
                    new SpecialArrowItem(defaultBuilder("ex_arrow"), ExplosionArrowEntity::new, ExplosionArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> FROST_ARROW =
            REGISTRY.register("frost_arrow", () ->
                    new SpecialArrowItem(defaultBuilder("frost_arrow"), FrostArrowEntity::new, FrostArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> GRASS_ARROW =
            REGISTRY.register("grass_arrow", () ->
                    new SpecialArrowItem(defaultBuilder("grass_arrow"), GrassArrowEntity::new, GrassArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> LASER_ARROW =
            REGISTRY.register("laser_arrow", () ->
                    new SpecialArrowItem(defaultBuilder("laser_arrow"), LaserArrowEntity::new, LaserArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> ROPE_ARROW =
            REGISTRY.register("rope_arrow", () ->
                    new SpecialArrowItem(defaultBuilder("rope_arrow"), RopeArrowEntity::new, RopeArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> SLIME_ARROW =
            REGISTRY.register("slime_arrow", () ->
                    new SpecialArrowItem(defaultBuilder("slime_arrow"), SlimeArrowEntity::new, SlimeArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> WARP_ARROW =
            REGISTRY.register("warp_arrow", () ->
                    new SpecialArrowItem(defaultBuilder("warp_arrow"), WarpArrowEntity::new, WarpArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> FIRE_ARROW =
            REGISTRY.register("fire_arrow", () ->
                    new SpecialArrowItem(defaultBuilder("fire_arrow"), FireArrowEntity::new, FireArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> CONFUSION_ARROW =
            REGISTRY.register("confusion_arrow", () ->
                    new SpecialArrowItem(defaultBuilder("confusion_arrow"), ConfusionArrowEntity::new, ConfusionArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> TORCH_ARROW =
            REGISTRY.register("torch_arrow", () ->
                    new SpecialArrowItem(defaultBuilder("torch_arrow"), TorchArrowEntity::new, TorchArrowEntity::new) {
                        @Override
                        public @NotNull AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity living, @Nullable ItemStack weapon) {
                            return Util.make(super.createArrow(level, stack, living, weapon), abstractArrow -> {
                                if (abstractArrow instanceof TorchArrowEntity torchArrow)
                                    torchArrow.setTorch(ItemData.getTag(stack)); //Should always be true, but fuck it
                            });
                        }

                        @Override
                        public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> components, TooltipFlag tooltipFlag) {
                            components.accept(ArmoryUtil.loadStack(ItemData.getTag(stack), "BATorch", Items.TORCH.getDefaultInstance()).getHoverName().copy().withStyle(ChatFormatting.GRAY));
                        }
                    });

    //--------------------------BOOMERANG-------------------------------

    public static final DeferredHolder<Item, BoomerangItem> WOODEN_BOOMERANG =
            REGISTRY.register("wooden_boomerang", () ->
                    new BoomerangItem(ToolMaterial.WOOD, 2.0F, -2.9F, defaultBuilder("wooden_boomerang")));

    public static final DeferredHolder<Item, BoomerangItem> GOLDEN_BOOMERANG =
            REGISTRY.register("golden_boomerang", () ->
                    new BoomerangItem(ToolMaterial.GOLD, 2.0F, -2.9F, defaultBuilder("golden_boomerang")));

    public static final DeferredHolder<Item, BoomerangItem> STONE_BOOMERANG =
            REGISTRY.register("stone_boomerang", () ->
                    new BoomerangItem(ToolMaterial.STONE, 2.0F, -2.9F, defaultBuilder("stone_boomerang")));

    public static final DeferredHolder<Item, BoomerangItem> IRON_BOOMERANG =
            REGISTRY.register("iron_boomerang", () ->
                    new BoomerangItem(ToolMaterial.IRON, 2.0F, -2.9F, defaultBuilder("iron_boomerang")));

    public static final DeferredHolder<Item, BoomerangItem> DIAMOND_BOOMERANG =
            REGISTRY.register("diamond_boomerang", () ->
                    new BoomerangItem(ToolMaterial.DIAMOND, 2.0F, -2.9F, defaultBuilder("diamond_boomerang")));

    public static final DeferredHolder<Item, BoomerangItem> NETHERITE_BOOMERANG =
            REGISTRY.register("netherite_boomerang", () ->
                    new BoomerangItem(ToolMaterial.NETHERITE, 2.0F, -2.9F, defaultBuilder("netherite_boomerang")));


    //------------------------------------BOMB-----------------------------------------------------------

    public static final DeferredHolder<Item, BombItem> BOMB =
            REGISTRY.register("bomb", () ->
                    new BombItem(defaultBuilder("bomb").stacksTo(16), BombEntity::new, BombEntity::new));

    public static final DeferredHolder<Item, BombItem> BOUNCY_BOMB =
            REGISTRY.register("bouncy_bomb", () ->
                    new BombItem(defaultBuilder("bouncy_bomb").stacksTo(16), BouncyBombEntity::new, BouncyBombEntity::new));

    public static final DeferredHolder<Item, BombItem> STICKY_BOMB =
            REGISTRY.register("sticky_bomb", () ->
                    new BombItem(defaultBuilder("sticky_bomb").stacksTo(16), StickyBombEntity::new, StickyBombEntity::new));


    //------------------------------------MELEE-----------------------------------------------------------

    public static final DeferredHolder<Item, SpearItem> GOLDEN_SPEAR =
            REGISTRY.register("golden_spear", () -> new SpearItem(ToolMaterial.GOLD, 3, -2.6F, 1F,
                    defaultBuilder("golden_spear")));

    public static final DeferredHolder<Item, SpearItem> WOODEN_SPEAR =
            REGISTRY.register("wooden_spear", () -> new SpearItem(ToolMaterial.WOOD, 3, -2.6F, 1F,
                    defaultBuilder("wooden_spear")));

    public static final DeferredHolder<Item, SpearItem> STONE_SPEAR =
            REGISTRY.register("stone_spear", () -> new SpearItem(ToolMaterial.STONE, 3, -2.6F, 1F,
                    defaultBuilder("stone_spear")));

    public static final DeferredHolder<Item, SpearItem> IRON_SPEAR =
            REGISTRY.register("iron_spear", () -> new SpearItem(ToolMaterial.IRON, 3, -2.6F, 1F,
                    defaultBuilder("iron_spear").durability(250)));

    public static final DeferredHolder<Item, SpearItem> DIAMOND_SPEAR =
            REGISTRY.register("diamond_spear", () -> new SpearItem(ToolMaterial.DIAMOND, 3, -2.6F, 1F,
                    defaultBuilder("diamond_spear").durability(1562)));

    public static final DeferredHolder<Item, SpearItem> NETHERITE_SPEAR =
            REGISTRY.register("netherite_spear", () -> new SpearItem(ToolMaterial.NETHERITE, 3, -2.6F, 1F,
                    defaultBuilder("netherite_spear").durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, FireRodItem> FIRE_ROD =
            REGISTRY.register("fire_rod", () -> new FireRodItem(ToolMaterial.WOOD, 1, -2.6F, 1F, ParticleTypes.FLAME,
                    defaultBuilder("fire_rod").durability(8)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, DaggerItem> GOLDEN_DAGGER =
            REGISTRY.register("golden_dagger", () -> new DaggerItem(ToolMaterial.GOLD, 1.0F, -2.0F, -1F,
                    defaultBuilder("golden_dagger").durability(32)));

    public static final DeferredHolder<Item, DaggerItem> WOODEN_DAGGER =
            REGISTRY.register("wooden_dagger", () -> new DaggerItem(ToolMaterial.WOOD, 1.0F, -2.0F, -1F,
                    defaultBuilder("wooden_dagger").durability(59)));

    public static final DeferredHolder<Item, DaggerItem> STONE_DAGGER =
            REGISTRY.register("stone_dagger", () -> new DaggerItem(ToolMaterial.STONE, 1.0F, -2.0F, -1F,
                    defaultBuilder("stone_dagger").durability(131)));

    public static final DeferredHolder<Item, DaggerItem> IRON_DAGGER =
            REGISTRY.register("iron_dagger", () -> new DaggerItem(ToolMaterial.IRON, 1.0F, -2.0F, -1F,
                    defaultBuilder("iron_dagger").durability(250)));

    public static final DeferredHolder<Item, DaggerItem> DIAMOND_DAGGER =
            REGISTRY.register("diamond_dagger", () -> new DaggerItem(ToolMaterial.DIAMOND, 1.0F, -2.0F, -1F,
                    defaultBuilder("diamond_dagger").durability(1562)));

    public static final DeferredHolder<Item, DaggerItem> NETHERITE_DAGGER =
            REGISTRY.register("netherite_dagger", () -> new DaggerItem(ToolMaterial.NETHERITE, 1.0F, -2.0F, -1F,
                    defaultBuilder("netherite_dagger").durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, BattleaxeItem> GOLDEN_BATTLEAXE =
            REGISTRY.register("golden_battleaxe", () -> new BattleaxeItem(ToolMaterial.GOLD, 7.0F, -3.1F,
                    defaultBuilder("golden_battleaxe").durability(32)));

    public static final DeferredHolder<Item, BattleaxeItem> WOODEN_BATTLEAXE =
            REGISTRY.register("wooden_battleaxe", () -> new BattleaxeItem(ToolMaterial.WOOD, 7.0F, -3.3F,
                    defaultBuilder("wooden_battleaxe").durability(59)));

    public static final DeferredHolder<Item, BattleaxeItem> STONE_BATTLEAXE =
            REGISTRY.register("stone_battleaxe", () -> new BattleaxeItem(ToolMaterial.STONE, 8.0F, -3.3F,
                    defaultBuilder("stone_battleaxe").durability(131)));

    public static final DeferredHolder<Item, BattleaxeItem> IRON_BATTLEAXE =
            REGISTRY.register("iron_battleaxe", () -> new BattleaxeItem(ToolMaterial.IRON, 7.0F, -3.2F,
                    defaultBuilder("iron_battleaxe").durability(250)));

    public static final DeferredHolder<Item, BattleaxeItem> DIAMOND_BATTLEAXE =
            REGISTRY.register("diamond_battleaxe", () -> new BattleaxeItem(ToolMaterial.DIAMOND, 6.0F, -3.1F,
                    defaultBuilder("diamond_battleaxe").durability(1562)));

    public static final DeferredHolder<Item, BattleaxeItem> NETHERITE_BATTLEAXE =
            REGISTRY.register("netherite_battleaxe", () -> new BattleaxeItem(ToolMaterial.NETHERITE, 6.0F, -3.1F,
                    defaultBuilder("netherite_battleaxe").durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, HalberdItem> GOLDEN_HALBERD =
            REGISTRY.register("golden_halberd", () -> new HalberdItem(ToolMaterial.GOLD,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder("golden_halberd").durability(32)));

    public static final DeferredHolder<Item, HalberdItem> WOODEN_HALBERD =
            REGISTRY.register("wooden_halberd", () -> new HalberdItem(ToolMaterial.WOOD,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder("wooden_halberd").durability(59)));

    public static final DeferredHolder<Item, HalberdItem> STONE_HALBERD =
            REGISTRY.register("stone_halberd", () -> new HalberdItem(ToolMaterial.STONE,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder("stone_halberd").durability(131)));

    public static final DeferredHolder<Item, HalberdItem> IRON_HALBERD =
            REGISTRY.register("iron_halberd", () -> new HalberdItem(ToolMaterial.IRON,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder("iron_halberd").durability(250)));

    public static final DeferredHolder<Item, HalberdItem> DIAMOND_HALBERD =
            REGISTRY.register("diamond_halberd", () -> new HalberdItem(ToolMaterial.DIAMOND,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder("diamond_halberd").durability(1562)));

    public static final DeferredHolder<Item, HalberdItem> NETHERITE_HALBERD =
            REGISTRY.register("netherite_halberd", () -> new HalberdItem(ToolMaterial.NETHERITE,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder("netherite_halberd").durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, MaceItem> GOLDEN_MACE =
            REGISTRY.register("golden_mace", () -> new MaceItem(ToolMaterial.GOLD, 9.0F, -3.3F,
                    defaultBuilder("golden_mace").durability(32)));

    public static final DeferredHolder<Item, MaceItem> WOODEN_MACE =
            REGISTRY.register("wooden_mace", () -> new MaceItem(ToolMaterial.WOOD, 9.0F, -3.5F,
                    defaultBuilder("wooden_mace").durability(59)));

    public static final DeferredHolder<Item, MaceItem> STONE_MACE =
            REGISTRY.register("stone_mace", () -> new MaceItem(ToolMaterial.STONE, 10.0F, -3.5F,
                    defaultBuilder("stone_mace").durability(131)));

    public static final DeferredHolder<Item, MaceItem> IRON_MACE =
            REGISTRY.register("iron_mace", () -> new MaceItem(ToolMaterial.IRON, 9.0F, -3.4F,
                    defaultBuilder("iron_mace").durability(250)));

    public static final DeferredHolder<Item, MaceItem> DIAMOND_MACE =
            REGISTRY.register("diamond_mace", () -> new MaceItem(ToolMaterial.DIAMOND, 8.0F, -3.3F,
                    defaultBuilder("diamond_mace").durability(1562)));

    public static final DeferredHolder<Item, MaceItem> NETHERITE_MACE =
            REGISTRY.register("netherite_mace", () -> new MaceItem(ToolMaterial.NETHERITE, 8.0F, -3.3F,
                    defaultBuilder("netherite_mace").durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, SpikyBallItem> GOLDEN_SPIKY_BALL =
            REGISTRY.register("golden_spiky_ball", () -> new SpikyBallItem(ToolMaterial.GOLD, defaultBuilder("golden_spiky_ball")));

    public static final DeferredHolder<Item, SpikyBallItem> WOODEN_SPIKY_BALL =
            REGISTRY.register("wooden_spiky_ball", () -> new SpikyBallItem(ToolMaterial.WOOD, defaultBuilder("wooden_spiky_ball")));

    public static final DeferredHolder<Item, SpikyBallItem> STONE_SPIKY_BALL =
            REGISTRY.register("stone_spiky_ball", () -> new SpikyBallItem(ToolMaterial.STONE, defaultBuilder("stone_spiky_ball")));

    public static final DeferredHolder<Item, SpikyBallItem> IRON_SPIKY_BALL =
            REGISTRY.register("iron_spiky_ball", () -> new SpikyBallItem(ToolMaterial.IRON, defaultBuilder("iron_spiky_ball")));

    public static final DeferredHolder<Item, SpikyBallItem> DIAMOND_SPIKY_BALL =
            REGISTRY.register("diamond_spiky_ball", () -> new SpikyBallItem(ToolMaterial.DIAMOND, defaultBuilder("diamond_spiky_ball")));

    public static final DeferredHolder<Item, SpikyBallItem> NETHERITE_SPIKY_BALL =
            REGISTRY.register("netherite_spiky_ball", () -> new SpikyBallItem(ToolMaterial.NETHERITE, defaultBuilder("netherite_spiky_ball")));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, FlailItem> GOLDEN_FLAIL =
            REGISTRY.register("golden_flail", () -> new FlailItem(ToolMaterial.GOLD, 3, GOLDEN_SPIKY_BALL.get(),
                    defaultBuilder("golden_flail").durability(32)));

    public static final DeferredHolder<Item, FlailItem> WOODEN_FLAIL =
            REGISTRY.register("wooden_flail", () -> new FlailItem(ToolMaterial.WOOD, 3, WOODEN_SPIKY_BALL.get(),
                    defaultBuilder("wooden_flail").durability(59)));

    public static final DeferredHolder<Item, FlailItem> STONE_FLAIL =
            REGISTRY.register("stone_flail", () -> new FlailItem(ToolMaterial.STONE, 3, STONE_SPIKY_BALL.get(),
                    defaultBuilder("stone_flail").durability(131)));

    public static final DeferredHolder<Item, FlailItem> IRON_FLAIL =
            REGISTRY.register("iron_flail", () -> new FlailItem(ToolMaterial.IRON, 3, IRON_SPIKY_BALL.get(),
                    defaultBuilder("iron_flail").durability(250)));

    public static final DeferredHolder<Item, FlailItem> DIAMOND_FLAIL =
            REGISTRY.register("diamond_flail", () -> new FlailItem(ToolMaterial.DIAMOND, 3, DIAMOND_SPIKY_BALL.get(),
                    defaultBuilder("diamond_flail").durability(1562)));

    public static final DeferredHolder<Item, FlailItem> NETHERITE_FLAIL =
            REGISTRY.register("netherite_flail", () -> new FlailItem(ToolMaterial.NETHERITE, 3, NETHERITE_SPIKY_BALL.get(),
                    defaultBuilder("netherite_flail").durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, FlintlockItem> FLINTLOCK_PISTOL =
            REGISTRY.register("flintlock_pistol", () -> new FlintlockItem(defaultBuilder("flintlock_pistol").durability(32),
                    true, 5.5D, 2F, 1F, 0.05D));

    public static final DeferredHolder<Item, FlintlockItem> MUSKET =
            REGISTRY.register("musket", () -> new FlintlockItem(defaultBuilder("musket").durability(64),
                    false, 10D, 1.5F, 1F, 0.2D));

    public static final DeferredHolder<Item, FlintlockItem> BLUNDERBUSS =
            REGISTRY.register("blunderbuss", () -> new FlintlockItem(defaultBuilder("blunderbuss").durability(80),
                    false, 12D, 1.2F, 8F, 0.4D));

    public static final DeferredHolder<Item, Item> GUN_STOCK =
            REGISTRY.register("gun_stock", () -> new Item(defaultBuilder("gun_stock")));

    public static final DeferredHolder<Item, Item> MUSKET_PARTS =
            REGISTRY.register("musket_parts", () -> new Item(defaultBuilder("musket_parts")));

    public static final DeferredHolder<Item, Item> BLUNDERBUSS_PARTS =
            REGISTRY.register("blunderbuss_parts", () -> new Item(defaultBuilder("blunderbuss_parts")));

    public static final DeferredHolder<Item, Item> MUSKET_BALL =
            REGISTRY.register("musket_ball", () -> new Item(defaultBuilder("musket_ball").stacksTo(16)));

    public static final DeferredHolder<Item, Item> BUNDLE_SHOT =
            REGISTRY.register("bundle_shot", () -> new Item(defaultBuilder("bundle_shot").stacksTo(16)));

    public static final DeferredHolder<Item, CannonItem> CANNON =
            REGISTRY.register("cannon", () -> new CannonItem(defaultBuilder("cannon").stacksTo(1)));

    public static final DeferredHolder<Item, Item> CANNON_BALL =
            REGISTRY.register("cannon_ball", () -> new Item(defaultBuilder("cannon_ball").stacksTo(16)));

    public static final DeferredHolder<Item, CarcassRoundItem> CARCASS_ROUND =
            REGISTRY.register("carcass_round", () -> new CarcassRoundItem(defaultBuilder("carcass_round").stacksTo(16)));

    public static final DeferredHolder<Item, Item> SIEGE_ROUND =
            REGISTRY.register("siege_round", () -> new Item(defaultBuilder("siege_round").stacksTo(16)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, LongBowItem> LONGBOW =
            REGISTRY.register("longbow", () -> new LongBowItem(defaultBuilder("longbow").durability(512)));

    public static final DeferredHolder<Item, BlockItem> EXPLORERS_ROPE =
            REGISTRY.register("explorers_rope", () -> new BlockItem(BrassArmoryBlocks.EXPLORERS_ROPE_BLOCK.get(), defaultBuilder("explorers_rope")));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, KatanaItem> KATANA =
            REGISTRY.register("katana", () -> new KatanaItem(ToolMaterial.IRON, 3, -2F, true, defaultBuilder("katana").durability(512)));

    public static final DeferredHolder<Item, GliderItem> GLIDER =
            REGISTRY.register("glider", () -> new GliderItem(defaultBuilder("glider").durability(24)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, WarpCrystalItem> PETTY_WARP_CRYSTAL =
            REGISTRY.register("petty_warp_crystal", () -> new WarpCrystalItem(defaultBuilder("petty_warp_crystal").stacksTo(1), 80));

    public static final DeferredHolder<Item, WarpCrystalItem> LESSER_WARP_CRYSTAL =
            REGISTRY.register("lesser_warp_crystal", () -> new WarpCrystalItem(defaultBuilder("lesser_warp_crystal").stacksTo(1), 160));

    public static final DeferredHolder<Item, WarpCrystalItem> COMMON_WARP_CRYSTAL =
            REGISTRY.register("common_warp_crystal", () -> new WarpCrystalItem(defaultBuilder("common_warp_crystal").stacksTo(1), 320));

    public static final DeferredHolder<Item, WarpCrystalItem> GREATER_WARP_CRYSTAL =
            REGISTRY.register("greater_warp_crystal", () -> new WarpCrystalItem(defaultBuilder("greater_warp_crystal").stacksTo(1), 640));

    public static final DeferredHolder<Item, WarpCrystalItem> GRAND_WARP_CRYSTAL =
            REGISTRY.register("grand_warp_crystal", () -> new WarpCrystalItem(defaultBuilder("grand_warp_crystal").stacksTo(1), 1280));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, QuiverItem> QUIVER =
            REGISTRY.register("quiver", () -> new QuiverItem(defaultBuilder("quiver").stacksTo(1)));

    @NotNull
    public static Item.Properties defaultBuilder(String name) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(BrassArmory.MOD_ID, name)));
    }
    
    // Attach event handler for registry.
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
