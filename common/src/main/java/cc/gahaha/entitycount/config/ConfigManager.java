package cc.gahaha.entitycount.config;

import cc.gahaha.entitycount.utils.DisplayEntryEntityType;
import cc.gahaha.entitycount.utils.ExtendString;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dev.architectury.platform.Platform;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.util.math.Vec3d;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import static cc.gahaha.entitycount.EntityCount.LOGGER;

public class ConfigManager {
    //private static final Path CONFIG_FILE = Paths.get("config", "entitycount.json");
    private static final Path CONFIG_FILE = Platform.getConfigFolder().resolve("entitycount.json");
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final JsonObject configJson = new JsonObject();
    @Getter
    private static boolean showEntitiesCount = true;
    @Getter
    private static String entityType = "All";
    @Getter
    private static String listMode = "Blacklist";
    @Getter
    private static float scale = 10.0F;
    @Getter
    private static int textColor = 0xffffffff;
    @Getter
    private static int backgroundColor = 0x3f808080;
    @Getter
    private static float x = 0.01F;
    @Getter
    private static float y = 0.01F;
    @Getter
    private static int maxListLength = -1;
    @Getter
    private static int threshold = -1;
    @Getter
    private static List<String> whiteListNormal = List.of();
    @Getter
    private static List<String> whiteListItem = List.of();
    @Getter
    private static List<String> blackListNormal = List.of();
    @Getter
    private static List<String> blackListItem = List.of();
    @Getter
    private static List<String> pinnedListNormal = List.of();
    @Getter
    private static List<String> pinnedListItem = List.of();
    @Getter
    private static boolean expandItemDisplay = true;
    @Getter
    private static boolean expandItemDisplayPrefix = false;
    @Getter
    private static boolean pinnedShowEvenZero = false;
    @Getter
    private static int itemEntityColor = -256;
    @Getter
    private static Vec3d coord;

    // Getter methods for combined lists with ExtendString
    public static List<ExtendString> getPinnedList() {
        List<ExtendString> combined = new java.util.ArrayList<>();
        for (String item : pinnedListNormal) {
            combined.add(new ExtendString(item, DisplayEntryEntityType.NORMAL));
        }
        for (String item : pinnedListItem) {
            combined.add(new ExtendString(item, DisplayEntryEntityType.ITEM));
        }
        return combined;
    }

    public static List<String> getWhiteList() {
        List<String> combined = new java.util.ArrayList<>();
        combined.addAll(whiteListNormal);
        combined.addAll(whiteListItem);
        return combined;
    }

    public static List<String> getBlackList() {
        List<String> combined = new java.util.ArrayList<>();
        combined.addAll(blackListNormal);
        combined.addAll(blackListItem);
        return combined;
    }

    public static final class Default {
        public static final boolean showEntitiesCount = true;
        public static final String entityType = "All";
        public static final String listMode = "Blacklist";
        public static final float scale = 7.0F;
        public static final int textColor = 0xffffffff;
        public static final int backgroundColor = 0x3f808080;
        public static final float x = 0.01F;
        public static final float y = 0.01F;
        public static final int maxListLength = -1;
        public static final int threshold = -1;
        public static final List<String> whiteListNormal = List.of();
        public static final List<String> whiteListItem = List.of();
        public static final List<String> blackListNormal = List.of();
        public static final List<String> blackListItem = List.of();
        public static final List<String> pinnedListNormal = List.of();
        public static final List<String> pinnedListItem = List.of();
        public static final boolean expandItemDisplay = true;
        public static final boolean expandItemDisplayPrefix = false;
        public static final boolean pinnedShowEvenZero = false;
        public static final int itemEntityColor = 0xffffff64;
    }

    public static void setShowEntitiesCount(boolean showEntitiesCount) {
        ConfigManager.showEntitiesCount = showEntitiesCount;
        configJson.addProperty("showEntitiesCount", showEntitiesCount);
        writeJson();
    }

    public static void setEntityType(String entityType) {
        ConfigManager.entityType = entityType;
        configJson.addProperty("displayEntryEntityType", entityType);
        writeJson();
    }

    public static void setListMode(String listMode) {
        ConfigManager.listMode = listMode;
        configJson.addProperty("listMode", listMode);
        writeJson();
    }

    public static void setWhiteListNormal(List<String> whiteListNormal) {
        List<String> mutableList = new java.util.ArrayList<>(whiteListNormal);
        mutableList.removeIf(s -> s.trim().isEmpty());
        ConfigManager.whiteListNormal = mutableList;
        configJson.add("whiteListNormal", GSON.toJsonTree(mutableList));
        writeJson();
    }

    public static void setWhiteListItem(List<String> whiteListItem) {
        List<String> mutableList = new java.util.ArrayList<>(whiteListItem);
        mutableList.removeIf(s -> s.trim().isEmpty());
        ConfigManager.whiteListItem = mutableList;
        configJson.add("whiteListItem", GSON.toJsonTree(mutableList));
        writeJson();
    }

    public static void setBlackListNormal(List<String> blackListNormal) {
        List<String> mutableList = new java.util.ArrayList<>(blackListNormal);
        mutableList.removeIf(s -> s.trim().isEmpty());
        ConfigManager.blackListNormal = mutableList;
        configJson.add("blackListNormal", GSON.toJsonTree(mutableList));
        writeJson();
    }

    public static void setBlackListItem(List<String> blackListItem) {
        List<String> mutableList = new java.util.ArrayList<>(blackListItem);
        mutableList.removeIf(s -> s.trim().isEmpty());
        ConfigManager.blackListItem = mutableList;
        configJson.add("blackListItem", GSON.toJsonTree(mutableList));
        writeJson();
    }

    public static void setScale(float scale) {
        ConfigManager.scale = scale;
        setCoord();
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

    private static void setCoord() {
        coord = new Vec3d(ConfigManager.x, ConfigManager.y, ConfigManager.scale);
    }
    public static void setCoord(Vec3d coord) {
        setX((float) coord.x);
        setY((float) coord.y);
        setScale((float) coord.z);
    }

    public static void setX(float x) {
        x = Math.max(0.0F, x);
        x = Math.min(1.0F, x);
        ConfigManager.x = x;
        setCoord();
        configJson.addProperty("x", x);
        writeJson();
    }

    public static void setY(float y) {
        y = Math.max(0.0F, y);
        y = Math.min(1.0F, y);
        ConfigManager.y = y;
        setCoord();
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

    public static void setPinnedListNormal(List<String> pinnedListNormal) {
        List<String> mutableList = new java.util.ArrayList<>(pinnedListNormal);
        mutableList.removeIf(s -> s.trim().isEmpty());
        ConfigManager.pinnedListNormal = mutableList;
        configJson.add("pinnedListNormal", GSON.toJsonTree(mutableList));
        writeJson();
    }

    public static void setPinnedListItem(List<String> pinnedListItem) {
        List<String> mutableList = new java.util.ArrayList<>(pinnedListItem);
        mutableList.removeIf(s -> s.trim().isEmpty());
        ConfigManager.pinnedListItem = mutableList;
        configJson.add("pinnedListItem", GSON.toJsonTree(mutableList));
        writeJson();
    }

    public static void setExpandItemDisplay(boolean expandItemDisplay) {
        ConfigManager.expandItemDisplay = expandItemDisplay;
        configJson.addProperty("expandItemDisplay", expandItemDisplay);
        writeJson();
    }

    public static void setExpandItemDisplayPrefix(boolean expandItemDisplayPrefix) {
        ConfigManager.expandItemDisplayPrefix = expandItemDisplayPrefix;
        configJson.addProperty("expandItemDisplayPrefix", expandItemDisplayPrefix);
        writeJson();
    }

    public static void setPinnedShowEvenZero(boolean pinnedShowEvenZero) {
        ConfigManager.pinnedShowEvenZero = pinnedShowEvenZero;
        configJson.addProperty("pinnedShowEvenZero", pinnedShowEvenZero);
        writeJson();
    }

    public static void setItemEntityColor(int itemEntityColor) {
        ConfigManager.itemEntityColor = itemEntityColor;
        configJson.addProperty("itemEntityColor", itemEntityColor);
        writeJson();
    }

    public static void reset() {
        setShowEntitiesCount(Default.showEntitiesCount);
        setEntityType(Default.entityType);
        setListMode(Default.listMode);
        setWhiteListNormal(Default.whiteListNormal);
        setWhiteListItem(Default.whiteListItem);
        setBlackListNormal(Default.blackListNormal);
        setBlackListItem(Default.blackListItem);
        setPinnedListNormal(Default.pinnedListNormal);
        setPinnedListItem(Default.pinnedListItem);
        setExpandItemDisplay(Default.expandItemDisplay);
        setExpandItemDisplayPrefix(Default.expandItemDisplayPrefix);
        setPinnedShowEvenZero(Default.pinnedShowEvenZero);
        setItemEntityColor(Default.itemEntityColor);
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
        setCoord();
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
            parseConfigValue(loadedJson, j -> j.get("displayEntryEntityType").getAsString(), ConfigManager::setEntityType, Default.entityType);
            parseConfigValue(loadedJson, j -> j.get("listMode").getAsString(), ConfigManager::setListMode, Default.listMode);
            parseConfigValue(loadedJson, j -> GSON.fromJson(j.get("whiteListNormal"), new TypeToken<List<String>>(){}.getType()), ConfigManager::setWhiteListNormal, Default.whiteListNormal);
            parseConfigValue(loadedJson, j -> GSON.fromJson(j.get("whiteListItem"), new TypeToken<List<String>>(){}.getType()), ConfigManager::setWhiteListItem, Default.whiteListItem);
            parseConfigValue(loadedJson, j -> GSON.fromJson(j.get("blackListNormal"), new TypeToken<List<String>>(){}.getType()), ConfigManager::setBlackListNormal, Default.blackListNormal);
            parseConfigValue(loadedJson, j -> GSON.fromJson(j.get("blackListItem"), new TypeToken<List<String>>(){}.getType()), ConfigManager::setBlackListItem, Default.blackListItem);
            parseConfigValue(loadedJson, j -> GSON.fromJson(j.get("pinnedListNormal"), new TypeToken<List<String>>(){}.getType()), ConfigManager::setPinnedListNormal, Default.pinnedListNormal);
            parseConfigValue(loadedJson, j -> GSON.fromJson(j.get("pinnedListItem"), new TypeToken<List<String>>(){}.getType()), ConfigManager::setPinnedListItem, Default.pinnedListItem);
            parseConfigValue(loadedJson, j -> j.get("expandItemDisplay").getAsBoolean(), ConfigManager::setExpandItemDisplay, Default.expandItemDisplay);
            parseConfigValue(loadedJson, j -> j.get("expandItemDisplayPrefix").getAsBoolean(), ConfigManager::setExpandItemDisplayPrefix, Default.expandItemDisplayPrefix);
            parseConfigValue(loadedJson, j -> j.get("pinnedShowEvenZero").getAsBoolean(), ConfigManager::setPinnedShowEvenZero, Default.pinnedShowEvenZero);
            parseConfigValue(loadedJson, j -> j.get("itemEntityColor").getAsInt(), ConfigManager::setItemEntityColor, Default.itemEntityColor);
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