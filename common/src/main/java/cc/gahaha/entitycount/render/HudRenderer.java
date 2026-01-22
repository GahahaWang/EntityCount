package cc.gahaha.entitycount.render;

import cc.gahaha.entitycount.event.CountEntityEvent;

import cc.gahaha.entitycount.config.ConfigManager;
import cc.gahaha.entitycount.utils.DisplayEntryEntityType;
import cc.gahaha.entitycount.utils.EntityListFilter;
import cc.gahaha.entitycount.utils.ExtendString;
import cc.gahaha.entitycount.utils.MainSwitch;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

@Environment(EnvType.CLIENT)
public class HudRenderer {
    public static final Identifier ENTITY_COUNT_HUD_ID = Identifier.of("entitycount", "entitycount-hud");

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!MainSwitch.canComputeAndRender()) return;
        renderEntityCountHUD(context, EntityListFilter.getProcessedList(CountEntityEvent.entityCountMap), ConfigManager.getCoord());
    }
    public static void renderEntityCountHUD(DrawContext context, List<Object2IntOpenHashMap.Entry<ExtendString>> entryList, Vec3d coord) {
        float scale = (float) (coord.z/10);
        int tmpx = (int)Math.floor(coord.x*MinecraftClient.getInstance().getWindow().getScaledWidth() / scale);
        int tmpy = (int)Math.floor(coord.y*MinecraftClient.getInstance().getWindow().getScaledHeight() / scale);

        // 取得 MatrixStack 並進行縮放
        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale, scale);

        context.fill(tmpx, tmpy, tmpx + 80, tmpy + (11*entryList.size()), ConfigManager.getBackgroundColor());

        for (Object2IntOpenHashMap.Entry<ExtendString> entry : entryList) {
            String displayText = entry.getKey().value() + ": " + entry.getIntValue();
            
            // 根據實體類型選擇顏色
            int textColor = entry.getKey().displayEntryEntityType() == DisplayEntryEntityType.ITEM 
                ? ConfigManager.getItemEntityColor() 
                : ConfigManager.getTextColor();

            context.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    Text.literal(displayText),
                    tmpx + 3,
                    tmpy + 3,
                    textColor,
                    true
            );
            tmpy += (int)(11); // 每列向下移動一點
        }
        context.getMatrices().popMatrix();
    }
}