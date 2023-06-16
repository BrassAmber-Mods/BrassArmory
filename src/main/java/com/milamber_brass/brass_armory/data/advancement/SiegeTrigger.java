package com.milamber_brass.brass_armory.data.advancement;

import com.google.gson.JsonObject;
import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SiegeTrigger extends SimpleCriterionTrigger<SiegeTrigger.Instance> {

    public static final ResourceLocation ID = BrassArmory.locate("siege");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull SiegeTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext ctx) {
        return new SiegeTrigger.Instance(player);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate player) {
            super(SiegeTrigger.ID, player);
        }

        public static @NotNull SiegeTrigger.Instance directHit() {
            return new SiegeTrigger.Instance(ContextAwarePredicate.ANY);
        }
    }
}