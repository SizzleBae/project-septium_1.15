package com.sizzlebae.projectseptium.capabilities;

public enum AetherType {
    WATER(0,48),
    FIRE(1, 16),
    EARTH(2,104),
    WIND(3,120);

    public final byte id;
    public final byte colorStart;

    AetherType(int id, int colorStart) {
        this.id = (byte) id;
        this.colorStart = (byte) colorStart;
    }
}
