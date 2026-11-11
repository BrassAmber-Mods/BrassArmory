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

public class BombCatchTrigger extends SimpleCriterionTrigger<BombCatchTrigger.Instance> {
    public static final Identifier ID = BrassArmory.locate("bomb_catch");
    @Override
    public Codec<Instance> codec() { return Instance.CODEC; }
    public void trigger(ServerPlayer player) { this.trigger(player, instance -> true); }

    public record Instance(Optional<Holder<LootItemCondition>> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Instance::player)
        ).apply(instance, Instance::new));
        public static Criterion<Instance> catchBomb() {
            return new Criterion<>(BrassArmoryAdvancements.CATCH_BOMB, new Instance(Optional.empty()));
        }
    }
}
