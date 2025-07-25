package com.example.eqtutmod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;


public class SkyBlock extends Block {
    public static BooleanProperty ACTIVATED = BooleanProperty.create("activated");
    public static BooleanProperty UPDATE = BooleanProperty.create("update");

    public SkyBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .noOcclusion()
                .strength(1.0f)
                .isSuffocating((s, r, p) -> false).isViewBlocking((s, r, p) -> false));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean b) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 20);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean powered = ctx.getLevel().hasNeighborSignal(ctx.getClickedPos());
        return this.defaultBlockState()
                .setValue(ACTIVATED, false)
                .setValue(UPDATE, powered);
    }


    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        level.setBlock(pos, state.cycle(UPDATE), 3);
        boolean blocked = false;
        int skyDarken = 0;
        if (!state.getValue(ACTIVATED))
            skyDarken = 15 - level.getSkyDarken();

        BlockPos downPos = pos.below();
        while (level.getBlockState(downPos).isAir()) {
            level.setBlock(downPos, Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, skyDarken), 3);
            downPos = downPos.below();
            if (level.getBlockState(downPos).isSolid()) {
                downPos = downPos.below();
                blocked = true;
                break;
            }
        }

        if (blocked) {
            while (level.getBlockState(downPos).is(Blocks.LIGHT)) {
                level.setBlock(downPos, Blocks.AIR.defaultBlockState(), 3);
                downPos = downPos.below();
            }
        }
        level.scheduleTick(pos, this, 20);
    }


    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean b) {
        if (!level.isClientSide() && level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.setValue(ACTIVATED, true), 3);
        } else if (!level.isClientSide() && !level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.setValue(ACTIVATED, false), 3);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean b) {
        if (!level.isClientSide()) {
            BlockPos downPos = pos.below();
            while (level.getBlockState(downPos).is(Blocks.LIGHT)) {
                level.setBlock(downPos, Blocks.AIR.defaultBlockState(), 3);
                downPos = downPos.below();
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
        builder.add(UPDATE);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }
}
