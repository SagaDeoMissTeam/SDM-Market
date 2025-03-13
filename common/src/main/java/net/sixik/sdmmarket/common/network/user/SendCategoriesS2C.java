package net.sixik.sdmmarket.common.network.user;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.sixik.sdmmarket.client.gui.user.MarketUserScreen;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.network.MarketNetwork;

@Deprecated
public class SendCategoriesS2C extends BaseS2CMessage {

    private final CompoundTag nbt;

    public SendCategoriesS2C(CompoundTag nbt){
        this.nbt = nbt;
    }

    public SendCategoriesS2C(FriendlyByteBuf buf){
        this.nbt = buf.readAnySizeNbt();
    }

    @Override
    public MessageType getType() {
        return MarketNetwork.SEND_CATEGORIES;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MarketDataManager.USER_CLIENT.deserialize(nbt);

        if(Minecraft.getInstance().screen instanceof ScreenWrapper wrapper){
            if(wrapper.getGui() instanceof MarketUserScreen userScreen){
                userScreen.entriesPanel.addEntries();
            }
        }
    }
}
