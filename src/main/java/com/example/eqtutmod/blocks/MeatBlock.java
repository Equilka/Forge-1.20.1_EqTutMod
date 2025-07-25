package com.example.eqtutmod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MeatBlock extends Block {

    public MeatBlock(){
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean b) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 20);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        AABB area = new AABB(pos).inflate(1);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity entity : entities) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 30, 0));
        }
        level.scheduleTick(pos, this, 20);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        float chance = 0.3f;
        if(source.nextFloat() > chance){
            createParticle(pos.above(), level, source);
            createParticle(pos.east(), level, source);
            createParticle(pos.west(), level, source);
            createParticle(pos.south(), level, source);
            createParticle(pos.north(), level, source);
        }
    }

    private void createParticle(BlockPos pos, Level level, RandomSource source){
        level.addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT,
                pos.getX() + source.nextFloat(), pos.getY(), pos.getZ() + source.nextFloat(),
                0.009, 0.1, 0.006);
    }

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        if (pEntity.isSuppressingBounce()) {
            super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
        } else {
            pEntity.causeFallDamage(pFallDistance, 0.0F, pLevel.damageSources().fall());
        }
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter pLevel, Entity pEntity) {
        if (pEntity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(pLevel, pEntity);
        } else {
            this.bounceUp(pEntity);
        }
    }

    private void bounceUp(Entity pEntity) {
        Vec3 movementVector = pEntity.getDeltaMovement();
        if (movementVector.y < 0.0) {
            double $$2 = pEntity instanceof LivingEntity ? 1.0 : 0.8;
            pEntity.setDeltaMovement(movementVector.x, -movementVector.y * $$2, movementVector.z);
        }
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        double movementVector = Math.abs(pEntity.getDeltaMovement().y);
        if (movementVector < 0.1 && !pEntity.isSteppingCarefully()) {
            double slowedVector = 0.4 + movementVector * 0.2;
            pEntity.setDeltaMovement(pEntity.getDeltaMovement().multiply(slowedVector, 1.0, slowedVector));
        }

        super.stepOn(pLevel, pPos, pState, pEntity);
    }
}
