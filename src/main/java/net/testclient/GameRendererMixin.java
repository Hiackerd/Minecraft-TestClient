package net.testclient.mixins;

import net.testclient.TestClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        // XRay - Chunks neu laden bei Aktivierung
        if (TestClient.isXrayEnabled()) {
            // Hier würde XRay Shader/Logik implementiert werden
        }
    }
    
    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void onBobView(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        // Kein Screen Bobbing bei Speed
        if (TestClient.isSpeedEnabled()) {
            ci.cancel();
        }
    }
}