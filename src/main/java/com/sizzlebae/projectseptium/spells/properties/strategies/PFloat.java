package com.sizzlebae.projectseptium.spells.properties.strategies;

import com.sizzlebae.projectseptium.spells.properties.PropertyStrategy;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;

public class PFloat extends PropertyStrategy<Float> {
    @Override
    public INBT serialize(Float value) {
        return FloatNBT.valueOf(value);
    }

    @Override
    public Float deserialize(INBT memento) {
        return ((FloatNBT)memento).getFloat();
    }

    @Override
    public boolean compare(PropertyStrategy<?> other) {
        return other instanceof PFloat;
    }
}
