package com.milamber_brass.brass_armory.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.IBlockRenderProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class RopeBlock extends Block implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty HAS_ARROW = BooleanProperty.create("arrow");
    private static final VoxelShape ROPE_NORTH = Block.box(6.0D, 0.0D, 12.0D, 10.0D, 16.0D, 16.0D);
    private static final VoxelShape ROPE_SOUTH = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 4.0D);
    private static final VoxelShape ROPE_EAST = Block.box(0.0D, 0.0D, 6.0D, 4.0D, 16.0D, 10.0D);
    private static final VoxelShape ROPE_WEST = Block.box(12.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    private static final VoxelShape ARROW_NORTH = Block.box(7.0D, 12.0D, 5.0D, 9.0D, 15.0D, 16.0D);
    private static final VoxelShape ARROW_SOUTH = Block.box(7.0D, 12.0D, 0.0D, 9.0D, 15.0D, 11.0D);
    private static final VoxelShape ARROW_EAST = Block.box(0.0D, 12.0D, 7.0D, 11.0D, 15.0D, 9.0D);
    private static final VoxelShape ARROW_WEST = Block.box(5.0D, 12.0D, 7.0D, 16.0D, 15.0D, 9.0D);
    private static final VoxelShape NORTH_WITH_ARROW = Shapes.or(ROPE_NORTH, ARROW_NORTH);
    private static final VoxelShape SOUTH_WITH_ARROW = Shapes.or(ROPE_SOUTH, ARROW_SOUTH);
    private static final VoxelShape EAST_WITH_ARROW = Shapes.or(ROPE_EAST, ARROW_EAST);
    private static final VoxelShape WEST_WITH_ARROW = Shapes.or(ROPE_WEST, ARROW_WEST);

    public RopeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.FALSE).setValue(HAS_ARROW, Boolean.FALSE));
    }

    public static boolean canAttachTo(BlockGetter blockReader, BlockPos pos, Direction direction) {
        BlockState blockstate = blockReader.getBlockState(pos);
        return blockstate.isFaceSturdy(blockReader, pos, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, HAS_ARROW);
    }

    /*********************************************************** Hitbox ********************************************************/

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return state.getValue(HAS_ARROW) ? NORTH_WITH_ARROW : ROPE_NORTH;
            case SOUTH:
                return state.getValue(HAS_ARROW) ? SOUTH_WITH_ARROW : ROPE_SOUTH;
            case EAST:
                return state.getValue(HAS_ARROW) ? EAST_WITH_ARROW : ROPE_EAST;
            case WEST:
            default:
                return state.getValue(HAS_ARROW) ? WEST_WITH_ARROW : ROPE_WEST;
        }
    }

    /*********************************************************** Placement ********************************************************/
    @ParametersAreNonnullByDefault
    public boolean canSurvive(BlockState state, BlockGetter worldIn, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        if (state.getValue(HAS_ARROW)) {
            return canAttachTo(worldIn, pos.relative(direction.getOpposite()), direction);
        } else {
            BlockState blockStateAbove = worldIn.getBlockState(pos.above());
            Block blockAbove = blockStateAbove.getBlock();
            return blockAbove instanceof RopeBlock;
        }
    }

    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing.getOpposite() == stateIn.getValue(FACING) && !stateIn.canSurvive(worldIn, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        } else if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }

        // Remove all the rope underneath a rope that's being broken.
        return !this.canSurvive(stateIn, worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (!context.replacingClickedOnBlock()) {
            BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite()));
            if (blockstate.is(this) && blockstate.getValue(FACING) == context.getClickedFace()) {
                return null;
            }
        }

        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate1 = this.defaultBlockState();
        BlockState blockStateAbove = context.getLevel().getBlockState(blockpos.above());
        Block blockAbove = blockStateAbove.getBlock();
        // Check if the placement is below an existing RopeBlock and match that block's direction.
        if (blockAbove instanceof RopeBlock) {
            return blockstate1.setValue(FACING, blockStateAbove.getValue(FACING));
        }

        LevelAccessor ilevelaccessor = context.getLevel();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                blockstate1 = blockstate1.setValue(FACING, direction.getOpposite());
                if (blockstate1.canSurvive(ilevelaccessor, blockpos)) {
                    return blockstate1.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
                }
            }
        }

        return null;
    }

    /*********************************************************** Extra Utils ********************************************************/

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    /**
     * Returns the BlockState with the given rotation from the passed BlockState. If inapplicable, returns the passed BlockState.
     */
    @Override
    @Nonnull
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    /**
     * Returns the BlockState with the given mirror of the passed BlockState. If inapplicable, returns the passed BlockState.
     */
    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

}
