package net.sixik.sdmmarket.client.gui.user.buyer;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.network.user.BuyOfferC2S;
import net.sixik.v2.color.RGBA;

public class MarketUserBuyerBuyButton extends SimpleTextButton {

    public MarketUserBuyerInfoPanel panel;
    public MarketUserBuyerBuyButton(MarketUserBuyerInfoPanel panel) {
        super(panel, Component.translatable("sdm.market.user.buy.buy"), Icon.empty());
        this.panel = panel;
    }

    @Override
    public boolean renderTitleInCenter() {
        return true;
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isLeft()) {
           new BuyOfferC2S(panel.panel.selectedEntry.serialize()).sendToServer();

//            for (MarketUserCategory category : MarketDataManager.USER_CLIENT.categories) {
//                if(category.categoryID.equals(panel.panel.selectedEntry.categoryID)) {
//                    category.removeEntry(panel.panel.selectedEntry.itemStack, panel.panel.selectedEntry.entryID);
//                    break;
//                }
//            }
//
//            panel.panel.entriesPanel.addEntries();
//            panel.panel.infoPanel.addElements();
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(100,100,100, 255/3).drawRoundFill(graphics,x,y,w,h, 4);
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        if(panel.canBuy()) {
            super.draw(graphics, theme, x, y, w, h);
        }
    }

    @Override
    public boolean checkMouseOver(int mouseX, int mouseY) {
        return panel.canBuy() && super.checkMouseOver(mouseX, mouseY);
    }
}
