package com.gahaha.entitycount.client.screen;

import com.gahaha.entitycount.client.config.ClothConfigIntergration;
import com.gahaha.entitycount.client.config.ConfigManager;
import com.gahaha.entitycount.client.render.HudRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static com.gahaha.entitycount.client.event.CountEntityEvent.defaultMap;

@Environment(EnvType.CLIENT)
public class EntityCountConfigScreen extends Screen {
    private final Screen parent;
    private boolean dragging = false;
    private boolean resizing = false;
    public EntityCountConfigScreen(Screen parent) {
        super(Text.literal("EntityCount Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        TextWidget ga = new TextWidget(Text.literal("Gahaha"), this.textRenderer);
        ga.setDimensionsAndPosition(40, 20, 70, this.height-80);
        this.addDrawableChild(ga);
        //進階設置
        this.addDrawableChild(ButtonWidget.builder(Text.literal("進階設置"), (b) -> {
            assert this.client != null;
            this.client.setScreen(new ClothConfigIntergration().getModConfigScreenFactory().create(this));
        }).dimensions(10, this.height - 80, 60, 20).build());

        // 開關按鈕
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.onOrOff(ConfigManager.getShowEntitiesCount()), (b) -> {
            ConfigManager.setShowEntitiesCount(!ConfigManager.getShowEntitiesCount());
            b.setMessage(ScreenTexts.onOrOff(ConfigManager.getShowEntitiesCount()));
        }).dimensions(10, this.height - 50, 40, 20).build());

        // 縮放
        /*
        TextFieldWidget scale = new TextFieldWidget(this.textRenderer, 60, this.height - 50, 20, 20, Text.literal("大小"));
        scale.setMaxLength(3);
        scale.setSuggestion("0.1 - 5.0");
        scale.setText(String.format("%.1f", (ConfigManager.scale)));
        scale.setChangedListener(s -> {
            ConfigManager.scale = ParseScale.parseScaleString(s);
        });
        this.addDrawableChild(scale);
        */

        // 重設全部
        this.addDrawableChild(ButtonWidget.builder(Text.literal("重設設定"), (b) -> {
            ConfigManager.reset();
        }).dimensions(this.width / 2 - 100, this.height - 50, 80, 20).build());

        /*
        // 文字顏色輸入框 (預期格式: #AARRGGBB)
        TextFieldWidget textColor = new TextFieldWidget(this.textRenderer, this.width / 2 - 10, this.height - 50, 80, 20, Text.literal("文字顏色"));
        textColor.setMaxLength(9);
        textColor.setSuggestion("文字顏色 #%08X");
        textColor.setText(String.format("#%08X", (ConfigManager.getTextColor())));
        textColor.setChangedListener(s -> {
            ConfigManager.setTextColor(ParseColor.parseColorARGBString(s));
        });
        this.addDrawableChild(textColor);

        // 背景色輸入框 (預期格式: #AARRGGBB)
        TextFieldWidget backgroundColor = new TextFieldWidget(this.textRenderer, this.width / 2 + 80, this.height - 50, 80, 20, Text.literal("BackgroundColor"));
        backgroundColor.setMaxLength(9);
        backgroundColor.setText(String.format("#%08X", ConfigManager.getBackgroundColor()));
        backgroundColor.setChangedListener(s -> {
            ConfigManager.setBackgroundColor(ParseColor.parseColorARGBString(s));
        });
        this.addDrawableChild(backgroundColor);
        */
        // 返回按鈕
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (b) -> {
            this.close();
        }).dimensions(this.width - 60, 20, 40, 20).build());
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 背景顯示遊戲畫面，可省略super.render()預設背景
        // 簡易繪製代表 EntityCount 顯示框
        //context.drawBorder(ConfigManager.x, ConfigManager.y, 100, 50, ConfigManager.textColor);
        super.render(context, mouseX, mouseY, delta);
        //context.fill(this.width / 2 - 10, this.height - 70,this.width / 2 + 10 , this.height - 50, ConfigManager.getTextColor());
        //context.fill(this.width / 2 + 80, this.height - 70, this.width / 2 + 100, this.height - 50, ConfigManager.getBackgroundColor());
        HudRenderer.renderDefault(context, defaultMap.entrySet().stream().toList());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isInsideBox((int)mouseX, (int)mouseY, defaultMap.size())) {
            // 開始拖曳
            this.dragging = true;
            return true;
        } else if (isInsideCorner((int)mouseX, (int)mouseY)) {
            // 開始縮放
            this.resizing = true;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.dragging = false;
        this.resizing = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.dragging) {
            ConfigManager.setX(ConfigManager.getX() + (float) (deltaX)/MinecraftClient.getInstance().getWindow().getScaledWidth());
            ConfigManager.setY(ConfigManager.getY() + (float) (deltaY)/MinecraftClient.getInstance().getWindow().getScaledHeight());
            return true;
        } else if (this.resizing) {
            // 調整縮放倍數的簡易示範
            ConfigManager.setScale(ConfigManager.getScale() + (float) deltaX * 0.1f);
            if (ConfigManager.getScale() < 1.0f) ConfigManager.setScale(10.0f);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private boolean isInsideBox(int mx, int my, int mapSize) {
        int sh = MinecraftClient.getInstance().getWindow().getScaledHeight();
        int sw = MinecraftClient.getInstance().getWindow().getScaledWidth();
        return mx >= ConfigManager.getX()*sw && mx <= ConfigManager.getX()*sw + (int)(70* ConfigManager.getScale()/10) + 3
               && my >= ConfigManager.getY()*sh && my < ConfigManager.getY()*sh + (int)(11*mapSize* ConfigManager.getScale()/10) + 3;

    }

    private boolean isInsideCorner(int mx, int my) {
        return mx >= ConfigManager.getX() + 90 && mx < ConfigManager.getX() + 100
                && my >= ConfigManager.getY() + 40 && my < ConfigManager.getY() + 50;
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(parent);
    }
}