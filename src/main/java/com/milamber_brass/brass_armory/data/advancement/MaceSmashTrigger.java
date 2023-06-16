package com.milamber_brass.brass_armory.data.advancement;

import com.google.gson.JsonObject;
import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MaceSmashTrigger extends SimpleCriterionTrigger<MaceSmashTrigger.Instance> {

    public static final ResourceLocation ID = BrassArmory.locate("mace_smash");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext ctx) {
        return new MaceSmashTrigger.Instance(player);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate player) {
            super(MaceSmashTrigger.ID, player);
        }

        public static @NotNull Instance maceSmash() {
            return new Instance(ContextAwarePredicate.ANY);
        }
    }
}