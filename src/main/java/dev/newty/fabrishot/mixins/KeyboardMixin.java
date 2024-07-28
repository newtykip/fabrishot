package dev.newty.fabrishot.mixins;

import dev.newty.fabrishot.Fabrishot;
import dev.newty.fabrishot.config.Config;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;screenshotKey:Lnet/minecraft/client/option/KeyBinding;"))
    private void preScreenshot(long window, int key, int scancode, int i, int j, CallbackInfo callbackInfo) {
        // Injecting here allows us to work inside other menus
        if (Fabrishot.SCREENSHOT_BINDING.matchesKey(key, scancode)) {
            Fabrishot.startCapture();
        }
    }

    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/ScreenshotRecorder;saveScreenshot(Ljava/io/File;Lnet/minecraft/client/gl/Framebuffer;Ljava/util/function/Consumer;)V"), cancellable = true)
    private void onScreenshot(CallbackInfo callbackInfo) {
        if (Config.OVERRIDE_SCREENSHOT_KEY) {
            Fabrishot.startCapture();
            callbackInfo.cancel();
        }
    }
}
