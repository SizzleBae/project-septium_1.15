package com.sizzlebae.projectseptium.items;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.*;
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
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;

public class ItemAetherMap extends FilledMapItem {
    public ItemAetherMap() {
        super(new Item.Properties().group(ItemGroup.MISC));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new AetherMapCapabilityProvider();
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote()) {
            MapData mapdata = getMapData(stack, worldIn);
            if (mapdata != null) {
                if (entityIn instanceof PlayerEntity) {
                    PlayerEntity playerentity = (PlayerEntity)entityIn;
                    mapdata.updateVisiblePlayers(playerentity, stack);
                }

                if (!mapdata.locked && (isSelected || entityIn instanceof PlayerEntity && ((PlayerEntity)entityIn).getHeldItemOffhand() == stack)) {
                    this.updateMapData(stack, worldIn, entityIn, mapdata);
                }

            }
        }
    }

    public void updateMapData(ItemStack stack, World worldIn, Entity viewer, MapData data) {

        AetherMap aetherMap = stack.getCapability(ModCapabilities.AETHER_MAP).orElseThrow(IllegalStateException::new);
        int chunkRange = aetherMap.chunkRange;
        int chunkCount = chunkRange * 2 + 1;
        byte[] chunkColors = aetherMap.chunkColors;

        if(chunkColors.length == 0) {
            return;
        }

        int mapPixels = 128;
        int chunkPixSize = 2;

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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack item = playerIn.getHeldItem(handIn);

        if(!worldIn.isRemote()) {
            updateData(item, worldIn, new ChunkPos(playerIn.chunkCoordX, playerIn.chunkCoordZ), 26);
        }

        return new ActionResult(ActionResultType.SUCCESS, item);
    }

    public void updateData(ItemStack stack, World worldIn, ChunkPos pos, int chunkRange) {

        AetherMap aetherMap = stack.getCapability(ModCapabilities.AETHER_MAP).orElseThrow(IllegalStateException::new);
        aetherMap.chunkRange = chunkRange;
        aetherMap.chunkColors = getChunkAetherColors(worldIn, pos, chunkRange);

        CompoundNBT tag = stack.getOrCreateTag();
        MapData data = null;
        if(!tag.contains("map")) {
            // Create and register map data if it does not exist already
            int i = worldIn.getNextMapId();
            data = new MapData(getMapName(i));
            worldIn.registerMapData(data);
            tag.putInt("map", i);
        } else {
            // Otherwise get existing map data
            data = getMapData(stack, worldIn);
        }

        data.scale = 3;
        data.trackingPosition = true;
        data.unlimitedTracking = true;
        data.xCenter = pos.x * 16 + 8;
        data.zCenter = pos.z * 16 + 8;
        data.dimension = worldIn.dimension.getType();
        data.markDirty();
    }

    private byte[] getChunkAetherColors(World worldIn, ChunkPos pos, int chunkRange) {
        int chunkCount = chunkRange * 2 + 1;

        byte[] colors = new byte[chunkCount * chunkCount];

        for(int z = -chunkRange; z < chunkRange + 1; z++) {
            for(int x = -chunkRange; x < chunkRange + 1; x++) {
                Chunk chunk = worldIn.getChunk(pos.x + x, pos.z + z);
                Aether aether = chunk.getCapability(ModCapabilities.AETHER).orElseThrow(IllegalStateException::new);

                // TODO: FIX ME
                if(aether.content.size() == 0) {
                    ProjectSeptium.LOGGER.error("Chunk has no aether??");
                    continue;
                }

                AetherEntry dominantAether = Collections.max(aether.content.values(), Comparator.comparingInt(data -> data.value));
                byte aetherColor = 0;
                if(dominantAether != null && dominantAether.value > 0) {
                    byte colorStrength = (byte)Math.min(dominantAether.value / Aether.AETHER_COLOR_SIZE, Aether.AETHER_COLOR_RANGE - 1);
                    aetherColor = (byte)(dominantAether.type.colorStart + colorStrength);
                }

                int chunkIndex = (z + chunkRange) + (x + chunkRange) * chunkCount;
                colors[chunkIndex] = aetherColor;
            }
        }

        return colors;
    }

}
