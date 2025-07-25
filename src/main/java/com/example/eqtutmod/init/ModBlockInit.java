package com.example.eqtutmod.init;

import com.example.eqtutmod.EqTutMod;
import com.example.eqtutmod.blocks.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = EqTutMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlockInit {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, EqTutMod.MODID);

    public static final RegistryObject<Block> BOULDER_BLOCK = registerBlock("boulder_block", BoulderBlock::new);
    public static final RegistryObject<Block> SKY_BLOCK = registerBlock("sky_block", SkyBlock::new);
    public static final RegistryObject<Block> MEAT_BLOCK = registerBlock("meat_block", MeatBlock::new);
    public static final RegistryObject<Block> DEMONCORE_BLOCK = registerBlock("demoncore_block", DemoncoreBlock::new);
    public static final RegistryObject<Block> WAXED_WEATHERED_CUT_COPPER_VERTICAL_SLAB = registerBlock("waxed_weathered_cut_copper_vertical_slab",
            WaxedWeatheredCutCopperVerticalSlabBlock::new);


    private static <T extends Block> RegistryObject<T> registerBlock (String name, Supplier<T> properties) {
        RegistryObject<T> block = REGISTER.register(name, properties);
        registerItem(name, block);
        return block;
    }

    private static <T extends Block> RegistryObject<BlockItem> registerItem (String name, RegistryObject<T> block){
        return ModItemInit.REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus bus){
        REGISTER.register(bus);
    }
}
