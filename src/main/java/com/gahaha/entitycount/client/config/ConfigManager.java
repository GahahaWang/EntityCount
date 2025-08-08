package com.gahaha.entitycount.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ConfigManager {
    private static final Path CONFIG_FILE = Paths.get("config", "entitycount.json");
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final JsonObject configJson = new JsonObject();
    private static boolean showEntitiesCount = true;
    private static String mode = "All";
    private static float scale = 10.0F;
    private static int textColor = -1;
    private static int backgroundColor = 1065386112;
    private static float x = 0.01F;
    private static float y = 0.01F;
    private static int maxListLength = -1;
    private static int threshold = -1;

    public static final class Default {
        public static final boolean showEntitiesCount = true;
        public static final String mode = "All";
        public static final float scale = 10.0F;
        public static final int textColor = -1;
        public static final int backgroundColor = 1065386112;
        public static final float x = 0.01F;
        public static final float y = 0.01F;
        public static final int maxListLength = -1;
        public static final int threshold = -1;
    }

    public static boolean getShowEntitiesCount() {
        return showEntitiesCount;
    }

    public static void setShowEntitiesCount(boolean showEntitiesCount) {
        ConfigManager.showEntitiesCount = showEntitiesCount;
        configJson.addProperty("showEntitiesCount", showEntitiesCount);
        writeJson();
    }

    public static String getMode() {
        return mode;
    }

    public static void setMode(String mode) {
        ConfigManager.mode = mode;
        configJson.addProperty("mode", mode);
        writeJson();
    }

    public static float getScale() {
        return scale;
    }

    public static void setScale(float scale) {
        ConfigManager.scale = scale;
        configJson.addProperty("scale", scale);
        writeJson();
    }

    public static int getTextColor() {
        return textColor;
    }

    public static void setTextColor(int textColor) {
        ConfigManager.textColor = textColor;
        configJson.addProperty("textColor", textColor);
        writeJson();
    }

    public static int getBackgroundColor() {
        return backgroundColor;
    }

    public static void setBackgroundColor(int backgroundColor) {
        ConfigManager.backgroundColor = backgroundColor;
        configJson.addProperty("backgroundColor", backgroundColor);
        writeJson();
    }

    public static float getX() {
        return x;
    }

    public static void setX(float x) {
        x = Math.max(0.0F, x);
        x = Math.min(1.0F, x);
        ConfigManager.x = x;
        configJson.addProperty("x", x);
        writeJson();
    }

    public static float getY() {
        return y;
    }

    public static void setY(float y) {
        y = Math.max(0.0F, y);
        y = Math.min(1.0F, y);
        ConfigManager.y = y;
        configJson.addProperty("y", y);
        writeJson();
    }

    public static int getMaxListLength() {
        return maxListLength;
    }

    public static void setMaxListLength(int maxListLength) {
        ConfigManager.maxListLength = maxListLength;
        configJson.addProperty("maxListLength", maxListLength);
        writeJson();
    }

    public static int getThreshold() {
        return threshold;
    }

    public static void setThreshold(int threshold) {
        ConfigManager.threshold = threshold;
        configJson.addProperty("threshold", threshold);
        writeJson();
    }

    public static void reset() {
        setShowEntitiesCount(true);
        setMode("All");
        setScale(10.0F);
        setTextColor(-1);
        setBackgroundColor(1065386112);
        setX(0.01F);
        setY(0.01F);
        setMaxListLength(-1);
        setThreshold(-1);
    }

    public static void init() {
        if (Files.exists(CONFIG_FILE, new LinkOption[0])) {
            load();
        } else {
            reset();
            writeJson();
        }

    }

    public static void load() {
        try {
            if (Files.exists(CONFIG_FILE, new LinkOption[0])) {
                JsonObject loadedJson = (JsonObject)GSON.fromJson(Files.newBufferedReader(CONFIG_FILE, StandardCharsets.UTF_8), JsonObject.class);
                if (loadedJson.has("showEntitiesCount")) {
                    setShowEntitiesCount(loadedJson.get("showEntitiesCount").getAsBoolean());
                }

                if (loadedJson.has("mode")) {
                    setMode(loadedJson.get("mode").getAsString());
                }

                if (loadedJson.has("scale")) {
                    setScale(loadedJson.get("scale").getAsFloat());
                }

                if (loadedJson.has("textColor")) {
                    setTextColor(loadedJson.get("textColor").getAsInt());
                }

                if (loadedJson.has("backgroundColor")) {
                    setBackgroundColor(loadedJson.get("backgroundColor").getAsInt());
                }

                if (loadedJson.has("x")) {
                    setX(loadedJson.get("x").getAsFloat());
                }

                if (loadedJson.has("y")) {
                    setY(loadedJson.get("y").getAsFloat());
                }

                if (loadedJson.has("maxListLength")) {
                    setMaxListLength(loadedJson.get("maxListLength").getAsInt());
                }

                if (loadedJson.has("threshold")) {
                    setThreshold(loadedJson.get("threshold").getAsInt());
                }
            }
        } catch (IOException var1) {
            IOException e = var1;
            e.printStackTrace();
        }

    }

    private static void writeJson() {
        try {
            FileWriter writer = new FileWriter(CONFIG_FILE.toFile(), StandardCharsets.UTF_8);

            try {
                GSON.toJson(configJson, writer);
            } catch (Throwable var4) {
                try {
                    writer.close();
                } catch (Throwable var3) {
                    var4.addSuppressed(var3);
                }

                throw var4;
            }

            writer.close();
        } catch (IOException var5) {
            IOException e = var5;
            e.printStackTrace();
        }

    }
}