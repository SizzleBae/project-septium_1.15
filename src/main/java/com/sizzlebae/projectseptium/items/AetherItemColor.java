package com.sizzlebae.projectseptium.items;

import com.sizzlebae.projectseptium.utils.IAetherTypeHolder;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;

public class AetherItemColor implements IItemColor {

    @Override
    public int getColor(@Nonnull ItemStack stack, int tintIndex) {
        if (tintIndex == 1) {
            return ((IAetherTypeHolder) stack.getItem()).getAetherType().tint.getRGB();
        }
        return Color.WHITE.getRGB();
    }

}
