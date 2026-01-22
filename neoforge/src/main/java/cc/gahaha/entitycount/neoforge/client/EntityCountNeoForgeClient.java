package cc.gahaha.entitycount.neoforge.client;

import cc.gahaha.entitycount.EntityCount;
import cc.gahaha.entitycount.config.ClothConfigIntegration;
import cc.gahaha.entitycount.config.Command;
import cc.gahaha.entitycount.config.ConfigManager;
import cc.gahaha.entitycount.event.CountEntityEvent;
import cc.gahaha.entitycount.render.HudRenderer;
import cc.gahaha.entitycount.utils.MainSwitch;
import net.minecraft.server.command.ServerCommandSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static cc.gahaha.entitycount.render.HudRenderer.ENTITY_COUNT_HUD_ID;

@EventBusSubscriber(modid = "entitycount", value = Dist.CLIENT)
@Mod(value = "entitycount", dist = Dist.CLIENT)
public class EntityCountNeoForgeClient extends EntityCount{

    public EntityCountNeoForgeClient(IEventBus modEventBus, ModContainer container) {
        ConfigManager.init(FMLPaths.CONFIGDIR.get());

        // 註冊配置畫面
        container.registerExtensionPoint(IConfigScreenFactory.class, (client, parent) ->
                ClothConfigIntegration.createConfigScreen(parent));

        // 註冊 HUD 圖層
        modEventBus.addListener(this::registerGuiLayers);
        modEventBus.addListener(RegisterKeyMappingsEvent.class, (registerKeyMappingsEvent -> {
            registerKeyMappingsEvent.register(openConfigScreenKey);
            registerKeyMappingsEvent.register(switchOnOff);
        }));
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (MainSwitch.canComputeAndRender())
            CountEntityEvent.updateEntityCount();
        while (openConfigScreenKey.wasPressed())
            openConfigScreen();
        while (switchOnOff.wasPressed())
            setSwitchOnOff();
    }
    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent event) {
        new Command<ServerCommandSource>().registerCommands(event.getDispatcher(), event.getBuildContext());
    }
    private void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
            VanillaGuiLayers.SCOREBOARD_SIDEBAR,
            ENTITY_COUNT_HUD_ID,
            HudRenderer::render
        );
    }
}

