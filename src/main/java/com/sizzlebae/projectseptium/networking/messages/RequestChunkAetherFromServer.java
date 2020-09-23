package com.sizzlebae.projectseptium.networking.messages;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;

public class RequestChunkAetherFromServer {

    public int chunkPosX;
    public int chunkPosZ;

    public RequestChunkAetherFromServer(Chunk chunk) {
        this(chunk.getPos().x, chunk.getPos().z);
    }

    public RequestChunkAetherFromServer(int chunkPosX, int chunkPosZ) {
        this.chunkPosX = chunkPosX;
        this.chunkPosZ = chunkPosZ;
    }

    public static RequestChunkAetherFromServer decode(PacketBuffer buf) {
        RequestChunkAetherFromServer result = new RequestChunkAetherFromServer(
                buf.readInt(),
                buf.readInt());

        return result;
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(chunkPosX);
        buf.writeInt(chunkPosZ);
    }
}
