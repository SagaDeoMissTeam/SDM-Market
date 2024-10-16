package net.sixik.sdmmarket.client.widgets;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.v2.color.RGBA;

public class MarketTextField extends TextField {

    public MarketTextField(Panel panel) {
        super(panel);
    }


    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(100,100,100,255/3).draw(graphics,x,y,w,h);
    }
}
