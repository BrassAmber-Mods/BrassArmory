package com.milamber_brass.brass_armory.data;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.data.advancement.*;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.KatanaItem;
import com.milamber_brass.brass_armory.util.ItemData;
import java.util.function.Consumer;
import net.minecraft.util.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.triggers.KilledTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.NotNull;

public class BrassAdvancementGenerator  {

    public void generate(BootstrapContext<Advancement> consumer) {

        AdvancementHolder root = Advancement.Builder.advancement()
                .rootDisplay(BrassArmoryItems.GOLDEN_BATTLEAXE.get(), Component.translatable("itemGroup.brass_armory"), Component.translatable("advancement.brass_armory.root.desc"), BrassArmory.locate("gui/advancements"), AdvancementType.TASK, false, false, false)
                .requirements(AdvancementRequirements.Strategy.OR).addCriterion("killed_something", KilledTrigger.TriggerInstance.playerKilledEntity()).addCriterion("killed_by_something", KilledTrigger.TriggerInstance.entityKilledPlayer())
                .save(consumer, this.savedName("root"));

        AdvancementHolder katana = Advancement.Builder.advancement().parent(root)
                .display(BrassArmoryItems.KATANA.get(), Component.translatable("advancement.brass_armory.katana"), Component.translatable("advancement.brass_armory.katana.desc"), AdvancementType.TASK, true, true, false)
                .addCriterion("katana", InventoryChangeTrigger.TriggerInstance.hasItems(BrassArmoryItems.KATANA.get()))
                .save(consumer, this.savedName("katana"));

        ItemStack witheredKatana = new ItemStack(BrassArmoryItems.KATANA.get());
        KatanaItem.setWither(witheredKatana, 100);

        Advancement.Builder.advancement().parent(katana)
                .display(ItemStackTemplate.fromNonEmptyStack(witheredKatana), Component.translatable("advancement.brass_armory.withered_katana"), Component.translatable("advancement.brass_armory.withered_katana.desc"), AdvancementType.CHALLENGE, true, true, false)
                .addCriterion("wither_katana", WitherKatanaTrigger.Instance.witherKatana())
                .save(consumer, this.savedName("withered_katana"));

        Advancement.Builder.advancement().parent(root)
                .display(BrassArmoryItems.DIAMOND_MACE.get(), Component.translatable("advancement.brass_armory.mace_smash"), Component.translatable("advancement.brass_armory.mace_smash.desc"), AdvancementType.TASK, true, true, false)
                .addCriterion("mace_smash", MaceSmashTrigger.Instance.maceSmash())
                .save(consumer, this.savedName("mace_smash"));

        AdvancementHolder light_bomb = Advancement.Builder.advancement().parent(root)
                .display(BrassArmoryItems.BOMB.get(), Component.translatable("advancement.brass_armory.light_bomb"), Component.translatable("advancement.brass_armory.light_bomb.desc"), AdvancementType.TASK, true, true, false)
                .addCriterion("light_bomb", BombLightTrigger.Instance.lightBomb())
                .save(consumer, this.savedName("light_bomb"));

        ItemStack litBomb = new ItemStack(BrassArmoryItems.STICKY_BOMB.get());
        ItemData.update(litBomb, tag -> tag.putBoolean("GuiDisplay", true));

        Advancement.Builder.advancement().parent(light_bomb)
                .display(ItemStackTemplate.fromNonEmptyStack(litBomb), Component.translatable("advancement.brass_armory.bomb_catch"), Component.translatable("advancement.brass_armory.bomb_catch.desc"), AdvancementType.CHALLENGE, true, true, false)
                .addCriterion("bomb_catch", BombCatchTrigger.Instance.catchBomb())
                .save(consumer, this.savedName("bomb_catch"));

        AdvancementHolder longbow = Advancement.Builder.advancement().parent(root)
                .display(BrassArmoryItems.LONGBOW.get(), Component.translatable("advancement.brass_armory.longbow"), Component.translatable("advancement.brass_armory.longbow.desc"), AdvancementType.TASK, true, true, false)
                .addCriterion("longbow", LongbowTrigger.Instance.longbow())
                .save(consumer, this.savedName("longbow"));

        Advancement.Builder.advancement().parent(longbow)
                .display(BrassArmoryItems.WARP_ARROW.get(), Component.translatable("advancement.brass_armory.long_distance_warp_arrow"), Component.translatable("advancement.brass_armory.long_distance_warp_arrow.desc"), AdvancementType.CHALLENGE, true, true, false)
                .addCriterion("long_distance_warp_arrow", LongDistanceWarpArrowTrigger.Instance.longDistanceWarpArrow())
                .save(consumer, this.savedName("long_distance_warp_arrow"));

        AdvancementHolder gun = Advancement.Builder.advancement().parent(root)
                .display(BrassArmoryItems.MUSKET.get(), Component.translatable("advancement.brass_armory.gun"), Component.translatable("advancement.brass_armory.gun.desc"), AdvancementType.TASK, true, true, false)
                .addCriterion("gun", GunTrigger.Instance.fire())
                .save(consumer, this.savedName("gun"));

        AdvancementHolder cannon = Advancement.Builder.advancement().parent(gun)
                .display(BrassArmoryItems.CANNON.get(), Component.translatable("advancement.brass_armory.cannon"), Component.translatable("advancement.brass_armory.cannon.desc"), AdvancementType.TASK, true, true, false)
                .addCriterion("cannon", CannonTrigger.Instance.fire())
                .save(consumer, this.savedName("cannon"));

        Advancement.Builder.advancement().parent(cannon)
                .display(BrassArmoryItems.SIEGE_ROUND.get(), Component.translatable("advancement.brass_armory.siege"), Component.translatable("advancement.brass_armory.siege.desc"), AdvancementType.GOAL, true, true, false)
                .addCriterion("siege", SiegeTrigger.Instance.directHit())
                .save(consumer, this.savedName("siege"));

        Advancement.Builder.advancement().parent(cannon)
                .display(ItemStackTemplate.fromNonEmptyStack(Util.make(new ItemStack(BrassArmoryItems.CARCASS_ROUND.get()), stack -> ItemData.update(stack, tag -> tag.putBoolean("BADragonRound", true)))), Component.translatable("advancement.brass_armory.dragon_round"), Component.translatable("advancement.brass_armory.dragon_round.desc"), AdvancementType.CHALLENGE, true, true, false)
                .addCriterion("dragon_round", DragonRoundTrigger.Instance.land())
                .save(consumer, this.savedName("dragon_round"));
    }

    private @NotNull String savedName(String name) {
        return "brass_armory:achievements/" + name;
    }
}
