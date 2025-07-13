package com.gahaha.entitycount.client;

import com.gahaha.entitycount.client.config.ConfigManager;
import com.gahaha.entitycount.client.event.CountEntityEvent;
import com.gahaha.entitycount.client.render.HudRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

public class EntityCountClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(new CountEntityEvent());
        HudElementRegistry.attachElementAfter(VanillaHudElements.SCOREBOARD, HudRenderer.ENTITY_COUNT_HUD_ID, new HudRenderer());
        //ClientTickEvents.END_CLIENT_TICK.register(new ScaleChangeEvent());
        ConfigManager.init();
    }
}
