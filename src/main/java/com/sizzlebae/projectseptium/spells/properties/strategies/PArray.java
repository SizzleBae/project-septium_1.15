package com.sizzlebae.projectseptium.spells.properties.strategies;

import com.sizzlebae.projectseptium.spells.properties.PropertyStrategy;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.util.ArrayList;

public class PArray<T> extends PropertyStrategy<ArrayList<T>> {

    private final PropertyStrategy<T> elementStrategy;

    public PArray(PropertyStrategy<T> elementStrategy) {
        this.elementStrategy = elementStrategy;
    }

    @Override
    public INBT serialize(ArrayList<T> value) {
        ListNBT tag = new ListNBT();
        value.forEach(item->tag.add(elementStrategy.serialize(item)));
        return tag;
    }

    @Override
    public ArrayList<T> deserialize(INBT memento) {
        ListNBT tag = (ListNBT) memento;

        ArrayList<T> result = new ArrayList<>();
        tag.forEach(item->result.add(elementStrategy.deserialize(item)));

        return result;

    }

    @Override
    public boolean compare(PropertyStrategy<?> other) {
        if(other instanceof PArray) {
            return ((PArray<?>) other).elementStrategy.getClass() == elementStrategy.getClass();
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
