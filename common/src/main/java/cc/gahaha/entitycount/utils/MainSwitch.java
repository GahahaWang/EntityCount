package cc.gahaha.entitycount.utils;

import cc.gahaha.entitycount.config.ConfigManager;
import net.minecraft.client.MinecraftClient;

public class MainSwitch {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static boolean canComputeAndRender() {
        if (client.player == null || client.world == null) return false;
        if (client.getDebugHud().shouldShowDebugHud()) return false;
        if (client.isPaused()) return false;
        return ConfigManager.isShowEntitiesCount();
    }
}