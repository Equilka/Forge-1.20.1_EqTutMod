package com.example.eqtutmod.events;

import com.example.eqtutmod.init.ModBlockInit;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SkyBlockColorEvent {

    @SubscribeEvent
    public static void onBlockColorLoad(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, world, pos, tintIndex) -> {
                    if (world == null || pos == null) return 0xFFFFFF;

                    double x = pos.getX() + 0.5;
                    double y = pos.getY() + 0.5;
                    double z = pos.getZ() + 0.5;

                    Vec3 skyColor = Minecraft.getInstance().level.getSkyColor(new Vec3(x, y, z), 1.0f);

                    int r = (int) (skyColor.x * 255);
                    int g = (int) (skyColor.y * 255);
                    int b = (int) (skyColor.z * 255);

                    return (r << 16) | (g << 8) | b;
                },
                ModBlockInit.SKY_BLOCK.get()
        );
    }
}
