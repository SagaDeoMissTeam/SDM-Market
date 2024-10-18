package net.sixik.sdmmarket.client.gui.user.selling;

import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.data.MarketUserManager;
import net.sixik.sdmmarket.common.utils.MarketItemHelper;
import net.sixik.v2.color.RGBA;

public class SellingItemButton extends SimpleTextButton {

    public ItemStack itemStack;

    public SellingItemButton(Panel panel, ItemStack itemStack) {
        super(panel, Component.empty(), ItemIcon.getItemIcon(itemStack));
        this.itemStack = itemStack.copy();
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isLeft()) {

            ((SellingUserScreen)getGui()).selectable.selectedItem = itemStack.copy();
            ((SellingUserScreen)getGui()).selectable.configCategory = MarketUserManager.getCategoryForItem(itemStack.copy(), MarketDataManager.CONFIG_CLIENT);
            ((SellingUserScreen)getGui()).selectable.entry = MarketUserManager.getEntryForItem(itemStack.copy(), MarketDataManager.CONFIG_CLIENT, ((SellingUserScreen)getGui()).selectable.configCategory);

            ((SellingUserScreen)getGui()).mainUserPanel.priceSell = 0;
            ((SellingUserScreen)getGui()).mainUserPanel.countItems = 0;
            ((SellingUserScreen)getGui()).mainUserPanel.countSell = 0;
            ((SellingUserScreen)getGui()).mainUserPanel.recreate();
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        for (Component tooltipLine : itemStack.getTooltipLines(Minecraft.getInstance().player, TooltipFlag.NORMAL)) {
            list.add(tooltipLine);
        }
    }

    public boolean isSelected() {
        if(((SellingUserScreen)getGui()).selectable.selectedItem.isEmpty()) return false;
        return MarketItemHelper.isEquals(itemStack.copy(), ((SellingUserScreen)getGui()).selectable.selectedItem.copy());
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        if(isSelected()) {
            RGBA.create(255,255,255,255/3).drawRoundFill(graphics,x,y,w,h,2);
        } else {
            RGBA.create(0,0,0,255/3).drawRoundFill(graphics,x,y,w,h,2);
        }
    }
}
