package net.sixik.sdmmarket.common.network.user;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.sdmmarket.common.data.MarketUserManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;

import java.util.Objects;

public class BuyOfferC2S extends BaseC2SMessage {

    public CompoundTag nbt;

    public BuyOfferC2S(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public BuyOfferC2S(FriendlyByteBuf buf) {
        this.nbt = buf.readAnySizeNbt();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.BUY_OFFER;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MarketUserEntry entry = new MarketUserEntry();
        entry.deserialize(nbt);


        MarketUserCategory category = MarketUserManager.getCategoryByID(entry.categoryID);
        if(category == null) return;

        MarketUserEntryList entryList = MarketUserManager.getEntryListByCategory(category, entry.itemStack);
        if(entryList == null) return;

        var iterator = entryList.entries.iterator();
        while(iterator.hasNext()) {
            MarketUserEntry userEntry = iterator.next();
            if(Objects.equals(userEntry.entryID, entry.entryID)) {
                if(userEntry.buyEntry((ServerPlayer) context.getPlayer())) {
                    iterator.remove();
                    new SyncMarketDataS2C().sendToAll(context.getPlayer().getServer());
                    MarketDataManager.saveMarketData(context.getPlayer().getServer());
                    break;
                }
            }
        }
    }
}
