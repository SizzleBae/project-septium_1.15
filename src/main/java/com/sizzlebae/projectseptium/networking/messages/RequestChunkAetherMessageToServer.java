package com.sizzlebae.projectseptium.networking.messages;

import com.sizzlebae.projectseptium.capabilities.Aether;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;

public class RequestChunkAetherMessageToServer {

    public int chunkPosX;
    public int chunkPosZ;
    public int dimension;

    public RequestChunkAetherMessageToServer(Chunk chunk) {
        this(chunk.getPos().x, chunk.getPos().z, chunk.getWorld().dimension.getType().getId());
    }

    public RequestChunkAetherMessageToServer(int chunkPosX, int chunkPosZ, int dimension) {
        this.chunkPosX = chunkPosX;
        this.chunkPosZ = chunkPosZ;
        this.dimension = dimension;
    }

    public static RequestChunkAetherMessageToServer decode(PacketBuffer buf) {
        RequestChunkAetherMessageToServer result = new RequestChunkAetherMessageToServer(
                buf.readInt(),
                buf.readInt(),
                buf.readInt());

        return result;
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(chunkPosX);
        buf.writeInt(chunkPosZ);
        buf.writeInt(dimension);
    }
}
