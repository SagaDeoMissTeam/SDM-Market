package net.sixik.sdmmarket.common.network.user.newN;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;

public class SendClearMarketDataS2C extends BaseS2CMessage {

    private final boolean needGet;

    public SendClearMarketDataS2C() {
        this(true);
    }

    public SendClearMarketDataS2C(boolean needGet) {
        this.needGet = needGet;
    }

    public SendClearMarketDataS2C(FriendlyByteBuf buf) {

        this.needGet = buf.readBoolean();
    }


    @Override
    public MessageType getType() {
        return MarketNetwork.SEND_CLEAR_MARKET_DATA;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(needGet);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MarketDataManager.USER_CLIENT.categories.clear();

        if(needGet) new SendGetMarketCategoryC2S().sendToServer();
    }
}
