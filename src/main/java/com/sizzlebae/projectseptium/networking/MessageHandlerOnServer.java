package com.sizzlebae.projectseptium.networking;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import com.sizzlebae.projectseptium.networking.messages.ChunkAetherToClient;
import com.sizzlebae.projectseptium.networking.messages.RequestChunkAetherFromServer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class MessageHandlerOnServer {

    public static void onRequestChunkAetherMessage(final RequestChunkAetherFromServer message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.setPacketHandled(true);

        ServerPlayerEntity sendingPlayer = ctx.getSender();

        ctx.enqueueWork(() -> {
            // Make sure that area is loaded to prevent server spam attack
            if(!sendingPlayer.world.isAreaLoaded(new BlockPos(message.chunkPosX * 16, 0, message.chunkPosZ * 16),0)) {
                return;
            }

            // Respond to sender with a chunk aether message
            Chunk chunk = sendingPlayer.world.getChunk(message.chunkPosX, message.chunkPosZ);
            Aether aether = chunk.getCapability(ModCapabilities.CAPABILITY_AETHER).orElse(null);

            ModChannel.simpleChannel.send(PacketDistributor.PLAYER.with(()->sendingPlayer), new ChunkAetherToClient(chunk, aether));

            ProjectSeptium.LOGGER.warn("Client requested aether chunk at: " + message.chunkPosX + ", " + message.chunkPosZ);

        });
    }

    public static boolean isThisProtocolAcceptedByServer(String protocolVersion) {
        return ModChannel.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }

}
