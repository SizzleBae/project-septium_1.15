package com.sizzlebae.projectseptium.blocks;

import com.sizzlebae.projectseptium.capabilities.AetherType;
import com.sizzlebae.projectseptium.tileentity.ModTileEntities;
import com.sizzlebae.projectseptium.utils.IAetherTypeHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCrystal extends DirectionalBlock implements IAetherTypeHolder {

    final AetherType aetherType;

    public BlockCrystal(AetherType aetherType) {
        super(Block.Properties.create(Material.ROCK).notSolid());
        this.aetherType = aetherType;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, Direction.random(context.getWorld().getRandom()));
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

}
