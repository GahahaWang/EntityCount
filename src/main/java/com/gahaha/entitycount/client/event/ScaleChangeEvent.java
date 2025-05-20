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
    private int initx = ConfigManager.x, inity = ConfigManager.y;
    private float initalScale = -1;
    private float lastGlobalScale = -1;
    @Override
    public void onEndTick(MinecraftClient minecraftClient) {
        if (!MainSwitch.canComputeAndRender()) return;
        // 假設這是「遊戲全局縮放」，實際取得方式請依遊戲版本/環境調整
        if (client.getWindow() == null) return;
        if (lastGlobalScale == -1) initalScale = lastGlobalScale = client.getWindow().getScaledHeight();

        float currentGlobalScale = client.getWindow().getScaledHeight();

        if (currentGlobalScale == initalScale) {
            ConfigManager.x = initx;
            ConfigManager.y = inity;
        }
        // 如果偵測到和先前的縮放比不同，更新 x / y 位置
        if (currentGlobalScale != lastGlobalScale) {
            float ratio = currentGlobalScale / lastGlobalScale;
            ConfigManager.x = (int)(ConfigManager.x * ratio);
            ConfigManager.y = (int)(ConfigManager.y * ratio);
            lastGlobalScale = currentGlobalScale;
        }
    }
}
