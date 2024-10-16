package net.sixik.sdmmarket.common;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.sixik.sdmmarket.common.data.MarketConfigData;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.data.MarketFileManager;
import net.sixik.sdmmarket.common.data.MarketUserManager;
import net.sixik.sdmmarket.common.network.user.SyncMarketDataS2C;

public class MarketEvents {

    public static void init() {


        LifecycleEvent.SERVER_STARTED.register((server) -> {
            MarketFileManager.init(server);
            MarketDataManager.CONFIG_SERVER = new MarketConfigData();
            MarketConfigData.load(server);
            MarketDataManager.loadMarketData(server);
            MarketUserManager.createOffersCategories(MarketDataManager.CONFIG_SERVER, MarketDataManager.USER_SERVER);
        });

        LifecycleEvent.SERVER_STOPPED.register((server) -> {
            MarketConfigData.save(server);
            MarketDataManager.saveMarketData(server);
        });

        PlayerEvent.PLAYER_JOIN.register(player -> {
            MarketDataManager.loadPlayer(player.server, player);
            new SyncMarketDataS2C().sendTo(player);
            MarketUserManager.syncUserData(player);
        });

        PlayerEvent.PLAYER_QUIT.register(player -> {
            MarketDataManager.savePlayer(player.server, player);
        });
    }
}
