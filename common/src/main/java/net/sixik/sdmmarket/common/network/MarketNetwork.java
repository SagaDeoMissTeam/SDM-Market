package net.sixik.sdmmarket.common.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.common.network.admin.*;
import net.sixik.sdmmarket.common.network.admin.config.EditMarketConfigC2S;
import net.sixik.sdmmarket.common.network.admin.config.InvokeMarketEditC2S;
import net.sixik.sdmmarket.common.network.admin.config.SyncMarketConfigS2C;
import net.sixik.sdmmarket.common.network.misc.SendOpenMarketScreenS2C;
import net.sixik.sdmmarket.common.network.user.*;
import net.sixik.sdmmarket.common.network.user.basket.TakeBasketEntryC2S;
import net.sixik.sdmmarket.common.network.user.newN.*;

public class MarketNetwork {

    private static final SimpleNetworkManager NET  = SimpleNetworkManager.create(SDMMarket.MOD_ID);

    public static final MessageType CREATE_CATEGORY = NET.registerC2S("create_category", CreateCategoryC2S::new);
    public static final MessageType EDIT_CATEGORY = NET.registerC2S("edit_category", EditCategoryC2S::new);
    public static final MessageType CREATE_CATEGORY_ENTRY = NET.registerC2S("create_category_entry", CreateCategoryEntryC2S::new);
    public static final MessageType EDIT_CATEGORY_ENTRY = NET.registerC2S("edit_category_entry", EditCategoryEntryC2S::new);
    public static final MessageType INVOKE_SYNC_ADMIN_CATEGORY = NET.registerC2S("invoke_sync_admin_category", InvokeSyncAdminCategoryC2S::new);
    public static final MessageType SYNC_ADMIN_CATEGORY = NET.registerS2C("sync_admin_category", SyncAdminCategoryS2C::new);
    public static final MessageType SYNC_MARKET_CONFIG = NET.registerS2C("sync_market_config", SyncMarketConfigS2C::new);
    public static final MessageType EDIT_MARKET_CONFIG = NET.registerC2S("edit_market_config", EditMarketConfigC2S::new);
    public static final MessageType OPEN_MARKET_CONFIG = NET.registerC2S("open_market_config", InvokeMarketEditC2S::new);

    public static final MessageType OPEN_MARKET_SCREEN = NET.registerS2C("open_market_screen", SendOpenMarketScreenS2C::new);

    public static final MessageType SYNC_MARKET_DATA = NET.registerS2C("sync_market_data", SyncMarketDataS2C::new);
    public static final MessageType UPDATE_UI = NET.registerS2C("update_ui", UpdateUIS2C::new);
    public static final MessageType SEND_CATEGORIES = NET.registerS2C("send_categories", SendCategoriesS2C::new);
    public static final MessageType SYNC_USER_DATA = NET.registerS2C("sync_user_data", SyncUserDataS2C::new);
    public static final MessageType CREATE_OFFER = NET.registerC2S("create_offer", CreateOfferC2S::new);
    public static final MessageType BUY_OFFER = NET.registerC2S("buy_offer", BuyOfferC2S::new);
    public static final MessageType TAKE_BASKET_ENTRY = NET.registerC2S("take_basket_entry", TakeBasketEntryC2S::new);
    public static final MessageType CLOSE_ENTRY = NET.registerC2S("close_entry", CloseEntryC2S::new);
    public static final MessageType SYNC_GLOBAL_CONFIG = NET.registerS2C("sync_global_config", SyncGlobalConfigS2C::new);

    public static final MessageType SEND_CLEAR_MARKET_DATA = NET.registerS2C("send_clear_market_data", SendClearMarketDataS2C::new);
    public static final MessageType SEND_DELETE_MARKET_ENTRY = NET.registerS2C("send_delete_market_entry", SendDeleteMarketEntryS2C::new);
    public static final MessageType SEND_GET_MARKET_CATEGORY = NET.registerC2S("send_get_market_category", SendGetMarketCategoryC2S::new);
    public static final MessageType SEND_GET_MARKET_ENTRIES = NET.registerC2S("send_get_market_entries", SendGetMarketEntriesC2S::new);
    public static final MessageType SEND_MARKET_CATEGORY = NET.registerS2C("send_market_category", SendMarketCategoryS2C::new);
    public static final MessageType SEND_MARKET_ENTRY = NET.registerS2C("send_market_entry", SendMarketEntryS2C::new);

    public static void init() {

    }
}
