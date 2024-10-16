package net.sixik.sdmmarket.common.network.admin;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.common.data.MarketConfigData;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.config.MarketConfigCategory;
import net.sixik.sdmmarket.common.market.config.AbstractMarketConfigEntry;
import net.sixik.sdmmarket.common.network.MarketNetwork;

import java.util.UUID;

public class CreateCategoryEntryC2S extends BaseC2SMessage {

    private final UUID categoryID;
    private final CompoundTag nbt;
    public CreateCategoryEntryC2S(UUID categoryID, CompoundTag nbt) {
        this.categoryID = categoryID;
        this.nbt = nbt;
    }

    public CreateCategoryEntryC2S(FriendlyByteBuf buf) {
        this.categoryID = buf.readUUID();
        this.nbt = buf.readAnySizeNbt();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.CREATE_CATEGORY_ENTRY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(categoryID);
        buf.writeNbt(nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if(context.getPlayer().hasPermissions(2)) {
            MarketConfigCategory category = MarketDataManager.CONFIG_SERVER.getCategory(categoryID);
            if (category == null) return;

            if(!nbt.isEmpty()) {
                AbstractMarketConfigEntry d1 = AbstractMarketConfigEntry.create(nbt);
                if (d1 == null) {
                    SDMMarket.LOGGER.error("Could not create entry: d1 == null");
                    return;
                }
                category.entries.add(d1);
            }
            MarketConfigData.save(context.getPlayer().getServer());

        }
    }
}
