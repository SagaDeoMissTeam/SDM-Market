package net.sixik.sdmmarket.common.network.user;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.data.MarketPlayerData;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.sdmmarket.common.data.MarketUserManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;
import net.sixik.sdmmarket.common.utils.MarketItemHelper;

public class CreateOfferC2S extends BaseC2SMessage {

    private final CompoundTag nbt;

    public CreateOfferC2S(CompoundTag nbt){
        this.nbt = nbt;
    }

    public CreateOfferC2S(FriendlyByteBuf buf){
        this.nbt = buf.readAnySizeNbt();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.CREATE_OFFER;
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
        if(category == null) {
            SDMMarket.LOGGER.error("Could not find category for entry " + entry.itemStack + " : " + entry.count);
            return;
        }

        MarketUserEntryList entryList = MarketUserManager.getEntryListByCategory(category, entry.itemStack);
        if(entryList == null) {
            SDMMarket.LOGGER.error("Could not find entrylist for category " + category.categoryName);
            return;
        }

        MarketPlayerData.PlayerData data = MarketDataManager.getPlayerData(context.getPlayer().getServer(), context.getPlayer());
        if(data == null) {
            SDMMarket.LOGGER.error("Could not find player data for player " + context.getPlayer());
            return;
        }

        entry.ownerID = data.playerID;

        if(data.countOffers <= 0) {
            SDMMarket.LOGGER.error("Player " + context.getPlayer() +  " has no more offers!");
            return;
        }

        System.out.println(entry.itemStack.toString() + "  " + entry.itemStack.getTag());

        if(!MarketItemHelper.sellItem(context.getPlayer(), entry.count, entry.itemStack)) return;

        entryList.addElement(entry);
        data.countOffers -= 1;
        data.playerOffers.add(entry.entryID);

        MarketDataManager.savePlayer(context.getPlayer().getServer(), data.playerID);
        MarketUserManager.syncUserData((ServerPlayer) context.getPlayer());

//        new UpdateUIS2C().sendTo((ServerPlayer) context.getPlayer());
        new SendCategoriesS2C(MarketDataManager.USER_SERVER.serialize()).sendToAll(context.getPlayer().getServer());

        MarketDataManager.saveMarketData(context.getPlayer().getServer());
    }
}
