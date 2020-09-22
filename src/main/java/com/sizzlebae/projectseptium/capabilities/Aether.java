package com.sizzlebae.projectseptium.capabilities;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;


public class Aether {
    public int water = 0;
    public int fire = 0;
    public int earth = 0;
    public int wind = 0;

    public Aether() {}

    public Aether(int initialWater, int initialFire, int initialEarth, int initialWind) {
        water = initialWater;
        fire = initialFire;
        earth = initialEarth;
        wind = initialWind;
    }

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

    public byte[] encode() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(water);
        buffer.writeInt(fire);
        buffer.writeInt(earth);
        buffer.writeInt(wind);
        return buffer.array();
    }

    public void decode(byte[] bytes) {
        ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
        water = buffer.readInt();
        fire = buffer.readInt();
        earth = buffer.readInt();
        wind = buffer.readInt();
    }

    public static Aether defaultInstance() {
        return new Aether();
    }

}
