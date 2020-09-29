package com.sizzlebae.projectseptium.items;

import com.sizzlebae.projectseptium.capabilities.AetherType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import javax.annotation.Nonnull;

public class ItemSepith extends Item implements IAetherTypeHolder {
    public final AetherType type;

    public ItemSepith(AetherType type) {
        super(new Item.Properties().group(ItemGroup.MISC));

        this.type = type;
    }



    @Override
    public AetherType getAetherType() {
        return type;
    }
}
