package com.sizzlebae.projectseptium.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sizzlebae.projectseptium.capabilities.AetherType;

import java.io.*;

public class AetherModelVariantsGenerator {

    static class SepithModel {
        String parent = "item/generated";

        static class Textures {
            String layer0 = "project-septium:item/sepith_layer0";
            String layer1 = "project-septium:item/sepith_layer1";
        }
        Textures textures = new Textures();
    }

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        for(AetherType type : AetherType.values()) {

            File file = new File("src/main/resources/assets/project-septium/models/item/sepith_" + type.name().toLowerCase() + ".json");


            try(PrintStream stream = new PrintStream(new FileOutputStream(file))) {

                stream.print(gson.toJson(new SepithModel()));
                System.out.println(file);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }
}
