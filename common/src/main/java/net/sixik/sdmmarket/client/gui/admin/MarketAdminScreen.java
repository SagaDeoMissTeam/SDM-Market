package net.sixik.sdmmarket.client.gui.admin;

import dev.ftb.mods.ftblibrary.ui.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.sdmmarket.client.gui.admin.category.CategoryButton;
import net.sixik.sdmmarket.client.gui.admin.category.CategoryPanel;
import net.sixik.sdmmarket.client.gui.admin.entry.EntryButton;
import net.sixik.sdmmarket.client.gui.admin.entry.EntryPanel;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.config.MarketConfigCategory;
import net.sixik.sdmmarket.common.market.config.AbstractMarketConfigEntry;
import net.sixik.sdmmarket.common.network.admin.InvokeSyncAdminCategoryC2S;
import net.sixik.v2.color.Colors;
import net.sixik.v2.color.RGBA;

import java.util.ArrayList;
import java.util.List;

public class MarketAdminScreen extends BaseScreen {

    public MarketConfigCategory selectedCategory = null;

    public CategoryPanel categoryPanel;
    public EntryPanel entryPanel;

    public PanelScrollBar scrollCategoryPanel;
    public PanelScrollBar scrollEntryPanel;

    public int sizeOfButtons = 48;

    public MarketAdminScreen() {
        new InvokeSyncAdminCategoryC2S().sendToServer();
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
        add(categoryPanel = new CategoryPanel(this));
        add(scrollCategoryPanel = new PanelScrollBar(this, ScrollBar.Plane.VERTICAL, categoryPanel) {
            @Override
            public void drawScrollBar(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                Colors.UI_GOLD_0.draw(graphics,x,y,w,h);
            }

            @Override
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                RGBA.create(0,0,0, 255/2).draw(graphics,x,y,w,h);
            }
        });
        add(entryPanel = new EntryPanel(this));
        add(scrollEntryPanel = new PanelScrollBar(this, ScrollBar.Plane.VERTICAL, entryPanel) {
            @Override
            public void drawScrollBar(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                Colors.UI_GOLD_0.draw(graphics,x,y,w,h);
            }

            @Override
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                RGBA.create(0,0,0, 255/2).draw(graphics,x,y,w,h);
            }
        });

        addCategoryButtons();
        addCategoryEntryButtons();
    }

    @Override
    public void alignWidgets() {
        categoryPanel.setSize(this.width / 5, this.height);
        entryPanel.setX(this.width / 5 + 2);
        entryPanel.setSize(this.width - 2 - this.width / 5, this.height);


        this.scrollCategoryPanel.setPosAndSize(
                this.categoryPanel.getPosX() + this.categoryPanel.getWidth() - this.getScrollbarWidth(),
                this.categoryPanel.getPosY(),
                this.getScrollbarWidth(),
                this.categoryPanel.getHeight()
        );

        this.scrollEntryPanel.setPosAndSize(
                this.entryPanel.getPosX() + this.entryPanel.getWidth() - this.getScrollbarWidth(),
                this.entryPanel.getPosY(),
                this.getScrollbarWidth(),
                this.entryPanel.getHeight() - 10
        );

        addCategoryButtons();
        addCategoryEntryButtons();
    }

    public void addCategoryEntryButtons() {
        if(selectedCategory != null) {
            entryPanel.clearWidgets();
            List<EntryButton> list = new ArrayList<>();

            for (AbstractMarketConfigEntry entry : selectedCategory.entries) {
                EntryButton button = new EntryButton(entryPanel, entry);
                button.setSize(sizeOfButtons, sizeOfButtons);
                list.add(button);
            }

            EntryButton button = new EntryButton(entryPanel, null);
            button.setEdit();
            button.setSize(sizeOfButtons, sizeOfButtons);
            list.add(button);

            calculatePositionsEntry(list);

            entryPanel.addAll(list);
        }

    }

    public void calculatePositionsEntry(List<EntryButton> list) {
        int x = 2;
        int y = 2;

        for (EntryButton button : list) {

            if(x + 2 + button.width >= entryPanel.width) {
                y += button.height + 2;
                button.setPos(2,y);
                x = button.width + 4;
            } else {
                button.setPos(x,y);
                x += button.width + 2;
            }
        }

    }

    public void addCategoryButtons() {
        categoryPanel.getWidgets().clear();
        List<CategoryButton> list = new ArrayList<>();

        for (MarketConfigCategory category : MarketDataManager.CONFIG_CLIENT.CATEGORIES) {
            CategoryButton button = new CategoryButton(categoryPanel, category);
            button.setSize(categoryPanel.width, 18);
            list.add(button);
        }

        CategoryButton button = new CategoryButton(categoryPanel, null);
        button.setSize(categoryPanel.width, 18);
        button.setEdit();
        list.add(button);

        calculatePositions(list);

        list.forEach(categoryPanel::add);
    }

    public void calculatePositions(List<CategoryButton> list) {
        int i = 0;
        for (CategoryButton button : list) {
            button.setPos(0,i);
            i += button.height + 2;
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {

    }

    protected int getScrollbarWidth() {
        return 2;
    }

    public static void refreshIfOpen() {
        if(Minecraft.getInstance().screen instanceof ScreenWrapper wrapper){
            if(wrapper.getGui() instanceof MarketAdminScreen adminScreen){
                adminScreen.refreshWidgets();
            }
        }
    }
}
