package com.sizzlebae.projectseptium.items;

import com.sizzlebae.projectseptium.utils.IAetherTypeHolder;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;

public class AetherBlockItemColor implements IItemColor {

    @Override
    public int getColor(@Nonnull ItemStack stack, int tintIndex) {
        if (tintIndex == 0) {
            Block block = ((BlockItem) stack.getItem()).getBlock();
            return ((IAetherTypeHolder) block).getAetherType().tint.getRGB();
        }
        return Color.WHITE.getRGB();
    }

}
