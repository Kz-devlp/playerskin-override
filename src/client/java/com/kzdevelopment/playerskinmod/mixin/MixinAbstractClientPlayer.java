package com.kzdevelopment.playerskinmod.mixin;

import com.kzdevelopment.playerskinmod.PlayerOverrideManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(AbstractClientPlayerEntity.class)
public class MixinAbstractClientPlayer {
    // The correct Yarn method name in AbstractClientPlayerEntity is getSkin() not getSkinTextures()
    @Inject(method = "getSkin", at = @At("HEAD"), cancellable = true)
    private void overrideSkin(CallbackInfoReturnable<SkinTextures> cir) {
        AbstractClientPlayerEntity self = (AbstractClientPlayerEntity) (Object) this;
        UUID selfUuid = self.getUuid();
        String fakeUser = PlayerOverrideManager.getSkinOverride(selfUuid);
        if (fakeUser == null) return;

        // Look up the target player in the server player list and use their skin
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayNetworkHandler handler = mc.getNetworkHandler();
        if (handler == null) return;

        for (PlayerListEntry entry : handler.getPlayerList()) {
            // authlib 7.x: GameProfile is a record — use name() not getName()
            if (fakeUser.equalsIgnoreCase(entry.getProfile().name())) {
                cir.setReturnValue(entry.getSkinTextures());
                return;
            }
        }
    }
}
