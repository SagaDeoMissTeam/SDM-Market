package net.sixik.sdmmarket.fabric;

import net.sixik.sdmmarket.SDMMarket;
import net.fabricmc.api.ModInitializer;

public class SDMMarketFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SDMMarket.init();
    }
}