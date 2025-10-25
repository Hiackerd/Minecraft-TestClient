package net.testclient.mixins;

import net.testclient.TestClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        
        // Flight aktivieren
        if (TestClient.isFlightEnabled()) {
            player.getAbilities().flying = true;
            player.getAbilities().allowFlying = true;
        }
        
        // Jetpack
        if (TestClient.isJetpackEnabled() && player.getWorld().isClient) {
            if (player.input.jumping) {
                player.addVelocity(0, 0.3, 0);
            }
        }
    }
    
    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void onTickMovement(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        
        // Speed
        if (TestClient.isSpeedEnabled()) {
            player.setMovementSpeed(0.15f * 2.5f); // 2.5x Speed
        }
        
        // Boat Fly
        if (TestClient.isBoatFlyEnabled() && player.getVehicle() != null) {
            player.getVehicle().setNoGravity(true);
            if (player.input.jumping) {
                player.getVehicle().addVelocity(0, 0.3, 0);
            }
        }
    }
    
    @Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
    private void onIsSneaking(CallbackInfoReturnable<Boolean> cir) {
        // Place in Air - erlaube Sneak in Luft
        if (TestClient.isPlaceAirEnabled()) {
            cir.setReturnValue(false);
        }
    }
}