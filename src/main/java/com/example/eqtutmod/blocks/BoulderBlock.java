package com.example.eqtutmod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BoulderBlock extends FallingBlock {
    private static final VoxelShape BOULDER_PART1 = Block.box(4.0, 0.0, 4.0, 12.0, 4.0, 12.0);
    private static final VoxelShape BOULDER_PART2 = Block.box(2.0, 4.0, 2.0, 14.0, 12.0, 14.0);
    private static final VoxelShape BOULDER_PART3 = Block.box(4.0, 12.0, 4.0, 12.0, 16.0, 12.0);

    private static final VoxelShape BOULDER = Shapes.or(
            BOULDER_PART1, BOULDER_PART2, BOULDER_PART3
    );

    public BoulderBlock(){
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE));
    }

    @Override
    protected void falling(FallingBlockEntity pFallingEntity) {
        pFallingEntity.setHurtsEntities(4.0F, 40);
    }

    @Override
    public void onLand(Level pLevel, BlockPos pPos, BlockState pState, BlockState pReplaceableState, FallingBlockEntity pFallingBlock) {
        if (!pFallingBlock.isSilent()) {
            pLevel.playSound(null, pPos, SoundEvents.DEEPSLATE_FALL, SoundSource.BLOCKS, 2.0F, 1.0F);
        }
    }

    @Override
    public DamageSource getFallDamageSource(Entity pEntity) {
        return pEntity.damageSources().fallingBlock(pEntity);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BOULDER;
    }
}
