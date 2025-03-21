package net.sixik.sdmmarket.client.gui.user.basket;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.sdmmarket.common.network.user.CloseEntryC2S;

import java.util.ArrayList;
import java.util.List;

public class BasketOfferButton extends SimpleTextButton {

    public MarketUserEntry entry;
    public BasketOfferButton(Panel panel, MarketUserEntry entry) {
        super(panel, TextComponent.EMPTY, Icon.EMPTY);
        this.entry = entry;

        setIcon(ItemIcon.getItemIcon(entry.itemStack));
        setTitle(new TextComponent(SDMMarket.moneyString(entry.price) + "   " + entry.count));
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        entry.itemStack.getTooltipLines(Minecraft.getInstance().player, TooltipFlag.Default.NORMAL).forEach(list::add);
        list.add(TextComponent.EMPTY);
        list.add(new TranslatableComponent("sdm.market.user.buy.price", entry.price));
        list.add(new TranslatableComponent("sdm.market.user.buy.count", entry.count));
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isRight()) {
            List<ContextMenuItem> list = new ArrayList<>();
            list.add(new ContextMenuItem(new TranslatableComponent("sdm.market.user.basket.remove"), Icons.REMOVE, () -> {
                new CloseEntryC2S(entry.entryID).sendToServer();
            }));

            getGui().openContextMenu(list);
        }
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(100,100,100,255/3).draw(graphics,x,y,w,h);
    }
}
