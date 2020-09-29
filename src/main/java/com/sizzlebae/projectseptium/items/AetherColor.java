package com.sizzlebae.projectseptium.items;

import com.google.common.base.Supplier;
import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.AetherType;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;

public class AetherColor implements IItemColor, Supplier<AetherType> {

    @Override
    public int getColor(@Nonnull ItemStack stack, int tintIndex) {
        Item item = stack.getItem();

        switch (tintIndex) {
            case 0:
                return Color.WHITE.getRGB();
            case 1: {
                if(item instanceof IAetherTypeHolder) {
                    return ((IAetherTypeHolder) item).getAetherType().tint.getRGB();
                }
            }
        }

        ProjectSeptium.LOGGER.error("AetherColor: " + tintIndex + " has no matching color for item " + item.getName());
        return Color.BLACK.getRGB();
    }

    @Override
    public AetherType get() {
        return null;
    }
}
