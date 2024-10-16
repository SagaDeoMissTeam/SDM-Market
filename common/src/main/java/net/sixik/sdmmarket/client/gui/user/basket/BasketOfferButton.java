package net.sixik.sdmmarket.client.gui.user.basket;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.sdmmarket.common.network.user.CloseEntryC2S;
import net.sixik.v2.color.RGBA;

import java.util.ArrayList;
import java.util.List;

public class BasketOfferButton extends SimpleTextButton {

    public MarketUserEntry entry;
    public BasketOfferButton(Panel panel, MarketUserEntry entry) {
        super(panel, Component.empty(), Icon.empty());
        this.entry = entry;

        setIcon(ItemIcon.getItemIcon(entry.itemStack));
        setTitle(Component.literal(SDMMarket.moneyString(entry.price) + "   " + entry.count));
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        entry.itemStack.getTooltipLines(Minecraft.getInstance().player, TooltipFlag.NORMAL).forEach(list::add);
        list.add(Component.empty());
        list.add(Component.translatable("sdm.market.user.buy.price", entry.price));
        list.add(Component.translatable("sdm.market.user.buy.count", entry.count));
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isRight()) {
            List<ContextMenuItem> list = new ArrayList<>();
            list.add(new ContextMenuItem(Component.translatable("sdm.market.user.basket.remove"), Icons.REMOVE, (d) -> {
                new CloseEntryC2S(entry.entryID).sendToServer();
            }));

            getGui().openContextMenu(list);
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(100,100,100,255/3).drawRoundFill(graphics,x,y,w,h, 6);
    }
}
