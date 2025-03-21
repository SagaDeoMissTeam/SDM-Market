package net.sixik.sdmmarket;

import dev.ftb.mods.ftblibrary.icon.Icon;

public class SDMMarketIcons {

    public static final Icon TOGGLE_DARK = getImage("toggles_dark");
    public static final Icon TOGGLE_WHITE = getImage("toggles_white");
    public static final Icon PRICE_TAG = getImage("price_tag_white");
    public static final Icon BASKET_OLD = getImage("strongbox_white");
    public static final Icon BASKET = getImage("shopping_cart_white");

    static Icon get(String id) {
        return Icon.getIcon(SDMMarket.MOD_ID + ":icons/" + id);
    }

    static Icon getImage(String id) {
        return Icon.getIcon(SDMMarket.MOD_ID + ":textures/icons/" + id + ".png");
    }
}
