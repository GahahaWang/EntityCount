package cc.gahaha.entitycount;

import cc.gahaha.entitycount.config.ClothConfigIntegration;
import cc.gahaha.entitycount.config.Command;
import cc.gahaha.entitycount.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityCount {
    public static final String MOD_ID = "entitycount";
    public static final String category = "key.category.entitycount.keybind";
    public static final Logger LOGGER = LoggerFactory.getLogger("EntityCount");
    public static KeyBinding openConfigScreenKey = new KeyBinding(
            "key.entitycount.openconfig",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            category
    );;
    public static KeyBinding switchOnOff = new KeyBinding(
            "key.entitycount.switchonoff",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_I,
            category
    );;

    public static void openConfigScreen () {
        var mc =  MinecraftClient.getInstance();
        mc.setScreen(ClothConfigIntegration.createConfigScreen(mc.currentScreen));
    }

    public static void setSwitchOnOff () {
        ConfigManager.setShowEntitiesCount(!ConfigManager.isShowEntitiesCount());
    }
}
