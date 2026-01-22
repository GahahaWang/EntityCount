package cc.gahaha.entitycount.clothconfighook;

import cc.gahaha.entitycount.utils.AtomicVec3d;
import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class Vec3dConsumerButtonEntry extends TooltipListEntry<Vec3d> {
    private final AtomicVec3d value;
    private final Vec3d original;
    private final ButtonWidget buttonWidget;
    private final ButtonWidget resetButton;
    private final Supplier<Vec3d> defaultValue;
    private final List<ClickableWidget> widgets;
    private final Function<Vec3d, Text> nameProvider;

    /** @deprecated */
    @Deprecated
    @ApiStatus.Internal
    public Vec3dConsumerButtonEntry(Text fieldName, Vec3d value, Text resetButtonKey, Supplier<Vec3d> defaultValue, Consumer<AtomicVec3d> buttonFunction, Consumer<Vec3d> saveConsumer) {
        this(fieldName, value, resetButtonKey, defaultValue, buttonFunction, saveConsumer, (Function)null);
    }

    /** @deprecated */
    @Deprecated
    @ApiStatus.Internal
    public Vec3dConsumerButtonEntry(Text fieldName, Vec3d value, Text resetButtonKey, Supplier<Vec3d> defaultValue, Consumer<AtomicVec3d> buttonFunction, Consumer<Vec3d> saveConsumer, Function<Vec3d, Text> nameProvider) {
        this(fieldName, value, resetButtonKey, defaultValue, buttonFunction, saveConsumer, nameProvider, (Supplier)null);
    }

    /** @deprecated */
    @Deprecated
    @ApiStatus.Internal
    public Vec3dConsumerButtonEntry(Text fieldName, Vec3d value, Text resetButtonKey, Supplier<Vec3d> defaultValue, Consumer<AtomicVec3d> buttonFunction, Consumer<Vec3d> saveConsumer, Function<Vec3d, Text> nameProvider, Supplier<Optional<Text[]>> tooltipSupplier) {
        this(fieldName, value, resetButtonKey, defaultValue, buttonFunction, saveConsumer, nameProvider, tooltipSupplier, false);
    }

    /** @deprecated */
    @Deprecated
    @ApiStatus.Internal
    public Vec3dConsumerButtonEntry(Text fieldName, Vec3d value, Text resetButtonKey, Supplier<Vec3d> defaultValue, Consumer<AtomicVec3d> buttonFunction, Consumer<Vec3d> saveConsumer, Function<Vec3d, Text> nameProvider, Supplier<Optional<Text[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, tooltipSupplier, requiresRestart);

        this.defaultValue = defaultValue;
        this.value = new AtomicVec3d(value);
        this.original = value;
        this.buttonWidget = ButtonWidget.builder(Text.empty(), (widget) -> {
            buttonFunction.accept(this.value);
        }).dimensions(0, 0, 150, 20).build();
        this.resetButton = ButtonWidget.builder(resetButtonKey, (widget) -> {
                this.value.set(this.getDefaultValue().get());
            }
        ).dimensions(0, 0, MinecraftClient.getInstance().textRenderer.getWidth(resetButtonKey) + 6, 20).build();
        this.saveCallback = saveConsumer;
        this.widgets = Lists.newArrayList(new ClickableWidget[]{this.buttonWidget, this.resetButton});
        this.nameProvider = nameProvider == null ? (t) -> Text.translatable(t instanceof Vec3dConsumerButtonEntry.Translatable ? ((Vec3dConsumerButtonEntry.Translatable)t).getKey() : t.toString()) : nameProvider;
    }

    public boolean isEdited() {
        return super.isEdited() || !this.value.get().equals(this.original);
    }

    @Override
    public Vec3d getValue() {
        return value.get();
    }

    public Optional<Vec3d> getDefaultValue() {
        return this.defaultValue == null ? Optional.empty() : Optional.ofNullable(this.defaultValue.get());
    }

    public void render(DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        
        Window window = MinecraftClient.getInstance().getWindow();
        this.resetButton.active = this.isEditable() && this.getDefaultValue().isPresent() && !Objects.equals(this.defaultValue.get(), this.value.get());
        this.resetButton.setY(y);
        this.buttonWidget.active = this.isEditable();
        this.buttonWidget.setY(y);
        this.buttonWidget.setMessage((Text)this.nameProvider.apply(this.value.get()));
        Text displayedFieldName = this.getDisplayedFieldName();
        if (MinecraftClient.getInstance().textRenderer.isRightToLeft()) {
            graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, displayedFieldName.asOrderedText(), window.getScaledWidth() - x - MinecraftClient.getInstance().textRenderer.getWidth(displayedFieldName), y + 6, this.getPreferredTextColor());
            this.resetButton.setX(x);
            this.buttonWidget.setX(x + this.resetButton.getWidth() + 2);
        } else {
            graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, displayedFieldName.asOrderedText(), x, y + 6, this.getPreferredTextColor());
            this.resetButton.setX(x + entryWidth - this.resetButton.getWidth());
            this.buttonWidget.setX(x + entryWidth - 150);
        }

        this.buttonWidget.setWidth(150 - this.resetButton.getWidth() - 2);
        this.resetButton.render(graphics, mouseX, mouseY, delta);
        this.buttonWidget.render(graphics, mouseX, mouseY, delta);
    }

    public List<? extends Element> children() {
        return this.widgets;
    }

    public List<? extends Selectable> narratables() {
        return this.widgets;
    }

    public interface Translatable {
        @NotNull String getKey();
    }
}
