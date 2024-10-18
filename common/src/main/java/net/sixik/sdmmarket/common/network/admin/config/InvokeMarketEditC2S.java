package net.sixik.sdmmarket.common.network.admin.config;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;

public class InvokeMarketEditC2S extends BaseC2SMessage {

    public InvokeMarketEditC2S(){}
    public InvokeMarketEditC2S(FriendlyByteBuf buf){}

    @Override
    public MessageType getType() {
        return MarketNetwork.OPEN_MARKET_CONFIG;
    }

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if(context.getPlayer().hasPermissions(2)) {
            new SyncMarketConfigS2C(MarketDataManager.GLOBAL_CONFIG_SERVER.serialize()).sendTo((ServerPlayer) context.getPlayer());
        }
    }
}
