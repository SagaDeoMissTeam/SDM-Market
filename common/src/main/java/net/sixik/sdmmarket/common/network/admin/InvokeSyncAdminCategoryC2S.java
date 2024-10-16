package net.sixik.sdmmarket.common.network.admin;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;

public class InvokeSyncAdminCategoryC2S extends BaseC2SMessage {

    public InvokeSyncAdminCategoryC2S() {}
    public InvokeSyncAdminCategoryC2S(FriendlyByteBuf buf) {}

    @Override
    public MessageType getType() {
        return MarketNetwork.INVOKE_SYNC_ADMIN_CATEGORY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        new SyncAdminCategoryS2C(MarketDataManager.CONFIG_SERVER.serialize()).sendTo((ServerPlayer) context.getPlayer());
    }
}
