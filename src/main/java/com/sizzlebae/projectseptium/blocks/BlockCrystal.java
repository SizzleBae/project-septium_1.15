package com.sizzlebae.projectseptium.blocks;

import com.sizzlebae.projectseptium.capabilities.AetherType;
import com.sizzlebae.projectseptium.utils.IAetherTypeHolder;
import com.sizzlebae.projectseptium.tileentity.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockCrystal extends Block implements IAetherTypeHolder {

    final AetherType aetherType;

    public BlockCrystal(AetherType aetherType) {
        super(Block.Properties.create(Material.ROCK).notSolid());
        this.aetherType = aetherType;
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

    @Override
    public AetherType getAetherType() {
        return aetherType;
    }

//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
//        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
//    }
}
