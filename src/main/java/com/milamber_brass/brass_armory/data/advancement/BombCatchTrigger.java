package com.milamber_brass.brass_armory.data.advancement;

import com.google.gson.JsonObject;
import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BombCatchTrigger extends SimpleCriterionTrigger<BombCatchTrigger.Instance> {

    public static final ResourceLocation ID = BrassArmory.locate("bomb_catch");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull Instance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext ctx) {
        return new BombCatchTrigger.Instance(predicate);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate player) {
            super(BombCatchTrigger.ID, player);
        }

        public static @NotNull Instance catchBomb() {
            return new Instance(ContextAwarePredicate.ANY);
        }
    }
}