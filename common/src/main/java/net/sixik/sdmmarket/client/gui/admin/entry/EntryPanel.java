package net.sixik.sdmmarket.client.gui.admin.entry;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.sixik.sdmmarket.client.gui.admin.MarketAdminScreen;


public class EntryPanel extends Panel {

    public EntryPanel(MarketAdminScreen panel) {
        super(panel);
    }

    @Override
    public void addWidgets() {

    }

    @Override
    public void alignWidgets() {

    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(0,0,0, 255/3).draw(graphics,x,y,w,h);
    }
}
