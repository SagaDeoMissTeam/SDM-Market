package net.sixik.sdmmarket.client.gui.user.selling;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.common.market.config.AbstractMarketConfigEntry;
import net.sixik.sdmmarket.common.market.config.MarketConfigCategory;

public class SellingUserScreen extends BaseScreen {

    public Selectable selectable = new Selectable();

    public SellingInventorySlotPanel sellingPanel;
    public SellingMainUserPanel mainUserPanel;

    @Override
    public boolean onInit() {
        setWidth(getScreen().getGuiScaledWidth() * 4/7);
        setHeight(getScreen().getGuiScaledHeight() * 4/6);

        return true;
    }


    @Override
    public void addWidgets() {
        add(sellingPanel = new SellingInventorySlotPanel(this));
        sellingPanel.addWidgets();
        add(mainUserPanel = new SellingMainUserPanel(this));
        mainUserPanel.addWidgets();
    }

    @Override
    public void alignWidgets() {

        sellingPanel.setPos(this.width -  this.width / 8, 0);
        sellingPanel.setSize(this.width / 8, this.height);
        sellingPanel.alignWidgets();

        mainUserPanel.setSize(this.width - this.width / 8 - 2, this.height);
        mainUserPanel.alignWidgets();
    }

    public void setProperty() {

    }


    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {

    }


    public static class Selectable {
        public ItemStack selectedItem = ItemStack.EMPTY;
        public MarketConfigCategory  configCategory = null;
        public AbstractMarketConfigEntry entry = null;

        public void clear() {
            selectedItem = ItemStack.EMPTY;
            configCategory = null;
            entry = null;
        }
    }
}
