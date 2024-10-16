package net.sixik.sdmmarket;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.sixik.sdmmarket.client.gui.user.MarketUserScreen;
import org.lwjgl.glfw.GLFW;

public class SDMMarketClient {

    public static final String MARKET_CATEGORY = "key.category.sdmshopr";
    public static final String MARKET_KEY_NAME = "key.sdmmarket.market";

    public static KeyMapping MARKET_KEY = new KeyMapping(MARKET_KEY_NAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_P, MARKET_CATEGORY);

    public static void init() {
        ClientTickEvent.CLIENT_PRE.register(SDMMarketClient::keyInput);
    }


    public static void keyInput(Minecraft mc) {
        if (MARKET_KEY.consumeClick()) {
            new MarketUserScreen().openGui();
        }
    }

    public static boolean hasPermission(){
        return Minecraft.getInstance().player.hasPermissions(2);
    }
}
