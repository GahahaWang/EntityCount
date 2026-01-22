package cc.gahaha.entitycount.fabric.client;

import cc.gahaha.entitycount.EntityCount;
import cc.gahaha.entitycount.config.ConfigManager;
import cc.gahaha.entitycount.render.HudRenderer;
import cc.gahaha.entitycount.utils.EntityListFilter;
import cc.gahaha.entitycount.utils.MainSwitch;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.util.Identifier;

import static cc.gahaha.entitycount.event.CountEntityEvent.entityCountMap;

public final class EntityCountFabricClient extends EntityCount implements ClientModInitializer {

    public static final Identifier ENTITY_COUNT_HUD_ID = Identifier.of("entitycount", "entitycount-hud");

    private final HudElement hudElement = (context, tickCounter) -> {
        if (!MainSwitch.canComputeAndRender()) return;
        HudRenderer.renderEntityCountHUD(context, EntityListFilter.getProcessedList(entityCountMap), ConfigManager.getCoord());
    };

    @Override
    public void onInitializeClient() {
        this.init();
        HudElementRegistry.attachElementAfter(VanillaHudElements.SCOREBOARD, ENTITY_COUNT_HUD_ID, hudElement);
    }
}
