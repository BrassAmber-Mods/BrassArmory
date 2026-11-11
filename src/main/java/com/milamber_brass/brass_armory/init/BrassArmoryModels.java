package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.client.model.CannonModel;
import com.milamber_brass.brass_armory.client.render.BulletRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class BrassArmoryModels {
    public static final ModelLayerLocation BULLET_MODEL = new ModelLayerLocation(BrassArmory.locate("bullet_model"), "main");
    public static final ModelLayerLocation CANNON_MODEL = new ModelLayerLocation(BrassArmory.locate("cannon_model"), "main");

        public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BULLET_MODEL, BulletRenderer::createBodyLayer);
        event.registerLayerDefinition(CANNON_MODEL, CannonModel::createBodyLayer);
    }
}
