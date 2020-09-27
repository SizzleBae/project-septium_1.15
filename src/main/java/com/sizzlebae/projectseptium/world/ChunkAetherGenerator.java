package com.sizzlebae.projectseptium.world;

import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.AetherEntry;
import com.sizzlebae.projectseptium.capabilities.AetherType;
import com.sizzlebae.projectseptium.utils.FastNoiseLite;
import net.minecraft.util.math.ChunkPos;

public class ChunkAetherGenerator {

    FastNoiseLite generator;

    public ChunkAetherGenerator(int seed) {
        generator = new FastNoiseLite(seed);
    }

    public void generateChunkAether(Aether aether, ChunkPos pos) {
        // Generate aether values based on river-like noise
        aether.put(generateAetherEntry(AetherType.WATER, pos));
        aether.put(generateAetherEntry(AetherType.FIRE, pos));
        aether.put(generateAetherEntry(AetherType.EARTH, pos));
        aether.put(generateAetherEntry(AetherType.WIND, pos));
    }

    private AetherEntry generateAetherEntry(AetherType type, ChunkPos pos) {
        int basis = generateLeyLineNoise(generator, pos, type.id, 300);
        return new AetherEntry(type, basis, basis);
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
