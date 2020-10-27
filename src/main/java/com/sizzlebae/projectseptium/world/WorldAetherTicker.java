package com.sizzlebae.projectseptium.world;
import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldAetherTicker {

    public final int tickInterval = 20;
    public final float aetherDistributionRate = 0.1f;
    public final float aetherRegenerationRate = 0.05f;

    private final WorldAether worldAether;

    public final HashSet<ChunkPos> tickingAetherSet = new HashSet<>();
    private final HashSet<ChunkPos> aethersToAdd = new HashSet<>();
    private final HashSet<ChunkPos> aethersToRemove = new HashSet<>();

    private int ticks = 0;

    public WorldAetherTicker(WorldAether worldAether) {
        this.worldAether = worldAether;
    }

    public void tick() {
        if(ticks % tickInterval == 0 && !worldAether.world.isRemote()) {
            // Resolve ticking aether set
            tickingAetherSet.addAll(aethersToAdd);
            tickingAetherSet.removeAll(aethersToRemove);

            HashSet<Aether> changedAethers = new HashSet<>();

            // Do passive regen for each aether
            regenerate(changedAethers);

            // Spread aether between chunks
            spreadAether(changedAethers);

            // Expend excess aether in various ways
            manifestAether(changedAethers);

            // Only notify changed aether chunk listeners
            changedAethers.forEach(Aether::notifyListeners);
        }

        ticks++;
    }

    private void manifestAether(HashSet<Aether> changedAethers) {
        for(ChunkPos pos : tickingAetherSet) {
            Aether aether = worldAether.loadChunkAether(pos);

            boolean changed = false;
            for(AetherEntry aetherEntry : aether.content.values()) {
                if(aetherEntry.basisOffset() > 0) {
                    // Aether entries with excess value should expend it somehow
                    ServerWorld world = (ServerWorld)worldAether.world;
                    BlockPos lightningPos = pos.asBlockPos().add(8, 0, 8);
                    lightningPos = lightningPos.add(0,world.getHeight(Heightmap.Type.WORLD_SURFACE, lightningPos.getX(), lightningPos.getZ()),0);

                    world.addLightningBolt(new LightningBoltEntity(world, lightningPos.getX(), lightningPos.getY(), lightningPos.getZ(), false));

                    aetherEntry.value *= 0.8;
                    changed = true;
                }
            }

            if(changed) {
                changedAethers.add(aether);
            }
        }
    }

    private void spreadAether(HashSet<Aether> changedAethers) {
        // A map of aethers to change, key is aether and value is a map of aether types with a key of aether type and value of change
        HashMap<Aether, HashMap<AetherType, Integer>> results = new HashMap<>();

        for(ChunkPos pos : tickingAetherSet) {
            Aether originAether = worldAether.loadChunkAether(pos);
            distributeWithNeighbor(originAether, worldAether.loadChunkAether(new ChunkPos(pos.x + 1, pos.z)), results);
            distributeWithNeighbor(originAether, worldAether.loadChunkAether(new ChunkPos(pos.x - 1, pos.z)), results);
            distributeWithNeighbor(originAether, worldAether.loadChunkAether(new ChunkPos(pos.x, pos.z + 1)), results);
            distributeWithNeighbor(originAether, worldAether.loadChunkAether(new ChunkPos(pos.x, pos.z - 1)), results);
        }

        // Apply changes
        for(Map.Entry<Aether, HashMap<AetherType, Integer>> changeEntry : results.entrySet()) {
            Aether aether = changeEntry.getKey();

            for(Map.Entry<AetherType, Integer> aetherEntryChange : changeEntry.getValue().entrySet()) {
                AetherType type = aetherEntryChange.getKey();
                if(aether.content.containsKey(type)) {
                    aether.content.get(type).value += aetherEntryChange.getValue();
                } else {
                    // If the aether does not exist in the chunk, create it
                    aether.put(new AetherEntry(type, aetherEntryChange.getValue(),0));
                }
            }

            changedAethers.add(aether);
        }
    }

    private void distributeWithNeighbor(Aether originAether, Aether neighborAether, HashMap<Aether, HashMap<AetherType, Integer>> results) {
        for(AetherEntry originEntry : originAether.content.values()) {
            int offset = originEntry.basisOffset();
            if(offset == 0) {
                continue;
            }

            AetherEntry neighborEntry = neighborAether.content.getOrDefault(originEntry.type, new AetherEntry(originEntry.type, 0, 0));

            HashMap<AetherType, Integer> resultOrigin = results.computeIfAbsent(originAether, k->new HashMap<>());
            HashMap<AetherType, Integer> resultNeighbor = results.computeIfAbsent(neighborAether, k->new HashMap<>());

            computeChange(originEntry, neighborEntry, resultOrigin, resultNeighbor);
        }
    }

    private void computeChange(AetherEntry origin, AetherEntry neighbor, HashMap<AetherType, Integer> resultOrigin, HashMap<AetherType, Integer> resultNeighbor) {
        int offset = origin.basisOffset();
        int change = (int) (offset / 4f * aetherDistributionRate);

        // Don't grab more aether than what is available in the neighbor aether
        if(change+neighbor.value < 0) {
            change = -neighbor.value;
        }

        int finalChange = change;
        resultNeighbor.merge(origin.type, change, (old,k)->old+finalChange);
        resultOrigin.merge(neighbor.type, -change, (old,k)->old-finalChange);
    }

    private void regenerate(HashSet<Aether> changedAethers) {
        for(ChunkPos pos : tickingAetherSet) {
            Aether aether = worldAether.loadChunkAether(pos);

            boolean changed = false;
            for(AetherEntry entry : aether.content.values()) {
                // Do passive regen if aether value is below basis
                if(entry.value < entry.basis) {
                    // Regenerate faster based on basis size, must always be at least one
                    entry.value += Math.max(entry.basis * aetherRegenerationRate, 1);
                    // Don't go above basis from regeneration
                    entry.value = Math.min(entry.value, entry.basis);
                    changed = true;
                }
            }

            if(changed) {
                changedAethers.add(aether);
            }
        }
    }

    public void add(ChunkPos pos) {
        aethersToAdd.add(pos);
        aethersToRemove.remove(pos);
    }

    public void remove(ChunkPos pos) {
        aethersToRemove.add(pos);
        aethersToAdd.remove(pos);
    }

    public LongArrayNBT writeTickingAethers() {
        long[] data = tickingAetherSet.stream().mapToLong(ChunkPos::asLong).toArray();
        return new LongArrayNBT(data);
    }

    public void readTickingAethers(LongArrayNBT tag) {

        ChunkPos[] arr = Arrays.stream(tag.getAsLongArray()).mapToObj(ChunkPos::new).toArray(ChunkPos[]::new);

        tickingAetherSet.clear();
        tickingAetherSet.addAll(Arrays.asList(arr));

    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if(event.side != LogicalSide.SERVER || event.phase == TickEvent.Phase.END) {
            return;
        }

        WorldAether worldAether = event.world.getCapability(ModCapabilities.WORLD_AETHER).orElseThrow(IllegalAccessError::new);
        worldAether.ticker.tick();
    }

}
