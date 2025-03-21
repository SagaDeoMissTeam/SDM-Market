package net.sixik.sdmmarket.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;


public class MarketTextField extends TextField {

    public MarketTextField(Panel panel) {
        super(panel);
    }


    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(100,100,100,255/3).draw(graphics,x,y,w,h);
    }
}
