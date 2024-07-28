package dev.newty.fabrishot.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.nio.file.Path;

/**
 * Callback for immediately after a screenshot is saved
 */
public interface ScreenshotSaveCallback {
    Event<ScreenshotSaveCallback> EVENT = EventFactory.createArrayBacked(ScreenshotSaveCallback.class, callbacks -> path -> {
        for (ScreenshotSaveCallback callback : callbacks) {
            callback.onSaved(path);
        }
    });

    void onSaved(Path path);
}
