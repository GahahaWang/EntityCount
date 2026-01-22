package cc.gahaha.entitycount.screen;

import cc.gahaha.entitycount.event.CountEntityEvent;
import cc.gahaha.entitycount.render.HudRenderer;
import cc.gahaha.entitycount.utils.AtomicVec3d;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class EntityCountConfigScreen extends Screen {

    private final @Nullable Screen parent;

    public EntityCountConfigScreen(@Nullable Screen parent, AtomicVec3d coord) {
        super(Text.literal("EntityCount Config"));
        this.parent = parent;
        setModify(coord.get());
        this.coord = coord;
    }

    private final AtomicVec3d coord;
    private float modifyX;
    private float modifyY;
    private float modifyScale;

    public Vec3d getModify() {
        return new Vec3d(modifyX, modifyY, modifyScale);
    }
    public void setModify(Vec3d vec3d) {
        modifyX = (float) vec3d.x;
        modifyY = (float) vec3d.y;
        modifyScale = (float) vec3d.z;
    }

    @Override
    protected void init() {
        Text line1 = Text.translatable("entitycount.config.set_coord_screen.hint1");
        Text line2 = Text.translatable("entitycount.config.set_coord_screen.hint2");
        Text line3 = Text.translatable("entitycount.config.set_coord_screen.hint3");
        TextWidget hintText1 = new TextWidget(line1, this.textRenderer);
        TextWidget hintText2 = new TextWidget(line2, this.textRenderer);
        TextWidget hintText3 = new TextWidget(line3, this.textRenderer);
        int textWidth1 = this.textRenderer.getWidth(line1);
        int textWidth2 = this.textRenderer.getWidth(line2);
        int textWidth3 = this.textRenderer.getWidth(line3);
        hintText1.setDimensionsAndPosition(textWidth1, 10, (this.width - textWidth1) / 2, 10);
        hintText2.setDimensionsAndPosition(textWidth2, 10, (this.width - textWidth2) / 2, 20);
        hintText3.setDimensionsAndPosition(textWidth3, 10, (this.width - textWidth3) / 2, 30);
        hintText1.setAlpha(0.7f);
        hintText2.setAlpha(0.7f);
        hintText3.setAlpha(0.7f);
        this.addDrawableChild(hintText1);
        this.addDrawableChild(hintText2);
        this.addDrawableChild(hintText3);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        HudRenderer.renderEntityCountHUD(context, CountEntityEvent.defaultList, this.getModify());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        this.modifyX += (float) (deltaX)/MinecraftClient.getInstance().getWindow().getScaledWidth();
        this.modifyY += (float) (deltaY)/MinecraftClient.getInstance().getWindow().getScaledHeight();
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.modifyScale += (float) verticalAmount * 0.1f;
        // 限制縮放範圍在 1.0 到 20.0 之間
        modifyScale = Math.max(1.0f, Math.min(20.0f, modifyScale));
        return true;
    }

    @Override
    public void close() {
        coord.set(this.getModify());
        this.client.setScreen(parent);
    }
}