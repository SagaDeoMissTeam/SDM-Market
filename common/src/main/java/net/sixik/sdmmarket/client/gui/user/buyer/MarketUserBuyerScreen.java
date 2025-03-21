package net.sixik.sdmmarket.client.gui.user.buyer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.client.gui.ui.Colors;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;

public class MarketUserBuyerScreen extends BaseScreen{

    public MarketUserEntry selectedEntry = null;

    public MarketUserCategory category;
    public MarketUserEntryList entryList;

    public MarketUserBuyerEntriesPanel entriesPanel;
    public PanelScrollBar scrollEntriesPanel;
    public MarketUserBuyerInfoPanel infoPanel;
    public MarketUserBuyerScreen(MarketUserCategory category, MarketUserEntryList entryList) {
        this.category = category;
        this.entryList = entryList;
    }

    public void updateEntries() {
        for (MarketUserCategory marketUserCategory : MarketDataManager.USER_CLIENT.categories) {
            if(marketUserCategory.categoryID.equals(category.categoryID)) {
                category = marketUserCategory;

                for (MarketUserEntryList entry : category.entries) {
                    if(ItemStack.matches(entry.itemStack, entryList.itemStack)) {
                      entryList = entry;
                      break;
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean onInit() {
        setWidth(getScreen().getGuiScaledWidth() * 4/7);
        setHeight(getScreen().getGuiScaledHeight() * 4/6);
        return true;
    }

    @Override
    public void addWidgets() {
        add(entriesPanel = new MarketUserBuyerEntriesPanel(this));
        entriesPanel.addWidgets();
        add(infoPanel = new MarketUserBuyerInfoPanel(this));
        infoPanel.addWidgets();
        add(this.scrollEntriesPanel = new PanelScrollBar(this, entriesPanel){
            @Override
            public void drawScrollBar(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
                Colors.UI_GOLD_0.draw(graphics,x,y,w,h);
            }

            @Override
            public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
                Color4I.rgba(0,0,0,255/2).draw(graphics,x,y,w,h);
            }
        });
    }

    @Override
    public void alignWidgets() {
        entriesPanel.setX(this.width / 3 + 2);
        entriesPanel.setSize(this.width - this.width / 3 - 2, this.height);
        entriesPanel.alignWidgets();

        infoPanel.setSize(this.width / 3 - 2, this.height);
        infoPanel.alignWidgets();

        this.scrollEntriesPanel.setPosAndSize(
                this.entriesPanel.posX + this.entriesPanel.width - this.getScrollbarWidth(),
                this.entriesPanel.posY,
                this.getScrollbarWidth(),
                this.entriesPanel.height
        );
    }

    protected int getScrollbarWidth() {
        return 2;
    }


    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
    }
}
