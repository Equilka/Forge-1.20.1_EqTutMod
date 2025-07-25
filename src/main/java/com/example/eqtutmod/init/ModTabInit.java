package com.example.eqtutmod.init;

import com.example.eqtutmod.EqTutMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTabInit {
    public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EqTutMod.MODID);

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = REGISTER.register("mod_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable("itemGroup.eqtutmod.mod_tab"))
            .icon(() -> ModBlockInit.MEAT_BLOCK.get().asItem().getDefaultInstance())
            .displayItems((itemDisplayParameters, tabData) -> {
                tabData.accept(ModBlockInit.SKY_BLOCK.get());
                tabData.accept(ModBlockInit.BOULDER_BLOCK.get());
                tabData.accept(ModBlockInit.DEMONCORE_BLOCK.get());
                tabData.accept(ModBlockInit.MEAT_BLOCK.get());
                tabData.accept(ModBlockInit.WAXED_WEATHERED_CUT_COPPER_VERTICAL_SLAB.get());
                tabData.accept(ModItemInit.BADAPPLE.get());
                tabData.accept(ModItemInit.PLASTER.get());
                tabData.accept(ModItemInit.SCREWDRIVER.get());
            })
            .build());
}
