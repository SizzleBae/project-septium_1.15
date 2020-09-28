package com.sizzlebae.projectseptium.capabilities;

public class AetherEntry {
    public AetherType type;
    public int value;
    public int basis;

    public AetherEntry(AetherType type, int value, int basis) {
        this.type = type;
        this.value = value;
        this.basis = basis;
    }

    public int basisOffset() {
        return value - basis;
    }
}
