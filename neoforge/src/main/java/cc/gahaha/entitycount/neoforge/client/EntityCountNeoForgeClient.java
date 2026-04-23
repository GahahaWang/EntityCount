package cc.gahaha.entitycount.neoforge.client;

import cc.gahaha.entitycount.EntityCount;
import cc.gahaha.entitycount.config.ClothConfigIntegration;
import cc.gahaha.entitycount.config.Command;
import cc.gahaha.entitycount.render.HudRenderer;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;

import static cc.gahaha.entitycount.EntityCount.MOD_ID;
import static cc.gahaha.entitycount.render.HudRenderer.ENTITY_COUNT_HUD_ID;

@Mod(value = MOD_ID, dist = Dist.CLIENT)
public final class EntityCountNeoForgeClient extends EntityCount{

    public EntityCountNeoForgeClient(IEventBus modEventBus, ModContainer container) {
        INSTANCE = this;
        super.init();
        modEventBus.addListener(this::registerKeyMappings);
        modEventBus.addListener(this::registerGuiLayers);
        NeoForge.EVENT_BUS.addListener(this::registerCommand);
        NeoForge.EVENT_BUS.addListener(this::onClientTickPost);
        // 註冊配置畫面
        container.registerExtensionPoint(IConfigScreenFactory.class, (client, parent) ->
                ClothConfigIntegration.createConfigScreen(parent));

    }

    private void registerCommand(RegisterClientCommandsEvent event) {
        Command.registerCommands(event.getDispatcher(), event.getBuildContext());
    }

    private void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(openConfigScreenKey);
        event.register(switchOnOff);
    }

    private void onClientTickPost(ClientTickEvent.Post ignored) {
        this.onClientTick(Minecraft.getInstance());
    }

    private void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
            VanillaGuiLayers.SCOREBOARD_SIDEBAR,
            ENTITY_COUNT_HUD_ID,
            HudRenderer::render
        );
    }

    @Override
    public @NonNull Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }
}

