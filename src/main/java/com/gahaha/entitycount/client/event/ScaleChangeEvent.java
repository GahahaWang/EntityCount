package com.gahaha.entitycount.client.event;

import com.gahaha.entitycount.client.config.ConfigManager;
import com.gahaha.entitycount.client.utils.MainSwitch;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class ScaleChangeEvent implements ClientTickEvents.EndTick{
    MinecraftClient client = MinecraftClient.getInstance();
    private final float initialX = ConfigManager.getX(), initialY = ConfigManager.getY();
    private float initialScale = -1;
    private float lastGlobalScale = -1;
    @Override
    public void onEndTick(MinecraftClient minecraftClient) {
        if (!MainSwitch.canComputeAndRender()) return;
        // 假設這是「遊戲全局縮放」，實際取得方式請依遊戲版本/環境調整
        if (client.getWindow() == null) return;
        if (lastGlobalScale == -1) initialScale = lastGlobalScale = client.getWindow().getScaledHeight();

        float currentGlobalScale = client.getWindow().getScaledHeight();

        if (currentGlobalScale == initialScale) {
            ConfigManager.setX(initialX);
            ConfigManager.setY(initialY);
        }
        // 如果偵測到和先前的縮放比不同，更新 x / y 位置
        if (currentGlobalScale != lastGlobalScale) {
            float ratio = currentGlobalScale / lastGlobalScale;
            ConfigManager.setX((int)(ConfigManager.getX() * ratio));
            ConfigManager.setY((int)(ConfigManager.getY() * ratio));
            lastGlobalScale = currentGlobalScale;
        }
    }
}
