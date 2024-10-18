package net.sixik.sdmmarket.client.gui.user.selling;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.data.MarketUserManager;
import net.sixik.v2.color.RGBA;
import net.sixik.v2.render.TextRenderHelper;

import java.util.ArrayList;
import java.util.List;

public class SellingInventorySlotPanel extends Panel {

    public SellingInventorySlotPanel(Panel panel) {
        super(panel);
    }

    @Override
    public void addWidgets() {
        addSellableItems();
    }

    @Override
    public void alignWidgets() {
        addSellableItems();
    }

    public void addSellableItems() {
        clearWidgets();
        List<SellingItemButton> itemButtons = new ArrayList<>();

        List<ItemStack> items = MarketUserManager.getAvaliableItems(Minecraft.getInstance().player, MarketDataManager.CONFIG_CLIENT);

        for (ItemStack item : items) {
            SellingItemButton button = new SellingItemButton(this, item.copyWithCount(1));
            button.setSize(this.width - 6, TextRenderHelper.getTextHeight() + 1);
            itemButtons.add(button);
        }

        int y = 0;
        for (SellingItemButton itemButton : itemButtons) {
            itemButton.itemStack.setCount(1);
            itemButton.setX(3);
            itemButton.setY(y);
            y += itemButton.height + 2;
        }

        addAll(itemButtons);
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(0,0,0,255/3).drawRoundFill(graphics,x,y,w,h,6);
    }
}
