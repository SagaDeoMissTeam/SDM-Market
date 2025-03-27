package net.sixik.sdmmarket.client.gui.user.buyer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.sdm.sdmshopr.SDMShopR;
import net.sixik.sdmmarket.client.gui.ui.TextRenderHelper;
import net.sixik.sdmmarket.client.widgets.MarketTextField;

public class MarketUserBuyerInfoPanel extends Panel {

    public Icon icon;
    public MarketUserBuyerScreen panel;
    public MarketUserBuyerBuyButton buyerBuyButton;

    public TextField moneyField;
    public MarketUserBuyerInfoPanel(MarketUserBuyerScreen panel) {
        super(panel);
        this.panel = panel;
        this.icon = ItemIcon.getItemIcon(panel.entryList.itemStack);
    }

    @Override
    public void addWidgets() {
        add(moneyField = new MarketTextField(this));
        add(buyerBuyButton = new MarketUserBuyerBuyButton(this));
    }

    @Override
    public void alignWidgets() {
        addElements();
    }

    public void addElements() {
        moneyField.setSize(this.width - 4, (TextRenderHelper.getTextHeight() + 1) * 2);
        moneyField.setPos(2,  36);
        moneyField.setText(new TranslatableComponent("sdm.market.user.buy.player_money", SDMShopR.getClientMoney()));
        moneyField.setMaxWidth(this.width - 4);

        this.buyerBuyButton.setPos(4, this.height - (TextRenderHelper.getTextHeight() + 3));
        this.buyerBuyButton.setSize(this.width - 8, TextRenderHelper.getTextHeight() + 1);
    }

    public boolean canBuy() {
        return panel.selectedEntry!= null && SDMShopR.getClientMoney() >= panel.selectedEntry.price;
    }

    public boolean isSelected() {
        return panel.selectedEntry != null;
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(0,0,0,255/3).draw(graphics,x,y,w,h);

        int j = w >= 34 ? 32 : 16;

        Color4I.rgba(100,100,100,255/3).draw(graphics,x + (this.width / 2) - j / 2,y + 2,j,j);
        icon.draw(graphics,x + (this.width / 2) - j / 2,y + 2, j, j);



    }
}
