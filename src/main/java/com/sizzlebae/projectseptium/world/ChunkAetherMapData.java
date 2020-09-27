package com.sizzlebae.projectseptium.world;

//import com.sizzlebae.projectseptium.capabilities.Aether;
//import com.sizzlebae.projectseptium.networking.ModChannel;
//import com.sizzlebae.projectseptium.networking.messages.ChunkAetherToClient;
//import net.minecraft.util.math.ChunkPos;
//import net.minecraft.world.World;
//import net.minecraft.world.chunk.Chunk;
//import net.minecraft.world.dimension.DimensionType;
//import net.minecraft.world.server.ServerWorld;
//import net.minecraftforge.fml.network.PacketDistributor;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import java.util.HashMap;

public class ChunkAetherMapData {
//
//    ChunkAetherGenerator generator = new ChunkAetherGenerator();
//
//    HashMap<DimensionType, HashMap<ChunkPos, Aether>> worldAetherMap = new HashMap<>();
//
//    @Nonnull
//    public Aether getChunkAether(World world, ChunkPos pos) {
//        Aether aether = getChunkAetherWithoutLoading(world, pos);
//
//        // If aether already exists, return it
//        if(aether != null) {
//            return aether;
//        }
//
//        // Get or create the aether map for this dimension
//        DimensionType dimension = world.getDimension().getType();
//        HashMap<ChunkPos, Aether> aetherMap = worldAetherMap.computeIfAbsent(dimension, k -> new HashMap<>());
//
//        aether = new Aether();
//
//        // Store new aether that is about to be loaded
//        aetherMap.put(pos, aether);
//
//        if(!world.isRemote() && world instanceof ServerWorld) {
//            // If this is on server, attempt to load from disk
//            if(!ChunkAetherIO.loadAetherChunk(aether, pos, (ServerWorld) world)) {
//                // If there is no aether on disk, generate new aether chunk
//                generator.generateChunkAether(aether, world, pos);
//            }
//
//            // Update clients when aether in chunk changes automatically
//            aether.addListener((data) -> {
//                Chunk chunk = world.getChunk(pos.x, pos.z);
//                ModChannel.simpleChannel.send(
//                        PacketDistributor.TRACKING_CHUNK.with(()->chunk),
//                        new ChunkAetherToClient(pos, data));
//            });
//
//            // Update clients now that chunk is loaded/generated
//            aether.notifyListeners();
//        } else {
//            //TODO: Maybe not do this?
//            // If on client, request aether data from server
//            //ModChannel.simpleChannel.send(PacketDistributor.SERVER.noArg(), new RequestChunkAetherFromServer(pos.x, pos.z));
//        }
//
//        return aether;
//    }
//
//    @Nullable
//    public Aether getChunkAetherWithoutLoading(World world, ChunkPos pos) {
//        HashMap<ChunkPos, Aether> aetherMap = worldAetherMap.get(world.getDimension().getType());
//
//        if(aetherMap != null) {
//            return aetherMap.get(pos);
//        }
//        return  null;
//    }

}
