package cc.gahaha.entitycount.multiversion;

import cc.gahaha.fastmultiversion.Mc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.Screen;

/**
 * The client API that differs between the supported Minecraft versions. 26.2 moved the current screen and
 * the HUD into {@code Minecraft.gui}; each method picks the right one for the version being built.
 */
public final class Multiversion {
    private Multiversion() {
    }

    public static Screen screen(Minecraft mc) {
        return Mc.is("[26.2,)") ? mc.gui.screen() : mc.screen;
    }

    public static void setScreen(Minecraft mc, Screen screen) {
        if (Mc.is("[26.2,)")) {
            mc.gui.setScreen(screen);
        } else {
            mc.setScreen(screen);
        }
    }

    public static ChatComponent chat(Minecraft mc) {
        return Mc.is("[26.2,)") ? mc.gui.hud.getChat() : mc.gui.getChat();
    }

    public static boolean isHudHidden(Minecraft mc) {
        return Mc.is("[26.2,)") ? mc.gui.hud.isHidden() : mc.options.hideGui;
    }
}
