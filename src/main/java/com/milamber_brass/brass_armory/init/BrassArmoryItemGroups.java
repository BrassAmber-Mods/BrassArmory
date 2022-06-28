package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.item.BABaseArrowItem;
import com.milamber_brass.brass_armory.item.BombItem;
import com.milamber_brass.brass_armory.item.KatanaItem;
import com.milamber_brass.brass_armory.item.abstracts.AbstractGunItem;
import com.milamber_brass.brass_armory.item.ammo_behaviour.AbstractAmmoBehaviour;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BrassArmoryItemGroups {

    public static final CreativeModeTab BRASS_ARMORY = new CreativeModeTab("brass_armory") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BrassArmoryItems.GOLDEN_BATTLEAXE.get());
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }

        @Override
        public int getSearchbarWidth() {
            return 79;
        }

        @Nonnull
        @Override
        public ResourceLocation getBackgroundImage() {
            return BrassArmory.locate("textures/gui/creative_tab.png");
        }

        @Override
        public int getLabelColor() {
            return 16755200;
        }

        @Override
        @ParametersAreNonnullByDefault
        public void fillItemList(NonNullList<ItemStack> itemStacks) {
            List<KatanaItem> katanaItems = new ArrayList<>();
            List<TieredItem> tieredItems = new ArrayList<>();
            List<BowItem> bowItems = new ArrayList<>();
            List<AbstractGunItem> gunItems = new ArrayList<>();
            List<Item> ammoItems = new ArrayList<>();
            List<BombItem> bombItems = new ArrayList<>();
            List<BABaseArrowItem> arrowItems = new ArrayList<>();
            List<BlockItem> blockItems = new ArrayList<>();

            List<Item> allItems = ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.getCreativeTabs().contains(this)).filter(item -> {
                if (item instanceof KatanaItem katanaItem) {
                    katanaItems.add(katanaItem);
                    return false;
                } else if (item instanceof TieredItem tieredItem) {
                    tieredItems.add(tieredItem);
                    return false;
                } else if (item instanceof BowItem bowItem) {
                    bowItems.add(bowItem);
                    return false;
                } else if (item instanceof AbstractGunItem gunItem) {
                    gunItems.add(gunItem);
                    return false;
                } else if (item instanceof BombItem bombItem) {
                    bombItems.add(bombItem);
                    return false;
                } else if (item instanceof BABaseArrowItem arrowItem) {
                    arrowItems.add(arrowItem);
                    return false;
                } else if (item instanceof BlockItem blockItem) {
                    blockItems.add(blockItem);
                    return false;
                } else if (AbstractAmmoBehaviour.itemHasAmmoBehaviour(item)) {
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
            })).forEach(item -> item.fillItemCategory(this, itemStacks));

            katanaItems.forEach(item -> item.fillItemCategory(this, itemStacks));
            gunItems.stream().sorted(Comparator.comparingDouble(AbstractGunItem::getDamageMultiplier)).forEach(item -> item.fillItemCategory(this, itemStacks));
            ammoItems.forEach(item -> item.fillItemCategory(this, itemStacks));
            bombItems.forEach(item -> item.fillItemCategory(this, itemStacks));
            allItems.forEach(item -> item.fillItemCategory(this, itemStacks));
            bowItems.forEach(item -> item.fillItemCategory(this, itemStacks));
            arrowItems.forEach(item -> item.fillItemCategory(this, itemStacks));
            blockItems.forEach(item -> item.fillItemCategory(this, itemStacks));
        }
    };

}
