package dev.newty.fabrishot.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class InstallClothConfigScreen extends Screen {
    private static final Text INSTALL_CLOTH_CONFIG = Text.of("You must install Cloth Config");
    private final Screen parent;

    public InstallClothConfigScreen(Screen parent) {
        super(NarratorManager.EMPTY);
        this.parent = parent;
    }

    @Override
    protected void init() {
        addDrawableChild(ButtonWidget.builder(ScreenTexts.OK, buttonWidget -> client.setScreen(parent))
                .position(width / 2 - 100, height - 52)
                .size(200, 20)
                .build());
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);

        int textWidth = client.textRenderer.getWidth(INSTALL_CLOTH_CONFIG);
        drawContext.drawTextWithShadow(client.textRenderer, INSTALL_CLOTH_CONFIG, (width - textWidth) / 2, height / 3, 0xFF0000);

        super.render(drawContext, mouseX, mouseY, delta);
    }
}
