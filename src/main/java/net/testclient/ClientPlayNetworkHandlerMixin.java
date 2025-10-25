package net.testclient.mixins;

import net.testclient.TestClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        // Use Server Commands - Automatisch / vor Commands
        if (TestClient.isLoggedIn() && message.startsWith(".")) {
            String command = message.substring(1);
            // Hier könnte man Commands automatisch ausführen
        }
    }
    
    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(net.minecraft.network.Packet<?> packet, CallbackInfo ci) {
        // Anti-Cheat Bypass - Packets modifizieren
        if (packet instanceof ChatMessageC2SPacket) {
            ChatMessageC2SPacket chatPacket = (ChatMessageC2SPacket) packet;
            String message = chatPacket.chatMessage();
            
            // Automatische Command-Ausführung
            if (TestClient.isLoggedIn() && message.startsWith(".")) {
                String command = "/" + message.substring(1);
                // Command automatisch senden
                net.minecraft.client.MinecraftClient.getInstance().getNetworkHandler()
                    .sendChatCommand(command.substring(1));
                ci.cancel();
            }
        }
    }
}