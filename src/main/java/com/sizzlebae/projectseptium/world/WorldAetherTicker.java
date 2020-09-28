package com.sizzlebae.projectseptium.world;
import com.google.common.collect.Sets;
import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.*;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldAetherTicker {

    private final float aetherDistributionRate = 0.1f;
    private final float aetherRegenerationRate = 0.05f;

    private final WorldAether worldAether;

    public final HashSet<ChunkPos> tickingAetherSet = new HashSet<>();
    private final HashSet<ChunkPos> aethersToAdd = new HashSet<>();
    private final HashSet<ChunkPos> aethersToRemove = new HashSet<>();

    private int ticks = 0;

    public WorldAetherTicker(WorldAether worldAether) {
        this.worldAether = worldAether;
    }

    public void tick() {
        // Resolve ticking aether set
        tickingAetherSet.addAll(aethersToAdd);
        tickingAetherSet.removeAll(aethersToRemove);

        if(ticks % 20 == 0) {
            regenerate();

            distributeAether();
        }

        ticks++;
    }

    private void distributeAether() {
        HashMap<ChunkPos, HashMap<AetherType, Integer>> changes = new HashMap<>();

        final AetherEntry emptyAetherPlaceholder = new AetherEntry(AetherType.WATER, 0, 0);

        for(ChunkPos pos : tickingAetherSet) {
            Aether aether = worldAether.loadChunkAether(pos);

            ChunkPos posN = new ChunkPos(pos.x + 1, pos.z);
            ChunkPos posS = new ChunkPos(pos.x - 1, pos.z);
            ChunkPos posE = new ChunkPos(pos.x, pos.z + 1);
            ChunkPos posW = new ChunkPos(pos.x, pos.z - 1);

            Aether aetherN = worldAether.loadChunkAether(posN);
            Aether aetherS = worldAether.loadChunkAether(posS);
            Aether aetherE = worldAether.loadChunkAether(posE);
            Aether aetherW = worldAether.loadChunkAether(posW);

            for(AetherEntry entry : aether.content.values()) {
                int offset = entry.basisOffset();
                if(offset == 0) {
                    continue;
                }

                AetherEntry entryN = aetherN.content.getOrDefault(entry.type, emptyAetherPlaceholder);
                AetherEntry entryS = aetherS.content.getOrDefault(entry.type, emptyAetherPlaceholder);
                AetherEntry entryE = aetherE.content.getOrDefault(entry.type, emptyAetherPlaceholder);
                AetherEntry entryW = aetherW.content.getOrDefault(entry.type, emptyAetherPlaceholder);

                HashMap<AetherType, Integer> resultOrigin = changes.computeIfAbsent(pos, k->new HashMap<>());

                computeChange(entry, entryN, resultOrigin, changes.computeIfAbsent(posN, k->new HashMap<>()));
                computeChange(entry, entryS, resultOrigin, changes.computeIfAbsent(posS, k->new HashMap<>()));
                computeChange(entry, entryE, resultOrigin, changes.computeIfAbsent(posE, k->new HashMap<>()));
                computeChange(entry, entryW, resultOrigin, changes.computeIfAbsent(posW, k->new HashMap<>()));
            }
        }

        // Apply changes
        for(Map.Entry<ChunkPos, HashMap<AetherType, Integer>> changeEntry : changes.entrySet()) {
            Aether aether = worldAether.loadChunkAether(changeEntry.getKey());

            for(Map.Entry<AetherType, Integer> aetherChangeEntry : changeEntry.getValue().entrySet()) {
                aether.content.get(aetherChangeEntry.getKey()).value += aetherChangeEntry.getValue();
            }

            aether.notifyListeners();
        }
    }

    private void computeChange(AetherEntry origin, AetherEntry neighbor, HashMap<AetherType, Integer> resultOrigin, HashMap<AetherType, Integer> resultNeighbor) {
        int offset = origin.basisOffset();
        int change = (int) (offset / 4f * aetherDistributionRate);

        resultNeighbor.merge(origin.type, change, (old,k)->old+change);
        resultOrigin.merge(neighbor.type, -change, (old,k)->old-change);
    }

    private void regenerate() {
        for(ChunkPos pos : tickingAetherSet) {
            Aether chunkAether = worldAether.loadChunkAether(pos);

            boolean changed = false;
            for(AetherEntry entry : chunkAether.content.values()) {
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
                chunkAether.notifyListeners();
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
