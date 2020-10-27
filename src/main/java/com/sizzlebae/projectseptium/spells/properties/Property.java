package com.sizzlebae.projectseptium.spells.properties;

import net.minecraft.nbt.INBT;

public class Property<T> {
//    public enum Strategy {
//        FLOAT(new FloatStrategy()),
//        COMPONENT(new ComponentStrategy());
//
//        PropertyStrategy<?> instance;
//        Strategy(PropertyStrategy<?> instance) {
//            this.instance = instance;
//        }
//
//    }

    protected final PropertyStrategy<T> strategy;
    protected T value;

    public Property(PropertyStrategy<T> strategy, T defaultValue) {
        this.strategy = strategy;
        this.value = defaultValue;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public INBT serialize() {
        return strategy.serialize(value);
    }

    public void deserialize(INBT tag) {
        value = strategy.deserialize(tag);
    }

    public boolean compare(PropertyStrategy<?> other) {
        return strategy.compare(other);
    }

}
