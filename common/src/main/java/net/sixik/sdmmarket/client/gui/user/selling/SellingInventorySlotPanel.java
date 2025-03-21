package net.sixik.sdmmarket.client.gui.user.selling;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.client.gui.ui.TextRenderHelper;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.data.MarketUserManager;


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
            ItemStack i = item.copy();
            i.setCount(1);
            SellingItemButton button = new SellingItemButton(this, i);
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
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(0,0,0,255/3).draw(graphics,x,y,w,h);
    }
}
