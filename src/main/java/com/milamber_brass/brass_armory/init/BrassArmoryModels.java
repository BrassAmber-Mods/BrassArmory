package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.client.model.CannonModel;
import com.milamber_brass.brass_armory.client.render.BulletRenderer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@OnlyIn(Dist.CLIENT)
public class BrassArmoryModels {
    public static final ModelLayerLocation BULLET_MODEL = new ModelLayerLocation(BrassArmory.locate("bullet_model"), "main");
    public static final ModelLayerLocation CANNON_MODEL = new ModelLayerLocation(BrassArmory.locate("cannon_model"), "main");

    @ParametersAreNonnullByDefault
    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BULLET_MODEL, BulletRenderer::createBodyLayer);
        event.registerLayerDefinition(CANNON_MODEL, CannonModel::createBodyLayer);
    }
}
