package net.sixik.sdmmarket.common.market.user;

import java.util.UUID;

public class MarketUserAnyCategory extends MarketUserCategory{

    public static final UUID DEFAULT = UUID.fromString("619a4773-efd4-46e5-97c5-5ce1ce2da517");

    public MarketUserAnyCategory() {
        super(DEFAULT, "Misc");
    }
}
