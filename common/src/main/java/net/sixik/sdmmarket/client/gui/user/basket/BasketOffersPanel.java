package net.sixik.sdmmarket.client.gui.user.basket;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;

import java.util.ArrayList;
import java.util.List;

public class BasketOffersPanel extends Panel {
    public MarketUserBasketScreen screen;
    public BasketOffersPanel(MarketUserBasketScreen panel) {
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
        List<BasketOfferButton> list = new ArrayList<>();
        for (MarketUserEntry clientOffer : MarketDataManager.PLAYER_CLIENT_DATA.findClientOffers()) {
            BasketOfferButton button = new BasketOfferButton(this, clientOffer);
            button.setSize(this.width - 6, 18);
            list.add(button);
        }
        calculatePosition(list);

        addAll(list);

    }

    private void calculatePosition(List<BasketOfferButton> list) {
        int y = 2;
        for (BasketOfferButton button : list) {
            button.setPos(2, y);
            y += button.height + 2;
        }
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(0,0,0, 255/3).draw(graphics,x,y,w,h);
    }
}
