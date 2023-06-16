package com.milamber_brass.brass_armory.data.advancement;

import com.google.gson.JsonObject;
import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DragonRoundTrigger extends SimpleCriterionTrigger<DragonRoundTrigger.Instance> {

    public static final ResourceLocation ID = BrassArmory.locate("dragon_round");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull DragonRoundTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext ctx) {
        return new DragonRoundTrigger.Instance(player);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate player) {
            super(DragonRoundTrigger.ID, player);
        }

        public static @NotNull DragonRoundTrigger.Instance land() {
            return new DragonRoundTrigger.Instance(ContextAwarePredicate.ANY);
        }
    }
}