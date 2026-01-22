package cc.gahaha.entitycount.neoforge.client;

import cc.gahaha.entitycount.EntityCount;
import cc.gahaha.entitycount.config.ClothConfigIntegration;
import cc.gahaha.entitycount.event.CountEntityEvent;
import cc.gahaha.entitycount.render.HudRenderer;
import cc.gahaha.entitycount.utils.MainSwitch;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static cc.gahaha.entitycount.render.HudRenderer.ENTITY_COUNT_HUD_ID;

@Mod(value = "entitycount", dist = Dist.CLIENT)
public final class EntityCountNeoForgeClient extends EntityCount{

    public EntityCountNeoForgeClient(IEventBus modEventBus, ModContainer container) {
        this.init();

        // 註冊配置畫面
        container.registerExtensionPoint(IConfigScreenFactory.class, (client, parent) ->
                ClothConfigIntegration.createConfigScreen(parent));

        // 註冊 HUD 圖層
        modEventBus.addListener(this::registerGuiLayers);
        modEventBus.addListener(ClientTickEvent.Post.class, (tickEvent)->{
            if (MainSwitch.canComputeAndRender())
                CountEntityEvent.updateEntityCount();
            while (openConfigScreenKey.wasPressed())
                openConfigScreen();
            while (switchOnOff.wasPressed())
                setSwitchOnOff();
        });
        modEventBus.addListener(RegisterKeyMappingsEvent.class, (registerKeyMappingsEvent -> {
            registerKeyMappingsEvent.register(openConfigScreenKey);
            registerKeyMappingsEvent.register(switchOnOff);
        }));
    }

    private void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
            VanillaGuiLayers.SCOREBOARD_SIDEBAR,
            ENTITY_COUNT_HUD_ID,
            HudRenderer::render
        );
    }
}

