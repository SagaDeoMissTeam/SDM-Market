package net.sixik.sdmmarket.data.entry;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;
import net.sdm.sdmshopr.api.IEntryType;
import net.sdm.sdmshopr.utils.NBTUtils;
import net.sixik.sdmmarket.api.IMarketEntryType;
import net.sixik.sdmmarket.data.PlayerData;
import net.sixik.sdmmarket.utils.MarketSerializeHelper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarketEntry<T extends IMarketEntryType> implements INBTSerializable<CompoundTag> {

    public UUID uuid;
    public MarketCategory parentCategory;

    public long createDate;
    public long dateWhenClose;
    public long dateWhenAutoClose;

    public SearchEntryData searchEntryData = new SearchEntryData();

    public PlayerData ownerPlayer;
    public PlayerData playerWhoBuy = null;

    public int price;
    public int count;
    public boolean isSell = false;
    public T type;

    public MarketEntry(MarketCategory parentCategory){
        this(null,0,0,null,parentCategory);
    }

    public MarketEntry(Player ownerPlayer, int price, int count, T type, MarketCategory parentCategory){
        this.createDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        this.dateWhenAutoClose = LocalDateTime.now().plusDays(5).toInstant(ZoneOffset.UTC).toEpochMilli();
        if(ownerPlayer != null)
            this.ownerPlayer = new PlayerData(ownerPlayer);
        this.price = price;
        this.count = count;
        this.type = type;
        this.uuid = UUID.randomUUID();
        this.dateWhenClose = -1;
        this.parentCategory = parentCategory;
    }

    public void getConfig(ConfigGroup group){
        type.getConfig(group);
        group.addInt("price", price, v -> price = v, 1, 1, Integer.MAX_VALUE);
        group.addInt("count", count, v -> count = v, 1, 1, Integer.MAX_VALUE);
        searchEntryData.getConfig(group);
    }

    public Icon getIcon(){
        return type.getIcon(count);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean canCreate(){
        return type.getCountOnPlayer() != 0 && type.getCountOnPlayer() >= count;
    }

    public void close(){
        this.dateWhenClose = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        parentCategory.entries.remove(this);
    }

    public MarketEntry<T> addTags(String... tags){
        this.searchEntryData.tags.addAll(new ArrayList<>(List.of(tags)));
        return this;
    }

    public boolean isOwner(Player player){
        return player.getUUID().equals(ownerPlayer.playerUUID);
    }

    public boolean isOwnerClient(){
        return isOwner(Minecraft.getInstance().player);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putLong("createDate", createDate);
        nbt.putUUID("uuid", uuid);
        nbt.putLong("dateWhenAutoClose", dateWhenAutoClose);
        nbt.putLong("dateWhenClose", dateWhenClose);
        nbt.put("ownerPlayer", ownerPlayer.serializeNBT());
        if(nbt.contains("playerWhoBuy")){
            nbt.put("playerWhoBuy", playerWhoBuy.serializeNBT());
        }
        nbt.putInt("price", price);
        nbt.putInt("count", count);
        nbt.putBoolean("isSell", isSell);
        nbt.put("type", type.serializeNBT());
        nbt.put("searchEntryData", searchEntryData.serializeNBT());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.createDate = nbt.getLong("createDate");
        this.uuid = nbt.getUUID("uuid");
        this.dateWhenAutoClose = nbt.getLong("dateWhenAutoClose");
        this.dateWhenClose = nbt.getLong("dateWhenClose");
        this.ownerPlayer = new PlayerData();
        this.ownerPlayer.deserializeNBT(nbt.getCompound("ownerPlayer"));
        if(nbt.contains("playerWhoBuy")){
            this.playerWhoBuy = new PlayerData();
            this.playerWhoBuy.deserializeNBT(nbt.getCompound("playerWhoBuy"));
        }

        this.searchEntryData.deserializeNBT(nbt.getCompound("searchEntryData"));
        this.price = nbt.getInt("price");
        this.count = nbt.getInt("count");
        this.isSell = nbt.getBoolean("isSell");
        this.type = MarketSerializeHelper.deserializeMarketType(nbt.getCompound("type"));
    }

    @Override
    public String toString() {
        return "MarketEntry{" +
                "uuid=" + uuid +
                ", parentCategory=" + parentCategory +
                ", createDate=" + createDate +
                ", dateWhenClose=" + dateWhenClose +
                ", dateWhenAutoClose=" + dateWhenAutoClose +
                ", searchEntryData=" + searchEntryData +
                ", ownerPlayer=" + ownerPlayer +
                ", playerWhoBuy=" + playerWhoBuy +
                ", price=" + price +
                ", count=" + count +
                ", isSell=" + isSell +
                ", type=" + type +
                '}';
    }
}
