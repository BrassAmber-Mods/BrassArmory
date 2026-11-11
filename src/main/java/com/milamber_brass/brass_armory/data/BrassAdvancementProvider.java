package com.milamber_brass.brass_armory.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.worldgen.BootstrapContext;

public final class BrassAdvancementProvider {
    public static void bootstrap(BootstrapContext<Advancement> output) {
        new BrassAdvancementGenerator().generate(output);
    }
}
