package net.sixik.sdmmarket.client.gui.user.buyer;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.sdmmarket.client.SearchData;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.v2.color.RGBA;
import net.sixik.v2.render.TextRenderHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MarketUserBuyerEntriesPanel extends Panel {

    public MarketUserBuyerScreen panel;

    public MarketUserBuyerEntriesPanel(MarketUserBuyerScreen panel) {
        super(panel);
        this.panel = panel;
    }

    @Override
    public void addWidgets() {
        addEntries();
    }

    @Override
    public void alignWidgets() {
        addEntries();
    }

    public void addEntries() {
        clearWidgets();
        panel.entryList.sort(true);

        List<MarketUserBuyerEntryButton> list = new ArrayList<>();

        for (MarketUserEntry entry : panel.entryList.entries) {
            if(entry.isVisiable() && !Objects.equals(Minecraft.getInstance().player.getGameProfile().getId(), entry.ownerID)){
                MarketUserBuyerEntryButton button = new MarketUserBuyerEntryButton(this, entry);
                button.setSize(this.width - 6, TextRenderHelper.getTextHeight() + 3);
                list.add(button);
            }
        }

        calculatePositionsEntry(list);

        addAll(list);
    }

    public void calculatePositionsEntry(List<MarketUserBuyerEntryButton> list) {
        int y = 2;
        for (MarketUserBuyerEntryButton marketUserBuyerEntryButton : list) {
            marketUserBuyerEntryButton.setPos(2, y);
            y += marketUserBuyerEntryButton.height + 2;
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(0,0,0,255/3).drawRoundFill(graphics,x,y,w,h,6);
    }
}
