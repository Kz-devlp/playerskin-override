package com.kzdevelopment.playerskinmod.mixin;

import com.kzdevelopment.playerskinmod.PlayerOverrideManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

// getDisplayName() is declared in PlayerEntity (not AbstractClientPlayerEntity)
@Mixin(PlayerEntity.class)
public class MixinPlayerRenderer {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void overrideDisplayName(CallbackInfoReturnable<Text> cir) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        UUID uuid = self.getUuid();
        String fake = PlayerOverrideManager.getNameOverride(uuid);
        if (fake != null) {
            cir.setReturnValue(Text.literal(fake));
        }
    }
}
