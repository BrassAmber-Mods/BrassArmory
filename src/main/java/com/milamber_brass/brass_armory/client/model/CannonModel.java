// Made with Blockbench 4.4.3
package com.milamber_brass.brass_armory.client.model;

import com.milamber_brass.brass_armory.entity.CannonEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class CannonModel<T extends CannonEntity> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart cannon_whole;
	private final ModelPart pedestal;
	private final ModelPart cannon;
	private final ModelPart cannon_barrel;
	private final ModelPart chamber_r1;
	public final ModelPart cannon_end;

	public CannonModel(ModelPart root) {
		this.root = root;
		this.cannon_whole = this.root.getChild("cannon_whole");
		this.cannon = this.cannon_whole.getChild("cannon");
		this.pedestal = this.cannon_whole.getChild("pedestal");
		this.cannon_barrel = this.cannon.getChild("cannon_barrel");
		this.chamber_r1 = this.cannon_barrel.getChild("chamber_r1");
		this.cannon_end = this.cannon_barrel.getChild("cannon_end");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition partRoot = mesh.getRoot();

		PartDefinition cannon_whole = partRoot.addOrReplaceChild("cannon_whole", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cannon = cannon_whole.addOrReplaceChild("cannon", CubeListBuilder.create()
						.texOffs(0, 68).addBox(-8.0F, -7.0F, -3.0F, 16.0F, 5.0F, 7.0F)
						.texOffs(0, 68).addBox(-8.0F, -12.0F, -3.0F, 16.0F, 5.0F, 7.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cannon_barrel = cannon.addOrReplaceChild("cannon_barrel", CubeListBuilder.create()
						.texOffs(48, 60).addBox(-6.0F, -5.5F, -6.0F, 12.0F, 12.0F, 13.0F),
				PartPose.offset(0.0F, -11.5F, 0.0F));

		cannon_barrel.addOrReplaceChild("chamber_r1", CubeListBuilder.create()
						.texOffs(112, 102).addBox(0.0F, -12.5F, 5.0F, 0.0F, 8.0F, 8.0F),
				PartPose.rotation( 0.0F, 3.1416F, 0.0F));

		cannon_barrel.addOrReplaceChild("cannon_end", CubeListBuilder.create()
						.texOffs(0, 81).addBox(-5.0F, -12.0F, -5.0F, 10.0F, 10.0F, 14.0F)
						.texOffs(0, 106).addBox(-6.0F, -13.0F, 9.0F, 12.0F, 12.0F, 3.0F)
						.texOffs(31, 112).addBox(-6.0F, -11.0F, 12.0F, 2.0F, 8.0F, 1.0F)
						.texOffs(46, 112).addBox(4.0F, -11.0F, 12.0F, 2.0F, 8.0F, 1.0F)
						.texOffs(31, 122).addBox(-6.0F, -3.0F, 12.0F, 12.0F, 2.0F, 1.0F)
						.texOffs(31, 107).addBox(-6.0F, -13.0F, 12.0F, 12.0F, 2.0F, 1.0F),
				PartPose.offset(0.0F, 7.5F, 10.0F));

		cannon_whole.addOrReplaceChild("pedestal", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-8.0F, -2.0F, -8.0F, 16.0F, 2.0F, 16.0F)
						.texOffs(0, 31).addBox(-10.0F, -3.0F, -10.0F, 20.0F, 1.0F, 20.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(T cannon, float limbSwing, float fuse, float partialTick, float yRot, float xRot) {
		this.cannon_barrel.xRot = xRot * -Mth.DEG_TO_RAD;
		this.cannon.yRot = yRot * Mth.DEG_TO_RAD;
	}
}