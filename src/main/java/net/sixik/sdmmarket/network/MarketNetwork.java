package net.sixik.sdmmarket.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;

public interface MarketNetwork {

    SimpleNetworkManager NET = SimpleNetworkManager.create("sdmmarket");

    MessageType SEND_BUY_ENTRY = NET.registerC2S("send_buy_entry", SendBuyEntryC2S::new);
    MessageType SEND_CREATE_ENTRY = NET.registerC2S("send_create_entry", SendEntryCreateC2S::new);
    MessageType SYNC_MARKET_DATA = NET.registerS2C("sync_market_data", SyncMarketDataS2C::new);

    static void init() {
    }
}
