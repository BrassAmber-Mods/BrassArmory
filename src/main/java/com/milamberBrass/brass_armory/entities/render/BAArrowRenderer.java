package com.milamberBrass.brass_armory.entities.render;

import com.milamberBrass.brass_armory.BrassArmory;
import com.milamberBrass.brass_armory.entities.custom.BAArrowEntity;
import com.milamberBrass.brass_armory.util.ArrowType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BAArrowRenderer extends ArrowRenderer<BAArrowEntity> {
   public static final ResourceLocation RES_ARROW = new ResourceLocation("textures/entity/projectiles/arrow.png");
   public static final ResourceLocation RES_DIRT_ARROW = new ResourceLocation(BrassArmory.MOD_ID, "entity/projectile/dirt_arrow");
   public static final ResourceLocation RES_EX_ARROW = new ResourceLocation(BrassArmory.MOD_ID, "entity/projectile/dirt_arrow");
   public static final ResourceLocation RES_FROST_ARROW = new ResourceLocation(BrassArmory.MOD_ID, "entity/projectile/dirt_arrow");
   public static final ResourceLocation RES_GRASS_ARROW = new ResourceLocation(BrassArmory.MOD_ID, "entity/projectile/dirt_arrow");
   public static final ResourceLocation RES_LASER_ARROW = new ResourceLocation(BrassArmory.MOD_ID, "entity/projectile/dirt_arrow");
   public static final ResourceLocation RES_ROPE_ARROW = new ResourceLocation(BrassArmory.MOD_ID, "entity/projectile/dirt_arrow");
   public static final ResourceLocation RES_SLIME_ARROW = new ResourceLocation(BrassArmory.MOD_ID, "entity/projectile/dirt_arrow");
   public static final ResourceLocation RES_WARP_ARROW = new ResourceLocation(BrassArmory.MOD_ID, "entity/projectile/dirt_arrow");


   public BAArrowRenderer(EntityRendererManager manager) {
      super(manager);
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getEntityTexture(BAArrowEntity entity) {
      if (entity.arrowType == ArrowType.DIRT) {
         return RES_DIRT_ARROW;
      } else if (entity.arrowType == ArrowType.EXPLOSION) {
         return RES_EX_ARROW;
      } else if (entity.arrowType == ArrowType.FROST) {
         return RES_FROST_ARROW;
      }else if (entity.arrowType == ArrowType.GRASS) {
         return RES_GRASS_ARROW;
      }else if (entity.arrowType == ArrowType.LASER) {
         return RES_LASER_ARROW;
      }else if (entity.arrowType == ArrowType.ROPE) {
         return RES_ROPE_ARROW;
      }else if (entity.arrowType == ArrowType.SLIME) {
         return RES_SLIME_ARROW;
      }else if (entity.arrowType == ArrowType.WARP) {
         return RES_WARP_ARROW;
      }
      return RES_ARROW;
   }
}