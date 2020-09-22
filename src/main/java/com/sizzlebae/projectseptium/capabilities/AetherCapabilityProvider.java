package com.sizzlebae.projectseptium.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AetherCapabilityProvider implements ICapabilitySerializable<INBT> {

    private Aether aether = new Aether((int)(Math.random() * 100), (int)(Math.random() * 100), (int)(Math.random() * 100), (int)(Math.random() * 100));

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if(cap == ModCapabilities.CAPABILITY_AETHER) {
            return (LazyOptional<T>)LazyOptional.of(() -> aether);
        }

        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return ModCapabilities.CAPABILITY_AETHER.writeNBT(aether, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        ModCapabilities.CAPABILITY_AETHER.readNBT(aether, null, nbt);
    }
}
