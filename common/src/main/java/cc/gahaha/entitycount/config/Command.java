package cc.gahaha.entitycount.config;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static cc.gahaha.entitycount.EntityCount.MOD_ID;

public class Command {
    public static final SuggestionProvider<SharedSuggestionProvider> ALL_ENTITIES = SuggestionProviders.register(Identifier.fromNamespaceAndPath(MOD_ID,"all_entities"), (context, builder) -> {
        Stream<EntityType<?>> entityTypeStream = BuiltInRegistries.ENTITY_TYPE.stream().filter((entityType) -> entityType.isEnabled((context.getSource()).enabledFeatures()));
        DefaultedRegistry<EntityType<?>> entityTypeDefaultedRegistry = BuiltInRegistries.ENTITY_TYPE;
        Objects.requireNonNull(entityTypeDefaultedRegistry);
        return SharedSuggestionProvider.suggestResource(entityTypeStream, builder, entityTypeDefaultedRegistry::getKey, EntityType::getDescription);
    });
    public static final SuggestionProvider<SharedSuggestionProvider> ALL_ITEMS = SuggestionProviders.register(Identifier.fromNamespaceAndPath(MOD_ID,"all_items"), (context, builder) -> {
        Stream<Item> itemStream = BuiltInRegistries.ITEM.stream().filter((entityType) -> entityType.isEnabled((context.getSource()).enabledFeatures()));
        DefaultedRegistry<Item> itemDefaultedRegistry = BuiltInRegistries.ITEM;
        Objects.requireNonNull(itemDefaultedRegistry);
        return SharedSuggestionProvider.suggestResource(itemStream, builder, itemDefaultedRegistry::getKey,  item -> new ItemStack(item).getItemName());
    });

    private static LiteralArgumentBuilder<CommandSourceStack> literalNode(String name) {
    return LiteralArgumentBuilder.literal(name);
    }

    private static <T> RequiredArgumentBuilder<CommandSourceStack, T> argumentNode(String name, ArgumentType<T> type) {
    return RequiredArgumentBuilder.argument(name, type);
    }

    @SuppressWarnings("unchecked")
    public static void registerCommands(CommandDispatcher<? extends SharedSuggestionProvider> rawDispatcher, CommandBuildContext registryAccess) {
        CommandDispatcher<CommandSourceStack> dispatcher = (CommandDispatcher<CommandSourceStack>) rawDispatcher;
        ResourceArgument<EntityType<?>> entityTypeArgumentType = ResourceArgument.resource(registryAccess, Registries.ENTITY_TYPE);
        ResourceArgument<Item> itemArgumentType = ResourceArgument.resource(registryAccess, Registries.ITEM);
        RequiredArgumentBuilder<CommandSourceStack, Holder.Reference<EntityType<?>>> entityTypeArgument = argumentNode("entity", entityTypeArgumentType);
        entityTypeArgument.suggests(SuggestionProviders.cast(ALL_ENTITIES));
        RequiredArgumentBuilder<CommandSourceStack, Holder.Reference<Item>> itemArgument = argumentNode("item", itemArgumentType);
        itemArgument.suggests(SuggestionProviders.cast(ALL_ITEMS));

        dispatcher.register(literalNode("entitycount")
            .then(literalNode("toggle")
                        .executes(ctx -> toggleDisplay()))
            .then(literalNode("whitelist")
                .then(literalNode("add")
                                .then(entityTypeArgument
                                        .executes(ctx -> addToWhitelistNormal(getEntityTranslationName(ctx))))
                    .then(literalNode("item")
                                        .then(itemArgument
                                                .executes(ctx -> addToWhitelistItem(getItemTranslationName(ctx))))))
                .then(literalNode("remove")
                                .then(entityTypeArgument
                                        .executes(ctx -> removeFromWhitelistNormal(getEntityTranslationName(ctx))))
                    .then(literalNode("item")
                                        .then(itemArgument
                                                .executes(ctx -> removeFromWhitelistItem(getItemTranslationName(ctx))))))
                .then(literalNode("clear").executes(ctx -> clearWhitelist()))
                .then(literalNode("list").executes(ctx -> listWhitelist())))
            .then(literalNode("blacklist")
                .then(literalNode("add")
                                .then(entityTypeArgument
                                        .executes(ctx -> addToBlacklistNormal(getEntityTranslationName(ctx))))
                    .then(literalNode("item")
                                        .then(itemArgument
                                                .executes(ctx -> addToBlacklistItem(getItemTranslationName(ctx))))))
                .then(literalNode("remove")
                                .then(entityTypeArgument
                                        .executes(ctx -> removeFromBlacklistNormal(getEntityTranslationName(ctx))))
                    .then(literalNode("item")
                                        .then(itemArgument
                                                .executes(ctx -> removeFromBlacklistItem(getItemTranslationName(ctx))))))
                .then(literalNode("clear").executes(ctx -> clearBlacklist()))
                .then(literalNode("list").executes(ctx -> listBlacklist())))
            .then(literalNode("pinnedlist")
                .then(literalNode("add")
                                .then(entityTypeArgument
                                        .executes(ctx -> addToPinnedNormal(getEntityTranslationName(ctx))))
                    .then(literalNode("item")
                                        .then(itemArgument
                                                .executes(ctx -> addToPinnedItem(getItemTranslationName(ctx))))))
                .then(literalNode("remove")
                                .then(entityTypeArgument
                                        .executes(ctx -> removeFromPinnedNormal(getEntityTranslationName(ctx))))
                    .then(literalNode("item")
                                        .then(itemArgument
                                                .executes(ctx -> removeFromPinnedItem(getItemTranslationName(ctx))))))
                .then(literalNode("clear").executes(ctx -> clearPinned()))
                .then(literalNode("list").executes(ctx -> listPinned())))
            .then(literalNode("filtermode")
                .then(literalNode("Whitelist").executes(ctx -> setListMode("Whitelist")))
                .then(literalNode("Blacklist").executes(ctx -> setListMode("Blacklist"))))
            .then(literalNode("countliving")
                .then(literalNode("true").executes(ctx -> setCountLiving("true")))
                .then(literalNode("false").executes(ctx -> setCountLiving("false"))))
            .then(literalNode("countnonliving")
                .then(literalNode("true").executes(ctx -> setCountNonLiving("true")))
                .then(literalNode("false").executes(ctx -> setCountNonLiving("false"))))
            .then(literalNode("threshold")
                .then(argumentNode("value", IntegerArgumentType.integer(-1)).executes(ctx -> setThreshold(IntegerArgumentType.getInteger(ctx, "value")))))
            .then(literalNode("maxlength")
                .then(argumentNode("value", IntegerArgumentType.integer(-1)).executes(ctx -> setMaxLength(IntegerArgumentType.getInteger(ctx, "value")))))
            .then(literalNode("countrange")
                .then(argumentNode("value", IntegerArgumentType.integer(0, ConfigManager.MAX_RENDER_DISTANCE)).executes(ctx -> setCountRange(IntegerArgumentType.getInteger(ctx, "value")))))
            .then(literalNode("expanditem")
                .then(literalNode("true").executes(ctx -> setExpandItem("true")))
                .then(literalNode("false").executes(ctx -> setExpandItem("false"))))
            .then(literalNode("pinnedshowevenzero")
                .then(literalNode("true").executes(ctx -> setPinnedShowEvenZero("true")))
                .then(literalNode("false").executes(ctx -> setPinnedShowEvenZero("false"))))
            .then(literalNode("reload").executes(ctx -> reload()))
            .then(literalNode("reset").executes(ctx -> reset()))
        );
    }

    // Whitelist Normal commands
    private static int addToWhitelistNormal(String entity) {
        List<String> list = new ArrayList<>(ConfigManager.getWhiteListNormal());
        if (list.contains(entity)) {
            addMessage("already exist");
            return 0;
        } else {
            list.add(entity);
            ConfigManager.setWhiteListNormal(list);
            addMessage("ok");
        }
        return 1;
    }

    private static int removeFromWhitelistNormal(String entity) {
        List<String> list = new ArrayList<>(ConfigManager.getWhiteListNormal());
        if (!list.contains(entity)) {
            addMessage("not found");
            return 0;
        } else {
            list.remove(entity);
            ConfigManager.setWhiteListNormal(list);
            addMessage("ok");
        }
        return 1;
    }

    // Whitelist Item commands
    private static int addToWhitelistItem(String item) {
        List<String> list = new ArrayList<>(ConfigManager.getWhiteListItem());
        if (list.contains(item)) {
            addMessage("already exist");
            return 0;
        } else {
            list.add(item);
            ConfigManager.setWhiteListItem(list);
            addMessage("ok");
        }
        return 1;
    }

    private static int removeFromWhitelistItem(String item) {
        List<String> list = new ArrayList<>(ConfigManager.getWhiteListItem());
        if (!list.contains(item)) {
            addMessage("not found");
            return 0;
        } else {
            list.remove(item);
            ConfigManager.setWhiteListItem(list);
            addMessage("ok");
        }
        return 1;
    }

    private static int clearWhitelist() {
        ConfigManager.setWhiteListNormal(List.of());
        ConfigManager.setWhiteListItem(List.of());
        addMessage("whitelist cleared");
        return 1;
    }

    private static int listWhitelist() {
        List<String> listNormal = ConfigManager.getWhiteListNormal();
        List<String> listItem = ConfigManager.getWhiteListItem();
        if (listNormal.isEmpty() && listItem.isEmpty()) {
            addMessage("whitelist is empty");
        } else {
            if (!listNormal.isEmpty()) {
                addMessage("Whitelist Normal: " + String.join(", ", listNormal));
            }
            if (!listItem.isEmpty()) {
                addMessage("Whitelist Item: " + String.join(", ", listItem));
            }
        }
        return 1;
    }

    // Blacklist Normal commands
    private static int addToBlacklistNormal(String entity) {
        List<String> list = new ArrayList<>(ConfigManager.getBlackListNormal());
        if (list.contains(entity)) {
            addMessage("already exist");
            return 0;
        } else {
            list.add(entity);
            ConfigManager.setBlackListNormal(list);
            addMessage("ok");
        }
        return 1;
    }

    private static int removeFromBlacklistNormal(String entity) {
        List<String> list = new ArrayList<>(ConfigManager.getBlackListNormal());
        if (!list.contains(entity)) {
            addMessage("not found");
            return 0;
        } else {
            list.remove(entity);
            ConfigManager.setBlackListNormal(list);
            addMessage("ok");
        }
        return 1;
    }

    // Blacklist Item commands
    private static int addToBlacklistItem(String item) {
        List<String> list = new ArrayList<>(ConfigManager.getBlackListItem());
        if (list.contains(item)) {
            addMessage("already exist");
            return 0;
        } else {
            list.add(item);
            ConfigManager.setBlackListItem(list);
            addMessage("ok");
        }
        return 1;
    }

    private static int removeFromBlacklistItem(String item) {
        List<String> list = new ArrayList<>(ConfigManager.getBlackListItem());
        if (!list.contains(item)) {
            addMessage("not found");
            return 0;
        } else {
            list.remove(item);
            ConfigManager.setBlackListItem(list);
            addMessage("ok");
        }
        return 1;
    }

    private static int clearBlacklist() {
        ConfigManager.setBlackListNormal(List.of());
        ConfigManager.setBlackListItem(List.of());
        addMessage("blacklist cleared");
        return 1;
    }

    private static int listBlacklist() {
        List<String> listNormal = ConfigManager.getBlackListNormal();
        List<String> listItem = ConfigManager.getBlackListItem();
        if (listNormal.isEmpty() && listItem.isEmpty()) {
            addMessage("blacklist is empty");
        } else {
            if (!listNormal.isEmpty()) {
                addMessage("Blacklist Normal: " + String.join(", ", listNormal));
            }
            if (!listItem.isEmpty()) {
                addMessage("Blacklist Item: " + String.join(", ", listItem));
            }
        }
        return 1;
    }

    // Pinned Normal list commands
    private static int addToPinnedNormal(String entity) {
        List<String> list = new ArrayList<>(ConfigManager.getPinnedListNormal());
        if (list.contains(entity)) {
            addMessage("already exist");
            return 0;
        } else {
            list.add(entity);
            ConfigManager.setPinnedListNormal(list);
            addMessage("ok");
        }
        return 1;
    }

    private static int removeFromPinnedNormal(String entity) {
        List<String> list = new ArrayList<>(ConfigManager.getPinnedListNormal());
        if (!list.contains(entity)) {
            addMessage("not found");
            return 0;
        } else {
            list.remove(entity);
            ConfigManager.setPinnedListNormal(list);
            addMessage("ok");
        }
        return 1;
    }

    // Pinned Item list commands
    private static int addToPinnedItem(String item) {
        List<String> list = new ArrayList<>(ConfigManager.getPinnedListItem());
        if (list.contains(item)) {
            addMessage("already exist");
            return 0;
        } else {
            list.add(item);
            ConfigManager.setPinnedListItem(list);
            addMessage("ok");
        }
        return 1;
    }

    private static int removeFromPinnedItem(String item) {
        List<String> list = new ArrayList<>(ConfigManager.getPinnedListItem());
        if (!list.contains(item)) {
            addMessage("not found");
            return 0;
        } else {
            list.remove(item);
            ConfigManager.setPinnedListItem(list);
            addMessage("ok");
        }
        return 1;
    }

    private static int clearPinned() {
        ConfigManager.setPinnedListNormal(List.of());
        ConfigManager.setPinnedListItem(List.of());
        addMessage("pinned list cleared");
        return 1;
    }

    private static int listPinned() {
        List<String> listNormal = ConfigManager.getPinnedListNormal();
        List<String> listItem = ConfigManager.getPinnedListItem();
        if (listNormal.isEmpty() && listItem.isEmpty()) {
            addMessage("pinned list is empty");
        } else {
            if (!listNormal.isEmpty()) {
                addMessage("Pinned Normal: " + String.join(", ", listNormal));
            }
            if (!listItem.isEmpty()) {
                addMessage("Pinned Item: " + String.join(", ", listItem));
            }
        }
        return 1;
    }

    // Configuration commands
    private static int setListMode(String mode) {
        if (!mode.equals("Whitelist") && !mode.equals("Blacklist")) {
            addMessage("invalid mode");
            return 0;
        } else {
            ConfigManager.setListMode(mode);
            addMessage("list mode set to " + mode);
        }
        return 1;
    }

    private static int setCountLiving(String value) {
        boolean count = Boolean.parseBoolean(value);
        ConfigManager.setCountLivingEntity(count);
        addMessage("count living entity set to " + count);
        return 1;
    }

    private static int setCountNonLiving(String value) {
        boolean count = Boolean.parseBoolean(value);
        ConfigManager.setCountNonLivingEntity(count);
        addMessage("count non-living entity set to " + count);
        return 1;
    }

    private static int setThreshold(int value) {
        ConfigManager.setThreshold(value);
        addMessage("threshold set to " + value);
        return 1;
    }

    private static int setMaxLength(int value) {
        ConfigManager.setMaxListLength(value);
        addMessage("max length set to " + value);
        return 1;
    }

    private static int setCountRange(int value) {
        ConfigManager.setCountRange(value);
        addMessage("count range set to " + value + " chunks");
        return 1;
    }

    private static int toggleDisplay() {
        boolean current = ConfigManager.isShowEntitiesCount();
        ConfigManager.setShowEntitiesCount(!current);
        addMessage("display " + (!current ? "enabled" : "disabled"));
        return 1;
    }

    private static int setExpandItem(String value) {
        boolean expand = Boolean.parseBoolean(value);
        ConfigManager.setExpandItemDisplay(expand);
        addMessage("expand item set to " + expand);
        return 1;
    }

    private static int setPinnedShowEvenZero(String value) {
        boolean show = Boolean.parseBoolean(value);
        ConfigManager.setPinnedShowEvenZero(show);
        addMessage("pinned show even zero set to " + show);
        return 1;
    }

    private static int reload() {
        ConfigManager.load();
        addMessage("config reloaded");
        return 1;
    }

    private static int reset() {
        ConfigManager.reset();
        addMessage("config reset to default");
        return 1;
    }

    private static void addMessage(String text) {
        Minecraft.getInstance().gui.hud.getChat().addClientSystemMessage(Component.nullToEmpty("EntityCount: "+text));
    }

    @SuppressWarnings("unchecked")
    private static String getEntityTranslationName(CommandContext<CommandSourceStack> context) {
        Holder.Reference<EntityType<?>> entry = context.getArgument("entity", Holder.Reference.class);
        return Component.translatable(entry.value().toString()).getString();
    }
    @SuppressWarnings("unchecked")
    private static String getItemTranslationName(CommandContext<CommandSourceStack> context) {
        Holder.Reference<Item> entry = context.getArgument("item", Holder.Reference.class);
        return Component.translatable(entry.value().getDescriptionId()).getString();
    }
}
