package net.sixik.sdmmarket.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.sixik.sdmmarket.data.MarketData;
import net.sixik.sdmmarket.data.entry.MarketCategory;
import net.sixik.sdmmarket.data.entry.MarketEntry;


public class SendEntryCreateC2S extends BaseC2SMessage {

    public SendEntryCreateC2S(FriendlyByteBuf buf){
        this.nbt = buf.readAnySizeNbt();
    }

    public CompoundTag nbt;

    public SendEntryCreateC2S(CompoundTag nbt){
        this.nbt = nbt;
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.SEND_CREATE_ENTRY;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeNbt(nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext packetContext) {


        MarketCategory category = MarketData.SERVER.getOrCreate("test");

        try {


            MarketEntry<?> entry = new MarketEntry<>(category);
            entry.deserializeNBT(nbt);
            entry.type.executeOnCreate((ServerPlayer) packetContext.getPlayer(), entry.count);

            category.entries.add(entry);
        } catch (Exception e){
            e.printStackTrace();
        }

        MarketData.SERVER.save();
        MarketData.Methods.sync(packetContext.getPlayer().getServer());

    }
}
