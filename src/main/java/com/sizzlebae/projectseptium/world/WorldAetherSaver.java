package com.sizzlebae.projectseptium.world;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import com.sizzlebae.projectseptium.capabilities.WorldAether;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldAetherSaver {

    @SubscribeEvent
    public static void onWorldSave(WorldEvent.Save event) {
        if(event.getWorld() instanceof ServerWorld) {
            WorldAether worldAether = ((ServerWorld) event.getWorld()).getCapability(ModCapabilities.WORLD_AETHER).orElseThrow(IllegalStateException::new);
//            ((ServerWorld) event.getWorld()).save();
            worldAether.saveAllAetherChunks();
        }

    }
}
