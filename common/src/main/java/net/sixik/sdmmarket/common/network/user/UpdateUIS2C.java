package net.sixik.sdmmarket.common.network.user;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.api.MarketAPI;
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
        MarketAPI.updateUI();
    }
}
