package net.sixik.sdmmarket.client.gui.user.basket;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.TooltipFlag;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.common.market.basketEntry.AbstractBasketEntry;
import net.sixik.sdmmarket.common.market.basketEntry.BasketItemEntry;
import net.sixik.sdmmarket.common.market.basketEntry.BasketMoneyEntry;
import net.sixik.sdmmarket.common.network.user.basket.TakeBasketEntryC2S;
import net.sixik.v2.color.RGBA;

public class BasketEntryButton extends SimpleTextButton {
    public AbstractBasketEntry basketMoneyEntry;
    public BasketEntryButton(Panel panel, AbstractBasketEntry basketMoneyEntry) {
        super(panel, Component.empty(), Icon.empty());
        this.basketMoneyEntry = basketMoneyEntry;

        if(basketMoneyEntry instanceof BasketItemEntry itemEntry) {
            MutableComponent mutableComponent = Component.empty().append(itemEntry.itemStack.getHoverName()).withStyle(itemEntry.itemStack.getRarity().color);
            setTitle(mutableComponent);
            return;
        }
        if(basketMoneyEntry instanceof BasketMoneyEntry moneyEntry){
            setTitle(Component.literal(SDMMarket.moneyString(moneyEntry.moneyCount)));
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        if(basketMoneyEntry instanceof BasketItemEntry itemEntry) {
            list.add(Component.literal("Item: "));
            itemEntry.itemStack.getTooltipLines(Minecraft.getInstance().player, TooltipFlag.NORMAL).forEach(list::add);
            return;
        }
        if(basketMoneyEntry instanceof BasketMoneyEntry moneyEntry){
            list.add(Component.literal("Money: "));
            list.add(Component.literal(SDMMarket.moneyString(moneyEntry.moneyCount)));
        }
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isLeft()) {
            new TakeBasketEntryC2S(basketMoneyEntry.basketEntryID).sendToServer();
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(100,100,100,255/3).drawRoundFill(graphics,x,y,w,h, 6);
    }
}
