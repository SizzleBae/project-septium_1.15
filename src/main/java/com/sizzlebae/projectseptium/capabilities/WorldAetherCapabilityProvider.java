package com.sizzlebae.projectseptium.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WorldAetherCapabilityProvider  implements ICapabilitySerializable<INBT> {

    private WorldAether worldAether;

    public WorldAetherCapabilityProvider(World world) {
        worldAether = new WorldAether(world);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(ModCapabilities.WORLD_AETHER == cap) {
            return (LazyOptional<T>) LazyOptional.of(()->worldAether);
        }

        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(INBT nbt) {

    }
}
