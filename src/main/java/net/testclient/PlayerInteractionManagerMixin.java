package net.testclient.mixins;

import net.testclient.TestClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class PlayerInteractionManagerMixin {
    
    @Inject(method = "getReachDistance", at = @At("HEAD"), cancellable = true)
    private void onGetReachDistance(CallbackInfoReturnable<Float> cir) {
        // Erweiterte Reichweite für alle Module
        if (TestClient.isLoggedIn()) {
            cir.setReturnValue(10.0f);
        }
    }
    
    @Inject(method = "hasExtendedReach", at = @At("HEAD"), cancellable = true)
    private void onHasExtendedReach(CallbackInfoReturnable<Boolean> cir) {
        if (TestClient.isLoggedIn()) {
            cir.setReturnValue(true);
        }
    }
    
    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    private void onAttackBlock(CallbackInfoReturnable<Boolean> cir) {
        // Place in Air - Blöcke in Luft platzieren
        if (TestClient.isPlaceAirEnabled()) {
            cir.setReturnValue(true);
        }
    }
}