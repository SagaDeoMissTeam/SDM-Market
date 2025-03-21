package net.sixik.sdmmarket.client.gui.user.basket;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.sixik.sdmmarket.client.gui.ui.Colors;
import net.sixik.sdmmarket.client.gui.ui.TextRenderHelper;
import net.sixik.sdmmarket.client.widgets.MarketTextField;

public class MarketUserBasketScreen extends BaseScreen {

    public PanelScrollBar scrollEntriesPanel;
    public BasketEntriesPanel entriesPanel;
    public BasketOffersPanel offersPanel;
    public TextField basketTitle;
    public TextField entriesTitle;

    @Override
    public boolean onInit() {
        setWidth(getScreen().getGuiScaledWidth() * 4/7);
        setHeight(getScreen().getGuiScaledHeight() * 4/6);
        return true;
    }

    @Override
    public void addWidgets() {
        add(entriesPanel = new BasketEntriesPanel(this));
        entriesPanel.addWidgets();
        add(offersPanel = new BasketOffersPanel(this));
        offersPanel.addWidgets();
        add(scrollEntriesPanel = new PanelScrollBar(this, entriesPanel) {
            @Override
            public void drawScrollBar(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
                Colors.UI_GOLD_0.draw(graphics,x,y,w,h);
            }

            @Override
            public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
                Color4I.rgba(0,0,0,255/2).draw(graphics,x,y,w,h);
            }
        });

        add(basketTitle = new MarketTextField(this));
        add(entriesTitle = new MarketTextField(this));
    }

    @Override
    public void alignWidgets() {
        this.entriesPanel.setSize(this.width / 2 - 2, this.height);
        this.offersPanel.setSize(this.width / 2 - 2, this.height);
        this.offersPanel.setPos(this.width / 2 + 2, 0);
        entriesPanel.alignWidgets();
        offersPanel.alignWidgets();

        basketTitle.setText("Basket");
        basketTitle.setPos(2, -(TextRenderHelper.getTextHeight() + 4));
        basketTitle.setSize(this.width / 2 - 6, TextRenderHelper.getTextHeight());

        entriesTitle.setText("Offers");
        entriesTitle.setPos(this.width / 2 + 4, -(TextRenderHelper.getTextHeight() + 4));
        entriesTitle.setSize(this.width / 2 - 6, TextRenderHelper.getTextHeight());

        this.scrollEntriesPanel.setPosAndSize(
                this.entriesPanel.posX + this.entriesPanel.width - this.getScrollbarWidth(),
                this.entriesPanel.posY,
                this.getScrollbarWidth(),
                this.entriesPanel.height
        );
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {

    }

    protected int getScrollbarWidth() {
        return 2;
    }
}
