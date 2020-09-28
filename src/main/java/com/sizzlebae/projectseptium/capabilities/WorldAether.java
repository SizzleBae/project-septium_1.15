package com.sizzlebae.projectseptium.capabilities;

import com.sizzlebae.projectseptium.networking.ModChannel;
import com.sizzlebae.projectseptium.networking.messages.RequestChunkAetherFromServer;
import com.sizzlebae.projectseptium.world.ChunkAetherGenerator;
import com.sizzlebae.projectseptium.world.ChunkAetherIO;
import com.sizzlebae.projectseptium.world.WorldAetherTicker;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class WorldAether {

    public final WorldAetherTicker ticker;
    public final ChunkAetherGenerator generator;
    public final ChunkAetherIO io;
    public final boolean isRemote;

    public final HashMap<ChunkPos, Aether> aetherMap = new HashMap<>();

    public WorldAether(ChunkAetherGenerator generator, ChunkAetherIO io, boolean isRemote) {
        if(!isRemote && io == null) {
            throw new IllegalStateException("WorldAether: Can't instantiate server side without IO.");
        }

        this.generator = generator;
        this.isRemote = isRemote;
        this.io = io;

        ticker = new WorldAetherTicker(this);
    }

    public static class Storage implements Capability.IStorage<WorldAether> {
        @Override
        public INBT writeNBT(Capability<WorldAether> capability, WorldAether instance, Direction side) {
            return instance.ticker.writeTickingAethers();
        }

        @Override
        public void readNBT(Capability<WorldAether> capability, WorldAether instance, Direction side, INBT nbt) {
            instance.ticker.readTickingAethers((LongArrayNBT) nbt);
        }
    }

    @Nonnull
    public Aether loadChunkAether(ChunkPos pos) {
        Aether aether = aetherMap.get(pos);

        // If aether already exists, return it
        if(aether != null) {
            return aether;
        }

        aether = new Aether();

        // Store new aether that is about to be loaded
        aetherMap.put(pos, aether);

        if(!isRemote) {
            // If this is on server, attempt to load from disk
            if(!io.loadAetherChunk(aether, pos)) {
                // If there is no aether on disk, generate new aether chunk
                generator.generateChunkAether(aether, pos);
            }

            // Update clients when aether in chunk changes automatically
            //TODO: DO this?
//            aether.addListener((data) -> {
//                Chunk chunk = world.getChunk(pos.x, pos.z);
//                ModChannel.simpleChannel.send(
//                        PacketDistributor.TRACKING_CHUNK.with(()->chunk),
//                        new ChunkAetherToClient(pos, data));
//            });

            // When aether changes, check if it is outside basis. And if it is, begin ticking it
            aether.addListener((data) -> {
                boolean atBasis = true;
                for(AetherEntry entry : data.content.values()) {
                    if(entry.value != entry.basis) {
                        atBasis = false;
                        break;
                    }
                }

                if(atBasis) {
                    ticker.remove(pos);
                } else {
                    ticker.add(pos);
                }
            });

            // Update clients now that chunk is loaded/generated
            //aether.notifyListeners();
        } else {
            //TODO: Maybe not do this?
            // If on client, request aether data from server
            ModChannel.simpleChannel.send(PacketDistributor.SERVER.noArg(), new RequestChunkAetherFromServer(pos.x, pos.z));
        }

        return aether;
    }

    public void saveAllAetherChunks() {
        this.aetherMap.forEach((pos, aether)->{
            io.saveAetherChunk(aether, pos);
        });
    }

    @Nullable
    public Aether getChunkAetherWithoutLoading(ChunkPos pos) {
        return aetherMap.get(pos);
    }

}
