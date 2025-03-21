package net.sixik.sdmmarket.client.gui.user.basket;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.TooltipFlag;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.common.market.basketEntry.AbstractBasketEntry;
import net.sixik.sdmmarket.common.market.basketEntry.BasketItemEntry;
import net.sixik.sdmmarket.common.market.basketEntry.BasketMoneyEntry;
import net.sixik.sdmmarket.common.network.user.basket.TakeBasketEntryC2S;

public class BasketEntryButton extends SimpleTextButton {
    public AbstractBasketEntry basketMoneyEntry;
    public BasketEntryButton(Panel panel, AbstractBasketEntry basketMoneyEntry) {
        super(panel, TextComponent.EMPTY, Icon.EMPTY);
        this.basketMoneyEntry = basketMoneyEntry;

        if(basketMoneyEntry instanceof BasketItemEntry itemEntry) {
            MutableComponent mutableComponent = TextComponent.EMPTY.copy().append(itemEntry.itemStack.getHoverName()).withStyle(itemEntry.itemStack.getRarity().color);
            setTitle(mutableComponent);
            return;
        }
        if(basketMoneyEntry instanceof BasketMoneyEntry moneyEntry){
            setTitle(new TextComponent(SDMMarket.moneyString(moneyEntry.moneyCount)));
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        if(basketMoneyEntry instanceof BasketItemEntry itemEntry) {
            list.add(new TextComponent("Item: "));
            itemEntry.itemStack.getTooltipLines(Minecraft.getInstance().player, TooltipFlag.Default.NORMAL).forEach(list::add);
            return;
        }
        if(basketMoneyEntry instanceof BasketMoneyEntry moneyEntry){
            list.add(new TextComponent("Money: "));
            list.add(new TextComponent(SDMMarket.moneyString(moneyEntry.moneyCount)));
        }
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isLeft()) {
            new TakeBasketEntryC2S(basketMoneyEntry.basketEntryID).sendToServer();
        }
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(100,100,100,255/3).draw(graphics,x,y,w,h);
    }
}
