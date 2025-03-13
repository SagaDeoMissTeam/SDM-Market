package net.sixik.sdmmarket.common.network.user.newN;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.network.MarketNetwork;


public class SendGetMarketCategoryC2S extends BaseC2SMessage {

    public SendGetMarketCategoryC2S() {}

    public SendGetMarketCategoryC2S(FriendlyByteBuf buf) {}

    @Override
    public MessageType getType() {
        return MarketNetwork.SEND_GET_MARKET_CATEGORY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        for (MarketUserCategory category : MarketDataManager.USER_SERVER.categories) {
            new SendMarketCategoryS2C(category.serializeWithoutEntries()).sendTo((ServerPlayer) context.getPlayer());
        }
    }
}
