package com.sizzlebae.projectseptium.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AetherMapCapabilityProvider implements ICapabilitySerializable<INBT> {

    AetherMap aetherMap = new AetherMap();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if(cap == ModCapabilities.AETHER_MAP) {
            return (LazyOptional<T>)LazyOptional.of(() -> aetherMap);
        }

        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return ModCapabilities.AETHER_MAP.writeNBT(aetherMap, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        ModCapabilities.AETHER_MAP.readNBT(aetherMap, null, nbt);
    }
}
