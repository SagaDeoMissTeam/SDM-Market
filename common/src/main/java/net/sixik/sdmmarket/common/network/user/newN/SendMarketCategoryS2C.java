package net.sixik.sdmmarket.common.network.user.newN;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.api.MarketAPI;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.network.MarketNetwork;


public class SendMarketCategoryS2C extends BaseS2CMessage {

    private final CompoundTag nbt;

    public SendMarketCategoryS2C(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public SendMarketCategoryS2C(FriendlyByteBuf buf) {
        this.nbt = buf.readAnySizeNbt();
    }


    @Override
    public MessageType getType() {
        return MarketNetwork.SEND_MARKET_CATEGORY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MarketUserCategory userCategory = new MarketUserCategory();
        userCategory.deserializeWithoutEntries(nbt);

        MarketDataManager.USER_CLIENT.categories.add(userCategory);

        new SendGetMarketEntriesC2S(userCategory.icon).sendToServer();
        MarketAPI.updateUI();
    }
}
