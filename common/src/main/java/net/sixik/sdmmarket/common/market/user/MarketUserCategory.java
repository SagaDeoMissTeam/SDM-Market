package net.sixik.sdmmarket.common.market.user;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sixik.sdmmarket.common.serializer.MarketSerializer;
import net.sixik.sdmmarket.common.serializer.SDMSerializeParam;
import net.sixik.sdmmarket.common.utils.INBTSerialize;
import net.sixik.sdmmarket.common.utils.NBTUtils;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class MarketUserCategory implements INBTSerialize  {

    public UUID categoryID;
    public ItemStack icon = Items.BARRIER.getDefaultInstance();
    public String categoryName;

    public CopyOnWriteArrayList<MarketUserEntryList> entries = new CopyOnWriteArrayList<>();

    public MarketUserCategory() {
        this(UUID.randomUUID(), "");
    }

    public MarketUserCategory(UUID categoryID, String name) {
        this.categoryID = categoryID;
        this.categoryName = name;
    }

    public MarketUserCategory setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public MarketUserCategory addEntry(MarketUserEntryList entry) {
        this.entries.add(entry);
        return this;
    }

    public void removeEntry(ItemStack item, UUID entryID) {
        for (MarketUserEntryList entry : entries) {
            if(ItemStack.matches(entry.itemStack, item)) {
                entry.entries.removeIf(s -> Objects.equals(s.entryID, entryID));
                return;
            }
        }
    }



    @Override
    public CompoundTag serialize() {
        return serialize(SDMSerializeParam.SERIALIZE_ALL_ENTRIES);
    }

    public CompoundTag serialize(int bits) {
        return MarketSerializer.MarketEntry.serializeCategory(this, bits);
    }


    @Override
    public void deserialize(CompoundTag nbt) {
        deserialize(nbt, SDMSerializeParam.SERIALIZE_ALL_ENTRIES);
    }

    public void deserialize(CompoundTag nbt, int bits) {
        MarketSerializer.MarketEntry.deserializeCategory(this, nbt, bits);
    }

    public CompoundTag serializeWithoutEntries() {
        return MarketSerializer.MarketEntry.serializeCategoryWithoutEntries(this);
    }

    public void deserializeWithoutEntries(CompoundTag nbt) {
        MarketSerializer.MarketEntry.deserializeCategoryWithoutEntries(this, nbt);
    }
}
