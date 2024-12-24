package net.sixik.sdmmarket.common.network.admin.config;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.common.data.MarketConfig;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;

public class SyncMarketConfigS2C extends BaseS2CMessage {

    private final CompoundTag nbt;

    public SyncMarketConfigS2C(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public SyncMarketConfigS2C(FriendlyByteBuf buf) {
        this.nbt = buf.readAnySizeNbt();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.SYNC_MARKET_CONFIG;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MarketConfig config = MarketDataManager.GLOBAL_CONFIG_CLIENT;
        config.deserialize(nbt);

        ConfigGroup group = new ConfigGroup("market_global_config", b -> {

            if(b) {
                new EditMarketConfigC2S(config.serialize()).sendToServer();
            }

            if(Minecraft.getInstance().screen instanceof ScreenWrapper wrapper) {
                if(wrapper.getGui() instanceof EditConfigScreen configScreen){
                    configScreen.closeGui();
                }
            }
        });
        config.getConfig(group);
        new EditConfigScreen(group).openGui();
    }
}
