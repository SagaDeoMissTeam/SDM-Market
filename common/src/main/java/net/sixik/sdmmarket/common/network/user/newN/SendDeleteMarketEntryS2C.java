package net.sixik.sdmmarket.common.network.user.newN;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.client.gui.user.buyer.MarketUserBuyerScreen;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.debuger.ErrorCodes;
import net.sixik.sdmmarket.common.debuger.ErrorHelper;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.sdmmarket.common.network.MarketNetwork;

import java.util.UUID;

public class SendDeleteMarketEntryS2C extends BaseS2CMessage {

    private final UUID categoryID;
    private final UUID entryID;

    public SendDeleteMarketEntryS2C(UUID categoryID, UUID entryID) {
        this.categoryID = categoryID;
        this.entryID = entryID;
    }

    public SendDeleteMarketEntryS2C(FriendlyByteBuf buf) {
        this.categoryID = buf.readUUID();
        this.entryID = buf.readUUID();
    }


    @Override
    public MessageType getType() {
        return MarketNetwork.SEND_DELETE_MARKET_ENTRY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(categoryID);
        buf.writeUUID(entryID);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        for (MarketUserCategory category : MarketDataManager.USER_CLIENT.categories) {

            if(category.categoryID.equals(categoryID)) {

                for (MarketUserEntryList entryList : category.entries) {

                    if (!entryList.entries.removeIf(entry -> entry.entryID.equals(entryID))) {
                        ErrorHelper.errorCode(ErrorCodes.E_ERROR_DELETE_ENTRY, entryID);
                    }

                    if(Minecraft.getInstance().screen instanceof ScreenWrapper screenWrapper) {
                        if (screenWrapper.getGui() instanceof MarketUserBuyerScreen buyerScreen) {
                            buyerScreen.selectedEntry = null;
                            buyerScreen.updateEntries();
                            buyerScreen.infoPanel.addElements();
                            buyerScreen.entriesPanel.addEntries();
                        }
                    }

                    return;
                }
            }
        }
    }
}
