package cc.gahaha.entitycount.config;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import static dev.architectury.event.events.client.ClientCommandRegistrationEvent.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Command {
    public static final SuggestionProvider<CommandSource> ALL_ENTITIES = SuggestionProviders.register(Identifier.of("entitycount","all_entities"), (context, builder) -> CommandSource.suggestFromIdentifier(Registries.ENTITY_TYPE.stream().filter((entityType) -> entityType.isEnabled(((CommandSource)context.getSource()).getEnabledFeatures())), builder, Registries.ENTITY_TYPE::getId, EntityType::getName));
    public static final SuggestionProvider<CommandSource> ALL_ITEMS = SuggestionProviders.register(Identifier.of("entitycount","all_items"), (context, builder) -> CommandSource.suggestFromIdentifier(Registries.ITEM.stream().filter((entityType) -> entityType.isEnabled(((CommandSource)context.getSource()).getEnabledFeatures())), builder, Registries.ITEM::getId, Item::getName));
    public static void register() {
        ClientCommandRegistrationEvent.EVENT.register(Command::registerCommands);
    }

    private static void registerCommands(CommandDispatcher<ClientCommandSourceStack> dispatcher, CommandRegistryAccess registryAccess) {
        RegistryEntryReferenceArgumentType<EntityType<?>> entityTypeArgumentType = RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE);
        RegistryEntryReferenceArgumentType<Item> itemArgumentType = RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ITEM);
        RequiredArgumentBuilder<ClientCommandSourceStack, ?> entityTypeArgument = argument("entity", entityTypeArgumentType).suggests(SuggestionProviders.cast(ALL_ENTITIES));
        RequiredArgumentBuilder<ClientCommandSourceStack, ?> itemArgument = argument("item", itemArgumentType).suggests(SuggestionProviders.cast(ALL_ITEMS));
        dispatcher.register(literal("entitycount")
                .then(literal("toggle")
                        .executes(ctx -> toggleDisplay()))
                .then(literal("whitelist")
                        .then(literal("add")
                                .then(entityTypeArgument
                                        .executes(ctx -> addToWhitelistNormal(getEntityTranslationName(ctx))))
                                .then(literal("item")
                                        .then(itemArgument
                                                .executes(ctx -> addToWhitelistItem(getItemTranslationName(ctx))))))
                        .then(literal("remove")
                                .then(entityTypeArgument
                                        .executes(ctx -> removeFromWhitelistNormal(getEntityTranslationName(ctx))))
                                .then(literal("item")
                                        .then(itemArgument
                                                .executes(ctx -> removeFromWhitelistItem(getItemTranslationName(ctx))))))
                        .then(literal("clear").executes(ctx -> clearWhitelist()))
                        .then(literal("list").executes(ctx -> listWhitelist())))
                .then(literal("blacklist")
                        .then(literal("add")
                                .then(entityTypeArgument
                                        .executes(ctx -> addToBlacklistNormal(getEntityTranslationName(ctx))))
                                .then(literal("item")
                                        .then(itemArgument
                                                .executes(ctx -> addToBlacklistItem(getItemTranslationName(ctx))))))
                        .then(literal("remove")
                                .then(entityTypeArgument
                                        .executes(ctx -> removeFromBlacklistNormal(getEntityTranslationName(ctx))))
                                .then(literal("item")
                                        .then(itemArgument
                                                .executes(ctx -> removeFromBlacklistItem(getItemTranslationName(ctx))))))
                        .then(literal("clear").executes(ctx -> clearBlacklist()))
                        .then(literal("list").executes(ctx -> listBlacklist())))
                .then(literal("pinnedlist")
                        .then(literal("add")
                                .then(entityTypeArgument
                                        .executes(ctx -> addToPinnedNormal(getEntityTranslationName(ctx))))
                                .then(literal("item")
                                        .then(itemArgument
                                                .executes(ctx -> addToPinnedItem(getItemTranslationName(ctx))))))
                        .then(literal("remove")
                                .then(entityTypeArgument
                                        .executes(ctx -> removeFromPinnedNormal(getEntityTranslationName(ctx))))
                                .then(literal("item")
                                        .then(itemArgument
                                                .executes(ctx -> removeFromPinnedItem(getItemTranslationName(ctx))))))
                        .then(literal("clear").executes(ctx -> clearPinned()))
                        .then(literal("list").executes(ctx -> listPinned())))
                .then(literal("listmode")
                        .then(literal("Whitelist").executes(ctx -> setListMode("Whitelist")))
                        .then(literal("Blacklist").executes(ctx -> setListMode("Blacklist"))))
                .then(literal("entitytype")
                        .then(literal("All").executes(ctx -> setEntityType("All")))
                        .then(literal("Living").executes(ctx -> setEntityType("Living"))))
                .then(literal("threshold")
                        .then(argument("value", IntegerArgumentType.integer(-1)).executes(ctx -> setThreshold(IntegerArgumentType.getInteger(ctx, "value")))))
                .then(literal("maxlength")
                        .then(argument("value", IntegerArgumentType.integer(-1)).executes(ctx -> setMaxLength(IntegerArgumentType.getInteger(ctx, "value")))))
                .then(literal("expanditem")
                        .then(literal("true").executes(ctx -> setExpandItem("true")))
                        .then(literal("false").executes(ctx -> setExpandItem("false"))))
                .then(literal("expanditemprefix")
                        .then(literal("true").executes(ctx -> setExpandItemPrefix("true")))
                        .then(literal("false").executes(ctx -> setExpandItemPrefix("false"))))
                .then(literal("pinnedshowevenzero")
                        .then(literal("true").executes(ctx -> setPinnedShowEvenZero("true")))
                        .then(literal("false").executes(ctx -> setPinnedShowEvenZero("false"))))
                .then(literal("reload").executes(ctx -> reload()))
                .then(literal("reset").executes(ctx -> reset()))
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

    private static int setEntityType(String type) {
        List<String> validTypes = List.of("All", "Living");
        if (!validTypes.contains(type)) {
            addMessage("invalid type");
            return 0;
        } else {
            ConfigManager.setEntityType(type);
            addMessage("entity type set to " + type);
        }
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

    private static int setExpandItemPrefix(String value) {
        boolean prefix = Boolean.parseBoolean(value);
        ConfigManager.setExpandItemDisplayPrefix(prefix);
        addMessage("expand item prefix set to " + prefix);
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
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("EntityCount: "+text));
    }

    @SuppressWarnings("unchecked")
    private static String getEntityTranslationName(CommandContext<ClientCommandSourceStack> context) {
        RegistryEntry.Reference<EntityType<?>> entry = context.getArgument("entity", RegistryEntry.Reference.class);
        return Text.translatable(entry.value().getTranslationKey()).getString();
    }
    @SuppressWarnings("unchecked")
    private static String getItemTranslationName(CommandContext<ClientCommandSourceStack> context) {
        RegistryEntry.Reference<Item> entry = context.getArgument("item", RegistryEntry.Reference.class);
        return Text.translatable(entry.value().getTranslationKey()).getString();
    }
}
