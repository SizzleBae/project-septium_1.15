package com.sizzlebae.projectseptium.items;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.AetherEntry;
import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import com.sizzlebae.projectseptium.capabilities.WorldAether;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;

public class ItemAetherMap extends FilledMapItem {
    public final static byte MAP_COLOR_SHADES = 3;
    public final static byte MAP_COLOR_SHADE_SIZE = 25;

    public ItemAetherMap() {
        super(new Item.Properties().group(ItemGroup.MISC));
    }

    @Override
    public void onCreated(ItemStack stack, @Nonnull World worldIn, PlayerEntity playerIn) {
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    @Override
    public void inventoryTick(@Nonnull ItemStack stack, World worldIn, @Nonnull Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote()) {
            MapData mapdata = getMapData(stack, worldIn);
            if (mapdata != null) {
                if (entityIn instanceof PlayerEntity) {
                    PlayerEntity playerentity = (PlayerEntity)entityIn;
                    mapdata.updateVisiblePlayers(playerentity, stack);
                }

            }
        }
    }

    public void drawMap(World world, int chunkRange, MapData data) {

        ChunkPos pos = new ChunkPos(data.xCenter / 16, data.zCenter / 16);
        int chunkCount = chunkRange * 2 + 1;
        byte[] chunkColors = getChunkAetherColors(world, pos, chunkRange);

        if(chunkColors.length == 0) {
            return;
        }

        int mapPixels = 128;
        int chunkPixSize = 16 / (1 << data.scale);


        boolean changed = false;
        for(int z = -chunkRange; z < chunkRange + 1; z++) {
            for(int x = -chunkRange; x < chunkRange + 1; x++) {
                int chunkIndex = (x + chunkRange) + (z + chunkRange) * chunkCount;
                byte chunkColor = chunkColors[chunkIndex];

                int pixStartX = mapPixels / 2 + x * chunkPixSize - chunkPixSize / 2;
                int pixStartY = mapPixels / 2 + z * chunkPixSize - chunkPixSize / 2;
                for(int pixY = 0; pixY < chunkPixSize; pixY++) {
                    for(int pixX = 0; pixX < chunkPixSize; pixX++) {
                        int pixelIndex = (pixY + pixStartY) + (pixStartX + pixX) * mapPixels;

                        // Bounds check
                        if(pixelIndex < 0 || pixelIndex >= data.colors.length) {
                            continue;
                        }

                        if(data.colors[pixelIndex] != chunkColor) {
                            data.colors[pixelIndex] = chunkColor;
                            changed = true;
                        }
                    }
                }

            }
        }

        if(changed) {
            data.updateMapData(0, 0);
            data.updateMapData(mapPixels - 1, mapPixels - 1);
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        ItemStack item = playerIn.getHeldItem(handIn);

        if(!worldIn.isRemote()) {
            MapData data = getOrCreateMapData(worldIn, item);
            updateMapData(worldIn, new ChunkPos(playerIn.chunkCoordX, playerIn.chunkCoordZ), 16, 3, data);
        }

        return new ActionResult<>(ActionResultType.SUCCESS, item);
    }

    public MapData getOrCreateMapData(World worldIn, ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        if(!tag.contains("map")) {
            // Create and register map data if it does not exist already
            int i = worldIn.getNextMapId();
            MapData data = new MapData(getMapName(i));
            worldIn.registerMapData(data);
            tag.putInt("map", i);
            return data;
        }

        // Otherwise get existing map data
        return getMapData(stack, worldIn);
    }

    public void updateMapData(World worldIn, ChunkPos pos, int chunkRange, int scale, MapData data) {

        data.scale = (byte)scale;
        data.trackingPosition = true;
        data.unlimitedTracking = true;
        data.xCenter = pos.x * 16 + 8;
        data.zCenter = pos.z * 16 + 8;
        data.dimension = worldIn.dimension.getType();

        drawMap(worldIn, chunkRange, data);

        data.markDirty();
    }

    private byte[] getChunkAetherColors(World worldIn, ChunkPos pos, int chunkRange) {
        int chunkCount = chunkRange * 2 + 1;

        byte[] colors = new byte[chunkCount * chunkCount];

        WorldAether worldAether = worldIn.getCapability(ModCapabilities.WORLD_AETHER).orElseThrow(IllegalStateException::new);

        for(int z = -chunkRange; z < chunkRange + 1; z++) {
            for(int x = -chunkRange; x < chunkRange + 1; x++) {
                Aether aether = worldAether.loadChunkAether(new ChunkPos(pos.x + x, pos.z + z));

                if(aether.content.size() == 0) {
                    ProjectSeptium.LOGGER.error("Chunk " + pos.toString() + " is missing aether.");
                    continue;
                }

                AetherEntry dominantAether = Collections.max(aether.content.values(), Comparator.comparingInt(data -> data.value));
                byte aetherColor = 0;
                if(dominantAether != null && dominantAether.value > 0) {
                    byte colorStrength = (byte)Math.min(dominantAether.value / MAP_COLOR_SHADE_SIZE, MAP_COLOR_SHADES);
                    aetherColor = (byte)(dominantAether.type.mapColor + colorStrength);
                }

                int chunkIndex = (z + chunkRange) + (x + chunkRange) * chunkCount;
                colors[chunkIndex] = aetherColor;
            }
        }

        return colors;
    }

}
