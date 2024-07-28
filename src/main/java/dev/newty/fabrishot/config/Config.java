package dev.newty.fabrishot.config;

import dev.newty.fabrishot.Fabrishot;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    public static String CUSTOM_FILE_NAME = "huge_%time%";
    public static boolean OVERRIDE_SCREENSHOT_KEY = false;
    public static boolean HIDE_HUD = false;
    public static NametagVisibility NAMETAG_VISIBILITY = NametagVisibility.All;
    public static boolean SHOWN_OWN_NAMETAG = true;
    public static boolean SAVE_FILE = true;
    public static boolean DISABLE_GUI_SCALING = false;
    public static int CAPTURE_WIDTH = 3840;
    public static int CAPTURE_HEIGHT = 2160;
    public static int CAPTURE_DELAY = 3;
    public static FileFormat CAPTURE_FILE_FORMAT = FileFormat.PNG;

    private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("fabrishot.properties");

    static {
        try (BufferedReader reader = Files.newBufferedReader(CONFIG)) {
            Properties properties = new Properties();
            properties.load(reader);

            Config.CUSTOM_FILE_NAME = properties.getProperty("custom_file_name", "huge_%time%");
            Config.OVERRIDE_SCREENSHOT_KEY = Boolean.parseBoolean(properties.getProperty("override_screenshot_key"));
            Config.HIDE_HUD = Boolean.parseBoolean(properties.getProperty("hide_hud"));
            Config.NAMETAG_VISIBILITY = NametagVisibility.valueOf(properties.getProperty("nametag_visibility"));
            Config.SHOWN_OWN_NAMETAG = Boolean.parseBoolean(properties.getProperty("show_own_nametag"));
            Config.SAVE_FILE = Boolean.parseBoolean(properties.getProperty("save_file"));
            Config.DISABLE_GUI_SCALING = Boolean.parseBoolean(properties.getProperty("disable_gui_scaling"));
            Config.CAPTURE_WIDTH = Integer.parseInt(properties.getProperty("width"));
            Config.CAPTURE_HEIGHT = Integer.parseInt(properties.getProperty("height"));
            Config.CAPTURE_DELAY = Integer.parseInt(properties.getProperty("delay"));
            Config.CAPTURE_FILE_FORMAT = FileFormat.valueOf(properties.getProperty("file_format"));
        } catch (Exception ignored) {
            save();
        }
    }

    static void save() {
        Properties properties = new Properties();
        properties.put("custom_file_name", Config.CUSTOM_FILE_NAME);
        properties.put("override_screenshot_key", String.valueOf(Config.OVERRIDE_SCREENSHOT_KEY));
        properties.put("hide_hud", String.valueOf(Config.HIDE_HUD));
        properties.put("save_file", String.valueOf(Config.SAVE_FILE));
        properties.put("disable_gui_scaling", String.valueOf(Config.DISABLE_GUI_SCALING));
        properties.put("width", String.valueOf(Config.CAPTURE_WIDTH));
        properties.put("height", String.valueOf(Config.CAPTURE_HEIGHT));
        properties.put("delay", String.valueOf(Config.CAPTURE_DELAY));
        properties.put("file_format", String.valueOf(Config.CAPTURE_FILE_FORMAT));

        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG)) {
            properties.store(writer, "Fabrishot screenshot config");
        } catch (IOException exception) {
            LogManager.getLogger(Fabrishot.class).error(exception.getMessage(), exception);
        }
    }
}
