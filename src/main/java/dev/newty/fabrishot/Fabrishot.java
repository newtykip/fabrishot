package dev.newty.fabrishot;

import dev.newty.fabrishot.capture.CaptureTask;
import dev.newty.fabrishot.config.Config;
import dev.newty.fabrishot.event.ScreenshotSaveCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Fabrishot implements ClientModInitializer {
    public static final KeyBinding SCREENSHOT_BINDING = new KeyBinding(
            "key.fabrishot.screenshot",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F9,
            "key.categories.misc");

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static CaptureTask task;

    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(SCREENSHOT_BINDING);
        ScreenshotSaveCallback.EVENT.register(path -> Fabrishot.printFileLink(path.toFile()));
    }

    private static void printFileLink(File file) {
        Text text = Text.literal(file.getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath())));
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.translatable("screenshot.success", text)));
    }

    public static void startCapture() {
        if (task == null) {
            task = new CaptureTask(getScreenshotFile(MinecraftClient.getInstance()));
        }
    }

    public static void onRenderPreOrPost() {
        if (task != null && task.onRenderTick()) {
            task = null;
        }
    }

    private static Path getScreenshotFile(MinecraftClient client) {
        Path dir = client.runDirectory.toPath().resolve("screenshots");

        try {
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        String world = "";

        if (client.getServer() != null) {
            world = client.getServer().getSaveProperties().getLevelName();
        } else if (client.getCurrentServerEntry() != null) {
            world = client.getCurrentServerEntry().name;
        }

        Path file;
        String prefix = Config.CUSTOM_FILE_NAME
                .replace("%time%", DATE_FORMAT.format(new Date()))
                .replace("%world%", world);

        // loop though suffixes while the file exists
        int i = 1;

        do {
            file = dir.resolve(prefix + (i++ == 1 ? "" : "_" + i) + '.' + Config.CAPTURE_FILE_FORMAT.name().toLowerCase(Locale.ROOT));
        } while (Files.exists(file));

        return file;
    }

    public static float getScaleFactor() {
        if (task != null) {
            return task.getScaleFactor();
        } else {
            return 1;
        }
    }

    public static boolean isInCapture() {
        return task != null;
    }
}
