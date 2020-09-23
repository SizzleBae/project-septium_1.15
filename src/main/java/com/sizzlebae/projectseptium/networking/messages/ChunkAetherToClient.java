package com.sizzlebae.projectseptium.networking.messages;

import com.sizzlebae.projectseptium.capabilities.Aether;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;

public class ChunkAetherToClient {

    public int chunkPosX;
    public int chunkPosZ;
    public byte[] aetherData;

    public ChunkAetherToClient(Chunk chunk, Aether aether) {
        this(chunk.getPos().x, chunk.getPos().z, aether.encode());
    }

    public ChunkAetherToClient(int chunkPosX, int chunkPosZ, byte[] aetherData) {
        this.chunkPosX = chunkPosX;
        this.chunkPosZ = chunkPosZ;
        this.aetherData = aetherData;
    }

    public static ChunkAetherToClient decode(PacketBuffer buf) {
        ChunkAetherToClient result = new ChunkAetherToClient(
                buf.readInt(),
                buf.readInt(),
                buf.readByteArray());

        return result;
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(chunkPosX);
        buf.writeInt(chunkPosZ);
        buf.writeByteArray(aetherData);
    }
}
