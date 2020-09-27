package com.sizzlebae.projectseptium.networking;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import com.sizzlebae.projectseptium.capabilities.WorldAether;
import com.sizzlebae.projectseptium.networking.messages.ChunkAetherToClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class MessageHandlerOnClient {

    public static void onChunkAetherMessage(final ChunkAetherToClient message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.setPacketHandled(true);

        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
        if (!clientWorld.isPresent()) {
            ProjectSeptium.LOGGER.warn("TargetEffectMessageToClient context could not provide a ClientWorld.");
            return;
        }

        // This code creates a new task which will be executed by the client during the next tick
        ctx.enqueueWork(() -> {

            // Update the client side chunk aether data to match the message data
            ClientWorld world = clientWorld.get();

            WorldAether worldAether = world.getCapability(ModCapabilities.WORLD_AETHER).orElseThrow(IllegalStateException::new);

            ChunkPos pos = new ChunkPos(message.chunkPosX, message.chunkPosZ);
            worldAether.getChunkAether(pos).decode(message.aetherData);

//            Chunk chunk = world.getChunk(message.chunkPosX, message.chunkPosZ);
//            Aether aether = chunk.getCapability(ModCapabilities.AETHER).orElse(null);
//            aether.decode(message.aetherData);
//            ProjectSeptium.AETHER_MAP.getChunkAether(world, pos).decode(message.aetherData);

//            ProjectSeptium.LOGGER.warn("Received aether chunk: " + message.chunkPosX + ", " + message.chunkPosZ + " - "
//                    + aether.water + "/" + aether.fire + "/" + aether.earth + "/" + aether.wind);

        });
    }

    public static boolean isThisProtocolAcceptedByClient(String protocolVersion) {
        return ModChannel.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }

}
