package com.sizzlebae.projectseptium.capabilities;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.networking.ModChannel;
import com.sizzlebae.projectseptium.networking.messages.ChunkAetherToClient;
import com.sizzlebae.projectseptium.networking.messages.RequestChunkAetherFromServer;
import com.sizzlebae.projectseptium.world.ChunkAetherGenerator;
import com.sizzlebae.projectseptium.world.ChunkAetherIO;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldAether {

    private ChunkAetherGenerator generator;

    private HashMap<ChunkPos, Aether> aetherMap = new HashMap<>();

    private World world;

    public WorldAether(World world) {
        this.world = world;

        generator = new ChunkAetherGenerator(world);
    }

    public static class Storage implements Capability.IStorage<WorldAether> {

        @Override
        public INBT writeNBT(Capability<WorldAether> capability, WorldAether instance, Direction side) {
            return new CompoundNBT();
        }

        @Override
        public void readNBT(Capability<WorldAether> capability, WorldAether instance, Direction side, INBT nbt) {
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if(event.side != LogicalSide.SERVER) {
            return;
        }


    }

    @Nonnull
    public Aether getChunkAether(ChunkPos pos) {
        Aether aether = aetherMap.get(pos);

        // If aether already exists, return it
        if(aether != null) {
            return aether;
        }

        aether = new Aether();

        // Store new aether that is about to be loaded
        aetherMap.put(pos, aether);

        if(!world.isRemote() && world instanceof ServerWorld) {
            // If this is on server, attempt to load from disk
            if(!ChunkAetherIO.loadAetherChunk(aether, pos, (ServerWorld) world)) {
                // If there is no aether on disk, generate new aether chunk
                generator.generateChunkAether(aether, pos);
            }

            // Update clients when aether in chunk changes automatically
            aether.addListener((data) -> {
                Chunk chunk = world.getChunk(pos.x, pos.z);
                ModChannel.simpleChannel.send(
                        PacketDistributor.TRACKING_CHUNK.with(()->chunk),
                        new ChunkAetherToClient(pos, data));
            });

            // Update clients now that chunk is loaded/generated
//            aether.notifyListeners();
        } else {
            //TODO: Maybe not do this?
            // If on client, request aether data from server
            ModChannel.simpleChannel.send(PacketDistributor.SERVER.noArg(), new RequestChunkAetherFromServer(pos.x, pos.z));
        }

        return aether;
    }

    @Nullable
    public Aether getChunkAetherWithoutLoading(ChunkPos pos) {
        return aetherMap.get(pos);
    }

}
