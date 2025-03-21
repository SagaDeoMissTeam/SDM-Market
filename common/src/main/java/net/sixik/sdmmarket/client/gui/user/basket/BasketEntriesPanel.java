package net.sixik.sdmmarket.client.gui.user.basket;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.basketEntry.AbstractBasketEntry;

import java.util.ArrayList;
import java.util.List;

public class BasketEntriesPanel extends Panel {
    public MarketUserBasketScreen screen;
    public BasketEntriesPanel(MarketUserBasketScreen panel) {
        super(panel);
        this.screen = panel;
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
        List<BasketEntryButton> list = new ArrayList<>();
        for (AbstractBasketEntry basketMoneyEntry : MarketDataManager.PLAYER_CLIENT_DATA.playerBasket.basketMoneyEntries) {
            BasketEntryButton button = new BasketEntryButton(this, basketMoneyEntry);
            button.setSize(width - 6, 18);
            list.add(button);
        }

        calculatePosition(list);

        addAll(list);
    }

    private void calculatePosition(List<BasketEntryButton> list) {
        int y = 2;
        for (BasketEntryButton abstractBasketEntry : list) {
            abstractBasketEntry.setPos(2, y);
            y += abstractBasketEntry.height + 2;
        }
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(0,0,0,255/3).draw(graphics,x,y,w,h);
    }
}
