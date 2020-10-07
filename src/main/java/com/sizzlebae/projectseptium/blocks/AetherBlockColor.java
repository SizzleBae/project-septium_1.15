package com.sizzlebae.projectseptium.blocks;

import com.sizzlebae.projectseptium.utils.IAetherTypeHolder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

public class AetherBlockColor implements IBlockColor {
    @Override
    public int getColor(@Nonnull BlockState blockState, @Nullable ILightReader lightReader, @Nullable BlockPos pos, int tintIndex) {
        if (tintIndex == 0) {
            return ((IAetherTypeHolder) blockState.getBlock()).getAetherType().tint.getRGB();
        }
        return Color.WHITE.getRGB();
    }
}
