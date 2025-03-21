package net.sixik.sdmmarket.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.Theme;


public class MarketTextBox extends TextBox {
    public MarketTextBox(Panel panel) {
        super(panel);
    }

    @Override
    public void drawTextBox(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(100,100,100, 255/3).draw(graphics,x,y,w,h);
    }
}
