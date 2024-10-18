package net.sixik.sdmmarket.common.network.misc;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.client.gui.user.MarketUserScreen;
import net.sixik.sdmmarket.common.network.MarketNetwork;

public class SendOpenMarketScreenS2C extends BaseS2CMessage {

    public SendOpenMarketScreenS2C(){}
    public SendOpenMarketScreenS2C(FriendlyByteBuf buf){}

    @Override
    public MessageType getType() {
        return MarketNetwork.OPEN_MARKET_SCREEN;
    }

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        new MarketUserScreen().openGui();
    }
}
