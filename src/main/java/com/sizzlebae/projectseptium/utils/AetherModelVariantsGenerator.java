package com.sizzlebae.projectseptium.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sizzlebae.projectseptium.capabilities.AetherType;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class AetherModelVariantsGenerator {

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String dir = "src/main/resources/assets/project-septium";

        for(AetherType type : AetherType.values()) {
            String aetherName = type.name().toLowerCase();

            JsonObject sepith_item = new JsonObject();
            sepith_item.addProperty("parent", "item/generated");
            JsonObject sepith_item_texture = new JsonObject();
            sepith_item_texture.addProperty("layer0", "project-septium:item/sepith_layer0");
            sepith_item_texture.addProperty("layer1", "project-septium:item/sepith_layer1");
            sepith_item.add("textures", sepith_item_texture);
            createJsonFile(gson, sepith_item,dir + "/models/item/sepith_" + aetherName + ".json");

            JsonObject septium_crystal = new JsonObject();
            JsonObject septium_crystal_variants = new JsonObject();
            JsonObject septium_crystal_default_variant = new JsonObject();
            septium_crystal_default_variant.addProperty("model", "project-septium:block/septium_crystal");
            septium_crystal_variants.add("", septium_crystal_default_variant);
            septium_crystal.add("variants", septium_crystal_variants);
            createJsonFile(gson, septium_crystal, dir + "/blockstates/septium_crystal_" + aetherName + ".json");

            JsonObject septium_crystal_item = new JsonObject();
            septium_crystal_item.addProperty("parent", "project-septium:block/septium_crystal");
            createJsonFile(gson, septium_crystal_item, dir + "/models/item/septium_crystal_" + aetherName + ".json");

        }
    }

    private static void createJsonFile(Gson gson,JsonObject obj, String path) {
        try(PrintStream stream = new PrintStream(new FileOutputStream(path))) {

            stream.print(gson.toJson(obj));
            System.out.println(path);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
