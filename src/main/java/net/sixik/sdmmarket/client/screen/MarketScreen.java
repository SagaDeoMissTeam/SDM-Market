package net.sixik.sdmmarket.client.screen;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class MarketScreen extends BaseScreen {

    public SearchPanel searchPanel;
    public EntryPanel entryPanel;

    public MarketScreen(){

    }

    public static void refreshIfOpen(){
        Screen var2 = Minecraft.getInstance().screen;
        if (var2 instanceof ScreenWrapper w) {
            BaseScreen var3 = w.getGui();
            if (var3 instanceof MarketScreen mts) {
                mts.refreshWidgets();
            }
        }
    }

    public static void refreshAll(){
        Screen var2 = Minecraft.getInstance().screen;
        if (var2 instanceof ScreenWrapper w) {
            BaseScreen var3 = w.getGui();
            if (var3 instanceof MarketScreen mts) {
                mts.refreshWidgets();
                mts.updateEntries();
            }
        }
    }

    public void updateEntries(){
        entryPanel.updateEntries();
    }



    @Override
    public boolean onInit() {
        this.setWidth(this.getScreen().getGuiScaledWidth() * 4 / 5);
        this.setHeight(this.getScreen().getGuiScaledHeight() * 4 / 5);
        this.closeContextMenu();
        return true;
    }

    @Override
    public void addWidgets() {
        add(searchPanel = new SearchPanel(this));
        searchPanel.setSize(this.width / 5, this.height);
        add(entryPanel = new EntryPanel(this));
        entryPanel.setSize(this.width - searchPanel.width, this.height);
    }

    @Override
    public void alignWidgets() {
        searchPanel.setPos(this.width - (this.width / 5), 0);
        searchPanel.setSize(this.width / 5, this.height);
        entryPanel.setSize(this.width - searchPanel.width, this.height);
    }
}
