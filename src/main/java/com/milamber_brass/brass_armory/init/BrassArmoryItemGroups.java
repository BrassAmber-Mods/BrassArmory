package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.behaviour.GunBehaviours;
import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.item.*;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BrassArmoryItemGroups {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BrassArmory.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BRASS_ARMORY = REGISTRY.register("brass_armory", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.brass_armory"))
            .icon(() -> new ItemStack(BrassArmoryItems.GOLDEN_BATTLEAXE.get()))
            .withSearchBar(79)
            .withBackgroundLocation(BrassArmory.locate("textures/gui/creative_tab.png"))
            .withLabelColor(16755200)
            .displayItems((parameters, output) -> {
                List<KatanaItem> katanaItems = new ArrayList<>();
                List<TieredItem> tieredItems = new ArrayList<>();
                List<BowItem> bowItems = new ArrayList<>();
                List<FlintlockItem> gunItems = new ArrayList<>();
                List<Item> ammoItems = new ArrayList<>();
                List<BombItem> bombItems = new ArrayList<>();
                List<SpecialArrowItem> arrowItems = new ArrayList<>();
                List<BlockItem> blockItems = new ArrayList<>();
                List<WarpCrystalItem> warpCrystalItems = new ArrayList<>();
                List<GliderItem> gliderItems = new ArrayList<>();
                List<Item> cannonStuff = new ArrayList<>();

                List<Item> allItems = BrassArmoryItems.REGISTRY.getEntries().stream().map(RegistryObject::get).filter(item -> {
                    if (item instanceof KatanaItem katanaItem) {
                        katanaItems.add(katanaItem);
                        return false;
                    } else if (item instanceof TieredItem tieredItem) {
                        tieredItems.add(tieredItem);
                        return false;
                    } else if (item instanceof BowItem bowItem) {
                        bowItems.add(bowItem);
                        return false;
                    } else if (item instanceof CannonItem || (item.getDefaultInstance().is(BrassArmoryTags.Items.CANNON_AMMO) && !item.getDefaultInstance().is(BrassArmoryTags.Items.BOMB))) {
                        cannonStuff.add(item);
                        return false;
                    } else if (item instanceof FlintlockItem gunItem) {
                        gunItems.add(gunItem);
                        return false;
                    } else if (item instanceof BombItem bombItem) {
                        bombItems.add(bombItem);
                        return false;
                    } else if (item instanceof SpecialArrowItem arrowItem) {
                        arrowItems.add(arrowItem);
                        return false;
                    } else if (item instanceof BlockItem blockItem) {
                        blockItems.add(blockItem);
                        return false;
                    } else if (item instanceof WarpCrystalItem warpCrystalItem) {
                        warpCrystalItems.add(warpCrystalItem);
                        return false;
                    } else if (item instanceof GliderItem gliderItem) {
                        gliderItems.add(gliderItem);
                        return false;
                    } else if (GunBehaviours.itemHasAmmoBehaviour(item)) {
                        ammoItems.add(item);
                        return false;
                    }
                    return true;
                }).toList();

                //We sort the list like vanilla does, while also accounting for any possible addon items, just in case
                tieredItems.stream().sorted(Comparator.comparingDouble(tieredItem -> {
                    Tier tier = tieredItem.getTier();
                    if (tier == Tiers.WOOD) return 0.1D;
                    if (tier == Tiers.STONE) return 0.2D;
                    if (tier == Tiers.GOLD) return 0.3D;
                    if (tier == Tiers.IRON) return 0.4D;
                    if (tier == Tiers.DIAMOND) return 0.5D;
                    if (tier == Tiers.NETHERITE) return 0.6D;
                    return tieredItem.getTier().getAttackDamageBonus();
                })).forEach(output::accept);

                katanaItems.forEach(output::accept);
                output.accept(Util.make(BrassArmoryItems.KATANA.get().getDefaultInstance(), stack -> KatanaItem.setWither(stack, 100)));

                allItems.forEach(output::accept);
                //Adventure items
                blockItems.forEach(output::accept);
                warpCrystalItems.stream().sorted(Comparator.comparingInt(WarpCrystalItem::getMaxDistance)).forEach(output::accept);

                gliderItems.forEach(output::accept);

                gunItems.stream().sorted(Comparator.comparingDouble(FlintlockItem::getDamageMultiplier)).forEach(output::accept);
                ammoItems.forEach(output::accept);
                bombItems.forEach(output::accept);

                bowItems.forEach(output::accept);
                arrowItems.forEach(output::accept);

                for (Item item : ForgeRegistries.ITEMS.getValues()) {
                    if (item instanceof StandingAndWallBlockItem torchItem && torchItem.getBlock() instanceof TorchBlock) {
                        output.accept(Util.make(BrassArmoryItems.TORCH_ARROW.get().getDefaultInstance(), stack -> ArmoryUtil.addStack(stack.getOrCreateTag(), torchItem.getDefaultInstance(), "BATorch")));
                    }
                }

                cannonStuff.remove(BrassArmoryItems.CARCASS_ROUND.get());
                cannonStuff.forEach(output::accept);
                output.accept(BrassArmoryItems.CARCASS_ROUND.get());

                output.accept(Util.make(BrassArmoryItems.CARCASS_ROUND.get().getDefaultInstance(), stack -> stack.getOrCreateTag().putBoolean("BADragonRound", true)));
                for(Potion potion : ForgeRegistries.POTIONS.getValues()) {
                    if (potion != Potions.EMPTY) {
                        output.accept(PotionUtils.setPotion(BrassArmoryItems.CARCASS_ROUND.get().getDefaultInstance(), potion));
                    }
                }
            }).build());

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
