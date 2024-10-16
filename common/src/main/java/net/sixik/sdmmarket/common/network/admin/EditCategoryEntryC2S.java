package net.sixik.sdmmarket.common.network.admin;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.common.data.MarketConfigData;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.config.MarketConfigCategory;
import net.sixik.sdmmarket.common.market.config.AbstractMarketConfigEntry;
import net.sixik.sdmmarket.common.data.MarketUserManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;
import net.sixik.sdmmarket.common.network.user.SyncMarketDataS2C;

import java.util.Objects;
import java.util.UUID;

public class EditCategoryEntryC2S extends BaseC2SMessage {

    private final UUID categoryID;
    private final UUID entryID;
    private final CompoundTag nbt;

    public EditCategoryEntryC2S(UUID categoryID, UUID entryID, CompoundTag nbt){
        this.categoryID = categoryID;
        this.entryID = entryID;
        this.nbt = nbt;
    }

    public EditCategoryEntryC2S(FriendlyByteBuf buf){
        this.categoryID = buf.readUUID();
        this.entryID = buf.readUUID();
        this.nbt = buf.readAnySizeNbt();
    }


    @Override
    public MessageType getType() {
        return MarketNetwork.EDIT_CATEGORY_ENTRY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.categoryID);
        buf.writeUUID(this.entryID);
        buf.writeNbt(this.nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if(context.getPlayer().hasPermissions(2)) {
            MarketConfigCategory category = MarketDataManager.CONFIG_SERVER.getCategory(categoryID);
            if (category == null) return;

            AbstractMarketConfigEntry entry = category.getEntry(entryID);
            if (entry == null) return;

            if(nbt.isEmpty()) {
                category.entries.removeIf(s -> Objects.equals(s.entryID, entryID));
            } else {
                entry.deserialize(nbt);
            }

            MarketConfigData.save(context.getPlayer().getServer());
            MarketUserManager.createOffersCategories(MarketDataManager.CONFIG_SERVER, MarketDataManager.USER_SERVER);
            new SyncMarketDataS2C().sendToAll(context.getPlayer().getServer());
        }
    }
}
