package net.sixik.sdmmarket.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sdm.sdmshopr.client.MainShopScreen;
import net.sdm.sdmshopr.shop.Shop;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.client.screen.MarketScreen;
import net.sixik.sdmmarket.common.SDMMarketCommon;
import org.lwjgl.glfw.GLFW;

public class SDMMarketClient extends SDMMarketCommon {
    public static final String SDMSHOP_CATEGORY = "key.category.sdmshopr";
    public static final String KEY_NAME = "key.sdmmarket.key";
//    public static KeyMapping KEY_MARKET = new KeyMapping(KEY_NAME, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, SDMSHOP_CATEGORY);

    public static KeyMapping KEY_SHOP = new KeyMapping(KEY_NAME, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, SDMSHOP_CATEGORY);

    public static SDMMarketClient INSTANCE = null;

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.addListener(this::keyInput);

        INSTANCE = this;
    }

    public void keyInput(InputEvent.Key event) {
        if (KEY_SHOP.consumeClick() && Shop.CLIENT != null) {
            (new MarketScreen()).openGui();
        }

    }


    @Mod.EventBusSubscriber(modid = SDMMarket.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModButton{
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(SDMMarketClient.KEY_SHOP);
        }
    }
}
