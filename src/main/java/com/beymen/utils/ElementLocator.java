package com.beymen.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.openqa.selenium.By;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

public class ElementLocator {

    private static final String ELEMENTS_DIR = "src/test/resources/elements/";
    private static final Map<String, By> elementMap = new HashMap<>();

    static {
        loadAllLocators();
    }

    private static void loadAllLocators() {
        File folder = new File(ELEMENTS_DIR);
        File[] jsonFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        if (jsonFiles == null || jsonFiles.length == 0) {
            throw new RuntimeException("No JSON files found in: " + ELEMENTS_DIR);
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<JsonObject>>() {}.getType();

        for (File jsonFile : jsonFiles) {
            try (FileReader reader = new FileReader(jsonFile)) {
                List<JsonObject> elements = gson.fromJson(reader, listType);

                for (JsonObject obj : elements) {
                    String key = obj.get("key").getAsString();
                    String value = obj.get("value").getAsString();
                    String type = obj.get("type").getAsString();

                    By locator = getLocator(type, value);
                    elementMap.put(key, locator);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to read file: " + jsonFile.getName() + " - " + e.getMessage());
            }
        }
    }

    // Locator'ı türüne göre döndüren yardımcı metod
    private static By getLocator(String type, String value) {
        switch (type.toLowerCase()) {
            case "id":
                return By.id(value);
            case "xpath":
                return By.xpath(value);
            case "css":
                return By.cssSelector(value);
            case "name":
                return By.name(value);
            default:
                throw new IllegalArgumentException("Unsupported locator type: " + type);
        }
    }

    public static By get(String key) {
        if (!elementMap.containsKey(key)) {
            throw new NoSuchElementException("Locator not found for key: " + key);
        }
        return elementMap.get(key);
    }
}