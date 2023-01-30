package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.arrow.*;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BouncyBombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.StickyBombEntity;
import com.milamber_brass.brass_armory.item.*;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Bus.MOD)
public class BrassArmoryItems {

    // Item Registry
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BrassArmory.MOD_ID);

    //------------------------------------RANGED----------------------------------------------------------

    public static final RegistryObject<SpecialArrowItem> DIRT_ARROW =
            REGISTRY.register("dirt_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), DirtArrowEntity::new, DirtArrowEntity::new));

    public static final RegistryObject<SpecialArrowItem> EX_ARROW =
            REGISTRY.register("ex_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), ExplosionArrowEntity::new, ExplosionArrowEntity::new));

    public static final RegistryObject<SpecialArrowItem> FROST_ARROW =
            REGISTRY.register("frost_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), FrostArrowEntity::new, FrostArrowEntity::new));

    public static final RegistryObject<SpecialArrowItem> GRASS_ARROW =
            REGISTRY.register("grass_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), GrassArrowEntity::new, GrassArrowEntity::new));

    public static final RegistryObject<SpecialArrowItem> LASER_ARROW =
            REGISTRY.register("laser_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), LaserArrowEntity::new, LaserArrowEntity::new));

    public static final RegistryObject<SpecialArrowItem> ROPE_ARROW =
            REGISTRY.register("rope_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), RopeArrowEntity::new, RopeArrowEntity::new));

    public static final RegistryObject<SpecialArrowItem> SLIME_ARROW =
            REGISTRY.register("slime_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), SlimeArrowEntity::new, SlimeArrowEntity::new));

    public static final RegistryObject<SpecialArrowItem> WARP_ARROW =
            REGISTRY.register("warp_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), WarpArrowEntity::new, WarpArrowEntity::new));

    public static final RegistryObject<SpecialArrowItem> FIRE_ARROW =
            REGISTRY.register("fire_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), FireArrowEntity::new, FireArrowEntity::new));

    public static final RegistryObject<SpecialArrowItem> CONFUSION_ARROW =
            REGISTRY.register("confusion_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), ConfusionArrowEntity::new, ConfusionArrowEntity::new));

    public static final RegistryObject<SpecialArrowItem> TORCH_ARROW =
            REGISTRY.register("torch_arrow", () ->
                    new SpecialArrowItem(defaultBuilder(), TorchArrowEntity::new, TorchArrowEntity::new) {
                        @Override
                        public @NotNull AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity living) {
                            return Util.make(super.createArrow(level, stack, living), abstractArrow -> {
                                if (abstractArrow instanceof TorchArrowEntity torchArrow)
                                    torchArrow.setTorch(stack.getOrCreateTag()); //Should always be true, but fuck it
                            });
                        }

                        @Override
                        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
                            components.add((new TranslatableComponent(ArmoryUtil.loadStack(stack.getOrCreateTag(), "BATorch", Items.TORCH.getDefaultInstance()).getDescriptionId())).withStyle(ChatFormatting.GRAY));
                        }

                        @Override
                        public void fillItemCategory(CreativeModeTab creativeModeTab, NonNullList<ItemStack> items) {
                            if (this.allowdedIn(creativeModeTab)) {
                                for (Item item : ForgeRegistries.ITEMS.getValues()) {
                                    if (item instanceof StandingAndWallBlockItem torchItem && torchItem.getBlock() instanceof TorchBlock) {
                                        items.add(Util.make(this.getDefaultInstance(), stack -> ArmoryUtil.addStack(stack.getOrCreateTag(), torchItem.getDefaultInstance(), "BATorch")));
                                    }
                                }
                            }
                        }
                    });

    //--------------------------BOOMERANG-------------------------------

    public static final RegistryObject<BoomerangItem> WOODEN_BOOMERANG =
            REGISTRY.register("wooden_boomerang", () ->
                    new BoomerangItem(Tiers.WOOD, 2.0F, -2.9F, defaultBuilder()));

    public static final RegistryObject<BoomerangItem> GOLDEN_BOOMERANG =
            REGISTRY.register("golden_boomerang", () ->
                    new BoomerangItem(Tiers.GOLD, 2.0F, -2.9F, defaultBuilder()));

    public static final RegistryObject<BoomerangItem> STONE_BOOMERANG =
            REGISTRY.register("stone_boomerang", () ->
                    new BoomerangItem(Tiers.STONE, 2.0F, -2.9F, defaultBuilder()));

    public static final RegistryObject<BoomerangItem> IRON_BOOMERANG =
            REGISTRY.register("iron_boomerang", () ->
                    new BoomerangItem(Tiers.IRON, 2.0F, -2.9F, defaultBuilder()));

    public static final RegistryObject<BoomerangItem> DIAMOND_BOOMERANG =
            REGISTRY.register("diamond_boomerang", () ->
                    new BoomerangItem(Tiers.DIAMOND, 2.0F, -2.9F, defaultBuilder()));

    public static final RegistryObject<BoomerangItem> NETHERITE_BOOMERANG =
            REGISTRY.register("netherite_boomerang", () ->
                    new BoomerangItem(Tiers.NETHERITE, 2.0F, -2.9F, defaultBuilder()));


    //------------------------------------BOMB-----------------------------------------------------------

    public static final RegistryObject<BombItem> BOMB =
            REGISTRY.register("bomb", () ->
                    new BombItem(defaultBuilder().stacksTo(16), BombEntity::new, BombEntity::new));

    public static final RegistryObject<BombItem> BOUNCY_BOMB =
            REGISTRY.register("bouncy_bomb", () ->
                    new BombItem(defaultBuilder().stacksTo(16), BouncyBombEntity::new, BouncyBombEntity::new));

    public static final RegistryObject<BombItem> STICKY_BOMB =
            REGISTRY.register("sticky_bomb", () ->
                    new BombItem(defaultBuilder().stacksTo(16), StickyBombEntity::new, StickyBombEntity::new));


    //------------------------------------MELEE-----------------------------------------------------------

    public static final RegistryObject<SpearItem> GOLDEN_SPEAR =
            REGISTRY.register("golden_spear", () -> new SpearItem(Tiers.GOLD, 3, -2.6F, 1F,
                    defaultBuilder()));

    public static final RegistryObject<SpearItem> WOODEN_SPEAR =
            REGISTRY.register("wooden_spear", () -> new SpearItem(Tiers.WOOD, 3, -2.6F, 1F,
                    defaultBuilder()));

    public static final RegistryObject<SpearItem> STONE_SPEAR =
            REGISTRY.register("stone_spear", () -> new SpearItem(Tiers.STONE, 3, -2.6F, 1F,
                    defaultBuilder()));

    public static final RegistryObject<SpearItem> IRON_SPEAR =
            REGISTRY.register("iron_spear", () -> new SpearItem(Tiers.IRON, 3, -2.6F, 1F,
                    defaultBuilder().durability(250)));

    public static final RegistryObject<SpearItem> DIAMOND_SPEAR =
            REGISTRY.register("diamond_spear", () -> new SpearItem(Tiers.DIAMOND, 3, -2.6F, 1F,
                    defaultBuilder().durability(1562)));

    public static final RegistryObject<SpearItem> NETHERITE_SPEAR =
            REGISTRY.register("netherite_spear", () -> new SpearItem(Tiers.NETHERITE, 3, -2.6F, 1F,
                    defaultBuilder().durability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<FireRodItem> FIRE_ROD =
            REGISTRY.register("fire_rod", () -> new FireRodItem(Tiers.WOOD, 1, -2.6F, 1F, ParticleTypes.FLAME,
                    defaultBuilder().durability(8)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<DaggerItem> GOLDEN_DAGGER =
            REGISTRY.register("golden_dagger", () -> new DaggerItem(Tiers.GOLD, 1.0F, -2.0F, -1F,
                    defaultBuilder().durability(32)));

    public static final RegistryObject<DaggerItem> WOODEN_DAGGER =
            REGISTRY.register("wooden_dagger", () -> new DaggerItem(Tiers.WOOD, 1.0F, -2.0F, -1F,
                    defaultBuilder().durability(59)));

    public static final RegistryObject<DaggerItem> STONE_DAGGER =
            REGISTRY.register("stone_dagger", () -> new DaggerItem(Tiers.STONE, 1.0F, -2.0F, -1F,
                    defaultBuilder().durability(131)));

    public static final RegistryObject<DaggerItem> IRON_DAGGER =
            REGISTRY.register("iron_dagger", () -> new DaggerItem(Tiers.IRON, 1.0F, -2.0F, -1F,
                    defaultBuilder().durability(250)));

    public static final RegistryObject<DaggerItem> DIAMOND_DAGGER =
            REGISTRY.register("diamond_dagger", () -> new DaggerItem(Tiers.DIAMOND, 1.0F, -2.0F, -1F,
                    defaultBuilder().durability(1562)));

    public static final RegistryObject<DaggerItem> NETHERITE_DAGGER =
            REGISTRY.register("netherite_dagger", () -> new DaggerItem(Tiers.NETHERITE, 1.0F, -2.0F, -1F,
                    defaultBuilder().defaultDurability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<BattleaxeItem> GOLDEN_BATTLEAXE =
            REGISTRY.register("golden_battleaxe", () -> new BattleaxeItem(Tiers.GOLD, 7.0F, -3.1F,
                    defaultBuilder().durability(32)));

    public static final RegistryObject<BattleaxeItem> WOODEN_BATTLEAXE =
            REGISTRY.register("wooden_battleaxe", () -> new BattleaxeItem(Tiers.WOOD, 7.0F, -3.3F,
                    defaultBuilder().durability(59)));

    public static final RegistryObject<BattleaxeItem> STONE_BATTLEAXE =
            REGISTRY.register("stone_battleaxe", () -> new BattleaxeItem(Tiers.STONE, 8.0F, -3.3F,
                    defaultBuilder().durability(131)));

    public static final RegistryObject<BattleaxeItem> IRON_BATTLEAXE =
            REGISTRY.register("iron_battleaxe", () -> new BattleaxeItem(Tiers.IRON, 7.0F, -3.2F,
                    defaultBuilder().durability(250)));

    public static final RegistryObject<BattleaxeItem> DIAMOND_BATTLEAXE =
            REGISTRY.register("diamond_battleaxe", () -> new BattleaxeItem(Tiers.DIAMOND, 6.0F, -3.1F,
                    defaultBuilder().durability(1562)));

    public static final RegistryObject<BattleaxeItem> NETHERITE_BATTLEAXE =
            REGISTRY.register("netherite_battleaxe", () -> new BattleaxeItem(Tiers.NETHERITE, 6.0F, -3.1F,
                    defaultBuilder().defaultDurability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<HalberdItem> GOLDEN_HALBERD =
            REGISTRY.register("golden_halberd", () -> new HalberdItem(Tiers.GOLD,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder().durability(32)));

    public static final RegistryObject<HalberdItem> WOODEN_HALBERD =
            REGISTRY.register("wooden_halberd", () -> new HalberdItem(Tiers.WOOD,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder().durability(59)));

    public static final RegistryObject<HalberdItem> STONE_HALBERD =
            REGISTRY.register("stone_halberd", () -> new HalberdItem(Tiers.STONE,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder().durability(131)));

    public static final RegistryObject<HalberdItem> IRON_HALBERD =
            REGISTRY.register("iron_halberd", () -> new HalberdItem(Tiers.IRON,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder().durability(250)));

    public static final RegistryObject<HalberdItem> DIAMOND_HALBERD =
            REGISTRY.register("diamond_halberd", () -> new HalberdItem(Tiers.DIAMOND,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder().durability(1562)));

    public static final RegistryObject<HalberdItem> NETHERITE_HALBERD =
            REGISTRY.register("netherite_halberd", () -> new HalberdItem(Tiers.NETHERITE,
                    5.0F,-3.4F, 0.5F,
                    3.0F, -3.1F, 1.0F,
                    defaultBuilder().defaultDurability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<MaceItem> GOLDEN_MACE =
            REGISTRY.register("golden_mace", () -> new MaceItem(Tiers.GOLD, 9.0F, -3.3F,
                    defaultBuilder().durability(32)));

    public static final RegistryObject<MaceItem> WOODEN_MACE =
            REGISTRY.register("wooden_mace", () -> new MaceItem(Tiers.WOOD, 9.0F, -3.5F,
                    defaultBuilder().durability(59)));

    public static final RegistryObject<MaceItem> STONE_MACE =
            REGISTRY.register("stone_mace", () -> new MaceItem(Tiers.STONE, 10.0F, -3.5F,
                    defaultBuilder().durability(131)));

    public static final RegistryObject<MaceItem> IRON_MACE =
            REGISTRY.register("iron_mace", () -> new MaceItem(Tiers.IRON, 9.0F, -3.4F,
                    defaultBuilder().durability(250)));

    public static final RegistryObject<MaceItem> DIAMOND_MACE =
            REGISTRY.register("diamond_mace", () -> new MaceItem(Tiers.DIAMOND, 8.0F, -3.3F,
                    defaultBuilder().durability(1562)));

    public static final RegistryObject<MaceItem> NETHERITE_MACE =
            REGISTRY.register("netherite_mace", () -> new MaceItem(Tiers.NETHERITE, 8.0F, -3.3F,
                    defaultBuilder().defaultDurability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<SpikyBallItem> GOLDEN_SPIKY_BALL =
            REGISTRY.register("golden_spiky_ball", () -> new SpikyBallItem(Tiers.GOLD, defaultBuilder()));

    public static final RegistryObject<SpikyBallItem> WOODEN_SPIKY_BALL =
            REGISTRY.register("wooden_spiky_ball", () -> new SpikyBallItem(Tiers.WOOD, defaultBuilder()));

    public static final RegistryObject<SpikyBallItem> STONE_SPIKY_BALL =
            REGISTRY.register("stone_spiky_ball", () -> new SpikyBallItem(Tiers.STONE, defaultBuilder()));

    public static final RegistryObject<SpikyBallItem> IRON_SPIKY_BALL =
            REGISTRY.register("iron_spiky_ball", () -> new SpikyBallItem(Tiers.IRON, defaultBuilder()));

    public static final RegistryObject<SpikyBallItem> DIAMOND_SPIKY_BALL =
            REGISTRY.register("diamond_spiky_ball", () -> new SpikyBallItem(Tiers.DIAMOND, defaultBuilder()));

    public static final RegistryObject<SpikyBallItem> NETHERITE_SPIKY_BALL =
            REGISTRY.register("netherite_spiky_ball", () -> new SpikyBallItem(Tiers.NETHERITE, defaultBuilder()));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<FlailItem> GOLDEN_FLAIL =
            REGISTRY.register("golden_flail", () -> new FlailItem(Tiers.GOLD, 3, GOLDEN_SPIKY_BALL.get(),
                    defaultBuilder().durability(32)));

    public static final RegistryObject<FlailItem> WOODEN_FLAIL =
            REGISTRY.register("wooden_flail", () -> new FlailItem(Tiers.WOOD, 3, WOODEN_SPIKY_BALL.get(),
                    defaultBuilder().durability(59)));

    public static final RegistryObject<FlailItem> STONE_FLAIL =
            REGISTRY.register("stone_flail", () -> new FlailItem(Tiers.STONE, 3, STONE_SPIKY_BALL.get(),
                    defaultBuilder().durability(131)));

    public static final RegistryObject<FlailItem> IRON_FLAIL =
            REGISTRY.register("iron_flail", () -> new FlailItem(Tiers.IRON, 3, IRON_SPIKY_BALL.get(),
                    defaultBuilder().durability(250)));

    public static final RegistryObject<FlailItem> DIAMOND_FLAIL =
            REGISTRY.register("diamond_flail", () -> new FlailItem(Tiers.DIAMOND, 3, DIAMOND_SPIKY_BALL.get(),
                    defaultBuilder().durability(1562)));

    public static final RegistryObject<FlailItem> NETHERITE_FLAIL =
            REGISTRY.register("netherite_flail", () -> new FlailItem(Tiers.NETHERITE, 3, NETHERITE_SPIKY_BALL.get(),
                    defaultBuilder().defaultDurability(2031)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<FlintlockItem> FLINTLOCK_PISTOL =
            REGISTRY.register("flintlock_pistol", () -> new FlintlockItem(defaultBuilder().defaultDurability(32),
                    true, 5.5D, 2F, 1F, 0.05D));

    public static final RegistryObject<FlintlockItem> MUSKET =
            REGISTRY.register("musket", () -> new FlintlockItem(defaultBuilder().defaultDurability(64),
                    false, 10D, 1.5F, 1F, 0.2D));

    public static final RegistryObject<FlintlockItem> BLUNDERBUSS =
            REGISTRY.register("blunderbuss", () -> new FlintlockItem(defaultBuilder().defaultDurability(80),
                    false, 12D, 1.2F, 8F, 0.4D));

    public static final RegistryObject<Item> GUN_STOCK =
            REGISTRY.register("gun_stock", () -> new Item(defaultBuilder()));

    public static final RegistryObject<Item> MUSKET_PARTS =
            REGISTRY.register("musket_parts", () -> new Item(defaultBuilder()));

    public static final RegistryObject<Item> BLUNDERBUSS_PARTS =
            REGISTRY.register("blunderbuss_parts", () -> new Item(defaultBuilder()));

    public static final RegistryObject<Item> MUSKET_BALL =
            REGISTRY.register("musket_ball", () -> new Item(defaultBuilder().stacksTo(16)));

    public static final RegistryObject<Item> BUNDLE_SHOT =
            REGISTRY.register("bundle_shot", () -> new Item(defaultBuilder().stacksTo(16)));

    public static final RegistryObject<CannonItem> CANNON =
            REGISTRY.register("cannon", () -> new CannonItem(defaultBuilder().stacksTo(1)));

    public static final RegistryObject<Item> CANNON_BALL =
            REGISTRY.register("cannon_ball", () -> new Item(defaultBuilder().stacksTo(16)));

    public static final RegistryObject<CarcassRoundItem> CARCASS_ROUND =
            REGISTRY.register("carcass_round", () -> new CarcassRoundItem(defaultBuilder().stacksTo(16)));

    public static final RegistryObject<Item> SIEGE_ROUND =
            REGISTRY.register("siege_round", () -> new Item(defaultBuilder().stacksTo(16)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<LongBowItem> LONGBOW =
            REGISTRY.register("longbow", () -> new LongBowItem(defaultBuilder().defaultDurability(512)));

    public static final RegistryObject<BlockItem> EXPLORERS_ROPE =
            REGISTRY.register("explorers_rope", () -> new BlockItem(BrassArmoryBlocks.EXPLORERS_ROPE_BLOCK.get(), defaultBuilder()));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<SwordItem> KATANA =
            REGISTRY.register("katana", () -> new KatanaItem(Tiers.IRON, 3, -2F, true, defaultBuilder().defaultDurability(512)));

    public static final RegistryObject<GliderItem> GLIDER =
            REGISTRY.register("glider", () -> new GliderItem(defaultBuilder().defaultDurability(24)));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<WarpCrystalItem> PETTY_WARP_CRYSTAL =
            REGISTRY.register("petty_warp_crystal", () -> new WarpCrystalItem(defaultBuilder().stacksTo(1), 80));

    public static final RegistryObject<WarpCrystalItem> LESSER_WARP_CRYSTAL =
            REGISTRY.register("lesser_warp_crystal", () -> new WarpCrystalItem(defaultBuilder().stacksTo(1), 160));

    public static final RegistryObject<WarpCrystalItem> COMMON_WARP_CRYSTAL =
            REGISTRY.register("common_warp_crystal", () -> new WarpCrystalItem(defaultBuilder().stacksTo(1), 320));

    public static final RegistryObject<WarpCrystalItem> GREATER_WARP_CRYSTAL =
            REGISTRY.register("greater_warp_crystal", () -> new WarpCrystalItem(defaultBuilder().stacksTo(1), 640));

    public static final RegistryObject<WarpCrystalItem> GRAND_WARP_CRYSTAL =
            REGISTRY.register("grand_warp_crystal", () -> new WarpCrystalItem(defaultBuilder().stacksTo(1), 1280));

    //---------------------------------------------------------------------------------------------------------

    public static final RegistryObject<QuiverItem> QUIVER =
            REGISTRY.register("quiver", () -> new QuiverItem(defaultBuilder().stacksTo(1)));

    @NotNull
    public static Item.Properties defaultBuilder() {
        return new Item.Properties().tab(BrassArmoryItemGroups.BRASS_ARMORY);
    }
    
    // Attach event handler for registry.
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
