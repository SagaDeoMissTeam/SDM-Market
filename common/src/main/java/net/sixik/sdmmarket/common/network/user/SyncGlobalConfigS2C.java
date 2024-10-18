package net.sixik.sdmmarket.common.network.user;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;

public class SyncGlobalConfigS2C extends BaseS2CMessage {

    private final CompoundTag nbt;

    public SyncGlobalConfigS2C(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public SyncGlobalConfigS2C(FriendlyByteBuf buf) {
        this.nbt = buf.readAnySizeNbt();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.SYNC_GLOBAL_CONFIG;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MarketDataManager.GLOBAL_CONFIG_CLIENT.deserialize(nbt);
    }
}
