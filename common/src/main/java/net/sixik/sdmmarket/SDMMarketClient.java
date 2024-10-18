package net.sixik.sdmmarket;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.ftb.mods.ftblibrary.ui.CustomClickEvent;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.sixik.sdmmarket.client.gui.user.MarketUserScreen;
import org.lwjgl.glfw.GLFW;

public class SDMMarketClient {

    public static final ResourceLocation OPEN_GUI = new ResourceLocation(SDMMarket.MOD_ID, "open_gui");
    public static final String MARKET_CATEGORY = "key.category.sdmshopr";
    public static final String MARKET_KEY_NAME = "key.sdmmarket.market";

    public static KeyMapping MARKET_KEY = new KeyMapping(MARKET_KEY_NAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_P, MARKET_CATEGORY);

    public static void init() {
        CustomClickEvent.EVENT.register(SDMMarketClient::customClick);
        ClientTickEvent.CLIENT_PRE.register(SDMMarketClient::keyInput);
        KeyMappingRegistry.register(MARKET_KEY);
    }


    public static void keyInput(Minecraft mc) {
        if (MARKET_KEY.consumeClick()) {
            new MarketUserScreen().openGui();
        }
    }

    public static EventResult customClick(CustomClickEvent event) {
        if (event.id().equals(OPEN_GUI)) {
            new MarketUserScreen().openGui();
            return EventResult.interruptTrue();
        }

        return EventResult.pass();
    }

    public static boolean hasPermission(){
        return Minecraft.getInstance().player.hasPermissions(2);
    }
}
