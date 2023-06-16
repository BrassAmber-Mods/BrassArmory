package com.milamber_brass.brass_armory.data;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.block.RopeBlock;
import com.milamber_brass.brass_armory.init.BrassArmoryBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.core.Direction.*;

public class BrassArmoryBlockModelBuilder extends BlockStateProvider {
    public static final ResourceLocation ROPE_ARROW_TEXTURE = BrassArmory.locate("entity/projectile/rope_arrow");
    protected static final ResourceLocation SOLID = new ResourceLocation("solid");
    protected static final ResourceLocation CUTOUT = new ResourceLocation("cutout");

    public BrassArmoryBlockModelBuilder(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BrassArmory.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        RegistryObject<RopeBlock> block = BrassArmoryBlocks.EXPLORERS_ROPE_BLOCK;
        String blockName = "block/" + block.getId().getPath();
        ResourceLocation textureLocation = BrassArmory.locate("block/" + block.getId().getPath());

        BlockModelBuilder rope = this.models().withExistingParent(blockName, "minecraft:block/block").texture("rope_texture", textureLocation).texture("particle", "#rope_texture").renderType(SOLID)
                .element().from(7, 0, 13).to(9, 16, 15)
                .face(NORTH).uvs(6, 0, 10, 16).texture("#rope_texture").end()
                .face(EAST).uvs(8, 0, 10, 16).texture("#rope_texture").end()
                .face(SOUTH).uvs(6, 0, 10, 16).texture("#rope_texture").end()
                .face(WEST).uvs(8, 0, 10, 16).texture("#rope_texture").end()
                .face(Direction.UP).uvs(6, 0, 10, 2).texture("#rope_texture").end()
                .face(Direction.DOWN).uvs(6, 14, 10, 16).texture("#rope_texture").end()
                .end()
                .element().from(6, 11, 12).to(10, 15, 16)
                .face(NORTH).uvs(6, 12, 10, 16).texture("#rope_texture").end()
                .face(EAST).uvs(6, 12, 10, 16).texture("#rope_texture").end()
                .face(SOUTH).uvs(6, 12, 10, 16).texture("#rope_texture").end()
                .face(WEST).uvs(6, 12, 10, 16).texture("#rope_texture").end()
                .face(Direction.UP).uvs(6, 0, 10, 2).texture("#rope_texture").end()
                .face(Direction.DOWN).uvs(6, 14, 10, 16).texture("#rope_texture").end()
                .end();

        BlockModelBuilder arrow = this.models().withExistingParent(blockName + "_arrow", "minecraft:block/block").texture("rope_texture", textureLocation).texture("particle", "#rope_texture").texture("arrow_texture", ROPE_ARROW_TEXTURE).renderType(CUTOUT)
                .element().from(7, 0, 13).to(9, 16, 15)
                .face(NORTH).uvs(6, 0, 10, 16).texture("#rope_texture").end()
                .face(EAST).uvs(8, 0, 10, 16).texture("#rope_texture").end()
                .face(SOUTH).uvs(6, 0, 10, 16).texture("#rope_texture").end()
                .face(WEST).uvs(8, 0, 10, 16).texture("#rope_texture").end()
                .face(Direction.UP).uvs(6, 0, 10, 2).texture("#rope_texture").end()
                .face(Direction.DOWN).uvs(6, 14, 10, 16).texture("#rope_texture").end()
                .end()
                .element().from(6, 11, 12).to(10, 15, 16)
                .face(NORTH).uvs(6, 12, 10, 16).texture("#rope_texture").end()
                .face(EAST).uvs(6, 12, 10, 16).texture("#rope_texture").end()
                .face(SOUTH).uvs(6, 12, 10, 16).texture("#rope_texture").end()
                .face(WEST).uvs(6, 12, 10, 16).texture("#rope_texture").end()
                .face(Direction.UP).uvs(6, 0, 10, 2).texture("#rope_texture").end()
                .face(Direction.DOWN).uvs(6, 14, 10, 16).texture("#rope_texture").end()
                .end()

                .element().from(8, 11, 5).to(8, 16, 21)
                .face(EAST).uvs(0, 0, 8, 2.5F).texture("#arrow_texture").rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
                .face(WEST).uvs(0, 0, 8, 2.5F).texture("#arrow_texture").end()
                .end()
                .element().from(5.5F, 13.5F, 5).to(10.5F, 13.5F, 21)
                .face(Direction.UP).uvs(8, 2.5F, 0, 0).texture("#arrow_texture").rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
                .face(Direction.DOWN).uvs(8, 5, 0, 7.5F).texture("#arrow_texture").rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                .end()
                .element().from(5.5F, 11, 6).to(10.5F, 16, 6)
                .face(NORTH).uvs(0, 7.5F, 2.5F, 10).texture("#arrow_texture").end()
                .face(SOUTH).uvs(0, 7.5F, 2.5F, 10).texture("#arrow_texture").end()
                .end();

        getVariantBuilder(block.get()).forAllStates(s -> {
            boolean has_arrow = s.getValue(RopeBlock.HAS_ARROW);
            return switch (s.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                case NORTH -> ConfiguredModel.builder().modelFile(has_arrow ? arrow : rope).build();
                case EAST -> ConfiguredModel.builder().modelFile(has_arrow ? arrow : rope).rotationY(90).build();
                case SOUTH -> ConfiguredModel.builder().modelFile(has_arrow ? arrow : rope).rotationY(180).build();
                case WEST -> ConfiguredModel.builder().modelFile(has_arrow ? arrow : rope).rotationY(270).build();
                default -> throw new IllegalStateException("Unexpected value: " + s.getValue(HorizontalDirectionalBlock.FACING));
            };
        });
    }
}
