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

public class CreateCategoryC2S extends BaseC2SMessage {

    private CompoundTag nbt;

    public CreateCategoryC2S(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public CreateCategoryC2S(FriendlyByteBuf buf) {
        this.nbt = buf.readAnySizeNbt();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.CREATE_CATEGORY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if(context.getPlayer().hasPermissions(2)) {
            MarketConfigCategory category = new MarketConfigCategory("");
            category.deserialize(nbt);
            MarketDataManager.CONFIG_SERVER.CATEGORIES.add(category);

            MarketConfigData.save(context.getPlayer().getServer());

            MarketUserManager.createOffersCategories(MarketDataManager.CONFIG_SERVER, MarketDataManager.USER_SERVER);
            new SyncMarketDataS2C().sendToAll(context.getPlayer().getServer());
        }
    }
}
