package net.sixik.sdmmarket.forge;

import dev.architectury.platform.forge.EventBuses;
import net.sixik.sdmmarket.SDMMarket;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SDMMarket.MOD_ID)
public class SDMMarketForge {
    public SDMMarketForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(SDMMarket.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SDMMarket.init();
    }
}