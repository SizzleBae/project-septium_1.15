package com.sizzlebae.projectseptium.spells.components;

import com.sizzlebae.projectseptium.spells.properties.Property;
import com.sizzlebae.projectseptium.spells.properties.PropertyStrategy;
import com.sizzlebae.projectseptium.spells.properties.strategies.PArray;
import com.sizzlebae.projectseptium.spells.properties.strategies.PFloat;
import com.sizzlebae.projectseptium.spells.sockets.SpellSocket;

import java.util.ArrayList;
import java.util.HashMap;

public class Origin extends SpellComponent {

    public final Property<Float> x = addProperty("x", new Property<>(new PFloat(), 0f));
    public final Property<Float> y = addProperty("y", new Property<>(new PFloat(), 0f));
    public final Property<Float> z = addProperty("z", new Property<>(new PFloat(), 0f));

    public final SpellSocket result = addChildSocket("result", new SpellSocket());

    public Origin() {
        result.parameters.put("position", new Property<>(new PArray<>(new PFloat()), new ArrayList<>()));
    }

    @Override
    public HashMap<String, PropertyStrategy<?>> getSocketRequirements() {
        return new HashMap<>();
    }
}
