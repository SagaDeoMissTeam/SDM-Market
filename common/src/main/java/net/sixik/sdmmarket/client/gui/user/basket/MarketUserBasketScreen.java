package net.sixik.sdmmarket.client.gui.user.basket;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.sdmmarket.client.widgets.MarketTextField;
import net.sixik.v2.color.Colors;
import net.sixik.v2.color.RGBA;
import net.sixik.v2.render.TextRenderHelper;

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
            public void drawScrollBar(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                Colors.UI_GOLD_0.draw(graphics,x,y,w,h);
            }

            @Override
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                RGBA.create(0,0,0, 255/2).draw(graphics,x,y,w,h);
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
                this.entriesPanel.getPosX() + this.entriesPanel.getWidth() - this.getScrollbarWidth(),
                this.entriesPanel.getPosY(),
                this.getScrollbarWidth(),
                this.entriesPanel.getHeight()
        );
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {

    }

    protected int getScrollbarWidth() {
        return 2;
    }
}
