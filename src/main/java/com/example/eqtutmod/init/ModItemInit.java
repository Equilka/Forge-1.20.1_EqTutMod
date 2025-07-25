package com.example.eqtutmod.init;

import com.example.eqtutmod.EqTutMod;
import com.example.eqtutmod.items.BadAppleItem;
import com.example.eqtutmod.items.PlasterItem;
import com.example.eqtutmod.items.ScrewdriverItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EqTutMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItemInit {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, EqTutMod.MODID);

    public static final RegistryObject<Item> SCREWDRIVER = REGISTER.register("screwdriver_item", ScrewdriverItem::new);
    public static final RegistryObject<Item> PLASTER = REGISTER.register("plaster_item", PlasterItem::new);
    public static final RegistryObject<Item> BADAPPLE = REGISTER.register("bad_apple_item", BadAppleItem::new);
}
