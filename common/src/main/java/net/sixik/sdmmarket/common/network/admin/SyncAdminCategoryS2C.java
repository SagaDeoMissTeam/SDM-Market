package net.sixik.sdmmarket.common.network.admin;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.client.gui.admin.MarketAdminScreen;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;

public class SyncAdminCategoryS2C extends BaseS2CMessage {

    private final CompoundTag nbt;

    public SyncAdminCategoryS2C(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public SyncAdminCategoryS2C(FriendlyByteBuf buf) {
        this.nbt = buf.readAnySizeNbt();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.SYNC_ADMIN_CATEGORY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MarketDataManager.CONFIG_CLIENT.deserialize(nbt);
        MarketAdminScreen.refreshIfOpen();
    }
}
