package dev.newty.fabrishot.config;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClothConfigBridge implements ConfigScreenFactory<Screen> {
    @Override
    public Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(Text.translatable("fabrishot.config.title"))
                .setSavingRunnable(Config::save)
                .setParentScreen(parent);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("fabrishot.config.category"));

        category.addEntry(entryBuilder.startStrField(Text.translatable("fabrishot.config.custom_file_name"), Config.CUSTOM_FILE_NAME)
                .setTooltip(Text.translatable("fabrishot.config.custom_file_name.tooltip"))
                .setDefaultValue("huge_%time%")
                .setSaveConsumer(b -> Config.CUSTOM_FILE_NAME = b)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("fabrishot.config.override_screenshot_key"), Config.OVERRIDE_SCREENSHOT_KEY)
                .setDefaultValue(false)
                .setSaveConsumer(b -> Config.OVERRIDE_SCREENSHOT_KEY = b)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("fabrishot.config.hide_hud"), Config.HIDE_HUD)
                .setDefaultValue(false)
                .setSaveConsumer(b -> Config.HIDE_HUD = b)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("fabrishot.config.save_file"), Config.SAVE_FILE)
                .setDefaultValue(true)
                .setSaveConsumer(b -> Config.SAVE_FILE = b)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("fabrishot.config.disable_gui_scaling"), Config.DISABLE_GUI_SCALING)
                .setDefaultValue(false)
                .setSaveConsumer(b -> Config.DISABLE_GUI_SCALING = b)
                .build());

        IntegerListEntry width = entryBuilder.startIntField(Text.translatable("fabrishot.config.width"), Config.CAPTURE_WIDTH)
                .setDefaultValue(3840)
                .setMin(1)
                .setMax(Math.min(65535, RenderSystem.maxSupportedTextureSize()))
                .setSaveConsumer(i -> Config.CAPTURE_WIDTH = i)
                .build();
        category.addEntry(width);

        IntegerListEntry height = entryBuilder.startIntField(Text.translatable("fabrishot.config.height"), Config.CAPTURE_HEIGHT)
                .setDefaultValue(2160)
                .setMin(1)
                .setMax(Math.min(65535, RenderSystem.maxSupportedTextureSize()))
                .setSaveConsumer(i -> Config.CAPTURE_HEIGHT = i)
                .build();
        category.addEntry(height);

        category.addEntry(new ScalingPresetEntry(220, width, height));

        category.addEntry(entryBuilder.startIntField(Text.translatable("fabrishot.config.delay"), Config.CAPTURE_DELAY)
                .setTooltip(Text.translatable("fabrishot.config.delay.tooltip"))
                .setDefaultValue(3)
                .setMin(3)
                .setSaveConsumer(i -> Config.CAPTURE_DELAY = i)
                .build());

        category.addEntry(entryBuilder.startEnumSelector(Text.translatable("fabrishot.config.file_format"), FileFormat.class, Config.CAPTURE_FILE_FORMAT)
                .setDefaultValue(FileFormat.PNG)
                .setSaveConsumer(t -> Config.CAPTURE_FILE_FORMAT = t)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("fabrishot.config.nametags"), Config.SHOW_NAMETAGS)
                .setDefaultValue(true)
                .setSaveConsumer(b -> Config.SHOW_NAMETAGS = b)
                .build());

        return builder.build();
    }
}
