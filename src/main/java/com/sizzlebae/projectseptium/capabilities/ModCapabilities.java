package com.sizzlebae.projectseptium.capabilities;

import com.sizzlebae.projectseptium.ProjectSeptium;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapabilities {

    @CapabilityInject(Aether.class)
    public static Capability<Aether> CAPABILITY_AETHER = null;

    @SubscribeEvent
    public static void onCommonStartupEvent(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(
                Aether.class,
                new Aether.Storage(),
                Aether::new
        );
    }
}
