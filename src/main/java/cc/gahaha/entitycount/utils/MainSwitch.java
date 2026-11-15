package cc.gahaha.entitycount.utils;

import cc.gahaha.entitycount.compat.Compat;
import cc.gahaha.entitycount.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntryList;

public class MainSwitch {
    private static final Minecraft client = Minecraft.getInstance();

    public MainSwitch() {
    }

    public static boolean shouldShowDebugHudFix() {
        DebugScreenEntryList profile = client.debugEntries;
        return profile.isOverlayVisible() && (!Compat.isHudHidden(client) || Compat.screen(client) != null);
    }

    public static boolean canComputeAndRender() {
        if (client.player != null && client.level != null) {
            if (shouldShowDebugHudFix()) {
                return false;
            } else {
                return !client.isPaused() && ConfigManager.isShowEntitiesCount();
            }
        } else {
            return false;
        }
    }
}