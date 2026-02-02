package cc.gahaha.entitycount.utils;

import cc.gahaha.entitycount.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.debug.DebugHudProfile;

public class MainSwitch {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    /**
     * Reference from minihud
     * https://github.com/sakura-ryoko/minihud/blob/LTS/1.21.10/src/main/java/fi/dy/masa/minihud/data/DebugDataManager.java#L157
     */
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