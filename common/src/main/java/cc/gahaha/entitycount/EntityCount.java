package cc.gahaha.entitycount;

import cc.gahaha.entitycount.config.ClothConfigIntegration;
import cc.gahaha.entitycount.config.ConfigManager;
import cc.gahaha.entitycount.event.CountEntityEvent;
import cc.gahaha.entitycount.utils.MainSwitch;
import com.mojang.blaze3d.platform.InputConstants;
import lombok.NonNull;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public abstract class EntityCount {
    protected static EntityCount INSTANCE = null;
    public static EntityCount getInstance() { return INSTANCE; }
    public static final String MOD_ID = "entitycount";
    public abstract @NonNull Path getConfigFolder();

    public static final KeyMapping.Category category = new KeyMapping.Category(Identifier.fromNamespaceAndPath(MOD_ID, "keybind"));
    public static final Logger LOGGER = LoggerFactory.getLogger("EntityCount");
    public static final KeyMapping openConfigScreenKey = new KeyMapping(
            "key.entitycount.openconfig",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            category
    );;
    public static final KeyMapping switchOnOff = new KeyMapping(
            "key.entitycount.switchonoff",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            category
    );;

    public void onClientTick(Minecraft client) {
        if (MainSwitch.canComputeAndRender())
            CountEntityEvent.updateEntityCount();
        while (openConfigScreenKey.consumeClick())
            openConfigScreen();
        while (switchOnOff.consumeClick())
            setSwitchOnOff();
    }

    public void init() {
        ConfigManager.init();
    }

    public void openConfigScreen () {
        var mc =  Minecraft.getInstance();
        mc.gui.setScreen(ClothConfigIntegration.createConfigScreen(mc.gui.screen()));
    }

    public void setSwitchOnOff () {
        ConfigManager.setShowEntitiesCount(!ConfigManager.isShowEntitiesCount());
    }
}
