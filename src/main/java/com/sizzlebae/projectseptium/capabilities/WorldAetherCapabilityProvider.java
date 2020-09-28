package com.sizzlebae.projectseptium.capabilities;

import com.sizzlebae.projectseptium.world.ChunkAetherGenerator;
import com.sizzlebae.projectseptium.world.ChunkAetherIO;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public class WorldAetherCapabilityProvider  implements ICapabilitySerializable<INBT> {

    private WorldAether worldAether;

    public WorldAetherCapabilityProvider(World world) {
        DimensionType dimension = world.getDimension().getType();
        int seed = (int) world.getSeed() + dimension.getId();
        ChunkAetherGenerator generator = new ChunkAetherGenerator(seed);

        ChunkAetherIO io = null;
        if(world instanceof ServerWorld) {
            File worldDirectory = ((ServerWorld)world).getSaveHandler().getWorldDirectory();
            File dimDirectory =  world.dimension.getType().getDirectory(worldDirectory);
            File aetherDirectory = new File(dimDirectory, "aether");
            io = new ChunkAetherIO(aetherDirectory);
        }

        worldAether = new WorldAether(generator, io, world.isRemote());
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
        return ModCapabilities.WORLD_AETHER.writeNBT(worldAether, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        ModCapabilities.WORLD_AETHER.readNBT(worldAether, null, nbt);
    }
}
