package com.sizzlebae.projectseptium.networking;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import com.sizzlebae.projectseptium.capabilities.WorldAether;
import com.sizzlebae.projectseptium.networking.messages.ChunkAetherToClient;
import com.sizzlebae.projectseptium.networking.messages.RequestChunkAetherFromServer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class MessageHandlerOnServer {

    public static void onRequestChunkAetherMessage(final RequestChunkAetherFromServer message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.setPacketHandled(true);

        ServerPlayerEntity sendingPlayer = ctx.getSender();
        if(sendingPlayer == null) {
            ProjectSeptium.LOGGER.error("RequestChunkAetherFromServer: Failed to retrieve sending player.");
            return;
        }

        ctx.enqueueWork(() -> {
            // Make sure that chunk exists to prevent server spam attack
//            if(!sendingPlayer.world.getChunkProvider().chunkExists(message.chunkPosX, message.chunkPosZ)) {
//                return;
//            }

            // Respond to sender with a chunk aether message
//            Chunk chunk = sendingPlayer.world.getChunk(message.chunkPosX, message.chunkPosZ);
//            Aether aether = chunk.getCapability(ModCapabilities.AETHER).orElse(null);
            WorldAether worldAether = sendingPlayer.world.getCapability(ModCapabilities.WORLD_AETHER).orElseThrow(IllegalStateException::new);
            ChunkPos pos = new ChunkPos(message.chunkPosX, message.chunkPosZ);
            Aether aether = worldAether.loadChunkAether(pos);

            ModChannel.simpleChannel.send(PacketDistributor.PLAYER.with(()->sendingPlayer),
                    new ChunkAetherToClient(pos, aether));

            //ProjectSeptium.LOGGER.warn("Client requested aether chunk at: " + message.chunkPosX + ", " + message.chunkPosZ);

        });
    }

    public static boolean isThisProtocolAcceptedByServer(String protocolVersion) {
        return ModChannel.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }

}
