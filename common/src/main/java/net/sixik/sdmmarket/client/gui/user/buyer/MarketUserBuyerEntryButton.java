package net.sixik.sdmmarket.client.gui.user.buyer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;

import java.util.Objects;

public class MarketUserBuyerEntryButton extends SimpleTextButton {

    public MarketUserEntry entry;

    public MarketUserBuyerEntriesPanel panel;
    public MarketUserBuyerEntryButton(MarketUserBuyerEntriesPanel panel, MarketUserEntry entry) {
        super(panel, TextComponent.EMPTY, Icon.EMPTY);
        this.panel = panel;
        this.entry = entry;

        setTitle(new TextComponent(SDMMarket.moneyString(entry.price)));
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isLeft()) {
            panel.panel.selectedEntry = entry;
        }
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        if(panel.panel.selectedEntry != null && Objects.equals(panel.panel.selectedEntry.entryID, entry.entryID)) {
            Color4I.rgba(255,255,255,255/3).draw(graphics,x,y,w,h);
        } else {
            Color4I.rgba(100,100,100,255/3).draw(graphics, x, y, w, h);
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {

        entry.itemStack.getTooltipLines(Minecraft.getInstance().player, TooltipFlag.Default.NORMAL).forEach(list::add);

        if(entry.itemStack.isDamageableItem() && entry.itemStack.isDamaged()){
            list.add(new TextComponent("Damage: " + (entry.itemStack.getMaxDamage() - entry.itemStack.getDamageValue()) + "/" + entry.itemStack.getMaxDamage()));
        }

        list.add(TextComponent.EMPTY);
        list.add(new TranslatableComponent("sdm.market.user.buy.price", entry.price));
        list.add(new TranslatableComponent("sdm.market.user.buy.count", entry.count));
    }

    @Override
    public void draw(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        drawBackground(graphics, theme, x, y, w, h);
        var s = h >= 16 ? 16 : 8;
        var off = (h - s) / 2;
        FormattedText title = getTitle();
        var textX = x;
        var textY = y + (h - theme.getFontHeight() + 1) / 2;

        var sw = theme.getStringWidth(title);
        var mw = w - (hasIcon() ? off + s : 0) - 6;

        if (sw > mw) {
            sw = mw;
            title = theme.trimStringToWidth(title, mw);
        }

        if (renderTitleInCenter()) {
            textX += (mw - sw + 6) / 2;
        } else {
            textX += 4;
        }

        if (hasIcon()) {
            drawIcon(graphics, theme, x + off, y + off, s, s);
            textX += off + s;
        }

        theme.drawString(graphics, title, textX, textY, theme.getContentColor(getWidgetType()), Theme.SHADOW);

        int f = this.width / 2;

        String d = String.valueOf(entry.count) + " qty";
        theme.drawString(graphics, d, x + f + f / 4, textY, theme.getContentColor(getWidgetType()), Theme.SHADOW);
    }
}
