package net.sixik.sdmmarket.common.network.user;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.client.gui.admin.MarketAdminScreen;
import net.sixik.sdmmarket.client.gui.user.MarketUserScreen;
import net.sixik.sdmmarket.client.gui.user.basket.MarketUserBasketScreen;
import net.sixik.sdmmarket.client.gui.user.buyer.MarketUserBuyerScreen;
import net.sixik.sdmmarket.client.gui.user.selling.SellingUserScreen;
import net.sixik.sdmmarket.common.network.MarketNetwork;

public class UpdateUIS2C extends BaseS2CMessage {

    public UpdateUIS2C() {

    }
    public UpdateUIS2C(FriendlyByteBuf buf) {

    }

    @Override
    public MessageType getType() {
        return MarketNetwork.UPDATE_UI;
    }

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if(Minecraft.getInstance().screen instanceof ScreenWrapper screenWrapper){
            if(screenWrapper.getGui() instanceof MarketUserScreen userScreen) {
                userScreen.clearWidgets();
                userScreen.addWidgets();
                userScreen.alignWidgets();
            }
            if(screenWrapper.getGui() instanceof SellingUserScreen sellingUserScreen){
                sellingUserScreen.selectable.clear();
                sellingUserScreen.clearWidgets();
                sellingUserScreen.addWidgets();
                sellingUserScreen.alignWidgets();
                sellingUserScreen.sellingPanel.addSellableItems();
            }
            if(screenWrapper.getGui() instanceof MarketAdminScreen adminScreen){
                adminScreen.clearWidgets();
                adminScreen.addWidgets();
                adminScreen.alignWidgets();
            }
            if(screenWrapper.getGui() instanceof MarketUserBasketScreen basketScreen){
                basketScreen.entriesPanel.addEntries();
                basketScreen.offersPanel.addEntries();
            }
            if(screenWrapper.getGui() instanceof MarketUserBuyerScreen buyerScreen) {
                buyerScreen.selectedEntry = null;
                buyerScreen.infoPanel.addElements();
                buyerScreen.entriesPanel.addEntries();
            }
        }
    }
}
