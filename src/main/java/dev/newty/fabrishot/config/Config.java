package dev.newty.fabrishot.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.newty.fabrishot.Fabrishot;
import dev.newty.fabrishot.config.controller.ShortFieldControllerBuilder;
import dev.newty.fabrishot.config.nametags.NametagVisibility;
import dev.newty.fabrishot.config.nametags.OwnNametagVisibility;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import java.lang.reflect.Field;
import java.nio.file.Path;

public class Config {
    // Screenshot

    @SerialEntry
    public short captureWidth = 3840;

    @SerialEntry
    public short captureHeight = 2160;

    @SerialEntry
    public short captureDelay = 3;

    @SerialEntry
    public FileFormat captureFileFormat = FileFormat.PNG;

    // GUI

    @SerialEntry
    public boolean disableGuiScaling = false;

    @SerialEntry
    public boolean hideHud = false;

    @SerialEntry
    public NametagVisibility nametagVisibility = NametagVisibility.All;

    @SerialEntry
    public OwnNametagVisibility ownNametagVisibility = OwnNametagVisibility.Screenshot;

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(Fabrishot.MOD_ID + ".json5");
    private static final ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(Identifier.of(Fabrishot.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(CONFIG_PATH)
                    .setJson5(true)
                    .build()
            )
            .build();

    private static Text getText(String key) {
        return Text.translatable("fabrishot.config." + key);
    }

    private static BooleanControllerBuilder booleanController(Option<Boolean> opt) {
        return BooleanControllerBuilder.create(opt)
                .formatValue(val -> getText(val ? "yes" : "no"))
                .coloured(true);
    }

    private static ShortFieldControllerBuilder positiveShortController(Option<Short> opt) {
        return ShortFieldControllerBuilder.create(opt)
                .min((short) 0);
    }

    private static ShortFieldControllerBuilder screenshotSizeController(Option<Short> opt) {
        return positiveShortController(opt)
                .formatValue(val -> Text.literal(val + "px"));
    }

    private static <T extends Enum<T>> ControllerBuilder<T> enumController(Class<T> enumClass, Option<T> opt) {
        return EnumControllerBuilder.create(opt)
                .enumClass(enumClass);
    }

    private static <T> Option.Builder<T> createOption(String key, boolean hasDescription, T defaultValue, String fieldKey) {
        Option.Builder<T> opt = Option.<T>createBuilder()
                .name(getText(key));

        try {
            Field field = Config.class.getField(fieldKey);
            Config obj = get();
            opt = opt.binding(
                    defaultValue,
                    () -> {
                        try {
                            return (T) field.get(obj);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }, val -> {
                        try {
                            field.set(obj, val);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        HANDLER.save();
                    });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if (hasDescription) {
            opt = opt.description(OptionDescription.createBuilder()
                    .text(Text.translatable("fabrishot.config" + key + ".description"))
                    .build());
        }

        return opt;
    }

    public static void load() {
        HANDLER.load();
    }

    public static Config get() {
        return HANDLER.instance();
    }

    public static Screen createScreen(@Nullable Screen parent) {
        Config config = get();

        // screenshot category
        Option<Short> screenshotWidth = Config.<Short>createOption("screenshot.size.width", false, (short) 3840, "captureWidth")
                .controller(Config::screenshotSizeController)
                .build();
        Option<Short> screenshotHeight = Config.<Short>createOption("screenshot.size.height", false, (short) 2160, "captureHeight")
                .controller(Config::screenshotSizeController)
                .build();

        Option<Short> captureDelay = Config.<Short>createOption("screenshot.delay", true, (short) 3, "captureDelay")
                .controller(Config::positiveShortController)
                .build();

        Option<FileFormat> fileFormat = Config.<FileFormat>createOption("screenshot.format", false, FileFormat.PNG, "captureFileFormat")
                .controller(opt -> enumController(FileFormat.class, opt))
                .build();

        // GUI category
        Option<Boolean> guiScaling = Config.<Boolean>createOption("gui.scaling", false, false, "disableGuiScaling")
                .controller(Config::booleanController)
                .build();

        Option<Boolean> hideHud = Config.<Boolean>createOption("gui.hide_hud", false,false, "hideHud")
                .controller(Config::booleanController)
                .build();

        Option<NametagVisibility> nametagVisibility = Config.<NametagVisibility>createOption("gui.nametags.visibility", false, NametagVisibility.All, "nametagVisibility")
                .controller(opt -> enumController(NametagVisibility.class, opt))
                .build();
        Option<OwnNametagVisibility> ownNametagVisibility = Config.<OwnNametagVisibility>createOption("gui.nametags.own", false, OwnNametagVisibility.Screenshot, "ownNametagVisibility")
                .controller(opt -> enumController(OwnNametagVisibility.class, opt))
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(getText("title"))

                .category(ConfigCategory.createBuilder()
                        .name(getText("screenshot"))
                        .group(OptionGroup.createBuilder()
                                .name(getText("screenshot.size"))
                                .option(screenshotWidth)
                                .option(screenshotHeight)
                                .build()
                        )
                        .option(captureDelay)
                        .option(fileFormat)
                        .build()
                )

                .category(ConfigCategory.createBuilder()
                        .name(getText("gui"))
                        .option(guiScaling)
                        .option(hideHud)
                        .group(OptionGroup.createBuilder()
                                .name(getText("gui.nametags"))
                                .option(nametagVisibility)
                                .option(ownNametagVisibility)
                                .build()
                        )
                        .build()
                )

                .build()
                .generateScreen(parent);
    }
}
