package com.gahaha.entitycount.client.utils;

import com.gahaha.entitycount.client.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.debug.DebugHudProfile;

public class MainSwitch {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static boolean shouldShowDebugHudFix() {
        DebugHudProfile profile = client.debugHudEntryList;
        return profile.isF3Enabled() && (!client.options.hudHidden || client.currentScreen != null);
    }

    public static boolean canComputeAndRender() {
        if (client.player == null || client.world == null) return false;
        if (shouldShowDebugHudFix()) return false;
        if (client.isPaused()) return false;
        return ConfigManager.isShowEntitiesCount();
    }
}