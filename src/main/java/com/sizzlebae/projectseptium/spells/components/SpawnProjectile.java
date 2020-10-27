package com.sizzlebae.projectseptium.spells.components;

import com.sizzlebae.projectseptium.spells.properties.Property;
import com.sizzlebae.projectseptium.spells.properties.PropertyStrategy;
import com.sizzlebae.projectseptium.spells.properties.strategies.PArray;
import com.sizzlebae.projectseptium.spells.properties.strategies.PFloat;

import java.util.ArrayList;
import java.util.HashMap;

public class SpawnProjectile extends SpellComponent {
    public final Property<Float> speed = addProperty("speed", new Property<>(new PFloat(), 0.0f));

    public final Property<ArrayList<Float>> test = addProperty("test", new Property<>(new PArray<>(new PFloat()), new ArrayList<>()));

    @Override
    public HashMap<String, PropertyStrategy<?>> getSocketRequirements() {
        HashMap<String, PropertyStrategy<?>> requirements = new HashMap<>();
        requirements.put("position", new PArray<>(new PFloat()));
        return requirements;
    }
}
