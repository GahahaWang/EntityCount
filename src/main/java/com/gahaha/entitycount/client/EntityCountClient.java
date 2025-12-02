package com.gahaha.entitycount.client;

import com.gahaha.entitycount.client.config.ConfigManager;
import com.gahaha.entitycount.client.event.CountEntityEvent;
import com.gahaha.entitycount.client.render.HudRenderer;
import com.gahaha.entitycount.client.screen.EntityCountConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityCountClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("EntityCount");
    
    private static KeyBinding openConfigScreenKey;
    
    @Override
    public void onInitializeClient() {
        KeyBinding.Category category = new KeyBinding.Category(Identifier.of("entitycount"));
        ClientTickEvents.END_CLIENT_TICK.register(new CountEntityEvent());
        HudElementRegistry.attachElementAfter(VanillaHudElements.SCOREBOARD, HudRenderer.ENTITY_COUNT_HUD_ID, new HudRenderer());
        ConfigManager.init();

        if (!FabricLoader.getInstance().isModLoaded("modmenu")) {
            // 註冊鍵綁定
            openConfigScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.entitycount.openconfig",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_O,
                    category
            ));

            // 處理鍵綁定事件
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                while (openConfigScreenKey.wasPressed()) {
                    client.setScreen(new EntityCountConfigScreen(client.currentScreen));
                }
            });
        }

    }
}
