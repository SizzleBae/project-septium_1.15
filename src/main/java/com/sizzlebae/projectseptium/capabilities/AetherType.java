package com.sizzlebae.projectseptium.capabilities;

import java.awt.*;

public enum AetherType {
    WATER(0,48, new Color(26, 46, 94)),
    FIRE(1, 16, new Color(97, 29, 29)),
    EARTH(2,104, new Color(69, 54, 1)),
    WIND(3,120, new Color(87, 93, 59));

    public final byte id;
    public final byte mapColor;
    public final Color tint;

    AetherType(int id, int mapColor, Color tint) {
        this.id = (byte) id;
        this.mapColor = (byte) mapColor;
        this.tint = tint;
    }
}
