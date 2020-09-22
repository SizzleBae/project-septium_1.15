package com.sizzlebae.projectseptium.networking.messages;

import com.sizzlebae.projectseptium.capabilities.Aether;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;

public class ChunkAetherMessageToClient {

    public int chunkPosX;
    public int chunkPosZ;
    public int dimension;
    public byte[] aetherData;

    public ChunkAetherMessageToClient(Chunk chunk, Aether aether) {
        this(chunk.getPos().x, chunk.getPos().z, chunk.getWorld().dimension.getType().getId(), aether.encode());
    }

    public ChunkAetherMessageToClient(int chunkPosX, int chunkPosZ, int dimension, byte[] aetherData) {
        this.chunkPosX = chunkPosX;
        this.chunkPosZ = chunkPosZ;
        this.dimension = dimension;
        this.aetherData = aetherData;
    }

    public static ChunkAetherMessageToClient decode(PacketBuffer buf) {
        ChunkAetherMessageToClient result = new ChunkAetherMessageToClient(
                buf.readInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readByteArray());

        return result;
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(chunkPosX);
        buf.writeInt(chunkPosZ);
        buf.writeInt(dimension);
        buf.writeByteArray(aetherData);
    }
}
