package com.sizzlebae.projectseptium.capabilities;

import com.sizzlebae.projectseptium.ProjectSeptium;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Aether {

    public HashMap<AetherType, AetherEntry> content = new HashMap<>();

    private ArrayList<Consumer<Aether>> listeners;

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

    public void addListener(Consumer<Aether> listener) {
        if(listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }

    public void removeListener(Consumer<Aether> listener) {
        if(!this.listeners.contains(listener)) {
            ProjectSeptium.LOGGER.warn("Attempted to remove listener from aether that does not exist!");
            return;
        }

        this.listeners.remove(listener);
    }

    public void notifyListeners() {
        if(listeners != null) {
            for(Consumer<Aether> listener : listeners) {
                listener.accept(this);
            }
        }
    }

    public void put(AetherEntry entry) {
        this.content.put(entry.type, entry);
    }

    public byte[] encode() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(content.size());
        for(AetherEntry entry : content.values()) {
            buffer.writeByte(entry.type.id);
            buffer.writeInt(entry.value);
            buffer.writeInt(entry.basis);
        }
        buffer.capacity(buffer.writerIndex());
        return buffer.array();
    }

    public void decode(byte[] bytes) {
        content.clear();

        ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
        byte size = buffer.readByte();
        for(int i = 0; i < size; i++) {
            AetherType type = AetherType.values()[buffer.readByte()];
            AetherEntry data = new AetherEntry(type, buffer.readInt(), buffer.readInt());
            content.put(type, data);
        }

        notifyListeners();
    }

    @Override
    public String toString() {
        String result = "";
        for(AetherEntry entry : content.values()) {
            result += "[" + entry.type.name() + ":" + entry.value + "/" + entry.basis + "]";
        }
        return  result;
    }
}
