package cc.gahaha.entitycount.screen;

import cc.gahaha.entitycount.event.CountEntityEvent;
import cc.gahaha.entitycount.render.HudRenderer;
import cc.gahaha.entitycount.utils.AtomicVec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntityCountConfigScreen extends Screen {
    private final @Nullable Screen parent;
    private final AtomicVec3d coord;
    private float modifyX;
    private float modifyY;
    private float modifyScale;

    public EntityCountConfigScreen(@Nullable Screen parent, AtomicVec3d coord) {
        super(Component.literal("EntityCount Config"));
        this.parent = parent;
        this.setModify(coord.get());
        this.coord = coord;
    }

    public Vec3 getModify() {
        return new Vec3((double)this.modifyX, (double)this.modifyY, (double)this.modifyScale);
    }

    public void setModify(Vec3 vec3d) {
        this.modifyX = (float)vec3d.x;
        this.modifyY = (float)vec3d.y;
        this.modifyScale = (float)vec3d.z;
    }

    @Override
    protected void init() {
        Component line1 = Component.translatable("entitycount.config.set_coord_screen.hint1");
        Component line2 = Component.translatable("entitycount.config.set_coord_screen.hint2");
        Component line3 = Component.translatable("entitycount.config.set_coord_screen.hint3");
        StringWidget hintText1 = new StringWidget(line1, this.font);
        StringWidget hintText2 = new StringWidget(line2, this.font);
        StringWidget hintText3 = new StringWidget(line3, this.font);
        int textWidth1 = this.font.width(line1);
        int textWidth2 = this.font.width(line2);
        int textWidth3 = this.font.width(line3);
        hintText1.setRectangle(textWidth1, 10, (this.width - textWidth1) / 2, 10);
        hintText2.setRectangle(textWidth2, 10, (this.width - textWidth2) / 2, 20);
        hintText3.setRectangle(textWidth3, 10, (this.width - textWidth3) / 2, 30);
        hintText1.setAlpha(0.7F);
        hintText2.setAlpha(0.7F);
        hintText3.setAlpha(0.7F);
        this.addRenderableWidget(hintText1);
        this.addRenderableWidget(hintText2);
        this.addRenderableWidget(hintText3);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
        HudRenderer.renderEntityCountHUD(context, CountEntityEvent.defaultList, this.getModify());
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
        this.modifyX += (float)offsetX / (float)Minecraft.getInstance().getWindow().getGuiScaledWidth();
        this.modifyY += (float)offsetY / (float)Minecraft.getInstance().getWindow().getGuiScaledHeight();
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.modifyScale += (float)verticalAmount * 0.1F;
        this.modifyScale = Math.clamp(this.modifyScale, 1.0F, 20.0F);
        return true;
    }

    @Override
    public void onClose() {
        this.coord.set(this.getModify());
        this.minecraft.gui.setScreen(this.parent);
    }
}