package com.sizzlebae.projectseptium.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChunkAetherCapabilityProvider implements ICapabilitySerializable<INBT> {

    World world;
    Chunk chunk;

    public ChunkAetherCapabilityProvider(Chunk chunk) {
        this.chunk = chunk;
        world = chunk.getWorld();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == ModCapabilities.AETHER) {
            WorldAether worldAether = world.getCapability(ModCapabilities.WORLD_AETHER).orElseThrow(IllegalStateException::new);
            return LazyOptional.of(()->(T)worldAether);
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
