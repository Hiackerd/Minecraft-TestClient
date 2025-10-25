package net.testclient.mixins;

import net.testclient.TestClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // NoFall - Kein Fallschaden
        if (TestClient.isNoFallEnabled() && source == source.getWorld().getDamageSources().fall()) {
            cir.setReturnValue(false);
        }
        
        // Kill Other - One-Hit Kill
        if (TestClient.isKillEnabled() && ((Object)this) instanceof net.minecraft.entity.player.PlayerEntity) {
            LivingEntity entity = (LivingEntity) (Object) this;
            if (entity.getWorld().isClient) {
                entity.setHealth(0.0f);
            }
        }
    }
    
    @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
    private void onIsInvisible(CallbackInfoReturnable<Boolean> cir) {
        // Invisible - Unsichtbar für andere
        if (TestClient.isInvisibleEnabled() && ((Object)this) instanceof net.minecraft.client.network.ClientPlayerEntity) {
            cir.setReturnValue(true);
        }
    }
}