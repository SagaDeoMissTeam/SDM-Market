package net.sixik.sdmmarket.common.network.admin.config;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.sixik.sdmmarket.common.data.MarketConfig;
import net.sixik.sdmmarket.common.data.MarketConfigData;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.data.MarketUserManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;
import net.sixik.sdmmarket.common.network.user.SyncGlobalConfigS2C;
import net.sixik.sdmmarket.common.network.user.SyncMarketDataS2C;

public class EditMarketConfigC2S extends BaseC2SMessage {

    private final CompoundTag nbt;

    public EditMarketConfigC2S(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public EditMarketConfigC2S(FriendlyByteBuf buf) {
        this.nbt = buf.readAnySizeNbt();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.EDIT_MARKET_CONFIG;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MarketDataManager.GLOBAL_CONFIG_SERVER = new MarketConfig();
        MarketDataManager.GLOBAL_CONFIG_SERVER.deserialize(nbt);

        if(MarketDataManager.GLOBAL_CONFIG_SERVER.sellAnyItems){
            MarketUserManager.createOffersCategories(MarketDataManager.CONFIG_SERVER, MarketDataManager.USER_SERVER);
            new SyncMarketDataS2C().sendToAll(context.getPlayer().getServer());
        }

        MarketConfigData.save(context.getPlayer().getServer());


        MarketDataManager.getPlayerData(context.getPlayer()).updateOffersCount();
        MarketUserManager.syncUserData((ServerPlayer) context.getPlayer());
        new SyncGlobalConfigS2C(MarketDataManager.GLOBAL_CONFIG_SERVER.serialize()).sendToAll(context.getPlayer().getServer());
    }
}
