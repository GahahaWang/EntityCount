package cc.gahaha.entitycount.render;

import cc.gahaha.entitycount.config.ConfigManager;
import cc.gahaha.entitycount.event.CountEntityEvent;
import cc.gahaha.entitycount.utils.DisplayEntryEntityType;
import cc.gahaha.entitycount.utils.EntityListFilter;
import cc.gahaha.entitycount.utils.ExtendString;
import cc.gahaha.entitycount.utils.MainSwitch;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import static cc.gahaha.entitycount.EntityCount.MOD_ID;

public class HudRenderer {
    public static final Identifier ENTITY_COUNT_HUD_ID = Identifier.fromNamespaceAndPath(MOD_ID, "entitycount-hud");

    public HudRenderer() {
    }

    public static void render(GuiGraphicsExtractor context, DeltaTracker tickCounter) {
        if (MainSwitch.canComputeAndRender()) {
            renderEntityCountHUD(context, EntityListFilter.getProcessedList(CountEntityEvent.entityCountMap), ConfigManager.getCoord());
        }
    }

    public static void renderEntityCountHUD(GuiGraphicsExtractor context, List<Object2IntMap.Entry<ExtendString>> entryList, Vec3 coord) {
        float scale = (float)(coord.z / (double)10.0F);
        int tmpx = (int)Math.floor(coord.x * (double)Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double)scale);
        int tmpy = (int)Math.floor(coord.y * (double)Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double)scale);
        context.pose().pushMatrix();
        context.pose().scale(scale, scale);
        context.fill(tmpx, tmpy, tmpx + 80, tmpy + 11 * entryList.size(), ConfigManager.getBackgroundColor());

        for(Object2IntMap.Entry<ExtendString> entry : entryList) {
            String var10000 = ((ExtendString)entry.getKey()).value();
            String displayText = var10000 + ": " + entry.getIntValue();
            int textColor = ((ExtendString)entry.getKey()).displayEntryEntityType() == DisplayEntryEntityType.ITEM ? ConfigManager.getItemEntityColor() : ConfigManager.getTextColor();
            context.text(Minecraft.getInstance().font, Component.literal(displayText), tmpx + 3, tmpy + 3, textColor, true);
            tmpy += 11;
        }

        context.pose().popMatrix();
    }
}
