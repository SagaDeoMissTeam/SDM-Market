package net.sixik.sdmmarket.client.widgets;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.v2.color.RGBA;

public class MarketTextBox extends TextBox {
    public MarketTextBox(Panel panel) {
        super(panel);
    }

    @Override
    public void drawTextBox(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(100,100,100, 255/3).drawRoundFill(graphics,x,y,w,h,2);
    }
}
