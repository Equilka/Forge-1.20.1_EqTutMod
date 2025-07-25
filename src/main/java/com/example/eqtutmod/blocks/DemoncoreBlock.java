package com.example.eqtutmod.blocks;

import com.example.eqtutmod.init.ModItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class DemoncoreBlock extends Block {
    public static BooleanProperty ACTIVATED = BooleanProperty.create("activated");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public DemoncoreBlock(){
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .lightLevel(state -> state.getValue(ACTIVATED) ? 15 : 2));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean b) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 20);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction dir = ctx.getHorizontalDirection().getOpposite();
        return this.defaultBlockState()
                .setValue(ACTIVATED, false)
                .setValue(FACING, dir);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        if (state.getValue(ACTIVATED)){
            AABB area = new AABB(pos).inflate(1);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
            for (LivingEntity entity : entities) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 1));
                entity.addEffect(new MobEffectInstance(MobEffects.HARM, 20, 0));
                entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1));
                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1));
            }
            level.playSound(null, pos, SoundEvents.BASALT_STEP, SoundSource.BLOCKS, 2.0F, 1.0F);
        }
        level.scheduleTick(pos, this, 20);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            if (player.getMainHandItem().isEmpty()){
                ItemStack stack = new ItemStack(ModItemInit.SCREWDRIVER.get());

                double x = pos.getX() + 0.5;
                double y = pos.getY() + 1.0;
                double z = pos.getZ() + 0.5;

                ItemEntity itemEntity = new ItemEntity(world, x, y, z, stack);

                world.addFreshEntity(itemEntity);

                world.setBlock(pos, state.setValue(ACTIVATED, true), 3);
            } else if (player.getMainHandItem().is(ModItemInit.SCREWDRIVER.get())) {
                player.getMainHandItem().shrink(1);

                world.setBlock(pos, state.setValue(ACTIVATED, false), 3);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
