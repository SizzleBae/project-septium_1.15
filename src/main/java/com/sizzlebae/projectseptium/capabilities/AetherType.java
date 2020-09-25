package com.sizzlebae.projectseptium.capabilities;

public enum AetherType {
    WATER(0,48),
    FIRE(1, 16),
    EARTH(2,104),
    WIND(3,120);

    public final byte id;
    public final byte mapColor;

    AetherType(int id, int mapColor) {
        this.id = (byte) id;
        this.mapColor = (byte) mapColor;
    }
}
