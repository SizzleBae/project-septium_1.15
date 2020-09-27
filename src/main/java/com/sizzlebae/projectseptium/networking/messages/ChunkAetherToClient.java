package com.sizzlebae.projectseptium.networking.messages;

import com.sizzlebae.projectseptium.capabilities.Aether;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;

public class ChunkAetherToClient {

    public int chunkPosX;
    public int chunkPosZ;
    public byte[] aetherData;

    public ChunkAetherToClient(ChunkPos pos, Aether aether) {
        this(pos.x, pos.z, aether.encode());
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
