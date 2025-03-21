package net.sixik.sdmmarket.client.gui.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextRenderHelper {

    /**
     * Draws text on the screen using the specified font and color.
     *
     * @param poseStack      The pose stack for rendering.
     * @param x              The x-coordinate of the text's position.
     * @param y              The y-coordinate of the text's position.
     * @param size           The size of the text.
     * @param text           The text to be rendered.
     * @param textColor      The color of the text.
     */
    public static void drawText(PoseStack poseStack, int x, int y, float size, Component text, int textColor) {
        drawText(poseStack, Minecraft.getInstance().font, x, y, size, text, textColor);
    }

    /**
     * Draws text on the screen using the specified font and color.
     *
     * @param poseStack      The pose stack for rendering.
     * @param font           The font to be used for rendering the text.
     * @param x              The x-coordinate of the text's position.
     * @param y              The y-coordinate of the text's position.
     * @param size           The size of the text.
     * @param text           The text to be rendered.
     * @param textColor      The color of the text.
     */
    public static void drawText(PoseStack poseStack, Font font, int x, int y, float size, Component text, int textColor) {
        poseStack.pushPose();
        poseStack.scale(size, size, 1.0f);
        poseStack.translate(x, y, 0);
        Minecraft.getInstance().font.drawShadow(poseStack,text, x, y, textColor);
        poseStack.popPose();
    }

    /**
     * Draws text on the screen using the default font and color.
     *
     * @param graphics       The graphics context for rendering.
     * @param text           The text to be rendered.
     * @param x              The x-coordinate of the text's position.
     * @param y              The y-coordinate of the text's position.
     */
    public static void drawText(PoseStack graphics, Component text, int x, int y) {
        Minecraft.getInstance().font.drawShadow(graphics, text.getString(), x, y, Color4I.rgb(255, 255, 255).rgb());
    }

    public static void drawText(PoseStack graphics, String text, int x, int y) {
        Minecraft.getInstance().font.drawShadow(graphics, text, x, y, Color4I.rgb(255, 255, 255).rgb());
    }

    public static void drawText(PoseStack graphics, Component text, int x, int y, Color4I rgb) {
        Minecraft.getInstance().font.drawShadow(graphics, text.getString(), x, y, rgb.rgb());
    }

    public static void drawText(PoseStack graphics, String text, int x, int y, Color4I rgb) {
        Minecraft.getInstance().font.drawShadow(graphics, text, x, y, rgb.rgb());
    }

    public static void drawTextOverWight(PoseStack graphics, Object text, int x, int y, int wight) {
        drawTextOverWight(graphics, Minecraft.getInstance().font, getText(text), new Vector2(x,y), wight, Color4I.rgb(255, 255, 255));
    }

    public static void drawTextOverWight(PoseStack graphics, Object text, Vector2 pos, int wight) {
        drawTextOverWight(graphics, Minecraft.getInstance().font, getText(text), pos, wight, Color4I.rgb(255, 255, 255));
    }

    public static void drawTextOverWight(PoseStack graphics, Object text, int x, int y, int wight, Color4I rgb) {
        drawTextOverWight(graphics, Minecraft.getInstance().font, getText(text), new Vector2(x,y), wight, rgb);
    }

    public static void drawTextOverWight(PoseStack graphics, Object text, Vector2 pos, int wight, Color4I rgb) {
        drawTextOverWight(graphics, Minecraft.getInstance().font, getText(text), pos, wight, rgb);
    }


    public static void drawTextOverWight(PoseStack graphics, Font font, Object text, int x, int y, int wight, Color4I rgb) {
        drawTextOverWight(graphics,font,getText(text),new Vector2(x,y), wight,rgb);
    }

    public static void drawTextOverWight(PoseStack graphics, Font font, String text, Vector2 pos, int wight, Color4I rgb) {
        if (font.width(text) > wight - 10) {
            while (font.width(text + "...") > wight - 10) {
                text = text.substring(0, text.length() - 1);
            }
            text += "...";
        }
        Minecraft.getInstance().font.drawShadow(graphics, text, pos.x, pos.y, rgb.rgb());
    }

    public static int getTextWight(PoseStack graphics, Object text, float size) {
        String f = "";
        if(text instanceof Component component)
            f = component.getString();
        else if(text instanceof String str)
            f = str;
        else if(text instanceof FormattedText formattedText)
            f = formattedText.getString();

        int d = getTextWidth(f);
        int h = (int) (getTextWidth(f) * size);
        return d + (d - h);
    }

    /**
     * Calculates the size of the given text when rendered with the specified scale,
     * ensuring that the width does not exceed the given maximum width.
     *
     * @param text      The text to calculate the size for.
     * @param wight     The maximum width allowed for the text.
     * @param scale     The initial scale at which to render the text.
     * @param steps     The number of iterations to adjust the scale to fit within the maximum width.
     *
     * @return A Vector2f representing the width and scale of the text.
     *         The width is the actual width of the rendered text, and the scale is the final scale used.
     *         If the text cannot fit within the maximum width after the specified number of steps,
     *         the scale will be less than the initial scale, and the width will be the maximum allowed width.
     */
    public static Vector2f getTextRenderSize(String text, int wight, float scale, int steps){
        float s = scale;
        float w = 0;

        for (int i = 0; i < steps; i++) {
            w = getTextWidth(text, s);
            s -= 0.01f;
            if(w <= wight)
                return new Vector2f(w,s);
        }

        return new Vector2f(w,s);
    }

    public static int getTextHeight(){
        return Minecraft.getInstance().font.lineHeight;
    }

    public static float getTextHeight(float scale){
        int d1 = getTextHeight();
        int d2 = (int) (d1 * scale);
        return d1 + (d1 - d2);
    }

    public static int getTextWidth(String text){
        return Minecraft.getInstance().font.width(text);
    }

    public static float getTextWidth(String text, float scale){
        int d1 = Minecraft.getInstance().font.width(text);
        int d2 = (int) (d1 * scale);

        return d1 + (d1 - d2);
    }

    public static int getTextWidth(Object object, float scale) {
        String text = getText(object);
        int i = getTextWidth(text);
        return i + (i - (int) (i * scale));
    }

    public static List<String> splitTextToLines(String text, float textScale, int wight) {
        int maxWidth = (int) (wight / textScale);
        if (text.isEmpty()) return Collections.emptyList();
        if (!text.contains(" ") && !text.contains("\n")) return Collections.singletonList(text);

        List<String> lines = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        int index = 0;
        int wordStartIndex = 0;
        boolean wordProcessing = false;
        char prevSymbol = '0';

        for (char symbol : text.toCharArray()) {
            if (symbol != ' ') {
                wordProcessing = true;
                if (prevSymbol == ' ') {
                    wordStartIndex = index;
                }
            }

            if (symbol == '\n') {
                lines.add(builder.toString());
                builder.delete(0, builder.length());
                index = 0;
                continue;
            }

            if (getTextWidth(builder.toString() + symbol) <= maxWidth) {
                builder.append(symbol);
            } else {
                if (symbol == '.' || symbol == ',' || symbol == '!' || symbol == '?') {
                    builder.append(symbol);
                }
                if (wordProcessing) {
                    lines.add(builder.toString().substring(0, wordStartIndex));
                    builder.delete(0, wordStartIndex);
                } else {
                    lines.add(builder.toString());
                    builder.delete(0, builder.length());
                }
                if (symbol != ' ') {
                    builder.append(symbol);
                }
                index = builder.length() - 1;
            }

            wordProcessing = false;
            prevSymbol = symbol;
            index++;
        }

        if (builder.length() != 0) {
            lines.add(builder.toString());
        }
        return lines;
    }


    public static String getText(Object object){
        if(object instanceof String str)
            return str;
        else if(object instanceof Component component) {
            return component.getString();
        }
        else return object.toString();
    }
}
