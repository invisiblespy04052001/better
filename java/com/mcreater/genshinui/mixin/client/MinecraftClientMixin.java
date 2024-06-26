package com.mcreater.genshinui.mixin.client;

import com.mcreater.genshinui.animation.AnimatedValue;
import com.mcreater.genshinui.animation.AnimationProvider;
import com.mcreater.genshinui.shaders.BoxBlurShader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.mcreater.genshinui.config.Configuration.OPTION_ENABLE_CHAT_SCREEN_VANILLA_RENDERING;
import static com.mcreater.genshinui.util.SafeValue.safeBoolean;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public Screen currentScreen;
    private final AnimatedValue blurSamples = new AnimatedValue(0, 0, 50, AnimationProvider.func(AnimationProvider.AnimationType.EASE_IN_OUT, AnimationProvider.AnimationMode.EXPONENTIAL));

    @Inject(at = @At("HEAD"), method = "render")
    public void onRender(boolean tick, CallbackInfo ci) {
        BoxBlurShader.setRadius((float) blurSamples.getCurrentValue());
    }

    @Inject(at = @At("HEAD"), method = "setScreen", cancellable = true)
    public void onSetScreen(Screen screen, CallbackInfo ci) {
        LogManager.getLogger(MinecraftClientMixin.class).info("Screen change: {} -> {}", currentScreen, screen);
        if (safeBoolean(OPTION_ENABLE_CHAT_SCREEN_VANILLA_RENDERING.getValue())) return;

        blurSamples.setExpectedValue(screen == null ? 0 : 16);

        if (screen == null) {
            if (currentScreen instanceof ChatScreen) {
                currentScreen.removed();
                ci.cancel();
            }
        }
    }
}
