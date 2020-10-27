package com.sizzlebae.projectseptium.spells.properties;

import net.minecraft.nbt.INBT;

public abstract class PropertyStrategy<T> {

    public abstract INBT serialize(T value);
    public abstract T deserialize(INBT memento);

    public abstract boolean compare(PropertyStrategy<?> other);

}
