package net.sixik.sdmmarket.client.gui.ui;

import com.mojang.blaze3d.vertex.PoseStack;

public class GLRenderHelper {

    public static void pushTransform(PoseStack guiGraphics, Vector2 pos, Vector2 size, float scale, float rotationAngle) {
        RenderHelper.pushTransform(guiGraphics, pos, size, scale, rotationAngle);
    }

    public static void pushTransform(PoseStack guiGraphics, Vector2 pos, Vector2 size, Vector2 screenSize, float scale, float rotationAngle) {
        RenderHelper.pushTransform(guiGraphics, pos, size, screenSize, scale, rotationAngle);
    }

    public static void popTransform(PoseStack guiGraphics) {
        RenderHelper.popTransform(guiGraphics);
    }
}
