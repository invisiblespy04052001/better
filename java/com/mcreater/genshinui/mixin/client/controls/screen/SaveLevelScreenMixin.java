package com.mcreater.genshinui.mixin.client.controls.screen;

import com.mcreater.genshinui.animation.AnimationNode;
import com.mcreater.genshinui.animation.AnimationProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.mcreater.genshinui.render.InternalFonts.STANDARD;
import static com.mcreater.genshinui.screens.ScreenHelper.*;

@Mixin(value = {SaveLevelScreen.class}, priority = Integer.MAX_VALUE)
public class SaveLevelScreenMixin extends Screen {
    private final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private AnimationNode node;
    private AnimationNode fakeProgress;
    protected SaveLevelScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    public void onInit(Text text, CallbackInfo ci) {
        node = null;
    }

    public void removed() {
        node = null;
        fakeProgress = null;
    }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (node == null) node = new AnimationNode(0, 3000, 0, 255);
        if (fakeProgress == null) fakeProgress = new AnimationNode(0, 1000, 0, 1);

        fillScreen(matrices, AnimationProvider.generateInteger(node, AnimationProvider.AnimationType.EASE_OUT, AnimationProvider.AnimationMode.EXPONENTIAL));

        drawCenteredTextWithoutShadow(
                matrices,
                CLIENT.textRenderer,
                title instanceof MutableText ? ((MutableText) title).fillStyle(Style.EMPTY.withFont(STANDARD)) : title,
                width / 2,
                height - 20 - 1 - 30,
                getTextColor()
        );
        ci.cancel();
    }
}
