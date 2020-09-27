package com.sizzlebae.projectseptium.capabilities;

import com.sizzlebae.projectseptium.ProjectSeptium;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID)
public class CapabilityCoupler {

    @SubscribeEvent
    public static void attachCapabilityToChunkHandler(AttachCapabilitiesEvent<Chunk> event) {
        Chunk chunk = event.getObject();
        ChunkAetherCapabilityProvider capabilityProvider = new ChunkAetherCapabilityProvider(chunk);
        event.addCapability(new ResourceLocation(ProjectSeptium.MODID), capabilityProvider);

        // Client should request aether data when loading a chunk
//        if (chunk.getWorld().isRemote()) {
//            ModChannel.simpleChannel.send(PacketDistributor.SERVER.noArg(), new RequestChunkAetherFromServer(chunk));
//        }
    }

    @SubscribeEvent
    public static void attachCapabilityToWorldHandler(AttachCapabilitiesEvent<World> event) {
        World world = event.getObject();
        event.addCapability(new ResourceLocation(ProjectSeptium.MODID), new WorldAetherCapabilityProvider(world));

    }
}
