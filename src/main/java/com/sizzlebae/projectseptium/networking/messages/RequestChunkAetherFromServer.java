package com.sizzlebae.projectseptium.networking.messages;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

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
}
