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
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class BrassArmoryItems {

    // Item Registry
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(BuiltInRegistries.ITEM, BrassArmory.MOD_ID);

    //------------------------------------RANGED----------------------------------------------------------

    public static final DeferredHolder<Item, SpecialArrowItem> DIRT_ARROW =
            REGISTRY.register("dirt_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), DirtArrowEntity::new, DirtArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> EX_ARROW =
            REGISTRY.register("ex_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), ExplosionArrowEntity::new, ExplosionArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> FROST_ARROW =
            REGISTRY.register("frost_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), FrostArrowEntity::new, FrostArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> GRASS_ARROW =
            REGISTRY.register("grass_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), GrassArrowEntity::new, GrassArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> LASER_ARROW =
            REGISTRY.register("laser_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), LaserArrowEntity::new, LaserArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> ROPE_ARROW =
            REGISTRY.register("rope_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), RopeArrowEntity::new, RopeArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> SLIME_ARROW =
            REGISTRY.register("slime_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), SlimeArrowEntity::new, SlimeArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> WARP_ARROW =
            REGISTRY.register("warp_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), WarpArrowEntity::new, WarpArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> FIRE_ARROW =
            REGISTRY.register("fire_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), FireArrowEntity::new, FireArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> CONFUSION_ARROW =
            REGISTRY.register("confusion_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), ConfusionArrowEntity::new, ConfusionArrowEntity::new));

    public static final DeferredHolder<Item, SpecialArrowItem> TORCH_ARROW =
            REGISTRY.register("torch_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), TorchArrowEntity::new, TorchArrowEntity::new) {
                        @Override
                        public @NotNull AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity living, @Nullable ItemStack weapon) {
                            return Util.make(super.createArrow(level, stack, living, weapon), abstractArrow -> {
                                if (abstractArrow instanceof TorchArrowEntity torchArrow)
                                    torchArrow.setTorch(ItemData.getTag(stack)); //Should always be true, but fuck it
                            });
                        }

                        @Override
                        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
                            components.add(Component.translatable(ArmoryUtil.loadStack(ItemData.getTag(stack), "BATorch", Items.TORCH.getDefaultInstance()).getDescriptionId()).withStyle(ChatFormatting.GRAY));
                        }
                    });

    //--------------------------BOOMERANG-------------------------------

    public static final DeferredHolder<Item, BoomerangItem> WOODEN_BOOMERANG =
            REGISTRY.register("wooden_boomerang", () ->
                    new BoomerangItem(Tiers.WOOD, 2.0F, -2.9F, defaultBuilder()));

    public static final DeferredHolder<Item, BoomerangItem> GOLDEN_BOOMERANG =
            REGISTRY.register("golden_boomerang", () ->
                    new BoomerangItem(Tiers.GOLD, 2.0F, -2.9F, defaultBuilder()));

    public static final DeferredHolder<Item, BoomerangItem> STONE_BOOMERANG =
            REGISTRY.register("stone_boomerang", () ->
                    new BoomerangItem(Tiers.STONE, 2.0F, -2.9F, defaultBuilder()));

    public static final DeferredHolder<Item, BoomerangItem> IRON_BOOMERANG =
            REGISTRY.register("iron_boomerang", () ->
                    new BoomerangItem(Tiers.IRON, 2.0F, -2.9F, defaultBuilder()));

    public static final DeferredHolder<Item, BoomerangItem> DIAMOND_BOOMERANG =
            REGISTRY.register("diamond_boomerang", () ->
                    new BoomerangItem(Tiers.DIAMOND, 2.0F, -2.9F, defaultBuilder()));

    public static final DeferredHolder<Item, BoomerangItem> NETHERITE_BOOMERANG =
            REGISTRY.register("netherite_boomerang", () ->
                    new BoomerangItem(Tiers.NETHERITE, 2.0F, -2.9F, defaultBuilder()));


    //------------------------------------BOMB-----------------------------------------------------------

    public static final DeferredHolder<Item, BombItem> BOMB =
            REGISTRY.register("bomb", () ->
                    new BombItem(defaultBuilder().stacksTo(16), BombEntity::new, BombEntity::new));

    public static final DeferredHolder<Item, BombItem> BOUNCY_BOMB =
            REGISTRY.register("bouncy_bomb", () ->
                    new BombItem(defaultBuilder().stacksTo(16), BouncyBombEntity::new, BouncyBombEntity::new));

    public static final DeferredHolder<Item, BombItem> STICKY_BOMB =
            REGISTRY.register("sticky_bomb", () ->
                    new BombItem(defaultBuilder().stacksTo(16), StickyBombEntity::new, StickyBombEntity::new));


    //------------------------------------MELEE-----------------------------------------------------------

    public static final DeferredHolder<Item, SpearItem> GOLDEN_SPEAR =
            REGISTRY.register("golden_spear", () -> new SpearItem(Tiers.GOLD, 3, -2.6F, 1F,
                    defaultBuilder()));

    public static final DeferredHolder<Item, SpearItem> WOODEN_SPEAR =
            REGISTRY.register("wooden_spear", () -> new SpearItem(Tiers.WOOD, 3, -2.6F, 1F,
                    defaultBuilder()));

    public static final DeferredHolder<Item, SpearItem> STONE_SPEAR =
            REGISTRY.register("stone_spear", () -> new SpearItem(Tiers.STONE, 3, -2.6F, 1F,
                    defaultBuilder()));

    public static final DeferredHolder<Item, SpearItem> IRON_SPEAR =
            REGISTRY.register("iron_spear", () -> new SpearItem(Tiers.IRON, 3, -2.6F, 1F,
                    defaultBuilder().durability(250)));

    public static final DeferredHolder<Item, SpearItem> DIAMOND_SPEAR =
            REGISTRY.register("diamond_spear", () -> new SpearItem(Tiers.DIAMOND, 3, -2.6F, 1F,
                    defaultBuilder().durability(1562)));

    public static final DeferredHolder<Item, SpearItem> NETHERITE_SPEAR =
            REGISTRY.register("netherite_spear", () -> new SpearItem(Tiers.NETHERITE, 3, -2.6F, 1F,
                    defaultBuilder().durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, FireRodItem> FIRE_ROD =
            REGISTRY.register("fire_rod", () -> new FireRodItem(Tiers.WOOD, 1, -2.6F, 1F, ParticleTypes.FLAME,
                    defaultBuilder().durability(8)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, DaggerItem> GOLDEN_DAGGER =
            REGISTRY.register("golden_dagger", () -> new DaggerItem(Tiers.GOLD, 1.0F, -2.0F, -1F,
                    defaultBuilder().durability(32)));

    public static final DeferredHolder<Item, DaggerItem> WOODEN_DAGGER =
            REGISTRY.register("wooden_dagger", () -> new DaggerItem(Tiers.WOOD, 1.0F, -2.0F, -1F,
                    defaultBuilder().durability(59)));

    public static final DeferredHolder<Item, DaggerItem> STONE_DAGGER =
            REGISTRY.register("stone_dagger", () -> new DaggerItem(Tiers.STONE, 1.0F, -2.0F, -1F,
                    defaultBuilder().durability(131)));

    public static final DeferredHolder<Item, DaggerItem> IRON_DAGGER =
            REGISTRY.register("iron_dagger", () -> new DaggerItem(Tiers.IRON, 1.0F, -2.0F, -1F,
                    defaultBuilder().durability(250)));

    public static final DeferredHolder<Item, DaggerItem> DIAMOND_DAGGER =
            REGISTRY.register("diamond_dagger", () -> new DaggerItem(Tiers.DIAMOND, 1.0F, -2.0F, -1F,
                    defaultBuilder().durability(1562)));

    public static final DeferredHolder<Item, DaggerItem> NETHERITE_DAGGER =
            REGISTRY.register("netherite_dagger", () -> new DaggerItem(Tiers.NETHERITE, 1.0F, -2.0F, -1F,
                    defaultBuilder().durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, BattleaxeItem> GOLDEN_BATTLEAXE =
            REGISTRY.register("golden_battleaxe", () -> new BattleaxeItem(Tiers.GOLD, 7.0F, -3.1F,
                    defaultBuilder().durability(32)));

    public static final DeferredHolder<Item, BattleaxeItem> WOODEN_BATTLEAXE =
            REGISTRY.register("wooden_battleaxe", () -> new BattleaxeItem(Tiers.WOOD, 7.0F, -3.3F,
                    defaultBuilder().durability(59)));

    public static final DeferredHolder<Item, BattleaxeItem> STONE_BATTLEAXE =
            REGISTRY.register("stone_battleaxe", () -> new BattleaxeItem(Tiers.STONE, 8.0F, -3.3F,
                    defaultBuilder().durability(131)));

    public static final DeferredHolder<Item, BattleaxeItem> IRON_BATTLEAXE =
            REGISTRY.register("iron_battleaxe", () -> new BattleaxeItem(Tiers.IRON, 7.0F, -3.2F,
                    defaultBuilder().durability(250)));

    public static final DeferredHolder<Item, BattleaxeItem> DIAMOND_BATTLEAXE =
            REGISTRY.register("diamond_battleaxe", () -> new BattleaxeItem(Tiers.DIAMOND, 6.0F, -3.1F,
                    defaultBuilder().durability(1562)));

    public static final DeferredHolder<Item, BattleaxeItem> NETHERITE_BATTLEAXE =
            REGISTRY.register("netherite_battleaxe", () -> new BattleaxeItem(Tiers.NETHERITE, 6.0F, -3.1F,
                    defaultBuilder().durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, HalberdItem> GOLDEN_HALBERD =
            REGISTRY.register("golden_halberd", () -> new HalberdItem(Tiers.GOLD,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder().durability(32)));

    public static final DeferredHolder<Item, HalberdItem> WOODEN_HALBERD =
            REGISTRY.register("wooden_halberd", () -> new HalberdItem(Tiers.WOOD,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder().durability(59)));

    public static final DeferredHolder<Item, HalberdItem> STONE_HALBERD =
            REGISTRY.register("stone_halberd", () -> new HalberdItem(Tiers.STONE,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder().durability(131)));

    public static final DeferredHolder<Item, HalberdItem> IRON_HALBERD =
            REGISTRY.register("iron_halberd", () -> new HalberdItem(Tiers.IRON,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder().durability(250)));

    public static final DeferredHolder<Item, HalberdItem> DIAMOND_HALBERD =
            REGISTRY.register("diamond_halberd", () -> new HalberdItem(Tiers.DIAMOND,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder().durability(1562)));

    public static final DeferredHolder<Item, HalberdItem> NETHERITE_HALBERD =
            REGISTRY.register("netherite_halberd", () -> new HalberdItem(Tiers.NETHERITE,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder().durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, MaceItem> GOLDEN_MACE =
            REGISTRY.register("golden_mace", () -> new MaceItem(Tiers.GOLD, 9.0F, -3.3F,
                    defaultBuilder().durability(32)));

    public static final DeferredHolder<Item, MaceItem> WOODEN_MACE =
            REGISTRY.register("wooden_mace", () -> new MaceItem(Tiers.WOOD, 9.0F, -3.5F,
                    defaultBuilder().durability(59)));

    public static final DeferredHolder<Item, MaceItem> STONE_MACE =
            REGISTRY.register("stone_mace", () -> new MaceItem(Tiers.STONE, 10.0F, -3.5F,
                    defaultBuilder().durability(131)));

    public static final DeferredHolder<Item, MaceItem> IRON_MACE =
            REGISTRY.register("iron_mace", () -> new MaceItem(Tiers.IRON, 9.0F, -3.4F,
                    defaultBuilder().durability(250)));

    public static final DeferredHolder<Item, MaceItem> DIAMOND_MACE =
            REGISTRY.register("diamond_mace", () -> new MaceItem(Tiers.DIAMOND, 8.0F, -3.3F,
                    defaultBuilder().durability(1562)));

    public static final DeferredHolder<Item, MaceItem> NETHERITE_MACE =
            REGISTRY.register("netherite_mace", () -> new MaceItem(Tiers.NETHERITE, 8.0F, -3.3F,
                    defaultBuilder().durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, SpikyBallItem> GOLDEN_SPIKY_BALL =
            REGISTRY.register("golden_spiky_ball", () -> new SpikyBallItem(Tiers.GOLD, defaultBuilder()));

    public static final DeferredHolder<Item, SpikyBallItem> WOODEN_SPIKY_BALL =
            REGISTRY.register("wooden_spiky_ball", () -> new SpikyBallItem(Tiers.WOOD, defaultBuilder()));

    public static final DeferredHolder<Item, SpikyBallItem> STONE_SPIKY_BALL =
            REGISTRY.register("stone_spiky_ball", () -> new SpikyBallItem(Tiers.STONE, defaultBuilder()));

    public static final DeferredHolder<Item, SpikyBallItem> IRON_SPIKY_BALL =
            REGISTRY.register("iron_spiky_ball", () -> new SpikyBallItem(Tiers.IRON, defaultBuilder()));

    public static final DeferredHolder<Item, SpikyBallItem> DIAMOND_SPIKY_BALL =
            REGISTRY.register("diamond_spiky_ball", () -> new SpikyBallItem(Tiers.DIAMOND, defaultBuilder()));

    public static final DeferredHolder<Item, SpikyBallItem> NETHERITE_SPIKY_BALL =
            REGISTRY.register("netherite_spiky_ball", () -> new SpikyBallItem(Tiers.NETHERITE, defaultBuilder()));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, FlailItem> GOLDEN_FLAIL =
            REGISTRY.register("golden_flail", () -> new FlailItem(Tiers.GOLD, 3, GOLDEN_SPIKY_BALL.get(),
                    defaultBuilder().durability(32)));

    public static final DeferredHolder<Item, FlailItem> WOODEN_FLAIL =
            REGISTRY.register("wooden_flail", () -> new FlailItem(Tiers.WOOD, 3, WOODEN_SPIKY_BALL.get(),
                    defaultBuilder().durability(59)));

    public static final DeferredHolder<Item, FlailItem> STONE_FLAIL =
            REGISTRY.register("stone_flail", () -> new FlailItem(Tiers.STONE, 3, STONE_SPIKY_BALL.get(),
                    defaultBuilder().durability(131)));

    public static final DeferredHolder<Item, FlailItem> IRON_FLAIL =
            REGISTRY.register("iron_flail", () -> new FlailItem(Tiers.IRON, 3, IRON_SPIKY_BALL.get(),
                    defaultBuilder().durability(250)));

    public static final DeferredHolder<Item, FlailItem> DIAMOND_FLAIL =
            REGISTRY.register("diamond_flail", () -> new FlailItem(Tiers.DIAMOND, 3, DIAMOND_SPIKY_BALL.get(),
                    defaultBuilder().durability(1562)));

    public static final DeferredHolder<Item, FlailItem> NETHERITE_FLAIL =
            REGISTRY.register("netherite_flail", () -> new FlailItem(Tiers.NETHERITE, 3, NETHERITE_SPIKY_BALL.get(),
                    defaultBuilder().durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, FlintlockItem> FLINTLOCK_PISTOL =
            REGISTRY.register("flintlock_pistol", () -> new FlintlockItem(defaultBuilder().durability(32),
                    true, 5.5D, 2F, 1F, 0.05D));

    public static final DeferredHolder<Item, FlintlockItem> MUSKET =
            REGISTRY.register("musket", () -> new FlintlockItem(defaultBuilder().durability(64),
                    false, 10D, 1.5F, 1F, 0.2D));

    public static final DeferredHolder<Item, FlintlockItem> BLUNDERBUSS =
            REGISTRY.register("blunderbuss", () -> new FlintlockItem(defaultBuilder().durability(80),
                    false, 12D, 1.2F, 8F, 0.4D));

    public static final DeferredHolder<Item, Item> GUN_STOCK =
            REGISTRY.register("gun_stock", () -> new Item(defaultBuilder()));

    public static final DeferredHolder<Item, Item> MUSKET_PARTS =
            REGISTRY.register("musket_parts", () -> new Item(defaultBuilder()));

    public static final DeferredHolder<Item, Item> BLUNDERBUSS_PARTS =
            REGISTRY.register("blunderbuss_parts", () -> new Item(defaultBuilder()));

    public static final DeferredHolder<Item, Item> MUSKET_BALL =
            REGISTRY.register("musket_ball", () -> new Item(defaultBuilder().stacksTo(16)));

    public static final DeferredHolder<Item, Item> BUNDLE_SHOT =
            REGISTRY.register("bundle_shot", () -> new Item(defaultBuilder().stacksTo(16)));

    public static final DeferredHolder<Item, CannonItem> CANNON =
            REGISTRY.register("cannon", () -> new CannonItem(defaultBuilder().stacksTo(1)));

    public static final DeferredHolder<Item, Item> CANNON_BALL =
            REGISTRY.register("cannon_ball", () -> new Item(defaultBuilder().stacksTo(16)));

    public static final DeferredHolder<Item, CarcassRoundItem> CARCASS_ROUND =
            REGISTRY.register("carcass_round", () -> new CarcassRoundItem(defaultBuilder().stacksTo(16)));

    public static final DeferredHolder<Item, Item> SIEGE_ROUND =
            REGISTRY.register("siege_round", () -> new Item(defaultBuilder().stacksTo(16)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, LongBowItem> LONGBOW =
            REGISTRY.register("longbow", () -> new LongBowItem(defaultBuilder().durability(512)));

    public static final DeferredHolder<Item, BlockItem> EXPLORERS_ROPE =
            REGISTRY.register("explorers_rope", () -> new BlockItem(BrassArmoryBlocks.EXPLORERS_ROPE_BLOCK.get(), defaultBuilder()));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, SwordItem> KATANA =
            REGISTRY.register("katana", () -> new KatanaItem(Tiers.IRON, 3, -2F, true, defaultBuilder().durability(512)));

    public static final DeferredHolder<Item, GliderItem> GLIDER =
            REGISTRY.register("glider", () -> new GliderItem(defaultBuilder().durability(24)));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, WarpCrystalItem> PETTY_WARP_CRYSTAL =
            REGISTRY.register("petty_warp_crystal", () -> new WarpCrystalItem(defaultBuilder().stacksTo(1), 80));

    public static final DeferredHolder<Item, WarpCrystalItem> LESSER_WARP_CRYSTAL =
            REGISTRY.register("lesser_warp_crystal", () -> new WarpCrystalItem(defaultBuilder().stacksTo(1), 160));

    public static final DeferredHolder<Item, WarpCrystalItem> COMMON_WARP_CRYSTAL =
            REGISTRY.register("common_warp_crystal", () -> new WarpCrystalItem(defaultBuilder().stacksTo(1), 320));

    public static final DeferredHolder<Item, WarpCrystalItem> GREATER_WARP_CRYSTAL =
            REGISTRY.register("greater_warp_crystal", () -> new WarpCrystalItem(defaultBuilder().stacksTo(1), 640));

    public static final DeferredHolder<Item, WarpCrystalItem> GRAND_WARP_CRYSTAL =
            REGISTRY.register("grand_warp_crystal", () -> new WarpCrystalItem(defaultBuilder().stacksTo(1), 1280));

    //---------------------------------------------------------------------------------------------------------

    public static final DeferredHolder<Item, QuiverItem> QUIVER =
            REGISTRY.register("quiver", () -> new QuiverItem(defaultBuilder().stacksTo(1)));

    @NotNull
    public static Item.Properties defaultBuilder() {
        return new Item.Properties();
    }
    
    // Attach event handler for registry.
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
