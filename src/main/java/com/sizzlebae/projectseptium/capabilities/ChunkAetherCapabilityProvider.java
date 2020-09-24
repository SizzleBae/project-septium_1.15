package com.sizzlebae.projectseptium.capabilities;

import com.sizzlebae.projectseptium.utils.FastNoiseLite;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class ChunkAetherCapabilityProvider implements ICapabilitySerializable<INBT> {

    private static final HashMap<World, FastNoiseLite> generators = new HashMap();

    public Aether aether = new Aether();
    private Chunk chunk;

    public ChunkAetherCapabilityProvider(Chunk chunk) {
        this.chunk = chunk;

        World world = chunk.getWorld();
        if(!world.isRemote()) {
            // Get the noise generator for the world the chunk belongs to
            FastNoiseLite generator = generators.get(world);

            if(generator == null) {
                // If one does not exist, create one instead and seed it with the world's random
                generator = new FastNoiseLite((int) chunk.getWorld().getSeed());
                generator.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
                generators.put(world, generator);
            }

            // Generate aether values based on river-like noise
            ChunkPos pos = chunk.getPos();
            aether.set(new AetherEntry(AetherType.WATER,
                    generateLeyLineNoise(generator, pos, 0, 300), 300));
            aether.set(new AetherEntry(AetherType.FIRE,
                    generateLeyLineNoise(generator, pos, 1, 300), 300));
            aether.set(new AetherEntry(AetherType.EARTH,
                    generateLeyLineNoise(generator, pos, 2, 300), 300));
            aether.set(new AetherEntry(AetherType.WIND,
                    generateLeyLineNoise(generator, pos, 3, 300), 300));
        }

    }

    private int generateLeyLineNoise(FastNoiseLite generator, ChunkPos pos, int index, float outputScale) {
        final float noiseScale = 0.2f;
        final float noisePower = 8;
        float offset = index * 4096;

        float rawNoise = generator.GetNoise(pos.x / noiseScale + offset, pos.z / noiseScale + offset);

        float normalizedNoise = (rawNoise + 1.0f) / 2.0f;

        float expNoise = (float)Math.pow(normalizedNoise, noisePower);

        return (int) (normalizedNoise * outputScale);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if(cap == ModCapabilities.AETHER) {
            return (LazyOptional<T>)LazyOptional.of(() -> aether);
        }

        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return ModCapabilities.AETHER.writeNBT(aether, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        ModCapabilities.AETHER.readNBT(aether, null, nbt);
    }
}
