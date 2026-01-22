package cc.gahaha.entitycount;

import cc.gahaha.entitycount.config.ClothConfigIntegration;
import cc.gahaha.entitycount.config.Command;
import cc.gahaha.entitycount.config.ConfigManager;
import cc.gahaha.entitycount.event.CountEntityEvent;
import cc.gahaha.entitycount.utils.MainSwitch;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityCount {
    public static final String MOD_ID = "entitycount";
    public static final Logger LOGGER = LoggerFactory.getLogger("EntityCount");
    public static KeyBinding openConfigScreenKey;
    public static KeyBinding switchOnOff;

    public void init() {
        ConfigManager.init();
        Command.register();
        ClientTickEvent.CLIENT_POST.register(client -> {
            if (MainSwitch.canComputeAndRender())
                CountEntityEvent.updateEntityCount(client);
            while (openConfigScreenKey.wasPressed())
                openConfigScreen();
            while (switchOnOff.wasPressed())
                setSwitchOnOff();
        });

        openConfigScreenKey = new KeyBinding(
                "key.entitycount.openconfig",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "category.entitycount"
        );

        switchOnOff = new KeyBinding(
                "key.entitycount.switchonoff",
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_I,
                "category.entitycount"
        );

        KeyMappingRegistry.register(openConfigScreenKey);
        KeyMappingRegistry.register(switchOnOff);
    }

    public void openConfigScreen () {
        var mc =  MinecraftClient.getInstance();
        mc.setScreen(ClothConfigIntegration.createConfigScreen(mc.currentScreen));
    }

    public void setSwitchOnOff () {
        ConfigManager.setShowEntitiesCount(!ConfigManager.isShowEntitiesCount());
    }
}
