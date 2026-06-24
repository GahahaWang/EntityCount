package cc.gahaha.entitycount.config;

import cc.gahaha.entitycount.clothconfighook.Vec3dConsumerButtonBuilder;
import cc.gahaha.entitycount.screen.EntityCountConfigScreen;
import cc.gahaha.entitycount.utils.Enums;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import java.text.DecimalFormat;

public class ClothConfigIntegration{

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("entitycount"));
        builder.getOrCreateCategory(Component.translatable("entitycount.general"))
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Component.translatable("entitycount.config.master_toggle"), ConfigManager.isShowEntitiesCount())
                        .setDefaultValue(ConfigManager.Default.showEntitiesCount)
                        .setSaveConsumer(ConfigManager::setShowEntitiesCount)
                        .setTooltip(Component.translatable("entitycount.config.master_toggle.tooltip"))
                        .build()
                )
                .addEntry(new Vec3dConsumerButtonBuilder(Component.translatable("text.cloth-config.reset_value"), Component.translatable("entitycount.config.set_coord_screen"), ConfigManager.getCoord())
                        .setButtonFunction((atomicValue)->{
                            Screen thisScreen = Minecraft.getInstance().gui.screen();
                            EntityCountConfigScreen configScreen = new EntityCountConfigScreen(thisScreen, atomicValue);
                            Minecraft.getInstance().gui.setScreen(configScreen);
                        })
                        .setSaveConsumer(ConfigManager::setCoord)
                        .setDefaultValue(new Vec3(ConfigManager.Default.x, ConfigManager.Default.y, ConfigManager.Default.scale))
                        .setNameProvider(
                                (vec3d) -> {
                                    DecimalFormat df = new DecimalFormat(".####");
                                    var xd = df.format(vec3d.x) + " ";
                                    var yd = df.format(vec3d.y) + " ";
                                    var sd = df.format(vec3d.z);
                                    return Component.literal(xd + yd + sd);
                                })
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Component.translatable("entitycount.config.count_living"), ConfigManager.isCountLivingEntity())
                        .setDefaultValue(ConfigManager.Default.countLivingEntity)
                        .setSaveConsumer(ConfigManager::setCountLivingEntity)
                        .setTooltip(Component.translatable("entitycount.config.count_living.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Component.translatable("entitycount.config.count_non_living"), ConfigManager.isCountNonLivingEntity())
                        .setDefaultValue(ConfigManager.Default.countNonLivingEntity)
                        .setSaveConsumer(ConfigManager::setCountNonLivingEntity)
                        .setTooltip(Component.translatable("entitycount.config.count_non_living.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .<String>startSelector(
                                Component.translatable("entitycount.config.filter_mode"),
                                Enums.FilterMode.strs,
                                ConfigManager.getListMode()
                        )
                        .setDefaultValue(ConfigManager.Default.listMode)
                        .setSaveConsumer(ConfigManager::setListMode)
                        .setTooltip(Component.translatable("entitycount.config.filter_mode.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startAlphaColorField(Component.translatable("entitycount.config.text_color"), ConfigManager.getTextColor())
                        .setDefaultValue(ConfigManager.Default.textColor)
                        .setSaveConsumer(ConfigManager::setTextColor)
                        .setTooltip(Component.translatable("entitycount.config.text_color.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startAlphaColorField(Component.translatable("entitycount.config.background_color"), ConfigManager.getBackgroundColor())
                        .setDefaultValue(ConfigManager.Default.backgroundColor)
                        .setSaveConsumer(ConfigManager::setBackgroundColor)
                        .setTooltip(Component.translatable("entitycount.config.background_color.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Component.translatable("entitycount.config.expand_item_display"), ConfigManager.isExpandItemDisplay())
                        .setDefaultValue(ConfigManager.Default.expandItemDisplay)
                        .setSaveConsumer(ConfigManager::setExpandItemDisplay)
                        .setTooltip(Component.translatable("entitycount.config.expand_item_display.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startAlphaColorField(Component.translatable("entitycount.config.item_entity_color"), ConfigManager.getItemEntityColor())
                        .setDefaultValue(ConfigManager.Default.itemEntityColor)
                        .setSaveConsumer(ConfigManager::setItemEntityColor)
                        .setTooltip(Component.translatable("entitycount.config.item_entity_color.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startIntSlider(Component.translatable("entitycount.config.type_limit"), ConfigManager.getMaxListLength(), -1, 50)
                        .setDefaultValue(ConfigManager.Default.maxListLength)
                        .setSaveConsumer(ConfigManager::setMaxListLength)
                        .setTooltip(Component.translatable("entitycount.config.type_limit.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startIntSlider(Component.translatable("entitycount.config.threshold"), ConfigManager.getThreshold(), -1, 100)
                        .setDefaultValue(ConfigManager.Default.threshold)
                        .setSaveConsumer(ConfigManager::setThreshold)
                        .setTooltip(Component.translatable("entitycount.config.threshold.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startIntSlider(Component.translatable("entitycount.config.count_range"), ConfigManager.getCountRange(), 0, ConfigManager.MAX_RENDER_DISTANCE)
                        .setDefaultValue(ConfigManager.Default.countRange)
                        .setSaveConsumer(ConfigManager::setCountRange)
                        .setTooltip(Component.translatable("entitycount.config.count_range.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Component.translatable("entitycount.config.pinned_show_even_zero"), ConfigManager.isPinnedShowEvenZero())
                        .setDefaultValue(ConfigManager.Default.pinnedShowEvenZero)
                        .setSaveConsumer(ConfigManager::setPinnedShowEvenZero)
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startTextDescription(Component.translatable("entitycount.config.normal_list_example", Component.translatable("entity.minecraft.player").getString()))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startStrList(Component.translatable("entitycount.config.whitelist_normal"), ConfigManager.getWhiteListNormal())
                        .setDefaultValue(ConfigManager.Default.whiteListNormal)
                        .setSaveConsumer(ConfigManager::setWhiteListNormal)
                        .setTooltip(Component.translatable("entitycount.config.whitelist_normal.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startStrList(Component.translatable("entitycount.config.blacklist_normal"), ConfigManager.getBlackListNormal())
                        .setDefaultValue(ConfigManager.Default.blackListNormal)
                        .setSaveConsumer(ConfigManager::setBlackListNormal)
                        .setTooltip(Component.translatable("entitycount.config.blacklist_normal.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startStrList(Component.translatable("entitycount.config.pinnedlist_normal"), ConfigManager.getPinnedListNormal())
                        .setDefaultValue(ConfigManager.Default.pinnedListNormal)
                        .setSaveConsumer(ConfigManager::setPinnedListNormal)
                        .setTooltip(Component.translatable("entitycount.config.pinnedlist_normal.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startTextDescription(Component.translatable("entitycount.config.item_list_example", Component.translatable("item.minecraft.diamond").getString()))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startStrList(Component.translatable("entitycount.config.whitelist_item"), ConfigManager.getWhiteListItem())
                        .setDefaultValue(ConfigManager.Default.whiteListItem)
                        .setSaveConsumer(ConfigManager::setWhiteListItem)
                        .setTooltip(Component.translatable("entitycount.config.whitelist_item.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startStrList(Component.translatable("entitycount.config.blacklist_item"), ConfigManager.getBlackListItem())
                        .setDefaultValue(ConfigManager.Default.blackListItem)
                        .setSaveConsumer(ConfigManager::setBlackListItem)
                        .setTooltip(Component.translatable("entitycount.config.blacklist_item.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startStrList(Component.translatable("entitycount.config.pinnedlist_item"), ConfigManager.getPinnedListItem())
                        .setDefaultValue(ConfigManager.Default.pinnedListItem)
                        .setSaveConsumer(ConfigManager::setPinnedListItem)
                        .setTooltip(Component.translatable("entitycount.config.pinnedlist_item.tooltip"))
                        .build()
                )
        ;
        return builder.build();
    }
}
