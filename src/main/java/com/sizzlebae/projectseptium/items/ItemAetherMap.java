package com.sizzlebae.projectseptium.items;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.AetherEntry;
import com.sizzlebae.projectseptium.capabilities.AetherType;
import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ItemAetherMap extends FilledMapItem {

    public ItemAetherMap() {
        super(new Item.Properties().group(ItemGroup.MISC));
    }


    @Nullable
    @Override
    protected MapData getCustomMapData(ItemStack stack, World worldIn) {
//        MapData mapdata = getData(stack, worldIn);
//        if (mapdata == null && !worldIn.isRemote) {
//            mapdata = createMapData(stack, worldIn, worldIn.getWorldInfo().getSpawnX(), worldIn.getWorldInfo().getSpawnZ(), 3, false, false, worldIn.dimension.getType());
//        }
//
//        mapdata.colors
//
//        return mapdata;

        MapData mapdata = super.getCustomMapData(stack, worldIn);
        return mapdata;
    }

    @Override
    public void updateMapData(World worldIn, Entity viewer, MapData data) {
        data.colors = getChunkAetherColors(worldIn, new ChunkPos(viewer.chunkCoordX, viewer.chunkCoordZ), 10);
        data.updateMapData(0,0);
        data.updateMapData(127,127);
    }

//    @Override
//    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
//        ItemStack item = playerIn.getHeldItem(handIn);
//
//        // The range of aether chunks from the user that the map will read
//        int chunkRange = 5;
//        int chunkX = playerIn.chunkCoordX;
//        int chunkZ = playerIn.chunkCoordZ;
//        byte[] colors = new byte[128 * 128];
//
//        for(int x = chunkX - chunkRange; x < chunkX + chunkRange + 1; x++) {
//            for(int z = chunkZ - chunkRange; z < chunkZ + chunkRange + 1; z++) {
//                Aether aether = worldIn.getChunk(x, z).getCapability(ModCapabilities.CAPABILITY_AETHER).orElse(null);
//
//                colors[(z * 5) + (x * 5) * 128] = (byte)aether.water;
//
//            }
//        }
//
//        CompoundNBT tag = item.getOrCreateTag();
//        tag.putByteArray("colors", colors);
//    }

    private byte[] getChunkAetherColors(World worldIn, ChunkPos pos, int chunkRange) {
        int mapPixels = 128;
        int chunkPixSize = 4;

        byte[] colors = new byte[mapPixels * mapPixels];
        for(int z = -chunkRange; z < chunkRange + 1; z++) {
            for(int x = -chunkRange; x < chunkRange + 1; x++) {
                Chunk chunk = worldIn.getChunk(pos.x + x, pos.z + z);
                Aether aether = chunk.getCapability(ModCapabilities.CAPABILITY_AETHER).orElse(null);

                AetherEntry dominantAether = Collections.max(aether.content.values(), Comparator.comparingInt(data -> data.value));
                byte aetherColor = 0;
                if(dominantAether != null && dominantAether.value > 0) {
                    byte colorStrength = (byte)Math.min(dominantAether.value / Aether.AETHER_COLOR_SIZE, Aether.AETHER_COLOR_RANGE - 1);
                    aetherColor = (byte)(dominantAether.type.colorStart + colorStrength);
                }

                int pixStartX = mapPixels / 2 + x * chunkPixSize - chunkPixSize / 2;
                int pixStartY = mapPixels / 2 + z * chunkPixSize - chunkPixSize / 2;
                for(int pixY = 0; pixY < chunkPixSize; pixY++) {
                    for(int pixX = 0; pixX < chunkPixSize; pixX++) {
                        colors[(pixY + pixStartY) + (pixStartX + pixX) * mapPixels] = aetherColor;
                    }
                }

            }
        }
        return colors;

    }

}
