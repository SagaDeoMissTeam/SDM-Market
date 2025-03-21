package net.sixik.sdmmarket.client.gui.user.buyer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.Minecraft;
import net.sixik.sdmmarket.client.gui.ui.TextRenderHelper;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;

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
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(0,0,0,255/3).draw(graphics,x,y,w,h);
    }
}
