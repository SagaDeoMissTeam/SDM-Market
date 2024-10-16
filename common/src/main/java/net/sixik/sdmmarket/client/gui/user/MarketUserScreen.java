package net.sixik.sdmmarket.client.gui.user;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.sdmmarket.SDMMarketClient;
import net.sixik.sdmmarket.client.gui.user.entries.EntriesPanel;
import net.sixik.sdmmarket.client.gui.user.misc.OpenAdminMenuButton;
import net.sixik.sdmmarket.client.gui.user.misc.OpenBasketMenuButton;
import net.sixik.sdmmarket.client.gui.user.misc.OpenSellingMenuButton;
import net.sixik.sdmmarket.client.gui.user.search.SearchPanel;
import net.sixik.sdmmarket.common.network.admin.InvokeSyncAdminCategoryC2S;
import net.sixik.v2.color.Colors;
import net.sixik.v2.color.RGBA;

public class MarketUserScreen extends BaseScreen {

    public OpenAdminMenuButton adminMenuButton;
    public OpenSellingMenuButton sellingMenuButton;
    public OpenBasketMenuButton basketMenuButton;
    public SearchPanel searchPanel;
    public EntriesPanel entriesPanel;
    public PanelScrollBar scrollEntriesPanel;


    public MarketUserScreen() {
        new InvokeSyncAdminCategoryC2S().sendToServer();
    }

    public void refreshEntries() {
        if(entriesPanel != null)
            entriesPanel.addEntries();
    }

    @Override
    public boolean onInit() {
        setWidth(getScreen().getGuiScaledWidth() * 4/5);
        setHeight(getScreen().getGuiScaledHeight() * 4/5);
        closeContextMenu();
        return true;
    }

    @Override
    public void addWidgets() {
        if(SDMMarketClient.hasPermission()) {
            add(this.adminMenuButton = new OpenAdminMenuButton(this));
        }

        add(sellingMenuButton = new OpenSellingMenuButton(this));
        add(basketMenuButton = new OpenBasketMenuButton(this));
        add(searchPanel = new SearchPanel(this));
        searchPanel.addWidgets();
        add(entriesPanel = new EntriesPanel(this));
        entriesPanel.addWidgets();
        add(scrollEntriesPanel = new PanelScrollBar(this, entriesPanel){
            @Override
            public void drawScrollBar(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                Colors.UI_GOLD_0.draw(graphics,x,y,w,h);
            }

            @Override
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                RGBA.create(0,0,0, 255/2).draw(graphics,x,y,w,h);
            }
        });
    }

    @Override
    public void alignWidgets() {
        if(SDMMarketClient.hasPermission()) {
            this.adminMenuButton.setPos(this.width + 2, 0);
        }
        this.sellingMenuButton.setPos(this.width + 2, this.height - sellingMenuButton.height);
        this.basketMenuButton.setPos(sellingMenuButton.posX, sellingMenuButton.posY - basketMenuButton.height - 2);

        this.searchPanel.setSize(this.width / 4, this.height);
        searchPanel.alignWidgets();
        this.entriesPanel.setSize(this.width - this.width / 4 - 4, this.height);
        this.entriesPanel.setX(this.width / 4 + 2);
        this.entriesPanel.alignWidgets();

        this.scrollEntriesPanel.setPosAndSize(
                this.entriesPanel.getPosX() + this.entriesPanel.getWidth() - this.getScrollbarWidth(),
                this.entriesPanel.getPosY(),
                this.getScrollbarWidth(),
                this.entriesPanel.getHeight()
        );

    }

    @Override
    public boolean onClosedByKey(Key key) {
        return key.esc();
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {

    }

    protected int getScrollbarWidth() {
        return 2;
    }

}
