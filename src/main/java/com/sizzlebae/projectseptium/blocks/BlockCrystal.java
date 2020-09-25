package com.sizzlebae.projectseptium.blocks;

import com.sizzlebae.projectseptium.tileentity.ModTileEntities;
import com.sizzlebae.projectseptium.tileentity.TileEntityAetherContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockCrystal extends Block {

    public BlockCrystal() {
        super(Block.Properties.create(Material.ROCK));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.AETHER_CONTAINER.create();
    }

//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
//        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
//    }
}
