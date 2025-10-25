package net.testclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class TestClient implements ClientModInitializer {
    
    // KeyBindings für Module
    public static KeyBinding flyKey, noFallKey, boatFlyKey, xrayKey, playerEspKey;
    public static KeyBinding mobEspKey, invisibleKey, speedKey, jetpackKey, placeAirKey;
    public static KeyBinding killKey, aimbotKey, diamondEspKey, loginKey;
    
    // Module States
    public static boolean flightEnabled = false;
    public static boolean noFallEnabled = false;
    public static boolean boatFlyEnabled = false;
    public static boolean xrayEnabled = false;
    public static boolean playerEspEnabled = false;
    public static boolean mobEspEnabled = false;
    public static boolean invisibleEnabled = false;
    public static boolean speedEnabled = false;
    public static boolean jetpackEnabled = false;
    public static boolean placeAirEnabled = false;
    public static boolean killEnabled = false;
    public static boolean aimbotEnabled = false;
    public static boolean diamondEspEnabled = false;
    public static boolean loggedIn = false;
    
    // Login System
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "979683599";
    private static boolean awaitingLogin = false;
    
    @Override
    public void onInitializeClient() {
        // Login Key
        loginKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.testclient.login", InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT, "category.testclient"
        ));
        
        // Module Keys
        flyKey = createKey("fly", GLFW.GLFW_KEY_G);
        noFallKey = createKey("nofall", GLFW.GLFW_KEY_N);
        boatFlyKey = createKey("boatfly", GLFW.GLFW_KEY_B);
        xrayKey = createKey("xray", GLFW.GLFW_KEY_X);
        playerEspKey = createKey("playeresp", GLFW.GLFW_KEY_P);
        mobEspKey = createKey("mobesp", GLFW.GLFW_KEY_M);
        invisibleKey = createKey("invisible", GLFW.GLFW_KEY_I);
        speedKey = createKey("speed", GLFW.GLFW_KEY_C);
        jetpackKey = createKey("jetpack", GLFW.GLFW_KEY_J);
        placeAirKey = createKey("placeair", GLFW.GLFW_KEY_K);
        killKey = createKey("kill", GLFW.GLFW_KEY_L);
        aimbotKey = createKey("aimbot", GLFW.GLFW_KEY_R);
        diamondEspKey = createKey("diamondesp", GLFW.GLFW_KEY_O);
        
        // Tick Handler
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            
            handleLoginSystem(client);
            if (loggedIn) {
                handleModules(client);
            }
        });
    }
    
    private KeyBinding createKey(String name, int key) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.testclient." + name, InputUtil.Type.KEYSYM,
            key, "category.testclient"
        ));
    }
    
    private void handleLoginSystem(net.minecraft.client.MinecraftClient client) {
        if (loginKey.wasPressed()) {
            if (!loggedIn) {
                if (!awaitingLogin) {
                    startLogin(client);
                } else {
                    completeLogin(client);
                }
            } else {
                logout(client);
            }
        }
    }
    
    private void startLogin(net.minecraft.client.MinecraftClient client) {
        client.player.sendMessage(Text.literal("§6➤ Login gestartet! Automatischer Login..."), false);
        awaitingLogin = true;
        // Auto-Login nach kurzer Zeit
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                completeLogin(client);
            } catch (InterruptedException e) {}
        }).start();
    }
    
    private void completeLogin(net.minecraft.client.MinecraftClient client) {
        loggedIn = true;
        awaitingLogin = false;
        client.player.sendMessage(Text.literal("§a✔ Erfolgreich eingeloggt als: §e" + USERNAME), false);
        client.player.sendMessage(Text.literal("§bVerfügbare Module aktiviert!"), false);
        showHelp(client);
    }
    
    private void logout(net.minecraft.client.MinecraftClient client) {
        loggedIn = false;
        resetAllModules(client);
        client.player.sendMessage(Text.literal("§c✘ Ausgeloggt - Alle Module deaktiviert"), false);
    }
    
    private void showHelp(net.minecraft.client.MinecraftClient client) {
        client.player.sendMessage(Text.literal("§6--- Cheat Menu ---"), false);
        client.player.sendMessage(Text.literal("§7Fly: §eG §7| NoFall: §eN §7| BoatFly: §eB"), false);
        client.player.sendMessage(Text.literal("§7XRay: §eX §7| PlayerESP: §eP §7| MobESP: §eM"), false);
        client.player.sendMessage(Text.literal("§7Invisible: §eI §7| Speed: §eC §7| Jetpack: §eJ"), false);
        client.player.sendMessage(Text.literal("§7PlaceAir: §eK §7| Kill: §eL §7| Aimbot: §eR"), false);
        client.player.sendMessage(Text.literal("§7DiamondESP: §eO §7| Login: §eRIGHT-SHIFT"), false);
    }
    
    private void handleModules(net.minecraft.client.MinecraftClient client) {
        // Fly
        if (flyKey.wasPressed()) {
            flightEnabled = !flightEnabled;
            client.player.sendMessage(Text.literal("Fly: " + (flightEnabled ? "§aON" : "§cOFF")), true);
        }
        
        // NoFall
        if (noFallKey.wasPressed()) {
            noFallEnabled = !noFallEnabled;
            client.player.sendMessage(Text.literal("NoFall: " + (noFallEnabled ? "§aON" : "§cOFF")), true);
        }
        
        // BoatFly
        if (boatFlyKey.wasPressed()) {
            boatFlyEnabled = !boatFlyEnabled;
            client.player.sendMessage(Text.literal("BoatFly: " + (boatFlyEnabled ? "§aON" : "§cOFF")), true);
        }
        
        // XRay
        if (xrayKey.wasPressed()) {
            xrayEnabled = !xrayEnabled;
            client.worldRenderer.reload();
            client.player.sendMessage(Text.literal("XRay: " + (xrayEnabled ? "§aON" : "§cOFF")), true);
        }
        
        // PlayerESP
        if (playerEspKey.wasPressed()) {
            playerEspEnabled = !playerEspEnabled;
            client.player.sendMessage(Text.literal("PlayerESP: " + (playerEspEnabled ? "§aON" : "§cOFF")), true);
        }
        
        // MobESP
        if (mobEspKey.wasPressed()) {
            mobEspEnabled = !mobEspEnabled;
            client.player.sendMessage(Text.literal("MobESP: " + (mobEspEnabled ? "§aON" : "§cOFF")), true);
        }
        
        // Invisible
        if (invisibleKey.wasPressed()) {
            invisibleEnabled = !invisibleEnabled;
            client.player.sendMessage(Text.literal("Invisible: " + (invisibleEnabled ? "§aON" : "§cOFF")), true);
        }
        
        // Speed
        if (speedKey.wasPressed()) {
            speedEnabled = !speedEnabled;
            client.player.sendMessage(Text.literal("Speed: " + (speedEnabled ? "§aON" : "§cOFF")), true);
        }
        
        // Jetpack
        if (jetpackKey.wasPressed()) {
            jetpackEnabled = !jetpackEnabled;
            client.player.sendMessage(Text.literal("Jetpack: " + (jetpackEnabled ? "§aON" : "§cOFF")), true);
        }
        
        // PlaceAir
        if (placeAirKey.wasPressed()) {
            placeAirEnabled = !placeAirEnabled;
            client.player.sendMessage(Text.literal("PlaceAir: " + (placeAirEnabled ? "§aON" : "§cOFF")), true);
        }
        
        // Kill
        if (killKey.wasPressed()) {
            killEnabled = !killEnabled;
            client.player.sendMessage(Text.literal("Kill: " + (killEnabled ? "§aON" : "§cOFF")), true);
        }
        
        // Aimbot
        if (aimbotKey.wasPressed()) {
            aimbotEnabled = !aimbotEnabled;
            client.player.sendMessage(Text.literal("Aimbot: " + (aimbotEnabled ? "§aON" : "§cOFF")), true);
        }
        
        // DiamondESP
        if (diamondEspKey.wasPressed()) {
            diamondEspEnabled = !diamondEspEnabled;
            client.player.sendMessage(Text.literal("DiamondESP: " + (diamondEspEnabled ? "§aON" : "§cOFF")), true);
        }
        
        applyActiveEffects(client);
    }
    
    private void applyActiveEffects(net.minecraft.client.MinecraftClient client) {
        if (flightEnabled) {
            client.player.getAbilities().flying = true;
            client.player.getAbilities().allowFlying = true;
        }
        
        if (speedEnabled) {
            client.player.setMovementSpeed(0.2f);
        }
        
        if (jetpackEnabled && client.options.jumpKey.isPressed()) {
            client.player.addVelocity(0, 0.5, 0);
        }
    }
    
    private void resetAllModules(net.minecraft.client.MinecraftClient client) {
        flightEnabled = noFallEnabled = boatFlyEnabled = xrayEnabled = playerEspEnabled = false;
        mobEspEnabled = invisibleEnabled = speedEnabled = jetpackEnabled = placeAirEnabled = false;
        killEnabled = aimbotEnabled = diamondEspEnabled = false;
        
        if (client.player != null) {
            client.player.getAbilities().flying = false;
            client.player.getAbilities().allowFlying = false;
            client.worldRenderer.reload();
        }
    }
    
    // Getter für Mixins
    public static boolean isFlightEnabled() { return loggedIn && flightEnabled; }
    public static boolean isNoFallEnabled() { return loggedIn && noFallEnabled; }
    public static boolean isBoatFlyEnabled() { return loggedIn && boatFlyEnabled; }
    public static boolean isXrayEnabled() { return loggedIn && xrayEnabled; }
    public static boolean isPlayerEspEnabled() { return loggedIn && playerEspEnabled; }
    public static boolean isMobEspEnabled() { return loggedIn && mobEspEnabled; }
    public static boolean isInvisibleEnabled() { return loggedIn && invisibleEnabled; }
    public static boolean isSpeedEnabled() { return loggedIn && speedEnabled; }
    public static boolean isJetpackEnabled() { return loggedIn && jetpackEnabled; }
    public static boolean isPlaceAirEnabled() { return loggedIn && placeAirEnabled; }
    public static boolean isKillEnabled() { return loggedIn && killEnabled; }
    public static boolean isAimbotEnabled() { return loggedIn && aimbotEnabled; }
    public static boolean isDiamondEspEnabled() { return loggedIn && diamondEspEnabled; }
    public static boolean isLoggedIn() { return loggedIn; }
}