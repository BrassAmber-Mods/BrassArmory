package com.milamber_brass.brass_armory.data;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.data.advancement.*;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.item.KatanaItem;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class BrassAdvancementProvider extends AdvancementProvider {

    public BrassAdvancementProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Advancement root = Advancement.Builder.advancement()
                .display(BrassArmoryItems.GOLDEN_BATTLEAXE.get(), new TranslatableComponent("itemGroup.brass_armory"), new TranslatableComponent("advancement.brass_armory.root.desc"), BrassArmory.locate("textures/gui/advancements.png"), FrameType.TASK, false, false, false)
                .requirements(RequirementsStrategy.OR).addCriterion("killed_something", KilledTrigger.TriggerInstance.playerKilledEntity()).addCriterion("killed_by_something", KilledTrigger.TriggerInstance.entityKilledPlayer())
                .save(consumer, this.savedName("root"));

        Advancement katana = Advancement.Builder.advancement().parent(root)
                .display(BrassArmoryItems.KATANA.get(), new TranslatableComponent("advancement.brass_armory.katana"), new TranslatableComponent("advancement.brass_armory.katana.desc"), null, FrameType.TASK, true, true, false)
                .addCriterion("katana", InventoryChangeTrigger.TriggerInstance.hasItems(BrassArmoryItems.KATANA.get()))
                .save(consumer, this.savedName("katana"));

        ItemStack witheredKatana = new ItemStack(BrassArmoryItems.KATANA.get());
        KatanaItem.setWither(witheredKatana, 100);

        Advancement.Builder.advancement().parent(katana)
                .display(witheredKatana, new TranslatableComponent("advancement.brass_armory.withered_katana"), new TranslatableComponent("advancement.brass_armory.withered_katana.desc"), null, FrameType.CHALLENGE, true, true, false)
                .addCriterion("wither_katana", WitherKatanaTrigger.Instance.witherKatana())
                .save(consumer, this.savedName("withered_katana"));

        Advancement.Builder.advancement().parent(root)
                .display(BrassArmoryItems.DIAMOND_MACE.get(), new TranslatableComponent("advancement.brass_armory.mace_smash"), new TranslatableComponent("advancement.brass_armory.mace_smash.desc"), null, FrameType.TASK, true, true, false)
                .addCriterion("mace_smash", MaceSmashTrigger.Instance.maceSmash())
                .save(consumer, this.savedName("mace_smash"));

        Advancement light_bomb = Advancement.Builder.advancement().parent(root)
                .display(BrassArmoryItems.BOMB.get(), new TranslatableComponent("advancement.brass_armory.light_bomb"), new TranslatableComponent("advancement.brass_armory.light_bomb.desc"), null, FrameType.TASK, true, true, false)
                .addCriterion("light_bomb", BombLightTrigger.Instance.lightBomb())
                .save(consumer, this.savedName("light_bomb"));

        ItemStack litBomb = new ItemStack(BrassArmoryItems.STICKY_BOMB.get());
        litBomb.getOrCreateTag().putBoolean("GuiDisplay", true);

        Advancement.Builder.advancement().parent(light_bomb)
                .display(litBomb, new TranslatableComponent("advancement.brass_armory.bomb_catch"), new TranslatableComponent("advancement.brass_armory.bomb_catch.desc"), null, FrameType.CHALLENGE, true, true, false)
                .addCriterion("bomb_catch", BombCatchTrigger.Instance.catchBomb())
                .save(consumer, this.savedName("bomb_catch"));

        Advancement longbow = Advancement.Builder.advancement().parent(root)
                .display(BrassArmoryItems.LONGBOW.get(), new TranslatableComponent("advancement.brass_armory.longbow"), new TranslatableComponent("advancement.brass_armory.longbow.desc"), null, FrameType.TASK, true, true, false)
                .addCriterion("longbow", LongbowTrigger.Instance.longbow())
                .save(consumer, this.savedName("longbow"));

        Advancement.Builder.advancement().parent(longbow)
                .display(BrassArmoryItems.WARP_ARROW.get(), new TranslatableComponent("advancement.brass_armory.long_distance_warp_arrow"), new TranslatableComponent("advancement.brass_armory.long_distance_warp_arrow.desc"), null, FrameType.CHALLENGE, true, true, false)
                .addCriterion("long_distance_warp_arrow", LongDistanceWarpArrowTrigger.Instance.longDistanceWarpArrow())
                .save(consumer, this.savedName("long_distance_warp_arrow"));
    }

    private @NotNull String savedName(String name) {
        return "brass_armory:achievements/" + name;
    }

    @Override
    public @NotNull String getName() {
        return "Brass's Armory Advancements";
    }
}
