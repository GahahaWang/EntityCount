package cc.gahaha.entitycount.fabric.client;

import cc.gahaha.entitycount.EntityCount;
import cc.gahaha.entitycount.config.Command;
import cc.gahaha.entitycount.config.ConfigManager;
import cc.gahaha.entitycount.render.HudRenderer;
import cc.gahaha.entitycount.utils.EntityListFilter;
import cc.gahaha.entitycount.utils.MainSwitch;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;

import static cc.gahaha.entitycount.event.CountEntityEvent.entityCountMap;

public final class EntityCountFabricClient extends EntityCount implements ClientModInitializer {

    public static final Identifier ENTITY_COUNT_HUD_ID = Identifier.fromNamespaceAndPath("entitycount", "entitycount-hud");

    public EntityCountFabricClient() {INSTANCE = this;}

    private final HudElement hudElement = (context, tickCounter) -> {
        if (!MainSwitch.canComputeAndRender()) return;
        HudRenderer.renderEntityCountHUD(context, EntityListFilter.getProcessedList(entityCountMap), ConfigManager.getCoord());
    };

    @Override
    public void onInitializeClient() {
        super.init();
        KeyMappingHelper.registerKeyMapping(openConfigScreenKey);
        KeyMappingHelper.registerKeyMapping(switchOnOff);
        HudElementRegistry.attachElementAfter(VanillaHudElements.SCOREBOARD, ENTITY_COUNT_HUD_ID, hudElement);
        ClientTickEvents.END_CLIENT_TICK.register(super::onClientTick);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            Command.registerCommands(dispatcher, registryAccess);
        });
    }

    @Override
    public @NonNull Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
