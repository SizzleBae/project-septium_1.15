package com.sizzlebae.projectseptium.world;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.AetherEntry;
import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import com.sizzlebae.projectseptium.capabilities.WorldAether;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldAetherTicker {

    private WorldAether worldAether;

    private final HashSet<ChunkPos> tickingAetherSet = new HashSet<>();
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
            for(ChunkPos pos : tickingAetherSet) {
                Aether chunkAether = worldAether.aetherMap.get(pos);

                for(AetherEntry entry : chunkAether.content.values()) {
                    // Do passive regen if aether value is below basis
                    if(entry.value < entry.basis) {
                        // Regenerate faster based on basis size
                        entry.value += entry.basis * 0.01;
                        entry.value = Math.min(entry.value, entry.basis);
                    }
                }

                ProjectSeptium.LOGGER.warn(chunkAether.toString());
                chunkAether.notifyListeners();
            }
        }

        ticks++;
    }

    public void add(ChunkPos pos) {
        aethersToAdd.add(pos);
        aethersToRemove.remove(pos);
    }

    public void remove(ChunkPos pos) {
        aethersToRemove.add(pos);
        aethersToAdd.remove(pos);
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
