package com.milamberBrass.brass_armory.blocks.custom;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class RopeBlock extends Block implements IWaterLoggable {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty HAS_ARROW = BooleanProperty.create("arrow");
	private static final VoxelShape ROPE_NORTH = Block.makeCuboidShape(6.0D, 0.0D, 12.0D, 10.0D, 16.0D, 16.0D);
	private static final VoxelShape ROPE_SOUTH = Block.makeCuboidShape(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 4.0D);
	private static final VoxelShape ROPE_EAST = Block.makeCuboidShape(4.0D, 0.0D, 6.0D, 0.0D, 16.0D, 10.0D);
	private static final VoxelShape ROPE_WEST = Block.makeCuboidShape(12.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
	private static final VoxelShape ARROW_NORTH = Block.makeCuboidShape(7.0D, 12.0D, 5.0D, 9.0D, 15.0D, 16.0D);
	private static final VoxelShape ARROW_SOUTH = Block.makeCuboidShape(7.0D, 12.0D, 0.0D, 9.0D, 15.0D, 11.0D);
	private static final VoxelShape ARROW_EAST = Block.makeCuboidShape(0.0D, 12.0D, 7.0D, 11.0D, 15.0D, 9.0D);
	private static final VoxelShape ARROW_WEST = Block.makeCuboidShape(5.0D, 12.0D, 7.0D, 16.0D, 15.0D, 9.0D);
	private static final VoxelShape NORTH_WITH_ARROW = VoxelShapes.or(ROPE_NORTH, ARROW_NORTH);
	private static final VoxelShape SOUTH_WITH_ARROW = VoxelShapes.or(ROPE_SOUTH, ARROW_SOUTH);
	private static final VoxelShape EAST_WITH_ARROW = VoxelShapes.or(ROPE_EAST, ARROW_EAST);
	private static final VoxelShape WEST_WITH_ARROW = VoxelShapes.or(ROPE_WEST, ARROW_WEST);

	public RopeBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, Boolean.valueOf(false)).with(HAS_ARROW, Boolean.valueOf(false)));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED, HAS_ARROW);
	}

	/*********************************************************** Hitbox ********************************************************/

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch ((Direction) state.get(FACING)) {
		case NORTH:
			return state.get(HAS_ARROW) ? NORTH_WITH_ARROW : ROPE_NORTH;
		case SOUTH:
			return state.get(HAS_ARROW) ? SOUTH_WITH_ARROW : ROPE_SOUTH;
		case EAST:
			return state.get(HAS_ARROW) ? EAST_WITH_ARROW : ROPE_EAST;
		case WEST:
		default:
			return state.get(HAS_ARROW) ? WEST_WITH_ARROW : ROPE_WEST;
		}
	}

	/*********************************************************** Placement ********************************************************/

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		Direction direction = state.get(FACING);
		if (state.get(HAS_ARROW)) {
			return canAttachTo(worldIn, pos.offset(direction.getOpposite()), direction);
		} else {
			BlockState blockStateAbove = worldIn.getBlockState(pos.up());
			Block blockAbove = blockStateAbove.getBlock();
			return blockAbove instanceof RopeBlock;
		}
	}

	public static boolean canAttachTo(IBlockReader blockReader, BlockPos pos, Direction direction) {
		BlockState blockstate = blockReader.getBlockState(pos);
		return blockstate.isSolidSide(blockReader, pos, direction);
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific face passed in.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState blockState = stateIn;
		if (facing.getOpposite() == blockState.get(FACING) && !blockState.isValidPosition(worldIn, currentPos)) {
			return Blocks.AIR.getDefaultState();
		} else if (blockState.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		// Remove all the rope underneath a rope that's being broken.
		return !this.isValidPosition(blockState, worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(blockState, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		if (!context.replacingClickedOnBlock()) {
			BlockState blockstate = context.getWorld().getBlockState(context.getPos().offset(context.getFace().getOpposite()));
			if (blockstate.matchesBlock(this) && blockstate.get(FACING) == context.getFace()) {
				return null;
			}
		}

		BlockPos blockpos = context.getPos();
		BlockState blockstate1 = this.getDefaultState();
		BlockState blockStateAbove = context.getWorld().getBlockState(blockpos.up());
		Block blockAbove = blockStateAbove.getBlock();
		// Check if the placement is below an existing RopeBlock and match that block's direction.
		if (blockAbove instanceof RopeBlock) {
			return blockstate1.with(FACING, blockStateAbove.get(FACING));
		}

		IWorldReader iworldreader = context.getWorld();
		FluidState fluidstate = context.getWorld().getFluidState(context.getPos());

		for (Direction direction : context.getNearestLookingDirections()) {
			if (direction.getAxis().isHorizontal()) {
				blockstate1 = blockstate1.with(FACING, direction.getOpposite());
				if (blockstate1.isValidPosition(iworldreader, blockpos)) {
					return blockstate1.with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER));
				}
			}
		}

		return null;
	}

	/*********************************************************** Extra Utils ********************************************************/

	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	/**
	 * Returns the BlockState with the given rotation from the passed BlockState. If inapplicable, returns the passed BlockState.
	 */
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	/**
	 * Returns the BlockState with the given mirror of the passed BlockState. If inapplicable, returns the passed BlockState.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
}
