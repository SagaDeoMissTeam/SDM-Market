package net.sixik.sdmmarket.client.gui.user.entries;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.client.gui.user.buyer.MarketUserBuyerScreen;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.v2.color.RGBA;

public class MarketUserCategoryListButton extends SimpleTextButton {

    public MarketUserCategory category;
    public MarketUserEntryList entryList;

    public MarketUserCategoryListButton(Panel panel) {
        super(panel, Component.empty(), Icon.empty());
    }

    public MarketUserCategoryListButton setItem(ItemStack item){
        setIcon(ItemIcon.getItemIcon(item));
        setTitle(Component.literal(item.getDisplayName().getString().replace("[", "").replace("]", "")));
        return this;
    }

    public void setData(MarketUserEntryList entryList, MarketUserCategory category){
        this.category = category;
        this.entryList = entryList;
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isLeft()) {
            new MarketUserBuyerScreen(category, entryList).openGui();
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(100,100,100,255/3).drawRoundFill(graphics,x,y,w,h, 6);
    }
}
