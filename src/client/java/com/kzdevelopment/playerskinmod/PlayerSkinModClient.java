package com.kzdevelopment.playerskinmod;

import com.kzdevelopment.playerskinmod.gui.PlayerSkinConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PlayerSkinModClient implements ClientModInitializer {
    public static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        PlayerOverrideManager.load();

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.playerskinmod.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                client.setScreen(new PlayerSkinConfigScreen());
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("skinoverride")
                        .executes(ctx -> {
                            MinecraftClient.getInstance().setScreen(new PlayerSkinConfigScreen());
                            return 1;
                        }))
        );
    }
}
