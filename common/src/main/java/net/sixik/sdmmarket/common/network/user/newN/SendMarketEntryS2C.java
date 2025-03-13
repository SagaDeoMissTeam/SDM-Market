package net.sixik.sdmmarket.common.network.user.newN;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.api.MarketAPI;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.sdmmarket.common.network.MarketNetwork;
import net.sixik.sdmmarket.common.utils.MarketItemHelper;

public class SendMarketEntryS2C extends BaseS2CMessage {

    private final CompoundTag nbt;
    private final ItemStack itemStack;

    public SendMarketEntryS2C(ItemStack itemStack, CompoundTag nbt) {
        this.nbt = nbt;
        this.itemStack = itemStack;
    }

    public SendMarketEntryS2C(FriendlyByteBuf buf) {
        this.nbt = buf.readAnySizeNbt();
        this.itemStack = buf.readItem();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.SEND_MARKET_ENTRY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(nbt);
        buf.writeItem(itemStack);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if(nbt.isEmpty()) return;

        MarketUserEntry userEntry = new MarketUserEntry();
        userEntry.deserialize(nbt);

        for (MarketUserCategory category : MarketDataManager.USER_CLIENT.categories) {
            for (MarketUserEntryList entry : category.entries) {
                if(MarketItemHelper.isEquals(entry.itemStack, itemStack)) {
                    entry.entries.add(userEntry);
                    MarketAPI.updateUI();
                    return;
                }
            }
        }
    }
}
