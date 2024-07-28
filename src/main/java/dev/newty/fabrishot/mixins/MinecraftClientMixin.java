package dev.newty.fabrishot.mixins;

import dev.newty.fabrishot.Fabrishot;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;render(Lnet/minecraft/client/render/RenderTickCounter;Z)V"))
    private void preRender(CallbackInfo callbackInfo) {
        Fabrishot.onRenderPreOrPost();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;render(Lnet/minecraft/client/render/RenderTickCounter;Z)V", shift = At.Shift.AFTER))
    private void postRender(CallbackInfo callbackInfo) {
        Fabrishot.onRenderPreOrPost();
    }

    @ModifyArg(method = "onResolutionChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setScaleFactor(D)V"), index = 0)
    private double adjustScaleFactor(double d) {
        return Fabrishot.getScaleFactor() * d;
    }
}
