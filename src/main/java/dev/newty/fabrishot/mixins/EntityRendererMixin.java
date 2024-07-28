package dev.newty.fabrishot.mixins;

import dev.newty.fabrishot.Fabrishot;
import dev.newty.fabrishot.config.Config;
import dev.newty.fabrishot.config.NametagVisibility;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void hideNametag(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (Fabrishot.isInCapture()) {
            boolean isPlayer = entity instanceof PlayerEntity || entity instanceof ArmorStandEntity;

            switch (Config.NAMETAG_VISIBILITY) {
                case NametagVisibility.None -> ci.cancel();
                case NametagVisibility.Entities -> {
                    if (isPlayer) ci.cancel();
                }
                case NametagVisibility.Players -> {
                    if (!isPlayer) ci.cancel();
                }
            }
        }
    }
}
