package com.milamber_brass.brass_armory.data.advancement;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.init.BrassArmoryAdvancements;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public class WitherKatanaTrigger extends SimpleCriterionTrigger<WitherKatanaTrigger.Instance> {
    public static final Identifier ID = BrassArmory.locate("wither_katana");
    @Override
    public Codec<Instance> codec() { return Instance.CODEC; }
    public void trigger(ServerPlayer player) { this.trigger(player, instance -> true); }

    public record Instance(Optional<Holder<LootItemCondition>> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Instance::player)
        ).apply(instance, Instance::new));
        public static Criterion<Instance> witherKatana() {
            return new Criterion<>(BrassArmoryAdvancements.WITHER_KATANA_TRIGGER, new Instance(Optional.empty()));
        }
    }
}
