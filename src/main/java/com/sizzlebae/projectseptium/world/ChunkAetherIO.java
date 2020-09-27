package com.sizzlebae.projectseptium.world;

import com.sizzlebae.projectseptium.capabilities.Aether;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class ChunkAetherIO {

    public static boolean saveAetherChunk(Aether aether, Chunk chunk, ServerWorld world) {
        File worldDirectory = world.getSaveHandler().getWorldDirectory();
        File dimDirectory =  world.dimension.getType().getDirectory(worldDirectory);

        // Make sure that the aether directory for this dimension exists
        File aetherDirectory = new File(dimDirectory.getAbsolutePath() + "/aether/");

        try {
            String fileName = chunk.getPos().x + "." + chunk.getPos().z + ".aed";
            File aetherFile = new File(aetherDirectory, fileName);

            FileUtils.writeByteArrayToFile(aetherFile, aether.encode());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean loadAetherChunk(Aether aether, ChunkPos pos, ServerWorld world) {

        File worldDirectory = world.getSaveHandler().getWorldDirectory();
        File dimDirectory =  world.dimension.getType().getDirectory(worldDirectory);

        File aetherDirectory = new File(dimDirectory.getAbsolutePath() + "/aether/");

        String fileName = pos.x + "." + pos.z + ".aed";
        File aetherFile = new File(aetherDirectory, fileName);

        if(!aetherFile.exists()) {
            return false;
        }

        try {
            aether.decode(FileUtils.readFileToByteArray(aetherFile));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
