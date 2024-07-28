package dev.newty.fabrishot.mixins;

import dev.newty.fabrishot.Fabrishot;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/ScreenshotRecorder;saveScreenshot(Ljava/io/File;Lnet/minecraft/client/gl/Framebuffer;Ljava/util/function/Consumer;)V"), cancellable = true)
    private void onScreenshot(CallbackInfo callbackInfo) {
        Fabrishot.startCapture();
        callbackInfo.cancel();
    }
}
