package com.milamber_brass.brass_armory.loot;

import com.google.common.base.Suppliers;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Supplier;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class WarpCrystalLootModifier extends LootModifier {
    public static final Supplier<MapCodec<WarpCrystalLootModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("loot_generator").forGetter(m -> m.loot_generator)
    ).apply(inst, WarpCrystalLootModifier::new)));

    private final Item loot_generator;

    public WarpCrystalLootModifier(LootItemCondition[] conditionsIn, Item loot_generator) {
        super(conditionsIn);
        this.loot_generator = loot_generator;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ObjectArrayList<ItemStack> extraDrops = new ObjectArrayList<>();
        var attacker = context.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        int looting = attacker instanceof LivingEntity living
                ? EnchantmentHelper.getEnchantmentLevel(context.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), living)
                : 0;
        for (ItemStack stack : generatedLoot) {
            if (stack.is(this.loot_generator)) {
                int roll = context.getRandom().nextInt(100) - (looting * 2) - (int)context.getLuck();
                if (roll < 2) extraDrops.add(BrassArmoryItems.GRAND_WARP_CRYSTAL.get().getDefaultInstance());
                else if (roll < 6) extraDrops.add(BrassArmoryItems.GREATER_WARP_CRYSTAL.get().getDefaultInstance());
                else if (roll < 14) extraDrops.add(BrassArmoryItems.COMMON_WARP_CRYSTAL.get().getDefaultInstance());
                else if (roll < 30) extraDrops.add(BrassArmoryItems.LESSER_WARP_CRYSTAL.get().getDefaultInstance());
                else if (roll < 62) extraDrops.add(BrassArmoryItems.PETTY_WARP_CRYSTAL.get().getDefaultInstance());
            }
        }
        generatedLoot.addAll(extraDrops);
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
