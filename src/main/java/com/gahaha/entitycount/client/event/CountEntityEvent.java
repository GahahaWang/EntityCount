package com.gahaha.entitycount.client.event;

import com.gahaha.entitycount.client.utils.MainSwitch;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class CountEntityEvent implements ClientTickEvents.EndTick{

    public static final Map<String, Integer> defaultMap = new HashMap<>() {
        {put("Gahaha", 1);}
        {put("Is", 1);}
        {put("So", 1);}
        {put("Handsome",1);}
    };

    public static Map<String, Integer> entityCountMap = new HashMap<>();

    @Override
    public void onEndTick(MinecraftClient client) {
        if (!MainSwitch.canComputeAndRender()) return;
        updateEntityCount(client);
    }
    private void updateEntityCount(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        Iterable<Entity> entities = client.world.getEntities();
        Map<String, Integer> countMap = new HashMap<>();
        for (Entity entity : entities) {
            String className = Text.translatable(entity.getType().getTranslationKey()).getString();
            countMap.put(className, countMap.getOrDefault(className, 0) + 1);
        }
        entityCountMap = countMap;
    }
}
