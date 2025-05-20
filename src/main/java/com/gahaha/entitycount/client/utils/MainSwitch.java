package com.gahaha.entitycount.client.utils;

import com.gahaha.entitycount.client.config.ConfigManager;
import net.minecraft.client.MinecraftClient;

public class MainSwitch {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    public static boolean canComputeAndRender() {
        if (client.player == null || client.world == null) return false;
        if (client.getDebugHud().shouldShowDebugHud()) return false;
        if (client.isPaused()) return false;
        return ConfigManager.showEntitiesCount;
    }
}