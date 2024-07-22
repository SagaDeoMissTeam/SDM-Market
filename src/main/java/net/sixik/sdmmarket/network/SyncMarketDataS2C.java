package net.sixik.sdmmarket.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.sixik.sdmmarket.client.screen.MarketScreen;
import net.sixik.sdmmarket.data.MarketData;

public class SyncMarketDataS2C extends BaseS2CMessage {

    public SyncMarketDataS2C(FriendlyByteBuf buf){
        this.nbt = buf.readAnySizeNbt();
    }

    public CompoundTag nbt;
    public SyncMarketDataS2C(CompoundTag nbt){
        this.nbt = nbt;
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.SYNC_MARKET_DATA;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeNbt(nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext packetContext) {
        if(packetContext.getEnv().isClient()){

            System.out.println(SNBTCompoundTag.of(nbt));

            MarketData.CLIENT = new MarketData();
            MarketData.CLIENT.deserializeNBT(nbt);

            MarketScreen.refreshAll();

        }
    }
}
