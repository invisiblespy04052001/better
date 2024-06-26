package com.mcreater.genshinui.screens;

import com.mcreater.genshinui.render.LogoProvider;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Vector;

public abstract class ScreenHelper extends Screen {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    protected ScreenHelper(Text title) {
        super(title);
    }
    public static void fillScreen(MatrixStack matrices, int opacity) {
        fill(matrices, 0, 0, CLIENT.getWindow().getWidth(), CLIENT.getWindow().getHeight(), getBackgroundColor(opacity));
    }
    public static boolean isNight() {
        int hour = LocalDateTime.now(ZoneId.systemDefault()).getHour();
        int min = LocalDateTime.now(ZoneId.systemDefault()).getMinute();
        double mixed = hour + min / 60D;
        return mixed >= 17.5 || mixed <= 6.5;
    }
    public static int getBackgroundColor() {
        return getBackgroundColor(255);
    }
    public static int getBackgroundColor(int opacity) {
        return isNight() ?
                new Color(17, 18, 25, opacity).getRGB() :
                new Color(253, 253, 253, opacity).getRGB();
    }
    public static int getTextColor() {
        return getTextColor(255);
    }
    public static int getTextColor(int opacity) {
        return isNight() ?
                new Color(229, 200, 137, opacity).getRGB() :
//                new Color(235, 235, 235, opacity).getRGB() :
                new Color(142, 149, 158, opacity).getRGB();
    }
    public static int getNarrationColor() {
        return getNarrationColor(255);
    }
    public static int getNarrationColor(int opacity) {
        return isNight() ?
//                new Color(24, 25, 32, opacity).getRGB() :
                new Color(45, 45, 45, opacity).getRGB() :
                new Color(226, 226, 226, opacity).getRGB();
    }
    public static int getNarrationCharacterColor() {
        return getNarrationCharacterColor(255);
    }
    public static int getNarrationCharacterColor(int opacity) {
        return new Color(199, 165, 10, opacity).getRGB();
    }
    public static int getTextBaseColor() {
        return getTextBaseColor(255);
    }
    public static int getTextBaseColor(int opacity) {
        return new Color(253, 253, 253, opacity).getRGB();
    }
    public static void drawCenteredTextWithoutShadow(MatrixStack matrices, TextRenderer textRenderer, Text text, int centerX, int y, int color) {
        textRenderer.draw(matrices, text, (float)(centerX - textRenderer.getWidth(text) / 2), (float)y, color);
    }
    public static void drawTextWithoutShadow(MatrixStack matrices, TextRenderer textRenderer, Text text, int x, int y, int color) {
        textRenderer.draw(matrices, text, x, (float)y, color);
    }
    public static void drawTexture(MatrixStack matrix, LogoProvider logo, float opacity, int x, int y, int width, int height, double percent) {
        int imgW = logo.getSize().getLeft();
        int imgH = logo.getSize().getRight();
        int tarY = y - imgH / 2;
        matrix.translate(0.0, 0.0, 50.0);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
        RenderSystem.setShaderTexture(0, logo.getLight());
        DrawableHelper.drawTexture(matrix, x, tarY, (int) (width * percent), height, 0.0F, 0.0F, (int) (imgW * percent), imgH, imgW, imgH);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
    public static void drawTexture(MatrixStack matrix, LogoProvider logo, float opacity, int x, int y, int width, int height) {
        drawTexture(matrix, logo, opacity, x, y, width, height, 1.0D);
    }

    public record VertexColor(float r, float g, float b, float a) {}
    public static final VertexColor TRANSPARENT = new VertexColor(0, 0, 0, 0);

    public static void drawRect(MatrixStack matrices, int x1, int y1, int x2, int y2, VertexColor c1, VertexColor c2, VertexColor c3, VertexColor c4) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x2, y1, 0)
                .color(c1.r, c1.g, c1.b, c1.a)
                .next();
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x1, y1, 0)
                .color(c2.r, c2.g, c2.b, c2.a)
                .next();
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x1, y2, 0)
                .color(c3.r, c3.g, c3.b, c3.a)
                .next();
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x2, y2, 0)
                .color(c4.r, c4.g, c4.b, c4.a)
                .next();

        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);

        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public static void draw7ElementsBase(MatrixStack matrix, int xStart, int y) {
        drawTexture(matrix, LogoProvider.PICKAXE, 0.15F, xStart, y, 16, 16);
        drawTexture(matrix, LogoProvider.AXE, 0.15F, xStart + 16, y, 16, 16);
        drawTexture(matrix, LogoProvider.SWORD, 0.15F, xStart + 16*2, y, 16, 16);
        drawTexture(matrix, LogoProvider.COMPASS, 0.15F, xStart + 16*3, y, 16, 16);
        drawTexture(matrix, LogoProvider.BOW, 0.15F, xStart + 16*4, y, 16, 16);
        drawTexture(matrix, LogoProvider.ENDER_EYE, 0.15F, xStart + 16*5, y, 16, 16);
        drawTexture(matrix, LogoProvider.NETHER_STAR, 0.15F, xStart + 16*6, y, 16, 16);
    }

    public static void draw7Elements(MatrixStack matrix, int xStart, int y, double progress) {
        List<Double> a = splitProgress(progress);
        drawTexture(matrix, LogoProvider.PICKAXE, 0.7F, xStart, y, 16, 16, a.get(0)); // 稿子 (挖矿)
        drawTexture(matrix, LogoProvider.AXE, 0.7F, xStart + 16, y, 16, 16, a.get(1)); // 斧头 (打怪（划掉）伐木)
        drawTexture(matrix, LogoProvider.SWORD, 0.7F, xStart + 16*2, y, 16, 16, a.get(2)); // 剑 (PVP)
        drawTexture(matrix, LogoProvider.COMPASS, 0.7F, xStart + 16*3, y, 16, 16, a.get(3)); // 指南针 (冒险)
        drawTexture(matrix, LogoProvider.BOW, 0.7F, xStart + 16*4, y, 16, 16, a.get(4)); // 弓 (boss空战)
        drawTexture(matrix, LogoProvider.ENDER_EYE, 0.7F, xStart + 16*5, y, 16, 16, a.get(5)); // 末影之眼 (深渊（)
        drawTexture(matrix, LogoProvider.NETHER_STAR, 0.7F, xStart + 16*6, y, 16, 16, a.get(6)); // 下界之星 (原石（误)
    }

    public static java.util.List<Double> splitProgress(double va) {
        List<Double> result = new Vector<>();
        double v = 1D / 7D;
        int fulled = (int) Math.floor(va / v);
        for (int ignored = 0; ignored < fulled; ignored++) result.add(1.0D);
        result.add((va / v) - fulled);
        for (int ignored2 = result.size(); ignored2 < 7; ignored2++) result.add(0.0D);
        return result;
    }

    public static void fill(Matrix4f matrix, double x1, double y1, double x2, double y2, int color) {
        double i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float j = (float)(color & 0xFF) / 255.0f;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, (float) x1, (float) y2, 0.0f).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix, (float) x2, (float) y2, 0.0f).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix, (float) x2, (float) y1, 0.0f).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix, (float) x1, (float) y1, 0.0f).color(g, h, j, f).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
