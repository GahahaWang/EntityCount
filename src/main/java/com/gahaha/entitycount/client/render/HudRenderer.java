package com.gahaha.entitycount.client.render;

import static com.gahaha.entitycount.client.event.CountEntityEvent.entityCountMap;

import com.gahaha.entitycount.client.config.ConfigManager;
import com.gahaha.entitycount.client.utils.EntityListFilter;
import com.gahaha.entitycount.client.utils.MainSwitch;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class HudRenderer implements HudElement {
    public static final Identifier ENTITY_COUNT_HUD_ID = Identifier.of("entitycount", "entitycount-hud");

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!MainSwitch.canComputeAndRender()) return;
        renderDefault(context, EntityListFilter.getProcessedList(entityCountMap));
    }
    public static void renderDefault(DrawContext context, List<Map.Entry<String, Integer>> entryList) {
        float scale = ConfigManager.getScale()/10;
        int tmpx = (int)(ConfigManager.getX()*MinecraftClient.getInstance().getWindow().getScaledWidth() / scale);
        int tmpy = (int)(ConfigManager.getY()*MinecraftClient.getInstance().getWindow().getScaledHeight() / scale);


        // 取得 MatrixStack 並進行縮放
        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale, scale);

        context.fill(tmpx, tmpy ,tmpx + 70, tmpy + (11*entryList.size()), ConfigManager.getBackgroundColor());

        for (Map.Entry<String, Integer> entry : entryList) {
            String displayText = entry.getKey() + ": " + entry.getValue();
            context.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    Text.literal(displayText),
                    tmpx + 3,
                    tmpy + 3,
                    ConfigManager.getTextColor(),
                    false
            );
            tmpy += (int)(11); // 每列向下移動一點
        }

        context.getMatrices().popMatrix();
    }
}