package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.client.render.BulletRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

import javax.annotation.ParametersAreNonnullByDefault;

public class BrassArmoryModels {
    public static final ModelLayerLocation BULLET_MODEL = new ModelLayerLocation(BrassArmory.locate("bullet_model"), "main");

    @ParametersAreNonnullByDefault
    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BULLET_MODEL, BulletRenderer::createBodyLayer);
    }
}
