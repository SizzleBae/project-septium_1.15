package com.sizzlebae.projectseptium.world;

import com.sizzlebae.projectseptium.capabilities.Aether;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class ChunkAetherIO {
    public File directory;
    public ChunkAetherIO(File directory) {
        this.directory = directory;
    }

    public boolean saveAetherChunk(Aether aether, ChunkPos pos) {
        try {
            String fileName = pos.x + "." + pos.z + ".aed";
            File aetherFile = new File(directory, fileName);

            FileUtils.writeByteArrayToFile(aetherFile, aether.encode());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean loadAetherChunk(Aether aether, ChunkPos pos) {
        String fileName = pos.x + "." + pos.z + ".aed";
        File aetherFile = new File(directory, fileName);

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
