package com.milamber_brass.brass_armory.client.render;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractSpecialArrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class SpecialArrowRenderer extends ArrowRenderer<AbstractSpecialArrowEntity, SpecialArrowRenderer.State> {
    public static final Identifier DIRT_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/dirt_arrow.png");
    public static final Identifier EX_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/ex_arrow.png");
    public static final Identifier FROST_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/frost_arrow.png");
    public static final Identifier GRASS_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/grass_arrow.png");
    public static final Identifier LASER_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/laser_arrow.png");
    public static final Identifier ROPE_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/rope_arrow.png");
    public static final Identifier SLIME_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/slime_arrow.png");
    public static final Identifier WARP_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/warp_arrow.png");
    public static final Identifier FIRE_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/fire_arrow.png");
    public static final Identifier CONFUSION_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/confusion_arrow.png");
    public static final Identifier TORCH_ARROW_TEXTURE = BrassArmory.locate("textures/entity/projectile/torch_arrow.png");

    public SpecialArrowRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public static class State extends net.minecraft.client.renderer.entity.state.ArrowRenderState {
        public Identifier texture;
    }

    @Override
    public State createRenderState() { return new State(); }

    @Override
    public void extractRenderState(AbstractSpecialArrowEntity entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.texture = entity.getTextureLocation();
    }

    @Override
    protected Identifier getTextureLocation(State state) { return state.texture; }
}
