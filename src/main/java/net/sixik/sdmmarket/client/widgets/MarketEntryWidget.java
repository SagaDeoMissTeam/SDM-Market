package net.sixik.sdmmarket.client.widgets;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.sdm.sdmshopr.SDMShopR;
import net.sixik.sdmmarket.client.screen.MarketBuyerScreen;
import net.sixik.sdmmarket.data.entry.MarketEntry;
import net.sixik.sdmmarket.network.SendBuyEntryC2S;

public class MarketEntryWidget extends SimpleTextButton {

    public MarketEntry<?> entry;

    public MarketEntryWidget(Panel panel, MarketEntry<?> entry) {
        super(panel, Component.literal(SDMShopR.moneyString(entry.price)), entry.getIcon());
        this.entry = entry;
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        entry.type.addTooltips(list);
        list.add(Component.empty());
        if(entry.isOwnerClient()){
            list.add(Component.translatable("sdm.market.lot.info.owner").withStyle(ChatFormatting.GREEN));
        } else
            list.add(Component.translatable("sdm.market.lot.info.seller").append(" ").append(entry.ownerPlayer.nickName));
        list.add(Component.translatable("sdm.market.lot.info.price").append(" ").append(String.valueOf(entry.price)));
        list.add(Component.translatable("sdm.market.lot.info.count").append(" ").append(String.valueOf(entry.count)));
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        if(mouseButton.isLeft()){
            new MarketBuyerScreen(entry).openGui();
        }
    }
}
