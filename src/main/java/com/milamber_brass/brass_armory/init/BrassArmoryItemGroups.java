package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.item.abstracts.ArmoryTieredItem;
import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.behaviour.GunBehaviours;
import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.item.*;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.util.ItemData;
import com.milamber_brass.brass_armory.util.PotionData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.TorchBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BrassArmoryItemGroups {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BrassArmory.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BRASS_ARMORY = REGISTRY.register("brass_armory", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.brass_armory"))
            .icon(() -> new ItemStack(BrassArmoryItems.GOLDEN_BATTLEAXE.get()))
            .withSearchBar(79)
            .backgroundTexture(BrassArmory.locate("textures/gui/creative_tab.png"))
            .withLabelColor(16755200)
            .displayItems((parameters, output) -> {
                List<KatanaItem> katanaItems = new ArrayList<>();
                List<ArmoryTieredItem> tieredItems = new ArrayList<>();
                List<BowItem> bowItems = new ArrayList<>();
                List<FlintlockItem> gunItems = new ArrayList<>();
                List<Item> ammoItems = new ArrayList<>();
                List<BombItem> bombItems = new ArrayList<>();
                List<SpecialArrowItem> arrowItems = new ArrayList<>();
                List<BlockItem> blockItems = new ArrayList<>();
                List<WarpCrystalItem> warpCrystalItems = new ArrayList<>();
                List<GliderItem> gliderItems = new ArrayList<>();
                List<Item> cannonStuff = new ArrayList<>();

                List<? extends Item> allItems = BrassArmoryItems.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(item -> {
                    if (item instanceof KatanaItem katanaItem) {
                        katanaItems.add(katanaItem);
                        return false;
                    } else if (item instanceof ArmoryTieredItem tieredItem) {
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
                    ToolMaterial tier = tieredItem.getTier();
                    if (tier == ToolMaterial.WOOD) return 0.1D;
                    if (tier == ToolMaterial.STONE) return 0.2D;
                    if (tier == ToolMaterial.GOLD) return 0.3D;
                    if (tier == ToolMaterial.IRON) return 0.4D;
                    if (tier == ToolMaterial.DIAMOND) return 0.5D;
                    if (tier == ToolMaterial.NETHERITE) return 0.6D;
                    return tieredItem.getTier().attackDamageBonus();
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

                for (Item item : BuiltInRegistries.ITEM) {
                    if (item instanceof StandingAndWallBlockItem torchItem && torchItem.getBlock() instanceof TorchBlock) {
                        output.accept(Util.make(BrassArmoryItems.TORCH_ARROW.get().getDefaultInstance(), stack -> ArmoryUtil.addStack(stack, torchItem.getDefaultInstance(), "BATorch")));
                    }
                }

                cannonStuff.remove(BrassArmoryItems.CARCASS_ROUND.get());
                cannonStuff.forEach(output::accept);
                output.accept(BrassArmoryItems.CARCASS_ROUND.get());

                output.accept(Util.make(BrassArmoryItems.CARCASS_ROUND.get().getDefaultInstance(), stack -> ItemData.update(stack, tag -> tag.putBoolean("BADragonRound", true))));
                for(var potion : BuiltInRegistries.POTION.listElements().toList()) {
                    if (!potion.value().getEffects().isEmpty()) {
                        output.accept(PotionData.setPotion(BrassArmoryItems.CARCASS_ROUND.get().getDefaultInstance(), potion));
                    }
                }
            }).build());

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
