package dev.newty.fabrishot.event;

import dev.newty.fabrishot.capture.Dimension;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.nio.ByteBuffer;

/**
 * Callback for immediately after a framebuffer is captured, but before it is saved
 */
public interface FramebufferCaptureCallback {
    Event<FramebufferCaptureCallback> EVENT = EventFactory.createArrayBacked(FramebufferCaptureCallback.class, callbacks -> (captureDimensions, buffer) -> {
        for (FramebufferCaptureCallback callback : callbacks) {
            callback.onCapture(captureDimensions, buffer);
            buffer.rewind();
        }
    });

    /**
     * Called whenever a framebuffer is captured
     *
     * @param captureDimensions The dimensions of the framebuffer
     * @param buffer            A raw image buffer of three channel 8-bit RGB
     */
    void onCapture(Dimension captureDimensions, ByteBuffer buffer);
}
