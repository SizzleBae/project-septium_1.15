package com.sizzlebae.projectseptium.spells.properties.strategies;

import com.sizzlebae.projectseptium.spells.SpellComponentIO;
import com.sizzlebae.projectseptium.spells.components.SpellComponent;
import com.sizzlebae.projectseptium.spells.properties.PropertyStrategy;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class PComponent extends PropertyStrategy<SpellComponent> {
    @Override
    public INBT serialize(SpellComponent value) {
        return SpellComponentIO.INSTANCE.serialize(value);
    }

    @Override
    public SpellComponent deserialize(INBT memento) {
        return SpellComponentIO.INSTANCE.deserialize((CompoundNBT) memento);
    }

    @Override
    public boolean compare(PropertyStrategy<?> other) {
        return other instanceof PComponent;
    }
}
