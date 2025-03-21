package net.sixik.sdmmarket.client.gui.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.client.renderer.GameRenderer;

public class RenderHelper {

    public static void addFillToBuffer(PoseStack graphics, BufferBuilder buffer, int x, int y, int w, int h, Color4I rgb){
        if (w > 0 && h > 0) {
            Matrix4f m = graphics.last().pose();
            int r = rgb.redi();
            int g = rgb.greeni();
            int b = rgb.bluei();
            int a = rgb.alphai();

            buffer.vertex(m, (float)x, (float)(y + h), 0.0F).color(r, g, b, a).endVertex();
            buffer.vertex(m, (float)(x + w), (float)(y + h), 0.0F).color(r, g, b, a).endVertex();
            buffer.vertex(m, (float)(x + w), (float)y, 0.0F).color(r, g, b, a).endVertex();
            buffer.vertex(m, (float)x, (float)y, 0.0F).color(r, g, b, a).endVertex();
        }
    }
    public static void pushTransform(PoseStack guiGraphics, Vector2 pos, Vector2 size, float scale, float rotationAngle) {
        Vector2 center = new Vector2(pos.x + size.x / 2, pos.y + size.y / 2);
        guiGraphics.pushPose();
        guiGraphics.translate(center.x, center.y, 0);
        guiGraphics.scale(scale, scale, 1.0F);
        guiGraphics.mulPose(Vector3f.ZP.rotationDegrees(rotationAngle));
        guiGraphics.translate(-center.x, -center.y, 0);
    }

    public static void pushTransform(PoseStack guiGraphics, Vector2 pos, Vector2 size, Vector2 screenSize, float scale, float rotationAngle) {
        Vector2 screenCenter = new Vector2(screenSize.x, screenSize.y);
        Vector2 center = new Vector2(pos.x + size.x / 2, pos.y + size.y / 2);
        guiGraphics.pushPose();
        guiGraphics.translate(screenCenter.x, screenCenter.y, 0);
        guiGraphics.translate(center.x, center.y, 0);
        guiGraphics.scale(scale, scale, 1.0F);
        guiGraphics.mulPose(Vector3f.ZP.rotationDegrees(rotationAngle));
        guiGraphics.translate(-center.x, -center.y, 0);
        guiGraphics.translate(-screenCenter.x, -screenCenter.y, 0);

    }

    public static void popTransform(PoseStack guiGraphics){
        guiGraphics.popPose();
    }


    public static void drawHollowRect(PoseStack graphics, int x, int y, int w, int h, Color4I col, boolean roundEdges) {
        if (w > 1 && h > 1 && col != null) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            addFillToBuffer(graphics, buffer, x, y + 1, 1, h - 2, col);
            addFillToBuffer(graphics, buffer, x + w - 1, y + 1, 1, h - 2, col);
            if (roundEdges) {
                addFillToBuffer(graphics, buffer, x + 1, y, w - 2, 1, col);
                addFillToBuffer(graphics, buffer, x + 1, y + h - 1, w - 2, 1, col);
            } else {
                addFillToBuffer(graphics, buffer, x, y, w, 1, col);
                addFillToBuffer(graphics, buffer, x, y + h - 1, w, 1, col);
            }

            tesselator.end();
        } else {
            col.draw(graphics, x, y, w, h);
        }
    }
}
