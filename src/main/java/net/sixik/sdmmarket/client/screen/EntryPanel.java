package net.sixik.sdmmarket.client.screen;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.misc.NordColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.sdm.sdmshopr.SDMShopRClient;
import net.sixik.sdmmarket.client.utils.Cursor2D;
import net.sixik.sdmmarket.client.widgets.MarketEntryWidget;
import net.sixik.sdmmarket.data.ClientData;
import net.sixik.sdmmarket.data.MarketData;
import net.sixik.sdmmarket.data.entry.MarketCategory;
import net.sixik.sdmmarket.data.entry.MarketEntry;

import java.util.ArrayList;
import java.util.List;

public class EntryPanel extends Panel {

    public MarketScreen marketScreen;

    public BlankPanel entryBlankPanel;
    public PanelScrollBar entryScrollPanel;

    public EntryPanel(MarketScreen panel) {
        super(panel);
        this.marketScreen = panel;
    }

    @Override
    public void addWidgets() {

        add(entryBlankPanel = new BlankPanel(this));

        this.entryScrollPanel = new PanelScrollBar(this,entryBlankPanel);
        this.entryScrollPanel.setCanAlwaysScroll(true);
        this.entryScrollPanel.setScrollStep(20.0);

        entryBlankPanel.setSize(this.width, this.height);

        add(entryScrollPanel);


        updateEntries();
    }

    @Override
    public void alignWidgets() {

    }

    public void updateEntries(){
        updateEntry(getEntries());
    }

    private void updateEntry(List<Widget> items){
        double value = entryScrollPanel.getValue();
        entryBlankPanel.setSize(this.width, this.height);
        this.entryBlankPanel.getWidgets().clear();
        this.entryBlankPanel.addAll(items);
        this.entryScrollPanel.setPosAndSize(
                this.entryBlankPanel.width + 6,
                this.entryBlankPanel.posY - 1,
                16,
                this.entryBlankPanel.height + 2);
        this.entryScrollPanel.setValue(Math.min(value, entryScrollPanel.getMaxValue()));
    }

    public List<Widget> getEntries(){
        List<Widget> widgets1 = new ArrayList<>();
        Cursor2D line = new Cursor2D(0,0);
        for (MarketCategory value : MarketData.CLIENT.categories.values()) {
            for (MarketEntry<?> entry : value.entries) {
                if (ClientData.hawAnyTags(entry.searchEntryData.tags) && (ClientData.searchField.isEmpty() || entry.type.search())) {
                    MarketEntryWidget entryWidget = new MarketEntryWidget(this.entryBlankPanel, entry);
                    entryWidget.setSize(this.entryBlankPanel.width - 10, Minecraft.getInstance().font.lineHeight + 6);
                    entryWidget.setPos(line.x, line.y);
                    widgets1.add(entryWidget);
                    line.y += Minecraft.getInstance().font.lineHeight + 6 + 2;
                }
            }
        }
        return widgets1;
    }

}
