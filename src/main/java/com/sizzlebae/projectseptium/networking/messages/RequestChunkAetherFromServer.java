package com.sizzlebae.projectseptium.networking.messages;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import com.sizzlebae.projectseptium.capabilities.WorldAether;
import com.sizzlebae.projectseptium.networking.ModChannel;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class RequestChunkAetherFromServer {

    public int chunkPosX;
    public int chunkPosZ;

    public RequestChunkAetherFromServer(ChunkPos pos) {
        this(pos.x, pos.z);
    }

    public RequestChunkAetherFromServer(int chunkPosX, int chunkPosZ) {
        this.chunkPosX = chunkPosX;
        this.chunkPosZ = chunkPosZ;
    }

    public static RequestChunkAetherFromServer decode(PacketBuffer buf) {
        return new RequestChunkAetherFromServer(
                buf.readInt(),
                buf.readInt());
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(chunkPosX);
        buf.writeInt(chunkPosZ);
    }


    public static void receivedOnServer(final RequestChunkAetherFromServer message, Supplier<NetworkEvent.Context> ctxSupplier) {
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

            ModChannel.simple.send(PacketDistributor.PLAYER.with(()->sendingPlayer),
                    new ChunkAetherToClient(pos, aether));

            //ProjectSeptium.LOGGER.warn("Client requested aether chunk at: " + message.chunkPosX + ", " + message.chunkPosZ);

        });
    }
}
