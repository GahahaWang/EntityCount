package cc.gahaha.entitycount.clothconfighook;

import cc.gahaha.entitycount.utils.AtomicVec3d;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class Vec3dConsumerButtonBuilder extends AbstractFieldBuilder<Vec3d, Vec3dConsumerButtonEntry, Vec3dConsumerButtonBuilder> {
    private Function<Vec3d, Text> nameProvider = null;
    private Consumer<AtomicVec3d> buttonFunction = atomicVec3d -> {};
    public Vec3dConsumerButtonBuilder(Text resetButtonKey, Text fieldNameKey, Vec3d value) {
        super(resetButtonKey, fieldNameKey);
        Objects.requireNonNull(value);
        this.value = value;
    }

    public Vec3dConsumerButtonBuilder setErrorSupplier(Function<Vec3d, Optional<Text>> errorSupplier) {
        return (Vec3dConsumerButtonBuilder)super.setErrorSupplier(errorSupplier);
    }

    public Vec3dConsumerButtonBuilder requireRestart() {
        return (Vec3dConsumerButtonBuilder)super.requireRestart();
    }

    public Vec3dConsumerButtonBuilder setButtonFunction(Consumer<AtomicVec3d> buttonFunction) {
        this.buttonFunction = buttonFunction;
        return this;
    }

    public Vec3dConsumerButtonBuilder setSaveConsumer(Consumer<Vec3d> saveConsumer) {
        return (Vec3dConsumerButtonBuilder)super.setSaveConsumer(saveConsumer);
    }

    public Vec3dConsumerButtonBuilder setDefaultValue(Supplier<Vec3d> defaultValue) {
        return (Vec3dConsumerButtonBuilder)super.setDefaultValue(defaultValue);
    }

    public Vec3dConsumerButtonBuilder setDefaultValue(Vec3d defaultValue) {
        return (Vec3dConsumerButtonBuilder)super.setDefaultValue(defaultValue);
    }

    public Vec3dConsumerButtonBuilder setTooltipSupplier(Function<Vec3d, Optional<Text[]>> tooltipSupplier) {
        return (Vec3dConsumerButtonBuilder)super.setTooltipSupplier(tooltipSupplier);
    }

    public Vec3dConsumerButtonBuilder setTooltipSupplier(Supplier<Optional<Text[]>> tooltipSupplier) {
        return (Vec3dConsumerButtonBuilder)super.setTooltipSupplier(tooltipSupplier);
    }

    public Vec3dConsumerButtonBuilder setTooltip(Text... tooltip) {
        return (Vec3dConsumerButtonBuilder)super.setTooltip(tooltip);
    }

    public Vec3dConsumerButtonBuilder setNameProvider(Function<Vec3d, Text> enumNameProvider) {
        this.nameProvider = enumNameProvider;
        return this;
    }

    public @NotNull Vec3dConsumerButtonEntry build() {
        Vec3dConsumerButtonEntry entry = new Vec3dConsumerButtonEntry(this.getFieldNameKey(), this.value, this.getResetButtonKey(), this.defaultValue, this.buttonFunction, this.getSaveConsumer(), this.nameProvider, (Supplier)null, this.isRequireRestart());
        //entry.setTooltipSupplier(() -> (Optional)this.getTooltipSupplier().apply(entry.getValue()));
        //if (this.errorSupplier != null) {
        //    entry.setErrorSupplier(() -> (Optional)this.errorSupplier.apply(entry.getValue()));
        //}

        return (Vec3dConsumerButtonEntry)this.finishBuilding(entry);
    }
}
