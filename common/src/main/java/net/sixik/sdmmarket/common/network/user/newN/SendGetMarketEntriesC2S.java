package net.sixik.sdmmarket.common.network.user.newN;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.sdmmarket.common.network.MarketNetwork;
import net.sixik.sdmmarket.common.serializer.MarketSerializer;
import net.sixik.sdmmarket.common.utils.MarketItemHelper;

public class SendGetMarketEntriesC2S extends BaseC2SMessage {

    private final ItemStack itemStack;

    public SendGetMarketEntriesC2S(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public SendGetMarketEntriesC2S(FriendlyByteBuf buf) {
        this.itemStack = buf.readItem();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.SEND_GET_MARKET_ENTRIES;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        for (final MarketUserCategory category : MarketDataManager.USER_SERVER.categories) {

            for (final MarketUserEntryList entry : category.entries) {

                if(!MarketItemHelper.isEquals(entry.itemStack, itemStack)) continue;

                for (final Tag tag : MarketSerializer.MarketEntry.serializeCategoryEntryListToList(entry)) {

                    if(tag instanceof CompoundTag nbt) {

                        new SendMarketEntryS2C(entry.itemStack, nbt);
                    }
                }

                return;
            }
        }

    }
}
