package net.sixik.sdmmarket.client.gui.user.buyer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.sixik.sdmmarket.common.network.user.BuyOfferC2S;

public class MarketUserBuyerBuyButton extends SimpleTextButton {

    public MarketUserBuyerInfoPanel panel;
    public MarketUserBuyerBuyButton(MarketUserBuyerInfoPanel panel) {
        super(panel, new TranslatableComponent("sdm.market.user.buy.buy"), Icon.EMPTY);
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
        }
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(100,100,100,255/3).draw(graphics,x,y,w,h);
    }

    @Override
    public void draw(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        if(panel.canBuy()) {
            super.draw(graphics, theme, x, y, w, h);
        }
    }

    @Override
    public boolean checkMouseOver(int mouseX, int mouseY) {
        return panel.canBuy() && super.checkMouseOver(mouseX, mouseY);
    }
}
