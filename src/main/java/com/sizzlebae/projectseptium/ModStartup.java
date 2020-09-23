package com.sizzlebae.projectseptium;

import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModStartup {

    @SubscribeEvent
    public static void onCommonStartupEvent(FMLCommonSetupEvent event) {

    }
}
