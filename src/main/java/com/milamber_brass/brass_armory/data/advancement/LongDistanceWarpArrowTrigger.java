package com.milamber_brass.brass_armory.data.advancement;

import com.google.gson.JsonObject;
import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LongDistanceWarpArrowTrigger extends SimpleCriterionTrigger<LongDistanceWarpArrowTrigger.Instance> {

    public static final ResourceLocation ID = BrassArmory.locate("long_distance_warp_arrow");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull Instance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext ctx) {
        return new LongDistanceWarpArrowTrigger.Instance(player);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(EntityPredicate.Composite player) {
            super(LongDistanceWarpArrowTrigger.ID, player);
        }

        public static @NotNull Instance longDistanceWarpArrow() {
            return new Instance(EntityPredicate.Composite.ANY);
        }
    }
}