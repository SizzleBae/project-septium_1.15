package com.sizzlebae.projectseptium.capabilities;

public class AetherEntry {
    public AetherType type;
    public int value;
    public int max;

    public AetherEntry(AetherType type, int value, int max) {
        this.type = type;
        this.value = value;
        this.max = max;
    }
}
