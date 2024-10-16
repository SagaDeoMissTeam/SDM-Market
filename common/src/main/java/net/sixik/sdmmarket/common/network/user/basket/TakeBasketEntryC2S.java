package net.sixik.sdmmarket.common.network.user.basket;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.data.MarketPlayerData;
import net.sixik.sdmmarket.common.market.basketEntry.AbstractBasketEntry;
import net.sixik.sdmmarket.common.network.MarketNetwork;
import net.sixik.sdmmarket.common.network.user.SyncUserDataS2C;
import net.sixik.sdmmarket.common.network.user.UpdateUIS2C;

import java.util.Objects;
import java.util.UUID;

public class TakeBasketEntryC2S extends BaseC2SMessage {

    public UUID basketEntry;

    public TakeBasketEntryC2S(UUID basketEntry) {
        this.basketEntry = basketEntry;
    }

    public TakeBasketEntryC2S(FriendlyByteBuf buf) {
        this.basketEntry = buf.readUUID();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.TAKE_BASKET_ENTRY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(basketEntry);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MarketPlayerData.PlayerData data = MarketDataManager.getPlayerData(context.getPlayer());
        if(data == null) return;

        AbstractBasketEntry find = null;
        for (AbstractBasketEntry entry : data.playerBasket.basketMoneyEntries) {
            if(Objects.equals(entry.basketEntryID, basketEntry)) {
                find = entry;
                break;
            }
        }

        if(find == null) return;

        find.givePlayer(context.getPlayer());
        data.playerBasket.basketMoneyEntries.remove(find);
        MarketDataManager.savePlayer(context.getPlayer().getServer(), context.getPlayer());
        new SyncUserDataS2C(data.serialize()).sendTo((ServerPlayer) context.getPlayer());
        new UpdateUIS2C().sendTo((ServerPlayer) context.getPlayer());
    }
}
