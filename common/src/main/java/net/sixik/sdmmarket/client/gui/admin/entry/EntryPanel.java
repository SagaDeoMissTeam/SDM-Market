package net.sixik.sdmmarket.client.gui.admin.entry;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.sdmmarket.client.gui.admin.MarketAdminScreen;
import net.sixik.v2.color.RGBA;

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
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(0,0,0,255/3).drawRoundFill(graphics, x,y,w,h, 6);
    }
}
