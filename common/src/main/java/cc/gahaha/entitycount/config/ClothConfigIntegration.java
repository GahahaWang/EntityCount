package cc.gahaha.entitycount.config;

import cc.gahaha.entitycount.clothconfighook.Vec3dConsumerButtonBuilder;
import cc.gahaha.entitycount.screen.EntityCountConfigScreen;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.text.DecimalFormat;

public class ClothConfigIntegration{

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("entitycount"));
        builder.getOrCreateCategory(Text.translatable("entitycount.general"))
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.translatable("entitycount.config.master_toggle"), ConfigManager.isShowEntitiesCount())
                        .setDefaultValue(ConfigManager.Default.showEntitiesCount)
                        .setSaveConsumer(ConfigManager::setShowEntitiesCount)
                        .setTooltip(Text.translatable("entitycount.config.master_toggle.tooltip"))
                        .build()
                )
                .addEntry(new Vec3dConsumerButtonBuilder(Text.translatable("text.cloth-config.reset_value"), Text.translatable("entitycount.config.set_coord_screen"), ConfigManager.getCoord())
                        .setButtonFunction((atomicValue)->{
                            Screen thisScreen = MinecraftClient.getInstance().currentScreen;
                            EntityCountConfigScreen configScreen = new EntityCountConfigScreen(thisScreen, atomicValue);
                            MinecraftClient.getInstance().setScreen(configScreen);
                        })
                        .setSaveConsumer(ConfigManager::setCoord)
                        .setDefaultValue(new Vec3d(ConfigManager.Default.x, ConfigManager.Default.y, ConfigManager.Default.scale))
                        //.setTooltip(Text.translatable("entitycount.config.set_coord_screen.tooltip"))
                        .setNameProvider(
                                (vec3d) -> {
                                    DecimalFormat df = new DecimalFormat(".####");
                                    var xd = df.format(vec3d.x) + " ";
                                    var yd = df.format(vec3d.y) + " ";
                                    var sd = df.format(vec3d.z);
                                    return Text.literal(xd + yd + sd);
                                })
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .<String>startSelector(
                                Text.translatable("entitycount.config.display_mode"),
                                new String[]{"Living", "All"},
                                ConfigManager.getEntityType()
                        )
                        .setDefaultValue(ConfigManager.Default.entityType)
                        .setSaveConsumer(ConfigManager::setEntityType)
                        .setTooltip(Text.translatable("entitycount.config.display_mode.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .<String>startSelector(
                                Text.translatable("entitycount.config.filter_mode"),
                                new String[]{"Blacklist", "Whitelist"},
                                ConfigManager.getListMode()
                        )
                        .setDefaultValue(ConfigManager.Default.listMode)
                        .setSaveConsumer(ConfigManager::setListMode)
                        .setTooltip(Text.translatable("entitycount.config.filter_mode.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startAlphaColorField(Text.translatable("entitycount.config.text_color"), ConfigManager.getTextColor())
                        .setDefaultValue(ConfigManager.Default.textColor)
                        .setSaveConsumer(ConfigManager::setTextColor)
                        .setTooltip(Text.translatable("entitycount.config.text_color.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startAlphaColorField(Text.translatable("entitycount.config.background_color"), ConfigManager.getBackgroundColor())
                        .setDefaultValue(ConfigManager.Default.backgroundColor)
                        .setSaveConsumer(ConfigManager::setBackgroundColor)
                        .setTooltip(Text.translatable("entitycount.config.background_color.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.translatable("entitycount.config.expand_item_display"), ConfigManager.isExpandItemDisplay())
                        .setDefaultValue(ConfigManager.Default.expandItemDisplay)
                        .setSaveConsumer(ConfigManager::setExpandItemDisplay)
                        .setTooltip(Text.translatable("entitycount.config.expand_item_display.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.translatable("entitycount.config.expand_item_display_prefix"), ConfigManager.isExpandItemDisplayPrefix())
                        .setDefaultValue(ConfigManager.Default.expandItemDisplayPrefix)
                        .setSaveConsumer(ConfigManager::setExpandItemDisplayPrefix)
                        .setTooltip(Text.translatable("entitycount.config.expand_item_display_prefix.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startAlphaColorField(Text.translatable("entitycount.config.item_entity_color"), ConfigManager.getItemEntityColor())
                        .setDefaultValue(ConfigManager.Default.itemEntityColor)
                        .setSaveConsumer(ConfigManager::setItemEntityColor)
                        .setTooltip(Text.translatable("entitycount.config.item_entity_color.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startIntSlider(Text.translatable("entitycount.config.type_limit"), ConfigManager.getMaxListLength(), -1, 50)
                        .setDefaultValue(ConfigManager.Default.maxListLength)
                        .setSaveConsumer(ConfigManager::setMaxListLength)
                        .setTooltip(Text.translatable("entitycount.config.type_limit.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startIntSlider(Text.translatable("entitycount.config.threshold"), ConfigManager.getThreshold(), -1, 100)
                        .setDefaultValue(ConfigManager.Default.threshold)
                        .setSaveConsumer(ConfigManager::setThreshold)
                        .setTooltip(Text.translatable("entitycount.config.threshold.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.translatable("entitycount.config.pinned_show_even_zero"), ConfigManager.isPinnedShowEvenZero())
                        .setDefaultValue(ConfigManager.Default.pinnedShowEvenZero)
                        .setSaveConsumer(ConfigManager::setPinnedShowEvenZero)
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startTextDescription(Text.translatable("entitycount.config.normal_list_example", Text.translatable("entity.minecraft.player").getString()))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startStrList(Text.translatable("entitycount.config.whitelist_normal"), ConfigManager.getWhiteListNormal())
                        .setDefaultValue(ConfigManager.Default.whiteListNormal)
                        .setSaveConsumer(ConfigManager::setWhiteListNormal)
                        .setTooltip(Text.translatable("entitycount.config.whitelist_normal.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startStrList(Text.translatable("entitycount.config.blacklist_normal"), ConfigManager.getBlackListNormal())
                        .setDefaultValue(ConfigManager.Default.blackListNormal)
                        .setSaveConsumer(ConfigManager::setBlackListNormal)
                        .setTooltip(Text.translatable("entitycount.config.blacklist_normal.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startStrList(Text.translatable("entitycount.config.pinnedlist_normal"), ConfigManager.getPinnedListNormal())
                        .setDefaultValue(ConfigManager.Default.pinnedListNormal)
                        .setSaveConsumer(ConfigManager::setPinnedListNormal)
                        .setTooltip(Text.translatable("entitycount.config.pinnedlist_normal.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startTextDescription(Text.translatable("entitycount.config.item_list_example", Text.translatable("item.minecraft.diamond").getString()))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startStrList(Text.translatable("entitycount.config.whitelist_item"), ConfigManager.getWhiteListItem())
                        .setDefaultValue(ConfigManager.Default.whiteListItem)
                        .setSaveConsumer(ConfigManager::setWhiteListItem)
                        .setTooltip(Text.translatable("entitycount.config.whitelist_item.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startStrList(Text.translatable("entitycount.config.blacklist_item"), ConfigManager.getBlackListItem())
                        .setDefaultValue(ConfigManager.Default.blackListItem)
                        .setSaveConsumer(ConfigManager::setBlackListItem)
                        .setTooltip(Text.translatable("entitycount.config.blacklist_item.tooltip"))
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startStrList(Text.translatable("entitycount.config.pinnedlist_item"), ConfigManager.getPinnedListItem())
                        .setDefaultValue(ConfigManager.Default.pinnedListItem)
                        .setSaveConsumer(ConfigManager::setPinnedListItem)
                        .setTooltip(Text.translatable("entitycount.config.pinnedlist_item.tooltip"))
                        .build()
                )
        ;
        return builder.build();
    }
}
