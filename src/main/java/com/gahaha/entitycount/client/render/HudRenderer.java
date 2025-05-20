package com.gahaha.entitycount.client.render;

import static com.gahaha.entitycount.client.event.CountEntityEvent.entityCountMap;

import com.gahaha.entitycount.client.config.ConfigManager;
import com.gahaha.entitycount.client.utils.EntityListFilter;
import com.gahaha.entitycount.client.utils.MainSwitch;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class HudRenderer implements HudLayerRegistrationCallback {
    private static final Identifier entityCountLayer = Identifier.of("entitycount", "entitycount-layer");

    @Override
    public void register(LayeredDrawerWrapper layeredDrawerWrapper) {
        layeredDrawerWrapper.attachLayerAfter(IdentifiedLayer.SCOREBOARD, entityCountLayer, HudRenderer::render);

    }
    public static void render (DrawContext context, @Nullable RenderTickCounter tickCounter) {
        if (!MainSwitch.canComputeAndRender()) return;
        renderDefault(context, EntityListFilter.getProcessedList(entityCountMap));
    }
    public static void renderDefault(DrawContext context, List<Map.Entry<String, Integer>> entryList) {
        int tmpx = (int)(ConfigManager.x / ConfigManager.scale);
        int tmpy = (int)(ConfigManager.y / ConfigManager.scale);


        // 取得 MatrixStack 並進行縮放
        context.getMatrices().push();
        context.getMatrices().scale(ConfigManager.scale, ConfigManager.scale, 1.0F);


        context.fill(tmpx- 3, tmpy - 3 ,tmpx + 70, tmpy + (11*entryList.size()), ConfigManager.backgroundColor);

        for (Map.Entry<String, Integer> entry : entryList) {
            String displayText = entry.getKey() + ": " + entry.getValue();
            context.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    Text.literal(displayText),
                    tmpx,
                    tmpy,
                    ConfigManager.textColor,
                    false
            );
            tmpy += (int)(11); // 每列向下移動一點
        }
        context.getMatrices().pop();
    }
}