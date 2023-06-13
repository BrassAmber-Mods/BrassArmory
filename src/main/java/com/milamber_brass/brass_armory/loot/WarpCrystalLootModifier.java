package com.milamber_brass.brass_armory.loot;

import com.google.common.base.Suppliers;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class WarpCrystalLootModifier extends LootModifier {
    public static final Supplier<Codec<WarpCrystalLootModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst).and(
                    ForgeRegistries.ITEMS.getCodec().fieldOf("loot_generator").forGetter(m -> m.loot_generator)
    ).apply(inst, WarpCrystalLootModifier::new)));

    private final Item loot_generator;

    public WarpCrystalLootModifier(LootItemCondition[] conditionsIn, Item loot_generator) {
        super(conditionsIn);
        this.loot_generator = loot_generator;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (ItemStack stack : generatedLoot) {
            if (stack.is(this.loot_generator)) {
                int roll = context.getRandom().nextInt(100) - (context.getLootingModifier() * 2) - (int)context.getLuck();
                if (roll < 2) generatedLoot.add(BrassArmoryItems.GRAND_WARP_CRYSTAL.get().getDefaultInstance());
                else if (roll < 6) generatedLoot.add(BrassArmoryItems.GREATER_WARP_CRYSTAL.get().getDefaultInstance());
                else if (roll < 14) generatedLoot.add(BrassArmoryItems.COMMON_WARP_CRYSTAL.get().getDefaultInstance());
                else if (roll < 30) generatedLoot.add(BrassArmoryItems.LESSER_WARP_CRYSTAL.get().getDefaultInstance());
                else if (roll < 62) generatedLoot.add(BrassArmoryItems.PETTY_WARP_CRYSTAL.get().getDefaultInstance());
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
