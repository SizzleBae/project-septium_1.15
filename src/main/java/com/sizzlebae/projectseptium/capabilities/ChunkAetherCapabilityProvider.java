package com.sizzlebae.projectseptium.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class ChunkAetherCapabilityProvider implements ICapabilitySerializable<INBT> {

    private static final HashMap<World, SimplexNoiseGenerator> generators = new HashMap();

    private Aether aether = new Aether();
    private Chunk chunk;

    public ChunkAetherCapabilityProvider(Chunk chunk) {
        this.chunk = chunk;

        World world = chunk.getWorld();
        if(!world.isRemote()) {
            // Get the noise generator for the world the chunk belongs to
            SimplexNoiseGenerator generator = generators.get(world);
            if(generator == null) {
                // If one does not exist, create one instead and seed it with the world's random
                generator = new SimplexNoiseGenerator(chunk.getWorld().getRandom());
                generators.put(world, generator);
            }

            // Generate aether values based on river-like noise
            ChunkPos pos = chunk.getPos();
            aether.set(new AetherEntry(AetherType.WATER,
                    generateLeyLineNoise(generator, pos, 0, 100), 100));
            aether.set(new AetherEntry(AetherType.FIRE,
                    generateLeyLineNoise(generator, pos, 1, 100), 100));
            aether.set(new AetherEntry(AetherType.EARTH,
                    generateLeyLineNoise(generator, pos, 2, 100), 100));
            aether.set(new AetherEntry(AetherType.WIND,
                    generateLeyLineNoise(generator, pos, 3, 100), 100));

        }

    }

    private int generateLeyLineNoise(SimplexNoiseGenerator generator, ChunkPos pos, int index, double outputScale) {
        final double noiseScale = 128;
        final double noisePower = 8;
        double offset = index * 4096;

        double rawNoise = generator.getValue(pos.x / noiseScale + offset, pos.z / noiseScale + offset);

        double normalizedNoise = (rawNoise + 1.0) / 2.0;

        double expNoise = Math.pow(normalizedNoise, noisePower);

        return (int) (expNoise * outputScale);
    }

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
