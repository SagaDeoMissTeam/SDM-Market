package net.sixik.sdmmarket.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.sdm.sdmshopr.SDMShopR;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.data.MarketData;
import net.sixik.sdmmarket.data.PlayerData;
import net.sixik.sdmmarket.data.entry.MarketEntry;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class SendBuyEntryC2S extends BaseC2SMessage {


    public UUID entryID;
    public SendBuyEntryC2S(FriendlyByteBuf buf){
        this.entryID = buf.readUUID();
    }

    public SendBuyEntryC2S(UUID entryID){
        this.entryID = entryID;
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.SEND_BUY_ENTRY;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUUID(entryID);
    }

    @Override
    public void handle(NetworkManager.PacketContext packetContext) {
        new Thread(() -> {
            ServerPlayer player = (ServerPlayer) packetContext.getPlayer();

            MarketEntry<?> entry = MarketData.SERVER.getEntry(entryID);
            if(entry != null){

                long money = SDMShopR.getMoney(player);
                if(money < entry.price) return;

                entry.type.executeOnBuy(player, entry.count ,entry.price);

                ServerPlayer owner = player.getServer().getPlayerList().getPlayer(entry.ownerPlayer.playerUUID);
                if(owner != null){
                    entry.type.executeOnCloseOwnerOnline(owner, entry.price);
                } else {
                    entry.type.executeOnCloseOwnerOffline(entry.ownerPlayer.playerUUID, entry.price);
                }

                entry.playerWhoBuy = new PlayerData(player);
                entry.isSell = true;
                entry.close();
                MarketData.Methods.sync(player.getServer());
            }
        }).start();
    }
}
