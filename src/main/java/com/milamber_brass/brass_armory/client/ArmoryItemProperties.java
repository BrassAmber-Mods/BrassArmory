package com.milamber_brass.brass_armory.client;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.HashMap;
import java.util.Map;

public record ArmoryItemProperties(Identifier name) implements RangeSelectItemModelProperty {
    public static final MapCodec<ArmoryItemProperties> MAP_CODEC = Identifier.CODEC.fieldOf("name").xmap(ArmoryItemProperties::new, ArmoryItemProperties::name);
    private static final ThreadLocal<ItemOwner> CURRENT_OWNER = new ThreadLocal<>();

    public static net.minecraft.world.entity.Entity ownerEntity() {
        return CURRENT_OWNER.get() instanceof net.minecraft.world.entity.Entity entity ? entity : null;
    }

    private static final Map<Item, Map<Identifier, Getter>> GETTERS = new HashMap<>();

    public static void register(Item item, Identifier name, Getter getter) {
        GETTERS.computeIfAbsent(item, key -> new HashMap<>()).put(name, getter);
    }

    @Override
    public float get(ItemStack stack, ClientLevel level, ItemOwner owner, int seed) {
        Getter getter = GETTERS.getOrDefault(stack.getItem(), Map.of()).get(name);
        ItemOwner previous = CURRENT_OWNER.get();
        CURRENT_OWNER.set(owner);
        try {
            return getter == null ? 0F : getter.get(stack, level, owner == null ? null : owner.asLivingEntity(), seed);
        } finally {
            if (previous == null) CURRENT_OWNER.remove();
            else CURRENT_OWNER.set(previous);
        }
    }

    @Override
    public MapCodec<ArmoryItemProperties> type() {
        return MAP_CODEC;
    }

    @FunctionalInterface
    public interface Getter {
        float get(ItemStack stack, ClientLevel level, LivingEntity owner, int seed);
    }
}
