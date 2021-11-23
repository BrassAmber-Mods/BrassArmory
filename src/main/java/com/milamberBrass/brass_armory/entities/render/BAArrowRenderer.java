package com.milamberBrass.brass_armory.entities.render;

import com.milamberBrass.brass_armory.BrassArmory;
import com.milamberBrass.brass_armory.entities.BAArrowEntity;
import com.milamberBrass.brass_armory.util.ArrowType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class BAArrowRenderer extends ArrowRenderer<BAArrowEntity> {

    public static final ResourceLocation RES_ARROW = new ResourceLocation("textures/entity/projectiles/arrow.png");
    public static final ResourceLocation RES_DIRT_ARROW = BrassArmory.locate("textures/entity/projectile/dirt_arrow.png");
    public static final ResourceLocation RES_EX_ARROW = BrassArmory.locate("textures/entity/projectile/ex_arrow.png");
    public static final ResourceLocation RES_FROST_ARROW = BrassArmory.locate("textures/entity/projectile/frost_arrow.png");
    public static final ResourceLocation RES_GRASS_ARROW = BrassArmory.locate("textures/entity/projectile/grass_arrow.png");
    public static final ResourceLocation RES_LASER_ARROW = BrassArmory.locate("textures/entity/projectile/laser_arrow.png");
    public static final ResourceLocation RES_ROPE_ARROW = BrassArmory.locate("textures/entity/projectile/rope_arrow.png");
    public static final ResourceLocation RES_SLIME_ARROW = BrassArmory.locate("textures/entity/projectile/slime_arrow.png");
    public static final ResourceLocation RES_WARP_ARROW = BrassArmory.locate("textures/entity/projectile/warp_arrow.png");
    public static final ResourceLocation RES_FIRE_ARROW = BrassArmory.locate("textures/entity/projectile/fire_arrow.png");
    public static final ResourceLocation RES_CONCUSS_ARROW = BrassArmory.locate("textures/entity/projectile/concussion_arrow.png");


    public BAArrowRenderer(EntityRendererManager manager) {
        super(manager);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(BAArrowEntity entity) {
        ArrowType arrowType = entity.getArrowType();
        if (arrowType == ArrowType.DIRT) {
            return RES_DIRT_ARROW;
        } else if (arrowType == ArrowType.EXPLOSION) {
            return RES_EX_ARROW;
        } else if (arrowType == ArrowType.FROST) {
            return RES_FROST_ARROW;
        } else if (arrowType == ArrowType.GRASS) {
            return RES_GRASS_ARROW;
        } else if (arrowType == ArrowType.LASER) {
            return RES_LASER_ARROW;
        } else if (arrowType == ArrowType.ROPE) {
            return RES_ROPE_ARROW;
        } else if (arrowType == ArrowType.SLIME) {
            return RES_SLIME_ARROW;
        } else if (arrowType == ArrowType.WARP) {
            return RES_WARP_ARROW;
        } else if (arrowType == ArrowType.FIRE) {
            return RES_FIRE_ARROW;
        } else if (arrowType == ArrowType.CONCUSSION) {
            return RES_CONCUSS_ARROW;
        }
        return RES_ARROW;
    }

}