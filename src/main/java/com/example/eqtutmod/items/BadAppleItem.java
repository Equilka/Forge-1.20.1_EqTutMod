package com.example.eqtutmod.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BadAppleItem extends Item {
    public BadAppleItem() {
        super(new Item.Properties()
                .food(new FoodProperties.Builder()
                .alwaysEat().nutrition(1).nutrition(2).build()));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (!world.isClientSide && entity instanceof Player player) {
            player.hurt(player.level().damageSources().magic(), Float.MAX_VALUE);
        }

        return super.finishUsingItem(stack, world, entity);
    }
}
