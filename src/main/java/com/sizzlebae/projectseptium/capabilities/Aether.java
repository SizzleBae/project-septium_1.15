package com.sizzlebae.projectseptium.capabilities;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import java.util.HashMap;
import java.util.Map;


public class Aether {
    public final static byte AETHER_COLOR_RANGE = 4;
    public final static byte AETHER_COLOR_SIZE = 25;

    public HashMap<AetherType, AetherEntry> content = new HashMap();

    public Aether() {}

    public static class Storage implements Capability.IStorage<Aether> {

        @Override
        public INBT writeNBT(Capability<Aether> capability, Aether instance, Direction side) {
            return new ByteArrayNBT(instance.encode());
        }

        @Override
        public void readNBT(Capability<Aether> capability, Aether instance, Direction side, INBT nbt) {
            instance.decode(((ByteArrayNBT)nbt).getByteArray());
        }
    }

    public void set(AetherEntry entry) {
        this.content.put(entry.type, entry);
    }

    public byte[] encode() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(content.size());
        for(AetherEntry entry : content.values()) {
            buffer.writeByte(entry.type.id);
            buffer.writeInt(entry.value);
            buffer.writeInt(entry.max);
        }
        return buffer.array();
    }

    public void decode(byte[] bytes) {
        ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
        byte size = buffer.readByte();
        for(int i = 0; i < size; i++) {
            AetherType type = AetherType.values()[buffer.readByte()];
            AetherEntry data = new AetherEntry(type, buffer.readInt(), buffer.readInt());
            content.put(type, data);
        }
    }

}
