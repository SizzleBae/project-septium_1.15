package com.sizzlebae.projectseptium.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class AetherMap  {

    public int chunkRange = 0;
    public byte[] chunkColors = new byte[0];

    public AetherMap() {}

    public static class Storage implements Capability.IStorage<AetherMap> {

        @Override
        public INBT writeNBT(Capability<AetherMap> capability, AetherMap instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("chunkRange", instance.chunkRange);
            tag.putByteArray("chunkColors", instance.chunkColors);
            return  tag;
        }

        @Override
        public void readNBT(Capability<AetherMap> capability, AetherMap instance, Direction side, INBT nbt) {
            CompoundNBT tag = (CompoundNBT)nbt;
            instance.chunkRange = tag.getInt("chunkRange");
            instance.chunkColors = tag.getByteArray("chunkColors");
        }
    }
}
