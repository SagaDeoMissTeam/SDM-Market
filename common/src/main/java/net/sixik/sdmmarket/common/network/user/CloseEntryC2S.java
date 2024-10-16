package net.sixik.sdmmarket.common.network.user;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.sdmmarket.common.data.MarketUserManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;

import java.util.UUID;

public class CloseEntryC2S extends BaseC2SMessage {

    private final UUID uuid;
    public CloseEntryC2S(UUID uuid) {
        this.uuid = uuid;
    }
    public CloseEntryC2S(FriendlyByteBuf entry) {
        this.uuid = entry.readUUID();
    }


    @Override
    public MessageType getType() {
        return MarketNetwork.CLOSE_ENTRY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MarketUserEntry entry = MarketUserManager.getEntryByID(uuid);
        if(entry == null) return;
        entry.closeEntry((ServerPlayer) context.getPlayer());
    }
}
