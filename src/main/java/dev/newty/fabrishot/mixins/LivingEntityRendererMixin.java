package dev.newty.fabrishot.mixins;

import dev.newty.fabrishot.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void viewOwnLabel(LivingEntity entity, CallbackInfoReturnable<Boolean> ci) {
        if (Config.SHOWN_OWN_NAMETAG && entity == MinecraftClient.getInstance().cameraEntity) ci.setReturnValue(MinecraftClient.isHudEnabled());
    }
}
