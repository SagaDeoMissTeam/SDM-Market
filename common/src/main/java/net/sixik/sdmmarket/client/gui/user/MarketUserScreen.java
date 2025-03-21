package net.sixik.sdmmarket.client.gui.user;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import net.minecraft.util.Mth;
import net.sixik.sdmmarket.SDMMarketClient;
import net.sixik.sdmmarket.client.gui.ui.Colors;
import net.sixik.sdmmarket.client.gui.user.entries.EntriesPanel;
import net.sixik.sdmmarket.client.gui.user.misc.OpenAdminMenuButton;
import net.sixik.sdmmarket.client.gui.user.misc.OpenBasketMenuButton;
import net.sixik.sdmmarket.client.gui.user.misc.OpenSellingMenuButton;
import net.sixik.sdmmarket.client.gui.user.search.SearchPanel;
import net.sixik.sdmmarket.common.network.admin.InvokeSyncAdminCategoryC2S;

public class MarketUserScreen extends BaseScreen {

    public OpenAdminMenuButton adminMenuButton;
    public OpenSellingMenuButton sellingMenuButton;
    public OpenBasketMenuButton basketMenuButton;
    public SearchPanel searchPanel;
    public EntriesPanel entriesPanel;
    public PanelScrollBar scrollEntriesPanel;


    public double entryScrollPos = 0.0;

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
            public void drawScrollBar(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
                Colors.UI_GOLD_0.draw(graphics,x,y,w,h);
            }

            @Override
            public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
                Color4I.rgba(0,0,0,255/3).draw(graphics,x,y,w,h);
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
                this.entriesPanel.posX + this.entriesPanel.width - this.getScrollbarWidth(),
                this.entriesPanel.posY,
                this.getScrollbarWidth(),
                this.entriesPanel.height
        );

        this.scrollEntriesPanel.setValue(this.entryScrollPos);

    }

    public void setScroll() {
        this.entryScrollPos = Mth.clamp(scrollEntriesPanel.getValue(), scrollEntriesPanel.getMinValue(), scrollEntriesPanel.getMaxValue());
    }

    @Override
    public boolean onClosedByKey(Key key) {
        return key.esc();
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {

    }

    protected int getScrollbarWidth() {
        return 2;
    }

}
