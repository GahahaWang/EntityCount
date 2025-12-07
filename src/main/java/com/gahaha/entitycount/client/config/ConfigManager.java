package com.gahaha.entitycount.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import static com.gahaha.entitycount.client.EntityCountClient.LOGGER;

@Environment(EnvType.CLIENT)
public class ConfigManager {
    private static final Path CONFIG_FILE = Paths.get("config", "entitycount.json");
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final JsonObject configJson = new JsonObject();
    @Getter
    private static boolean showEntitiesCount = true;
    @Getter
    private static String entityType = "All";
    @Getter
    private static String listMode = "Blacklist";
    @Getter
    private static float scale = 7.5F;
    @Getter
    private static int textColor = -1;
    @Getter
    private static int backgroundColor = 1065386112;
    @Getter
    private static float x = 0.01F;
    @Getter
    private static float y = 0.01F;
    @Getter
    private static int maxListLength = -1;
    @Getter
    private static int threshold = -1;
    @Getter
    private static List<String> whiteList = List.of();
    @Getter
    private static List<String> blackList = List.of();
    @Getter
    private static List<String> pinnedList = List.of();
    @Getter
    private static boolean expandItemDisplay = true;

    public static final class Default {
        public static final boolean showEntitiesCount = true;
        public static final String entityType = "All";
        public static final String listMode = "Blacklist";
        public static final float scale = 10.0F;
        public static final int textColor = -1;
        public static final int backgroundColor = 1065386112;
        public static final float x = 0.01F;
        public static final float y = 0.01F;
        public static final int maxListLength = -1;
        public static final int threshold = -1;
        public static final List<String> whiteList = List.of();
        public static final List<String> blackList = List.of();
        public static final List<String> pinnedList = List.of();
        public static final boolean expandItemDisplay = true;
    }

    public static void setShowEntitiesCount(boolean showEntitiesCount) {
        ConfigManager.showEntitiesCount = showEntitiesCount;
        configJson.addProperty("showEntitiesCount", showEntitiesCount);
        writeJson();
    }

    public static void setEntityType(String entityType) {
        ConfigManager.entityType = entityType;
        configJson.addProperty("entityType", entityType);
        writeJson();
    }

    public static void setListMode(String listMode) {
        ConfigManager.listMode = listMode;
        configJson.addProperty("listMode", listMode);
        writeJson();
    }

    public static void setWhiteList(List<String> whiteList) {
        ConfigManager.whiteList = whiteList;
        configJson.add("whiteList", GSON.toJsonTree(whiteList));
        writeJson();
    }

    public static void setBlackList(List<String> blackList) {
        ConfigManager.blackList = blackList;
        configJson.add("blackList", GSON.toJsonTree(blackList));
        writeJson();
    }

    public static void setScale(float scale) {
        ConfigManager.scale = scale;
        configJson.addProperty("scale", scale);
        writeJson();
    }

    public static void setTextColor(int textColor) {
        ConfigManager.textColor = textColor;
        configJson.addProperty("textColor", textColor);
        writeJson();
    }

    public static void setBackgroundColor(int backgroundColor) {
        ConfigManager.backgroundColor = backgroundColor;
        configJson.addProperty("backgroundColor", backgroundColor);
        writeJson();
    }

    public static void setX(float x) {
        x = Math.max(0.0F, x);
        x = Math.min(1.0F, x);
        ConfigManager.x = x;
        configJson.addProperty("x", x);
        writeJson();
    }

    public static void setY(float y) {
        y = Math.max(0.0F, y);
        y = Math.min(1.0F, y);
        ConfigManager.y = y;
        configJson.addProperty("y", y);
        writeJson();
    }

    public static void setMaxListLength(int maxListLength) {
        ConfigManager.maxListLength = maxListLength;
        configJson.addProperty("maxListLength", maxListLength);
        writeJson();
    }

    public static void setThreshold(int threshold) {
        ConfigManager.threshold = threshold;
        configJson.addProperty("threshold", threshold);
        writeJson();
    }

    public static void setPinnedList(List<String> pinnedList) {
        ConfigManager.pinnedList = pinnedList;
        configJson.add("pinnedList", GSON.toJsonTree(pinnedList));
        writeJson();
    }

    public static void setExpandItemDisplay(boolean expandItemDisplay) {
        ConfigManager.expandItemDisplay = expandItemDisplay;
        configJson.addProperty("expandItemDisplay", expandItemDisplay);
        writeJson();
    }

    public static void reset() {
        setShowEntitiesCount(Default.showEntitiesCount);
        setEntityType(Default.entityType);
        setListMode(Default.listMode);
        setWhiteList(Default.whiteList);
        setBlackList(Default.blackList);
        setPinnedList(Default.pinnedList);
        setExpandItemDisplay(Default.expandItemDisplay);
        setScale(Default.scale);
        setTextColor(Default.textColor);
        setBackgroundColor(Default.backgroundColor);
        setX(Default.x);
        setY(Default.y);
        setMaxListLength(Default.maxListLength);
        setThreshold(Default.threshold);
    }

    public static void init() {
        if (Files.exists(CONFIG_FILE)) {
            load();
        } else {
            reset();
            writeJson();
        }
    }

    @FunctionalInterface
    private interface ConfigParser<T> {
        T parse(JsonObject json) throws Exception;
    }
    private static <T> void parseConfigValue(JsonObject json, ConfigParser<T> parser, Consumer<T> setter, T defaultValue) {
        try {
            @NonNull var value = parser.parse(json);
            LOGGER.info(value.toString());
            setter.accept(value);
        } catch (Exception e) {
            setter.accept(defaultValue);
            LOGGER.error("Failed to parse config value, using default", e);
        }
    }

    public static void load() {
        try {
            JsonObject loadedJson = (JsonObject)GSON.fromJson(Files.newBufferedReader(CONFIG_FILE, StandardCharsets.UTF_8), JsonObject.class);
            if(loadedJson == null) {
                reset();
                return;
            }
            parseConfigValue(loadedJson, j -> j.get("showEntitiesCount").getAsBoolean(), ConfigManager::setShowEntitiesCount, Default.showEntitiesCount);
            parseConfigValue(loadedJson, j -> j.get("entityType").getAsString(), ConfigManager::setEntityType, Default.entityType);
            parseConfigValue(loadedJson, j -> j.get("listMode").getAsString(), ConfigManager::setListMode, Default.listMode);
            parseConfigValue(loadedJson, j -> GSON.fromJson(j.get("whiteList"), new TypeToken<List<String>>(){}.getType()), ConfigManager::setWhiteList, Default.whiteList);
            parseConfigValue(loadedJson, j -> GSON.fromJson(j.get("blackList"), new TypeToken<List<String>>(){}.getType()), ConfigManager::setBlackList, Default.blackList);
            parseConfigValue(loadedJson, j -> GSON.fromJson(j.get("pinnedList"), new TypeToken<List<String>>(){}.getType()), ConfigManager::setPinnedList, Default.pinnedList);
            parseConfigValue(loadedJson, j -> j.get("expandItemDisplay").getAsBoolean(), ConfigManager::setExpandItemDisplay, Default.expandItemDisplay);
            parseConfigValue(loadedJson, j -> j.get("scale").getAsFloat(), ConfigManager::setScale, Default.scale);
            parseConfigValue(loadedJson, j -> j.get("textColor").getAsInt(), ConfigManager::setTextColor, Default.textColor);
            parseConfigValue(loadedJson, j -> j.get("backgroundColor").getAsInt(), ConfigManager::setBackgroundColor, Default.backgroundColor);
            parseConfigValue(loadedJson, j -> j.get("x").getAsFloat(), ConfigManager::setX, Default.x);
            parseConfigValue(loadedJson, j -> j.get("y").getAsFloat(), ConfigManager::setY, Default.y);
            parseConfigValue(loadedJson, j -> j.get("maxListLength").getAsInt(), ConfigManager::setMaxListLength, Default.maxListLength);
            parseConfigValue(loadedJson, j -> j.get("threshold").getAsInt(), ConfigManager::setThreshold, Default.threshold);
        } catch (Exception e) {
            LOGGER.error("Failed to load config file", e);
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
        } catch (IOException e) {
            LOGGER.error("Failed to write config file", e);
        }

    }
}