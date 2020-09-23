package com.sizzlebae.projectseptium.capabilities;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.networking.ModChannel;
import com.sizzlebae.projectseptium.networking.messages.RequestChunkAetherFromServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID)
public class CapabilityAttachEventHandler {
    @SubscribeEvent
    public static void attachCapabilityToChunkHandler(AttachCapabilitiesEvent<Chunk> event) {
        Chunk chunk = event.getObject();
        event.addCapability(new ResourceLocation(ProjectSeptium.MODID), new ChunkAetherCapabilityProvider(chunk));

        // Client should request aether data when loading a chunk
        if (event.getObject().getWorld().isRemote()) {
            ModChannel.simpleChannel.send(PacketDistributor.SERVER.noArg(), new RequestChunkAetherFromServer(chunk));
        }
    }
}
