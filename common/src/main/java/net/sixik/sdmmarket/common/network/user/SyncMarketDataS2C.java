package net.sixik.sdmmarket.common.network.user;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.client.gui.user.buyer.MarketUserBuyerScreen;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;


@Deprecated
public class SyncMarketDataS2C extends BaseS2CMessage {

    private final CompoundTag nbt;

    public SyncMarketDataS2C() {
        this.nbt = MarketDataManager.USER_SERVER.serialize();
    }

    public SyncMarketDataS2C(FriendlyByteBuf buf) {
        this.nbt = buf.readAnySizeNbt();
    }


    @Override
    public MessageType getType() {
        return MarketNetwork.SYNC_MARKET_DATA;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MarketDataManager.USER_CLIENT.deserialize(nbt);


    }
}
