package net.sixik.sdmmarket.common.network.admin;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.common.data.MarketConfigData;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.config.MarketConfigCategory;
import net.sixik.sdmmarket.common.data.MarketUserManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;
import net.sixik.sdmmarket.common.network.user.SyncMarketDataS2C;

import java.util.Objects;
import java.util.UUID;

public class EditCategoryC2S extends BaseC2SMessage {

    private final UUID categoryID;
    private final CompoundTag nbt;
    public EditCategoryC2S(UUID categoryID, CompoundTag nbt) {
        this.categoryID = categoryID;
        this.nbt = nbt;
    }
    public EditCategoryC2S(FriendlyByteBuf buf) {
        this.categoryID = buf.readUUID();
        this.nbt = buf.readAnySizeNbt();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.EDIT_CATEGORY;
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

            if(nbt.isEmpty()) {
                MarketDataManager.CONFIG_SERVER.CATEGORIES.removeIf(s -> Objects.equals(s.categoryID, categoryID));
            } else {
                category.deserialize(nbt);
            }

            MarketConfigData.save(context.getPlayer().getServer());
            MarketUserManager.createOffersCategories(MarketDataManager.CONFIG_SERVER, MarketDataManager.USER_SERVER);
            new SyncMarketDataS2C().sendToAll(context.getPlayer().getServer());
        }
    }
}
