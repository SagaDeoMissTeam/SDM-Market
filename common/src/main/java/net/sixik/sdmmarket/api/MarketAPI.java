package net.sixik.sdmmarket.api;

import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.sixik.sdmmarket.client.gui.admin.MarketAdminScreen;
import net.sixik.sdmmarket.client.gui.user.MarketUserScreen;
import net.sixik.sdmmarket.client.gui.user.basket.MarketUserBasketScreen;
import net.sixik.sdmmarket.client.gui.user.buyer.MarketUserBuyerScreen;
import net.sixik.sdmmarket.client.gui.user.selling.SellingUserScreen;
import net.sixik.sdmmarket.common.network.user.newN.SendClearMarketDataS2C;

public class MarketAPI {

    public static void syncMarket(MinecraftServer server) {
        new SendClearMarketDataS2C().sendToAll(server);
    }

    public static void syncMarket(ServerPlayer player) {
        new SendClearMarketDataS2C().sendTo(player);
    }



    public static void updateUI() {
        if(Minecraft.getInstance().screen instanceof ScreenWrapper screenWrapper){
            if(screenWrapper.getGui() instanceof MarketUserScreen userScreen) {
                userScreen.clearWidgets();
                userScreen.addWidgets();
                userScreen.alignWidgets();
                userScreen.entriesPanel.addEntries();
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
                buyerScreen.updateEntries();
                buyerScreen.infoPanel.addElements();
                buyerScreen.entriesPanel.addEntries();
            }
        }
    }
}
