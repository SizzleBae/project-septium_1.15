package com.sizzlebae.projectseptium.world;

import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.AetherEntry;
import com.sizzlebae.projectseptium.capabilities.AetherType;
import com.sizzlebae.projectseptium.utils.FastNoiseLite;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class ChunkAetherGenerator {

    FastNoiseLite generator;

    public ChunkAetherGenerator(World world) {
        DimensionType dimension = world.getDimension().getType();
        generator = new FastNoiseLite((int) world.getSeed() + dimension.getId());
    }

    public void generateChunkAether(Aether aether, ChunkPos pos) {
        // Generate aether values based on river-like noise
        aether.put(new AetherEntry(AetherType.WATER,
                generateLeyLineNoise(generator, pos, 0, 300), 300));
        aether.put(new AetherEntry(AetherType.FIRE,
                generateLeyLineNoise(generator, pos, 1, 300), 300));
        aether.put(new AetherEntry(AetherType.EARTH,
                generateLeyLineNoise(generator, pos, 2, 300), 300));
        aether.put(new AetherEntry(AetherType.WIND,
                generateLeyLineNoise(generator, pos, 3, 300), 300));

    }

    private int generateLeyLineNoise(FastNoiseLite generator, ChunkPos pos, int index, float outputScale) {
        final float noiseScale = 0.2f;
        final float noisePower = 4;
        final float outputOffset = 0f;
        float offset = index * 100000;

        float rawNoise = generator.GetNoise(pos.x / noiseScale + offset, pos.z / noiseScale + offset);

        float normalizedNoise = (rawNoise + 1.0f) / 2.0f;

        float expNoise = (float)Math.pow(normalizedNoise, noisePower);

        float result = expNoise * outputScale + outputOffset * noisePower;
        return (int) Math.max(result, 0);
    }

}
